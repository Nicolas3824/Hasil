package com.example.skirpsii;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;

public class HistoryReservasi extends AppCompatActivity {

    RecyclerView recyclerView,recyclerView2;
    ArrayList<historyreservasidata> model;
    FirebaseAuth fAuth;
    String userID;
    TextView tvreservasi,tvriwayat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_history_reservasi);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.reservasiberjalan);
        recyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView2 = findViewById(R.id.riwayatreservasi);
        recyclerView2.setLayoutManager(layoutManager2);

        tvreservasi = findViewById(R.id.tvreservasi);
        tvriwayat = findViewById(R.id.tvriwayat);

        fAuth = FirebaseAuth.getInstance();

        historyreservasi();
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
                    model = new ArrayList<>();
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
                            model.add(new historyreservasidata(nama_restoran,jadwal_reservasi,jumlah_orang,status_reservasi,jenis_resto,foto_resto,id_restoran,id_reservasi,id_seluruh_menu));
                        }
                        AdapterHistoryReservasi adapter = new AdapterHistoryReservasi(HistoryReservasi.this,model);
                        recyclerView.setAdapter(adapter);
                        tvreservasi.setVisibility(View.GONE);
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
                Toast.makeText(HistoryReservasi.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    void historyreservasi(){
        userID = fAuth.getCurrentUser().getUid();
        String url = "https://restoratori.mikolindosorong.id/mobile/historyreservasi.php?data="+userID;
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
                            String nama_restoran = getData.getString("nama_restoran");
                            String id_restoran = getData.getString("id_restoran");
                            String jadwal_reservasi =getData.getString("jadwal_reservasi");
                            String jumlah_orang = getData.getString("jumlah_orang");
                            String status_reservasi = getData.getString("status_reservasi");
                            String jenis_resto = getData.getString("jenis_restoran");
                            String foto_resto = getData.getString("foto_resto");
                            String id_reservasi = getData.getString("id_reservasi");
                            String id_seluruh_menu = getData.getString("id_seluruh_menu");
                            model.add(new historyreservasidata(nama_restoran,jadwal_reservasi,jumlah_orang,status_reservasi,jenis_resto,foto_resto,id_restoran,id_reservasi,id_seluruh_menu));
                        }
                        AdapterHistoryReservasi adapter = new AdapterHistoryReservasi(HistoryReservasi.this,model);
                        recyclerView2.setAdapter(adapter);
                        tvriwayat.setVisibility(View.GONE);
                    }
                    else{
                        recyclerView2.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HistoryReservasi.this,error.toString(),Toast.LENGTH_SHORT).show();
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

class AdapterHistoryReservasi extends RecyclerView.Adapter<AdapterHistoryReservasi.ViewHolder> {
    ArrayList<historyreservasidata> model;
    LayoutInflater inflater;
    Context context;

    AdapterHistoryReservasi(Context context,ArrayList<historyreservasidata> model){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.from(parent.getContext()).inflate(R.layout.historyreservasitampilan,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.nama_restoran.setText(model.get(position).getNama_restoran());
        holder.jadwal_reservasi.setText(model.get(position).getJadwal_reservasi());
        holder.jumlah_orang.setText(model.get(position).getJumlah_orang());
        holder.status_reservasi.setText(model.get(position).getStatus_reservasi());
        final String[] temp = {""};
        if(model.get(position).getStatus_reservasi().equals("Rejected")){
            holder.form.setVisibility(View.VISIBLE);
            holder.review_restoran.setVisibility(View.GONE);
            holder.review_menu.setVisibility(View.GONE);
        }else if(model.get(position).getStatus_reservasi().equals("Finished")) {
            holder.form.setVisibility(View.GONE);
            String url = "https://restoratori.mikolindosorong.id/mobile/ceksudahreview.php?data="+model.get(position).getId_reservasi();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray getHasil = jsonObject.getJSONArray("hasil");
                        if(getHasil.length()>0){
                            holder.review_restoran.setVisibility(View.GONE);
                        }else {
                            holder.review_restoran.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();
                }
            }
            );
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
            String[] strings = model.get(position).getId_seluruh_menu().split(",");
            for(int i = 0;i<strings.length;i++){
                String url2 = "https://restoratori.mikolindosorong.id/mobile/ceksudahreviewmenu?data="+model.get(position).getId_reservasi()+"&data2="+strings[i];
                int finalI = i;
                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray getHasil = jsonObject.getJSONArray("hasil");
                            if(getHasil.length()>0){
                                if(temp[0].isEmpty()){
                                    holder.review_menu.setVisibility(View.GONE);
                                }
                            }else {
                                temp[0] = strings[finalI];
                                if(temp[0].isEmpty()){
                                    holder.review_menu.setVisibility(View.GONE);
                                }else {
                                    holder.review_menu.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
                );
                RequestQueue requestQueue2 = Volley.newRequestQueue(context);
                requestQueue2.add(stringRequest2);
            }
        }else {
            holder.form.setVisibility(View.GONE);
            holder.review_restoran.setVisibility(View.GONE);
            holder.review_menu.setVisibility(View.GONE);
        }
        holder.form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,FormPengembalian.class);
                i.putExtra("id_reservasi",model.get(position).getId_reservasi());
                context.startActivity(i);
            }
        });
        holder.review_restoran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,ReviewRestoranPage.class);
                i.putExtra("id_reservasi",model.get(position).getId_reservasi());
                i.putExtra("id_reservasi",model.get(position).getId_reservasi());
                i.putExtra("nama_restoran",model.get(position).getNama_restoran());
                i.putExtra("jenis_restoran",model.get(position).getJenis_restoran());
                i.putExtra("foto_resto",model.get(position).getFoto_resto());
                i.putExtra("id_restoran",model.get(position).getId_restoran());
                context.startActivity(i);
            }
        });
        holder.review_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,ReviewMenuPage.class);
                i.putExtra("id_reservasi",model.get(position).getId_reservasi());
                i.putExtra("id_menu", temp[0]);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nama_restoran,jadwal_reservasi,jumlah_orang,status_reservasi;
        Button form,review_restoran,review_menu;

        public ViewHolder(View itemView) {
            super(itemView);
            nama_restoran = itemView.findViewById(R.id.namarestoran);
            jadwal_reservasi = itemView.findViewById(R.id.jadwalreservasi);
            jumlah_orang = itemView.findViewById(R.id.jumlahorang);
            status_reservasi = itemView.findViewById(R.id.statusreservasi);
            form = itemView.findViewById(R.id.form);
            review_restoran = itemView.findViewById(R.id.review_restoran);
            review_menu = itemView.findViewById(R.id.review_menu);
        }
    }
}
