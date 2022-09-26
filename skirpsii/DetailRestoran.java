package com.example.skirpsii;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailRestoran extends AppCompatActivity {

    TextView nama_restoran,jenis_restoran;
    ListView listView;
    ArrayList<DataDetailRestoran> model;
    Context context;
    CheckBox favorit;
    FirebaseAuth fAuth;
    String userID;
    TextView total,kelayakan,suasana,pelayanan,fasilitas,higienis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_detail_restoran);

        fAuth = FirebaseAuth.getInstance();
        nama_restoran = findViewById(R.id.tvres);
        jenis_restoran = findViewById(R.id.jenisrestoran);
        listView = findViewById(R.id.list_view);
        favorit = findViewById(R.id.favorit);
        total = findViewById(R.id.totalrating);
        kelayakan = findViewById(R.id.ratingkelayakan);
        suasana = findViewById(R.id.ratingsuasana);
        pelayanan = findViewById(R.id.ratingpelayanan);
        fasilitas = findViewById(R.id.ratingtemp);
        higienis = findViewById(R.id.ratinghigienis);
        userID = fAuth.getCurrentUser().getUid();

        nama_restoran.setText(getIntent().getStringExtra("nama_restoran"));
        jenis_restoran.setText(getIntent().getStringExtra("jenis_restoran"));

        tampildata();

        setImage(getIntent().getStringExtra("foto_resto"));
        DecimalFormat formater = new DecimalFormat("0.00");

        String url2 = "https://restoratori.mikolindosorong.id/mobile/hitungratingrestoran?data="+getIntent().getStringExtra("id_restoran");
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray getHasil = jsonObject.getJSONArray("hasil");
                    double fasilitas1 = 0;
                    double kelayakan1 = 0;
                    double suasana1 = 0;
                    double higienis1 = 0;
                    double pelayanan1 = 0;
                    for (int i=0;i<getHasil.length();i++){
                        JSONObject getData = getHasil.getJSONObject(i);
                        String fasilitass = getData.getString("fasilitas");
                        String kelayakann = getData.getString("kelayakan_harga");
                        String suasanaa = getData.getString("suasana");
                        String higieniss = getData.getString("higienis");
                        String pelayanann = getData.getString("pelayanan");
                        String[] strings = fasilitass.split("\\.");
                        String[] strings1 = kelayakann.split("\\.");
                        String[] strings2 = suasanaa.split("\\.");
                        String[] strings3 = higieniss.split("\\.");
                        String[] strings4 = pelayanann.split("\\.");
                        double fasilitasangka = Integer.parseInt(strings[0]);
                        double kelayakanangka = Integer.parseInt(strings1[0]);
                        double suasanaangka = Integer.parseInt(strings2[0]);
                        double higienisangka = Integer.parseInt(strings3[0]);
                        double pelayananangka = Integer.parseInt(strings4[0]);
                        fasilitas1 = fasilitas1 + fasilitasangka;
                        kelayakan1 = kelayakan1 + kelayakanangka;
                        suasana1 = suasana1 + suasanaangka;
                        higienis1 = higienis1 + higienisangka;
                        pelayanan1 = pelayanan1 + pelayananangka;
                    }
                    if(getHasil.length() == 0){
                        kelayakan.setText("0");
                        suasana.setText("0");
                        pelayanan.setText("0");
                        fasilitas.setText("0");
                        higienis.setText("0");
                        total.setText("0");
                    }else{
                        fasilitas1 = fasilitas1/getHasil.length();
                        kelayakan1 = kelayakan1/getHasil.length();
                        suasana1 = suasana1/getHasil.length();
                        higienis1 = higienis1/getHasil.length();
                        pelayanan1 = pelayanan1/getHasil.length();
                        kelayakan.setText(formater.format(kelayakan1));
                        suasana.setText(formater.format(suasana1));
                        pelayanan.setText(formater.format(pelayanan1));
                        fasilitas.setText(formater.format(fasilitas1));
                        higienis.setText(formater.format(higienis1));
                        double temp = (fasilitas1+kelayakan1+suasana1+higienis1+pelayanan1)/5;
                        total.setText(formater.format(temp));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        requestQueue2.add(stringRequest2);

        String url = "https://restoratori.mikolindosorong.id/mobile/carifavoritrestorandetail?data="+userID.toString()+"&data2="+getIntent().getStringExtra("id_restoran");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray getHasil = jsonObject.getJSONArray("hasil");
                    if(getHasil.length()>0){
                        favorit.setChecked(true);
                    }
                    else {
                        favorit.setChecked(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

        favorit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(favorit.isChecked()){
                    simpanfavorit();
                }
                else {
                    deletefavorit(userID,getIntent().getStringExtra("id_restoran"));
                }
            }
        });
    }

    void deletefavorit(String id_konsumen,String id_restoran){
        String url = "https://restoratori.mikolindosorong.id/mobile/deletefavorit";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"Dihapus dari Favorit",Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();

                params.put("id_konsumen",id_konsumen);
                params.put("id_restoran",id_restoran);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    void simpanfavorit(){
        String url = "https://restoratori.mikolindosorong.id/mobile/simpanfavorit";
        userID = fAuth.getCurrentUser().getUid();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"Tersimpan ke Favorit",Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();

                params.put("id_restoran",getIntent().getStringExtra("id_restoran"));
                params.put("id_konsumen",userID);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    void tampildata(){
        String url = "https://restoratori.mikolindosorong.id/mobile/detailrestoran.php?data="+getIntent().getStringExtra("id_restoran").toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray getHasil = jsonObject.getJSONArray("hasil");
                    model = new ArrayList<>();
                    for (int i=0;i<getHasil.length();i++){
                        JSONObject getData = getHasil.getJSONObject(i);
                        String status_restoran = getData.getString("status_restoran");
                        String kontak = getData.getString("kontak");
                        String tingkat_harga =getData.getString("tingkat_harga");
                        String fasilitas_restoran = getData.getString("fasilitas_restoran");
                        String alamat = getData.getString("alamat");
                        String daerah_restoran = getData.getString("daerah_restoran");
                        String jam_buka =getData.getString("jam_buka");
                        String deskripsi = getData.getString("deskripsi");
                        model.add(new DataDetailRestoran(status_restoran,kontak,tingkat_harga,fasilitas_restoran,alamat,daerah_restoran,jam_buka,deskripsi));
                    }
                    AdapterDataRestoran adapter = new AdapterDataRestoran(DetailRestoran.this,model);
                    listView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailRestoran.this,error.toString(),Toast.LENGTH_SHORT).show();
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

    public void ReservasiRestoran(View view) {
        Intent i = new Intent(this,DaftarMenuReservasi.class);
        i.putExtra("id_restoran",getIntent().getStringExtra("id_restoran"));
        i.putExtra("nama_restoran",getIntent().getStringExtra("nama_restoran"));
        i.putExtra("jenis_restoran",getIntent().getStringExtra("jenis_restoran"));
        i.putExtra("foto_resto",getIntent().getStringExtra("foto_resto"));
        i.putExtra("status_restoran",getIntent().getStringExtra("status_restoran"));
        i.putExtra("rekening_pembayaran",getIntent().getStringExtra("rekening_pembayaran"));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(i);
    }

    public void ReviewRestoranPage(View view) {
        Intent i = new Intent(this,ReviewRestoranPage.class);
        i.putExtra("id_restoran",getIntent().getStringExtra("id_restoran"));
        i.putExtra("nama_restoran",getIntent().getStringExtra("nama_restoran"));
        i.putExtra("jenis_restoran",getIntent().getStringExtra("jenis_restoran"));
        i.putExtra("alamat",getIntent().getStringExtra("alamat"));
        i.putExtra("foto_resto",getIntent().getStringExtra("foto_resto"));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(i);
    }

    public void DaftarMenuRestoran(View view) {
        Intent i = new Intent(this,daftar_menu_restoran.class);
        i.putExtra("id_restoran",getIntent().getStringExtra("id_restoran"));
        i.putExtra("nama_restoran",getIntent().getStringExtra("nama_restoran"));
        i.putExtra("jenis_restoran",getIntent().getStringExtra("jenis_restoran"));
        i.putExtra("alamat",getIntent().getStringExtra("alamat"));
        i.putExtra("foto_resto",getIntent().getStringExtra("foto_resto"));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(i);
    }

    public void ReviewRestoranDetail(View view) {
        Intent i = new Intent(this,ReviewRestoran.class);
        i.putExtra("id_restoran",getIntent().getStringExtra("id_restoran"));
        i.putExtra("nama_restoran",getIntent().getStringExtra("nama_restoran"));
        i.putExtra("jenis_restoran",getIntent().getStringExtra("jenis_restoran"));
        i.putExtra("alamat",getIntent().getStringExtra("alamat"));
        i.putExtra("foto_resto",getIntent().getStringExtra("foto_resto"));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(i);
    }

    private void setImage(String foto_resto){

        ImageView image;
        image = findViewById(R.id.dtlimgres);
        Glide.with(this).asBitmap().load(foto_resto).into(image);
    }

}

class AdapterDataRestoran extends BaseAdapter {
    ArrayList<DataDetailRestoran> model;
    LayoutInflater inflater;
    Context context;

    AdapterDataRestoran(Context context,ArrayList<DataDetailRestoran> model){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int i) {
        return model.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    TextView status_restoran,kontak,tingkat_harga,fasilitas_restoran,alamat,daerah_restoran,jam_buka,deskripsi;
    Button tes;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.detaildatarestoran,viewGroup,false);
        status_restoran = v.findViewById(R.id.tvstatus);
        kontak = v.findViewById(R.id.tvnotelp);
        tingkat_harga = v.findViewById(R.id.tvtingkatharga);
        fasilitas_restoran = v.findViewById(R.id.tvfasilitas);
        alamat = v.findViewById(R.id.tvalamat);
        daerah_restoran = v.findViewById(R.id.tvdaerah);
        jam_buka = v.findViewById(R.id.tvjambuka);
        deskripsi = v.findViewById(R.id.tvdeskripsi);

        status_restoran.setText(model.get(i).getStatus_restoran());
        kontak.setText(model.get(i).getKontak());
        tingkat_harga.setText(model.get(i).getTingkat_harga());
        fasilitas_restoran.setText(model.get(i).getFasilitas_restoran());
        alamat.setText(model.get(i).getAlamat());
        daerah_restoran.setText(model.get(i).getDaerah_restoran());
        jam_buka.setText(model.get(i).getJam_buka());
        deskripsi.setText(model.get(i).getDeskripsi());

        return v;
    }

}

