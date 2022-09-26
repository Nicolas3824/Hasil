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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailMenu extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<hasilsearch> model;
    TextView tvmenu,tagmenu,hargamenu;
    CheckBox favorit;
    FirebaseAuth fAuth;
    String userID;
    ImageView foto_menu;
    TextView total,kelayakan,suasana,pelayanan,fasilitas,higienis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_detail_menu);

        fAuth = FirebaseAuth.getInstance();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.restoran);
        recyclerView.setLayoutManager(layoutManager);

        tvmenu = findViewById(R.id.tvmenu);
        tagmenu = findViewById(R.id.tagmenu);
        hargamenu = findViewById(R.id.hargamenu);
        favorit = findViewById(R.id.favorit);
        foto_menu = findViewById(R.id.dtlimgmen);
        total = findViewById(R.id.totalrating);
        kelayakan = findViewById(R.id.ratingkelayakan);
        suasana = findViewById(R.id.ratingsuasana);
        pelayanan = findViewById(R.id.ratingpelayanan);
        fasilitas = findViewById(R.id.ratingtemp);
        higienis = findViewById(R.id.ratinghigienis);
        userID = fAuth.getCurrentUser().getUid();

        tvmenu.setText(getIntent().getStringExtra("nama_menu"));
        tagmenu.setText(getIntent().getStringExtra("tag_menu"));
        hargamenu.setText(getIntent().getStringExtra("harga"));
        Glide.with(this).load(getIntent().getStringExtra("foto_menu")).into(foto_menu);

        tampilandata();

        DecimalFormat formater = new DecimalFormat("0.00");

        String url2 = "https://restoratori.mikolindosorong.id/mobile/hitungratingmenu?data="+getIntent().getStringExtra("id_menu");
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
                        String fasilitass = getData.getString("rasa");
                        String kelayakann = getData.getString("kesehatan");
                        String suasanaa = getData.getString("kualitas");
                        String higieniss = getData.getString("harga");
                        String pelayanann = getData.getString("ukuran");
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

        String url = "https://restoratori.mikolindosorong.id/mobile/carifavoritmenudetail?data="+userID.toString()+"&data2="+getIntent().getStringExtra("id_menu");
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
                    deletefavorit(userID,getIntent().getStringExtra("id_menu"));
                }
            }
        });

    }

    void deletefavorit(String id_konsumen,String id_menu){
        String url = "https://restoratori.mikolindosorong.id/mobile/deletefavoritmenu";
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
                params.put("id_menu",id_menu);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    void simpanfavorit(){
        String url = "https://restoratori.mikolindosorong.id/mobile/simpanfavoritmenu";
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

                params.put("id_menu",getIntent().getStringExtra("id_menu"));
                params.put("id_konsumen",userID);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    void tampilandata(){
        String url = "https://restoratori.mikolindosorong.id/mobile/detailrestoran.php?data="+getIntent().getStringExtra("id_restoran");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray getHasil = jsonObject.getJSONArray("hasil");
                    model = new ArrayList<>();
                    for (int i=0;i<getHasil.length();i++){
                        JSONObject getData = getHasil.getJSONObject(i);
                        String id_restoran = getData.getString("id_restoran");
                        String nama_restoran = getData.getString("nama_restoran");
                        String jenis_restoran =getData.getString("jenis_restoran");
                        String alamat = getData.getString("alamat");
                        String foto_resto = getData.getString("foto_resto");
                        String status_restoran = getData.getString("status_restoran");
                        String rekening_pembayaran = getData.getString("rekening_pembayaran");
                        model.add(new hasilsearch(id_restoran,nama_restoran,jenis_restoran,alamat,foto_resto,status_restoran,rekening_pembayaran));
                    }
                    AdapterDetailMenuRestoran adapter = new AdapterDetailMenuRestoran(DetailMenu.this,model);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailMenu.this,error.toString(),Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent (".ReviewRestoranPage");
        startActivity(i);
    }

    public void ReservasiRestoran(View view) {
        Intent i = new Intent (".BuatReservasi");
        startActivity(i);
    }
    public void ReviewMenuPage(View view) {
        Intent i = new Intent (this,ReviewMenuPage.class);
        startActivity(i);
    }

    public void ReviewMenu(View view) {
        Intent i = new Intent (this,ReviewMenu.class);
        i.putExtra("id_menu",getIntent().getStringExtra("id_menu"));
        i.putExtra("id_restoran",getIntent().getStringExtra("id_restoran"));
        i.putExtra("nama_menu",getIntent().getStringExtra("nama_menu"));
        i.putExtra("foto_menu",getIntent().getStringExtra("foto_menu"));
        i.putExtra("deskripsi",getIntent().getStringExtra("deskripsi"));
        i.putExtra("harga",getIntent().getStringExtra("harga"));
        i.putExtra("tag_menu",getIntent().getStringExtra("tag_menu"));
        startActivity(i);
    }
}

class AdapterDetailMenuRestoran extends RecyclerView.Adapter<AdapterDetailMenuRestoran.ViewHolder> {
    ArrayList<hasilsearch> model;
    LayoutInflater inflater;
    Context context;

    AdapterDetailMenuRestoran(Context context,ArrayList<hasilsearch> model){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.from(parent.getContext()).inflate(R.layout.tampilandatamenurestoran,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        hasilsearch hasilsearch = model.get(position);

        holder.jenis_restoran.setText(model.get(position).getJenis_restoran());
        holder.nama_restoran.setText(model.get(position).getNama_restoran());
        Glide.with(context).load(model.get(position).getFoto_resto()).into(holder.foto_resto);

        holder.reservasi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context,DaftarMenuReservasi.class);
                intent.putExtra("id_restoran",hasilsearch.getId_restoran());
                intent.putExtra("nama_restoran",hasilsearch.getNama_restoran());
                intent.putExtra("jenis_restoran",hasilsearch.getJenis_restoran());
                intent.putExtra("alamat",hasilsearch.getAlamat());
                intent.putExtra("foto_resto",hasilsearch.getFoto_resto());
                intent.putExtra("status_restoran",hasilsearch.getStatus_restoran());
                intent.putExtra("rekening_pembayaran",hasilsearch.getRekening_pembayaran());
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

        TextView nama_restoran,jenis_restoran;
        ImageView foto_resto;
        Button reservasi,review;

        public ViewHolder(View itemView) {
            super(itemView);
            nama_restoran = itemView.findViewById(R.id.tvres);
            jenis_restoran = itemView.findViewById(R.id.jenisrestoran);
            foto_resto = itemView.findViewById(R.id.dtlimgres);
            reservasi = itemView.findViewById(R.id.btnres);
        }
    }
}


