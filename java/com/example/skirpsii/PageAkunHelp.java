package com.example.skirpsii;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PageAkunHelp extends AppCompatActivity {

    Button upload,kirim;
    FirebaseAuth fAuth;
    String userID;
    Bitmap bitmap;
    String encodeImageString;
    TextView tes,tvriwayat;
    EditText jenis,isi;
    ArrayList<daftarkeluhan> model;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_page_akun_help);

        fAuth = FirebaseAuth.getInstance();
        upload = findViewById(R.id.upload);
        tes = findViewById(R.id.tes);
        kirim = findViewById(R.id.kirim);
        jenis = findViewById(R.id.jenis);
        isi = findViewById(R.id.isi);
        tvriwayat = findViewById(R.id.tvriwayat);
        userID = fAuth.getCurrentUser().getUid();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.riwayatkeluhan);
        recyclerView.setLayoutManager(layoutManager);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(PageAkunHelp.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/");
                                startActivityForResult(Intent.createChooser(intent,"BrowseImage"),1);
                            }
                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }
                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(TextUtils.equals(tes.getText().toString(),"Sudah Terupload"))) {
                    Toast.makeText(getApplicationContext(), "Silahkan Upload Bukti Reservasi", Toast.LENGTH_SHORT).show();
                    return;
                }
                inputkeluhan();
            }
        });

        tampilkeluhan();

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!= null && data.getData() != null){
            Uri filePath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                encodeBitmapImage(bitmap);
                tes.setText("Sudah Terupload");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
        encodeImageString = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }

    void inputkeluhan(){
        String Jenis = jenis.getText().toString().trim();
        String Isi = isi.getText().toString().trim();
        String url = "https://restoratori.mikolindosorong.id/mobile/insertkeluhan";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Terkirim", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("id_pengeluh",userID);
                params.put("judul_keluhan",Jenis);
                params.put("isi_keluhan",Isi);
                params.put("gambar_keluhan",encodeImageString);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    void tampilkeluhan(){
        String url = "https://restoratori.mikolindosorong.id/mobile/daftarkeluhanuser.php?data="+userID;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray getHasil = jsonObject.getJSONArray("hasil");
                    model = new ArrayList<>();
                    if(getHasil.length()>0){
                        for (int i=0;i<getHasil.length();i++){
                            JSONObject getData = getHasil.getJSONObject(i);
                            String tanggal_keluhan = getData.getString("tanggal_keluhan");
                            String judul_keluhan = getData.getString("judul_keluhan");
                            String isi_keluhan =getData.getString("isi_keluhan");
                            String status_keluhan = getData.getString("status_keluhan");
                            String nota_admin = getData.getString("nota_admin");
                            model.add(new daftarkeluhan(tanggal_keluhan,judul_keluhan,isi_keluhan,status_keluhan,nota_admin));
                        }
                        AdapterTampilanHelp adapter = new AdapterTampilanHelp(PageAkunHelp.this,model);
                        recyclerView.setAdapter(adapter);
                        tvriwayat.setVisibility(View.GONE);
                    }
                    else{
                        recyclerView.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PageAkunHelp.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void HomePage(View view) {
        Intent i = new Intent (".MainPage");
        startActivity(i);
    }

    public void SearchPage(View view) {
        Intent i = new Intent (".SearchPage");
        startActivity(i);
    }

    public void FavoritRestoranPage(View view) {
        Intent i = new Intent (".FavoritRestoranPage");
        startActivity(i);
    }

    public void HistoryReservasi(View view) {
        Intent i = new Intent (".HistoryReservasi");
        startActivity(i);
    }

    public void PageAkun(View view) {
        Intent i = new Intent (".PageAkun");
        startActivity(i);
    }

}

class AdapterTampilanHelp extends RecyclerView.Adapter<AdapterTampilanHelp.ViewHolder> {
    ArrayList<daftarkeluhan> model;
    LayoutInflater inflater;
    Context context;

    AdapterTampilanHelp(Context context,ArrayList<daftarkeluhan> model){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.from(parent.getContext()).inflate(R.layout.tampilandaftarkeluhan,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tanggal_keluhan.setText(model.get(position).getTanggal_keluhan());
        holder.judul_keluhan.setText(model.get(position).getJudul_keluhan());
        holder.isi_keluhan.setText(model.get(position).getIsi_keluhan());
        holder.status_keluhan.setText(model.get(position).getStatus_keluhan());
        holder.nota_admin.setText(model.get(position).getNota_admin());
    }

    @Override
    public int getItemCount() {
        return model.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tanggal_keluhan,judul_keluhan,isi_keluhan,status_keluhan,nota_admin;

        public ViewHolder(View itemView) {
            super(itemView);
            tanggal_keluhan = itemView.findViewById(R.id.tanggalkeluhan);
            judul_keluhan = itemView.findViewById(R.id.judulkeluhan);
            isi_keluhan = itemView.findViewById(R.id.isikeluhan);
            status_keluhan = itemView.findViewById(R.id.statuskeluhan);
            nota_admin = itemView.findViewById(R.id.nota_admin);
        }
    }
}
