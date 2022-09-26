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

public class FavoritRestoranPage extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<hasilsearch> model;
    FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_favorit_restoran_page);

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
        String url = "https://restoratori.mikolindosorong.id/mobile/tampilfavoritrestoran?data="+userID;
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
                    AdapterFavoritRestoran adapter = new AdapterFavoritRestoran(FavoritRestoranPage.this,model);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FavoritRestoranPage.this,error.toString(),Toast.LENGTH_SHORT).show();
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

    public void FavoritMenu(View view) {
        Intent i = new Intent (".FavoritMenuPage");
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

class AdapterFavoritRestoran extends RecyclerView.Adapter<AdapterFavoritRestoran.ViewHolder> {
    ArrayList<hasilsearch> model;
    LayoutInflater inflater;
    Context context;

    AdapterFavoritRestoran(Context context,ArrayList<hasilsearch> model){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.from(parent.getContext()).inflate(R.layout.searchlayout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        hasilsearch hasilsearch = model.get(position);

        holder.tes.setVisibility(View.GONE);
        holder.jenis_restoran.setText(model.get(position).getJenis_restoran());
        holder.alamat.setText(model.get(position).getAlamat());
        holder.nama_restoran.setText(model.get(position).getNama_restoran());
        Glide.with(context).load(model.get(position).getFoto_resto()).into(holder.foto_resto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        TextView nama_restoran,jenis_restoran,alamat;
        ImageView foto_resto;
        Button tes;

        public ViewHolder(View itemView) {
            super(itemView);
            nama_restoran = itemView.findViewById(R.id.namarestoran);
            jenis_restoran = itemView.findViewById(R.id.jenisrestoran);
            alamat = itemView.findViewById(R.id.alamatrestoran);
            tes = itemView.findViewById(R.id.tes);
            foto_resto = itemView.findViewById(R.id.fotoresto);
        }
    }
}
