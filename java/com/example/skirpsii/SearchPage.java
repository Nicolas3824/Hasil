package com.example.skirpsii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchPage extends AppCompatActivity {

    EditText cari;
    ListView listView;
    ArrayList<hasilsearch> model;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_search_page);
        cari = findViewById(R.id.etsearch);
        listView = findViewById(R.id.list_view);

        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cari_data(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    void cari_data(String data_cari){
        String url = "https://restoratori.mikolindosorong.id/mobile/searchhasil.php?data="+data_cari;
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
                    Adapter adapter = new Adapter(SearchPage.this,model);
                    listView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchPage.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void HomePage(View view) {
        Intent i = new Intent(".MainPage");
        startActivity(i);
    }

    public void FavoritRestoranPage(View view) {
        Intent i = new Intent(".FavoritRestoranPage");
        startActivity(i);
    }

    public void HistoryReservasi(View view) {
        Intent i = new Intent(".HistoryReservasi");
        startActivity(i);
    }

    public void PageAkun(View view) {
        Intent i = new Intent(".PageAkun");
        startActivity(i);
    }
}

class Adapter extends BaseAdapter {
    ArrayList<hasilsearch> model;
    LayoutInflater inflater;
    Context context;

    Adapter(Context context,ArrayList<hasilsearch> model){
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

    TextView nama_restoran,jenis_restoran,alamat;
    ImageView foto_resto;
    Button tes;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.searchlayout,viewGroup,false);
        nama_restoran = v.findViewById(R.id.namarestoran);
        jenis_restoran = v.findViewById(R.id.jenisrestoran);
        alamat = v.findViewById(R.id.alamatrestoran);
        tes = v.findViewById(R.id.tes);
        foto_resto = v.findViewById(R.id.fotoresto);

        hasilsearch hasilsearch = model.get(i);

        tes.setText("Detail");
        nama_restoran.setText(model.get(i).getNama_restoran());
        jenis_restoran.setText(model.get(i).getJenis_restoran());
        alamat.setText(model.get(i).getAlamat());
        Glide.with(context).load(model.get(i).getFoto_resto()).into(foto_resto);

        tes.setOnClickListener(new View.OnClickListener(){
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

        return v;
    }
}
