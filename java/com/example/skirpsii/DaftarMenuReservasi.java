package com.example.skirpsii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.CloseGuard;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class DaftarMenuReservasi extends AppCompatActivity implements HargaMenu {

    TextView nama_restoran,jenis_restoran;
    ImageView foto_resto;
    ArrayList<daftarmenurestoran> model;
    RecyclerView recyclerView;
    Button selesai;
    TextView tes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_daftar_menu_reservasi);

        nama_restoran = findViewById(R.id.tvres);
        jenis_restoran = findViewById(R.id.jenisrestoran);
        foto_resto = findViewById(R.id.dtlimgres);
        tes = findViewById(R.id.tes);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.menurestoran);
        recyclerView.setLayoutManager(layoutManager);

        Glide.with(this).load(getIntent().getStringExtra("foto_resto")).into(foto_resto);
        nama_restoran.setText(getIntent().getStringExtra("nama_restoran"));
        jenis_restoran.setText(getIntent().getStringExtra("jenis_restoran"));

        tampilandata(this);

    }

    void tampilandata(HargaMenu hargaMenu){
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
                    AdapterDaftarMenuReservasi adapter = new AdapterDaftarMenuReservasi(DaftarMenuReservasi.this,model,hargaMenu);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DaftarMenuReservasi.this,error.toString(),Toast.LENGTH_SHORT).show();
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

    @Override
    public void onListMenu(String arrayList,ArrayList<String> arrayList1,ArrayList<String> arrayList2) {
        if(arrayList.equals("0")){
            Toast.makeText(getApplicationContext(),"Silahkan pilih 1 menu",Toast.LENGTH_SHORT).show();
        }else{
            Intent i = new Intent(getApplicationContext(),BuatReservasi.class);
            i.putExtra("id_restoran",getIntent().getStringExtra("id_restoran"));
            i.putExtra("nama_restoran",getIntent().getStringExtra("nama_restoran"));
            i.putExtra("jenis_restoran",getIntent().getStringExtra("jenis_restoran"));
            i.putExtra("foto_resto",getIntent().getStringExtra("foto_resto"));
            i.putExtra("status_restoran",getIntent().getStringExtra("status_restoran"));
            i.putExtra("rekening_pembayaran",getIntent().getStringExtra("rekening_pembayaran"));
            i.putExtra("harga",arrayList);
            i.putExtra("id_menu_total",arrayList1);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }
}

class AdapterDaftarMenuReservasi extends RecyclerView.Adapter<AdapterDaftarMenuReservasi.ViewHolder> implements Serializable {
    ArrayList<daftarmenurestoran> model;
    LayoutInflater inflater;
    Context context;
    HargaMenu hargaMenu;
    ArrayList<String> id_menu = new ArrayList<String>();
    ArrayList<String> harga_menu = new ArrayList<String>();
    ArrayList<String> total_harga = new ArrayList<String>();

    public AdapterDaftarMenuReservasi(Context context,ArrayList<daftarmenurestoran> model,HargaMenu hargaMenu){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
        this.hargaMenu = hargaMenu;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.from(parent.getContext()).inflate(R.layout.checkboxmenu,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final int[] harga_total = {0};
        if(position == model.size()-1){
            holder.next.setVisibility(View.VISIBLE);
        }else {
            holder.next.setVisibility(View.GONE);
        }
        holder.nama_menu.setText(model.get(position).getNama_menu());
        holder.harga.setText(model.get(position).getHarga());
        holder.deskripsi.setText(model.get(position).getDeskripsi());
        Glide.with(context).load(model.get(position).getFoto_menu()).into(holder.foto_menu);
        final int[] y = {0};
        holder.tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(y[0] == 0){
                    id_menu.add(model.get(position).getId_menu());
                    String temp = model.get(position).getHarga();
                    harga_menu.add(String.valueOf(temp));
                    y[0]++;
                    holder.total.setText(String.valueOf(y[0]));
                }else{
                    y[0]++;
                    String temp = model.get(position).getHarga();
                    harga_menu.add(String.valueOf(temp));
                    holder.total.setText(String.valueOf(y[0]));
                }
            }
        });
        holder.kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(y[0] == 0){
                }else if (y[0] == 1){
                    id_menu.remove(model.get(position).getId_menu());
                    harga_menu.remove(model.get(position).getHarga());
                    y[0]--;
                    holder.total.setText(String.valueOf(y[0]));
                }else {
                    y[0]--;
                    harga_menu.remove(model.get(position).getHarga());
                    holder.total.setText(String.valueOf(y[0]));
                }
            }
        });
        holder.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0;i<harga_menu.size();i++){
                    String[] strings = String.valueOf(harga_menu.get(i)).split("\\.");
                    String[] strings1 = strings[1].split(" ");
                    String temp2 = strings1[1].concat(strings[2]);
                    int pertama = Integer.parseInt(temp2);
                    harga_total[0] = harga_total[0] + pertama;
                }
                hargaMenu.onListMenu(String.valueOf(harga_total[0]),id_menu,total_harga);
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nama_menu,harga,deskripsi,total;
        ImageView tambah,kurang;
        ImageButton foto_menu;
        Button next;

        public ViewHolder(View itemView) {
            super(itemView);
            nama_menu = itemView.findViewById(R.id.nama_menu);
            harga = itemView.findViewById(R.id.hargamenu);
            deskripsi = itemView.findViewById(R.id.deskripsimenu);
            foto_menu = itemView.findViewById(R.id.foto_menu);
            total = itemView.findViewById(R.id.total);
            tambah = itemView.findViewById(R.id.tambah);
            kurang = itemView.findViewById(R.id.kurang);
            next = itemView.findViewById(R.id.next);
        }
    }

}