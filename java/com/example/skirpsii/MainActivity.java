package com.example.skirpsii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView,recyclerView2,recyclerView3,recyclerView4,recyclerView5,recyclerView6;
    ArrayList<hasilsearch> model;
    ArrayList<historyreservasidata> model2;
    ArrayList<daftarmenurestoran> model3;
    ArrayList<clusterrestoran> model4;
    FirebaseAuth fAuth;
    String userID;
    TextView tvreservasi;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(layoutManager2);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView3 = findViewById(R.id.recyclerView3);
        recyclerView3.setLayoutManager(layoutManager3);

        LinearLayoutManager layoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView4 = findViewById(R.id.reservasiberjalan);
        recyclerView4.setLayoutManager(layoutManager4);

        LinearLayoutManager layoutManager5 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView5 = findViewById(R.id.rekomendasirestoran);
        recyclerView5.setLayoutManager(layoutManager5);

        LinearLayoutManager layoutManager6 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView6 = findViewById(R.id.rekomendasimenu);
        recyclerView6.setLayoutManager(layoutManager6);

        tvreservasi = findViewById(R.id.tvreservasi);

        fAuth = FirebaseAuth.getInstance();
        swipeRefreshLayout = findViewById(R.id.swp);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ambilcluster();
                ambilclustermenu();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        ambilcluster();
        ambilclustermenu();
        tampilandata();
        tampilandata2();
        tampilandata3();
        reservasi();
    }

    void reservasi(){
        userID = fAuth.getCurrentUser().getUid();
        String url = "https://restoratori.mikolindosorong.id/mobile/reservasisekarang.php?data="+userID;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray getHasil = jsonObject.getJSONArray("hasil");
                    model2 = new ArrayList<>();
                    if(getHasil.length()>0){
                        for (int i=0;i<getHasil.length();i++){
                            JSONObject getData = getHasil.getJSONObject(i);
                            String nama_restoran = getData.getString("nama_restoran");
                            String jadwal_reservasi =getData.getString("jadwal_reservasi");
                            String jumlah_orang = getData.getString("jumlah_orang");
                            String status_reservasi = getData.getString("status_reservasi");
                            String jenis_resto = getData.getString("jenis_restoran");
                            String foto_resto = getData.getString("foto_resto");
                            String id_restoran = getData.getString("id_restoran");
                            String id_reservasi = getData.getString("id_reservasi");
                            String id_seluruh_menu = "";
                            model2.add(new historyreservasidata(nama_restoran,jadwal_reservasi,jumlah_orang,status_reservasi,jenis_resto,foto_resto,id_restoran,id_reservasi,id_seluruh_menu));
                        }
                        AdapterHistoryReservasi adapter = new AdapterHistoryReservasi( MainActivity.this,model2);
                        recyclerView4.setAdapter(adapter);
                        tvreservasi.setVisibility(View.GONE);
                    }
                    else{
                        recyclerView4.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void SearchPage(View view) {
        Intent i = new Intent(this,SearchPage.class);
        startActivity(i);
    }
    public void FavoritRestoranPage(View view) {
        Intent i = new Intent (".FavoritRestoranPage");
        startActivity(i);
    }

    public void HistoryReservasi(View view) {
        Intent i = new Intent (this,HistoryReservasi.class);
        startActivity(i);
    }

    public void PageAkun(View view) {
        Intent i = new Intent (".PageAkun");
        startActivity(i);
    }

    public void ReservasiRestoran(View view) {
        Intent i = new Intent (this,teshasil.class);
        startActivity(i);
    }

    public void ambilcluster(){
        final String[] kluster = new String[1];
        ArrayList<String> array = new ArrayList<>();
        ArrayList<String> datarestoran = new ArrayList<>();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        String url = "https://restoratori.mikolindosorong.id/mobile/ambilclusteruser.php?data="+userID;
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray getHasil = jsonObject.getJSONArray("hasil");
                    JSONObject getData = getHasil.getJSONObject(0);
                    kluster[0] = getData.getString("kluster_restoran");
                    String url = "https://restoratori.mikolindosorong.id/mobile/ambilcluster.php?data="+kluster[0];
                    StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray getHasil = jsonObject.getJSONArray("hasil");
                                for (int e=0;e<getHasil.length();e++) {
                                    JSONObject getData = getHasil.getJSONObject(e);
                                    String id_konsumen = getData.getString("id_konsumen");
                                    array.add(id_konsumen);
                                }for(int i = 0 ; i<array.size();i++) {
                                    String url = "https://restoratori.mikolindosorong.id/mobile/ambilclusterdatarestoran.php?data=" + array.get(i);
                                    int finalI = i;
                                    StringRequest stringRequest3 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                JSONArray getHasil = jsonObject.getJSONArray("hasil");
                                                for (int w = 0; w < getHasil.length(); w++) {
                                                    JSONObject getData = getHasil.getJSONObject(w);
                                                    String id_restoran = getData.getString("id_restoran");
                                                    if (datarestoran.contains(id_restoran)) {
                                                    } else {
                                                        datarestoran.add(id_restoran);
                                                    }
                                                }
                                                if(array.size()-1 == finalI){
                                                    for(int b = 0 ; b<array.size();b++) {
                                                        String url = "https://restoratori.mikolindosorong.id/mobile/ambilclusterdatarestoranfavorit.php?data=" + array.get(b);
                                                        int finalB = b;
                                                        StringRequest stringRequest4 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                    JSONArray getHasil = jsonObject.getJSONArray("hasil");
                                                                    for (int z = 0; z < getHasil.length(); z++) {
                                                                        JSONObject getData = getHasil.getJSONObject(z);
                                                                        String id_restoran = getData.getString("id_restoran");
                                                                        if (datarestoran.contains(id_restoran)) {
                                                                        } else {
                                                                            datarestoran.add(id_restoran);
                                                                        }
                                                                    }
                                                                    if(array.size()-1 == finalI && array.size()-1 == finalB) {
                                                                        model4 = new ArrayList<>();
                                                                        ArrayList<String> temp = new ArrayList<>();
                                                                        for (int c = 0 ; c <datarestoran.size();c++){
                                                                            String url = "https://restoratori.mikolindosorong.id/mobile/tampilrekomendasirestoran?data="+datarestoran.get(c);
                                                                            int finalC = c;
                                                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                                                @Override
                                                                                public void onResponse(String response) {
                                                                                    try {
                                                                                        JSONObject jsonObject = new JSONObject(response);
                                                                                        JSONArray getHasil = jsonObject.getJSONArray("hasil");
                                                                                        JSONObject getData = getHasil.getJSONObject(0);
                                                                                        String id_restoran = getData.getString("id_restoran");
                                                                                        String nama_restoran = getData.getString("nama_restoran");
                                                                                        String jenis_restoran =getData.getString("jenis_restoran");
                                                                                        String alamat = getData.getString("alamat");
                                                                                        String foto_resto = getData.getString("foto_resto");
                                                                                        String status_restoran = getData.getString("status_restoran");
                                                                                        String rekening_pembayaran = getData.getString("rekening_pembayaran");
                                                                                        model4.add(new clusterrestoran(id_restoran,nama_restoran,jenis_restoran,alamat,foto_resto,status_restoran,rekening_pembayaran));
                                                                                        if(datarestoran.size()-1 == finalC){
                                                                                            AdapterRestoranCluster adapter2 = new AdapterRestoranCluster(MainActivity.this,model4);
                                                                                            recyclerView5.setAdapter(adapter2);
                                                                                        }else {
                                                                                        }
                                                                                    } catch (JSONException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                }
                                                                            }, new Response.ErrorListener() {
                                                                                @Override
                                                                                public void onErrorResponse(VolleyError error) {
                                                                                    Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                            );
                                                                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                                                            requestQueue.add(stringRequest);
                                                                        }
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        );
                                                        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                                                        requestQueue1.add(stringRequest4);
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    );
                                    RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
                                    requestQueue2.add(stringRequest3);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    );
                    RequestQueue requestQueue3 = Volley.newRequestQueue(getApplicationContext());
                    requestQueue3.add(stringRequest2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue4 = Volley.newRequestQueue(getApplicationContext());
        requestQueue4.add(stringRequest1);
    }

    public void ambilclustermenu(){
        final String[] kluster = new String[1];
        ArrayList<String> array = new ArrayList<>();
        ArrayList<String> datamenu = new ArrayList<>();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        String url = "https://restoratori.mikolindosorong.id/mobile/ambilclusterusermenu.php?data="+userID;
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray getHasil = jsonObject.getJSONArray("hasil");
                    JSONObject getData = getHasil.getJSONObject(0);
                    kluster[0] = getData.getString("kluster_menu");
                    String url = "https://restoratori.mikolindosorong.id/mobile/ambilclustermenu.php?data="+kluster[0];
                    StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray getHasil = jsonObject.getJSONArray("hasil");
                                for (int e=0;e<getHasil.length();e++) {
                                    JSONObject getData = getHasil.getJSONObject(e);
                                    String id_konsumen = getData.getString("id_konsumen");
                                    array.add(id_konsumen);
                                }for(int i = 0 ; i<array.size();i++) {
                                    String url = "https://restoratori.mikolindosorong.id/mobile/ambilclusterdatamenu.php?data=" + array.get(i);
                                    int finalI = i;
                                    StringRequest stringRequest3 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                JSONArray getHasil = jsonObject.getJSONArray("hasil");
                                                for (int w = 0; w < getHasil.length(); w++) {
                                                    JSONObject getData = getHasil.getJSONObject(w);
                                                    String id_seluruh_menu = getData.getString("id_seluruh_menu");
                                                    String[] strings = id_seluruh_menu.split(",");
                                                    for(int n = 0 ; n<strings.length;n++){
                                                        if (datamenu.contains(strings[n])) {
                                                        } else {
                                                            datamenu.add(strings[n]);
                                                        }
                                                    }
                                                }
                                                if(array.size()-1 == finalI){
                                                    for(int b = 0 ; b<array.size();b++) {
                                                        String url = "https://restoratori.mikolindosorong.id/mobile/ambilclusterdatamenufavorit.php?data=" + array.get(b);
                                                        int finalB = b;
                                                        StringRequest stringRequest4 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                    JSONArray getHasil = jsonObject.getJSONArray("hasil");
                                                                    for (int z = 0; z < getHasil.length(); z++) {
                                                                        JSONObject getData = getHasil.getJSONObject(z);
                                                                        String id_menu = getData.getString("id_menu");
                                                                        if (datamenu.contains(id_menu)) {
                                                                        } else {
                                                                            datamenu.add(id_menu);
                                                                        }
                                                                    }
                                                                    if(array.size()-1 == finalI && array.size()-1 == finalB) {
                                                                        model3 = new ArrayList<>();
                                                                        for (int c = 0 ; c <datamenu.size();c++){
                                                                            String url = "https://restoratori.mikolindosorong.id/mobile/tampilrekomendasimenu?data="+datamenu.get(c);
                                                                            int finalC = c;
                                                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                                                @Override
                                                                                public void onResponse(String response) {
                                                                                    try {
                                                                                        JSONObject jsonObject = new JSONObject(response);
                                                                                        JSONArray getHasil = jsonObject.getJSONArray("hasil");
                                                                                        JSONObject getData = getHasil.getJSONObject(0);
                                                                                        String id_restoran = getData.getString("id_restoran");
                                                                                        String id_menu = getData.getString("id_menu");
                                                                                        String nama_menu = getData.getString("nama_menu");
                                                                                        String tag_menu =getData.getString("tag_menu");
                                                                                        String harga = getData.getString("harga");
                                                                                        String foto_menu = getData.getString("foto_menu");
                                                                                        String deskripsi = getData.getString("deskripsi");
                                                                                        model3.add(new daftarmenurestoran(id_menu,id_restoran,nama_menu,foto_menu,harga,tag_menu,deskripsi));
                                                                                        if(datamenu.size()-1 == finalC){
                                                                                            AdapterMenuCluster adapter2 = new AdapterMenuCluster(MainActivity.this,model3);
                                                                                            recyclerView6.setAdapter(adapter2);
                                                                                        }else {
                                                                                        }
                                                                                    } catch (JSONException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                }
                                                                            }, new Response.ErrorListener() {
                                                                                @Override
                                                                                public void onErrorResponse(VolleyError error) {
                                                                                    Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                            );
                                                                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                                                            requestQueue.add(stringRequest);
                                                                        }
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        );
                                                        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                                                        requestQueue1.add(stringRequest4);
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    );
                                    RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
                                    requestQueue2.add(stringRequest3);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    );
                    RequestQueue requestQueue3 = Volley.newRequestQueue(getApplicationContext());
                    requestQueue3.add(stringRequest2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue4 = Volley.newRequestQueue(getApplicationContext());
        requestQueue4.add(stringRequest1);
    }

    void tampilandata(){
        String url = "https://restoratori.mikolindosorong.id/mobile/tampilandepan.php?data="+"Cafe";
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
                    AdapterTampilanDepan adapter = new AdapterTampilanDepan(MainActivity.this,model);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    void tampilandata2(){
        String url = "https://restoratori.mikolindosorong.id/mobile/tampilandepan.php?data="+"Continental Restaurant";
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
                    AdapterTampilanDepan adapter = new AdapterTampilanDepan(MainActivity.this,model);
                    recyclerView2.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    void tampilandata3(){
        String url = "https://restoratori.mikolindosorong.id/mobile/tampilandepan.php?data="+"Snack Bar";
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
                    AdapterTampilanDepan adapter = new AdapterTampilanDepan(MainActivity.this,model);
                    recyclerView3.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}

class AdapterTampilanDepan extends RecyclerView.Adapter<AdapterTampilanDepan.ViewHolder> {
    ArrayList<hasilsearch> model;
    LayoutInflater inflater;
    Context context;

    AdapterTampilanDepan(Context context,ArrayList<hasilsearch> model){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.from(parent.getContext()).inflate(R.layout.main_menu_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        hasilsearch hasilsearch = model.get(position);

        holder.nama_restoran.setText(model.get(position).getNama_restoran());
        Glide.with(context).load(model.get(position).getFoto_resto()).into(holder.foto_resto);

        holder.foto_resto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context,DetailRestoran.class);
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

        TextView nama_restoran;
        ImageButton foto_resto;

        public ViewHolder(View itemView) {
            super(itemView);
            nama_restoran = itemView.findViewById(R.id.name);
            foto_resto = itemView.findViewById(R.id.image);

        }
    }
}

class AdapterMenuCluster extends RecyclerView.Adapter<AdapterMenuCluster.ViewHolder> {
    ArrayList<daftarmenurestoran> model;
    LayoutInflater inflater;
    Context context;

    AdapterMenuCluster(Context context,ArrayList<daftarmenurestoran> model){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.from(parent.getContext()).inflate(R.layout.main_menu_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        daftarmenurestoran daftarmenurestoran = model.get(position);

        holder.nama_restoran.setText(model.get(position).getNama_menu());
        Glide.with(context).load(model.get(position).getFoto_menu()).into(holder.foto_resto);

        holder.foto_resto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context,ReviewMenu.class);
                intent.putExtra("id_menu",daftarmenurestoran.getId_menu());
                intent.putExtra("nama_menu",daftarmenurestoran.getNama_menu());
                intent.putExtra("harga",daftarmenurestoran.getHarga());
                intent.putExtra("tag_menu",daftarmenurestoran.getTag_menu());
                intent.putExtra("foto_menu",daftarmenurestoran.getFoto_menu());
                intent.putExtra("deskripsi",daftarmenurestoran.getDeskripsi());
                intent.putExtra("id_restoran",daftarmenurestoran.getId_restoran());
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

        TextView nama_restoran;
        ImageButton foto_resto;

        public ViewHolder(View itemView) {
            super(itemView);
            nama_restoran = itemView.findViewById(R.id.name);
            foto_resto = itemView.findViewById(R.id.image);

        }
    }
}

class AdapterRestoranCluster extends RecyclerView.Adapter<AdapterRestoranCluster.ViewHolder> {
    ArrayList<clusterrestoran> model;
    LayoutInflater inflater;
    Context context;

    AdapterRestoranCluster(Context context,ArrayList<clusterrestoran> model){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.from(parent.getContext()).inflate(R.layout.main_menu_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        clusterrestoran clusterrestoran = model.get(position);

        holder.nama_restoran.setText(model.get(position).getNama_restoran());
        Glide.with(context).load(model.get(position).getFoto_resto()).into(holder.foto_resto);

        holder.foto_resto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context,DetailRestoran.class);
                intent.putExtra("id_restoran",clusterrestoran.getId_restoran());
                intent.putExtra("nama_restoran",clusterrestoran.getNama_restoran());
                intent.putExtra("jenis_restoran",clusterrestoran.getJenis_restoran());
                intent.putExtra("alamat",clusterrestoran.getAlamat());
                intent.putExtra("foto_resto",clusterrestoran.getFoto_resto());
                intent.putExtra("status_restoran",clusterrestoran.getStatus_restoran());
                intent.putExtra("rekening_pembayaran",clusterrestoran.getRekening_pembayaran());
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

        TextView nama_restoran;
        ImageButton foto_resto;

        public ViewHolder(View itemView) {
            super(itemView);
            nama_restoran = itemView.findViewById(R.id.name);
            foto_resto = itemView.findViewById(R.id.image);

        }
    }
}