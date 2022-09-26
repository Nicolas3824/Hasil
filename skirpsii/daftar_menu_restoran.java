package com.example.skirpsii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class daftar_menu_restoran extends AppCompatActivity {

    TextView nama_restoran,jenis_restoran;
    ImageView foto_resto;
    ArrayList<daftarmenurestoran> model;
    RecyclerView recyclerView;
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
        setContentView(R.layout.activity_daftar_menu_restoran);

        fAuth = FirebaseAuth.getInstance();
        nama_restoran = findViewById(R.id.tvres);
        jenis_restoran = findViewById(R.id.jenisrestoran);
        foto_resto = findViewById(R.id.dtlimgres);
        favorit = findViewById(R.id.favorit);
        total = findViewById(R.id.totalrating);
        kelayakan = findViewById(R.id.ratingkelayakan);
        suasana = findViewById(R.id.ratingsuasana);
        pelayanan = findViewById(R.id.ratingpelayanan);
        fasilitas = findViewById(R.id.ratingtemp);
        higienis = findViewById(R.id.ratinghigienis);
        userID = fAuth.getCurrentUser().getUid();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.menurestoran);
        recyclerView.setLayoutManager(layoutManager);

        Glide.with(this).load(getIntent().getStringExtra("foto_resto")).into(foto_resto);
        nama_restoran.setText(getIntent().getStringExtra("nama_restoran"));
        jenis_restoran.setText(getIntent().getStringExtra("jenis_restoran"));

        tampilandata();

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

    void tampilandata(){
        String url = "https://restoratori.mikolindosorong.id/mobile/daftarmenu.php?data="+getIntent().getStringExtra("id_restoran");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray getHasil = jsonObject.getJSONArray("hasil");
                    model = new ArrayList<>();
                    for (int i=0;i<getHasil.length();i++){
                        JSONObject getData = getHasil.getJSONObject(i);
                        String id_menu = getData.getString("id_menu");
                        String id_restoran = getData.getString("id_restoran");
                        String nama_menu = getData.getString("nama_menu");
                        String foto_menu =getData.getString("foto_menu");
                        String harga = getData.getString("harga");
                        String tag_menu = getData.getString("tag_menu");
                        String deskripsi = getData.getString("deskripsi");
                        model.add(new daftarmenurestoran(id_menu,id_restoran,nama_menu,foto_menu,harga,tag_menu,deskripsi));
                    }
                    AdapterDaftarMenu adapter = new AdapterDaftarMenu(daftar_menu_restoran.this,model);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(daftar_menu_restoran.this,error.toString(),Toast.LENGTH_SHORT).show();
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


    public void DetailRestoranPage(View view) {
        Intent i = new Intent(this,DetailRestoran.class);
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
}

class AdapterDaftarMenu extends RecyclerView.Adapter<AdapterDaftarMenu.ViewHolder> {
    ArrayList<daftarmenurestoran> model;
    LayoutInflater inflater;
    Context context;

    AdapterDaftarMenu(Context context,ArrayList<daftarmenurestoran> model){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.from(parent.getContext()).inflate(R.layout.tampilandaftarmenu,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        daftarmenurestoran daftarmenurestoran = model.get(position);

        holder.nama_menu.setText(model.get(position).getNama_menu());
        holder.harga.setText(model.get(position).getHarga());
        holder.deskripsi.setText(model.get(position).getDeskripsi());
        Glide.with(context).asBitmap().load(model.get(position).getFoto_menu()).into(holder.foto_menu);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context,ReviewMenu.class);
                intent.putExtra("id_menu",daftarmenurestoran.getId_menu());
                intent.putExtra("id_restoran",daftarmenurestoran.getId_restoran());
                intent.putExtra("nama_menu",daftarmenurestoran.getNama_menu());
                intent.putExtra("foto_menu",daftarmenurestoran.getFoto_menu());
                intent.putExtra("deskripsi",daftarmenurestoran.getDeskripsi());
                intent.putExtra("harga",daftarmenurestoran.getHarga());
                intent.putExtra("tag_menu",daftarmenurestoran.getTag_menu());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return model.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nama_menu,harga,deskripsi;
        ImageButton foto_menu;

        public ViewHolder(View itemView) {
            super(itemView);
            nama_menu = itemView.findViewById(R.id.nama_menu);
            harga = itemView.findViewById(R.id.hargamenu);
            deskripsi = itemView.findViewById(R.id.deskripsimenu);
            foto_menu = itemView.findViewById(R.id.foto_menu);
        }
    }
}
