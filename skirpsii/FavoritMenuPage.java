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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
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

public class FavoritMenuPage extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<daftarmenurestoran> model;
    FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_favorit_menu_page);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getApplicationContext(),1);
        recyclerView.addItemDecoration(itemDecor);

        fAuth = FirebaseAuth.getInstance();

        tampilfavorit();

    }

    void tampilfavorit(){
        userID = fAuth.getCurrentUser().getUid();
        String url = "https://restoratori.mikolindosorong.id/mobile/tampilfavoritmenu?data="+userID;
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
                    AdapterFavoritMenu adapter = new AdapterFavoritMenu(FavoritMenuPage.this,model);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FavoritMenuPage.this,error.toString(),Toast.LENGTH_SHORT).show();
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

class AdapterFavoritMenu extends RecyclerView.Adapter<AdapterFavoritMenu.ViewHolder> {
    ArrayList<daftarmenurestoran> model;
    LayoutInflater inflater;
    Context context;

    AdapterFavoritMenu(Context context,ArrayList<daftarmenurestoran> model){
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
        holder.deskripsi.setText(model.get(position).getTag_menu());
        Glide.with(context).load(model.get(position).getFoto_menu()).into(holder.foto_menu);

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