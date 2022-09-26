package com.example.skirpsii;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewMenuPage extends AppCompatActivity {

    EditText rasa,harga,kualitas,kesehatan,ukuran,komentar;
    TextView nama,tag,harga_makanan;
    ImageView foto;
    Button konfirmasi;
    FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_review_menu_page);

        rasa = findViewById(R.id.rasa);
        harga = findViewById(R.id.harga);
        kesehatan = findViewById(R.id.kesehatan);
        kualitas = findViewById(R.id.kualitas);
        ukuran = findViewById(R.id.ukuran);
        komentar = findViewById(R.id.komentar);
        konfirmasi = findViewById(R.id.konfirmasi);
        foto = findViewById(R.id.dtlimgmen);
        nama = findViewById(R.id.tvmenu);
        tag = findViewById(R.id.tagmenu);
        harga_makanan = findViewById(R.id.hargamenu);
        fAuth = FirebaseAuth.getInstance();

        String url = "https://restoratori.mikolindosorong.id/mobile/carimenu.php?data="+getIntent().getStringExtra("id_menu");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray getHasil = jsonObject.getJSONArray("hasil");
                    for (int i=0;i<getHasil.length();i++){
                        JSONObject getData = getHasil.getJSONObject(i);
                        String nama_menu = getData.getString("nama_menu");
                        String tag_menu = getData.getString("tag_menu");
                        String harga = getData.getString("harga");
                        String foto_menu = getData.getString("foto_menu");
                        nama.setText(nama_menu);
                        tag.setText(tag_menu);
                        harga_makanan.setText(harga);
                        Glide.with(getApplicationContext()).load(foto_menu).into(foto);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReviewMenuPage.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Rasa = rasa.getText().toString();
                String Harga = harga.getText().toString();
                String Kualitas = kualitas.getText().toString();
                String Kesehatan = kesehatan.getText().toString();
                String Ukuran = ukuran.getText().toString();
                String Komentar = komentar.getText().toString();

                if(TextUtils.isEmpty(Rasa)){
                    rasa.setError("This Field is Required.");
                    return;
                }
                int value=0;
                value=Integer.parseInt(Rasa);
                if(value>5 || value<1){
                    rasa.setError("Harus antara 1-5");
                    return;
                }
                if(TextUtils.isEmpty(Harga)){
                    harga.setError("This Field is Required.");
                    return;
                }
                int value1=0;
                value1=Integer.parseInt(Harga);
                if(value1>5 || value1<1){
                    harga.setError("Harus antara 1-5");
                    return;
                }
                if(TextUtils.isEmpty(Kualitas)){
                    kualitas.setError("This Field is Required.");
                    return;
                }
                int value2=0;
                value2=Integer.parseInt(Kualitas);
                if(value2>5 || value2<1){
                    kualitas.setError("Harus antara 1-5");
                    return;
                }
                if(TextUtils.isEmpty(Ukuran)){
                    ukuran.setError("This Field is Required.");
                    return;
                }
                int value3=0;
                value3=Integer.parseInt(Ukuran);
                if(value3>5 || value3<1){
                    ukuran.setError("Harus antara 1-5");
                    return;
                }
                if(TextUtils.isEmpty(Kesehatan)){
                    kesehatan.setError("This Field is Required.");
                    return;
                }
                int value4=0;
                value4=Integer.parseInt(Kesehatan);
                if(value4>5 || value4<1){
                    kesehatan.setError("Harus antara 1-5");
                    return;
                }
                inputdata();
            }
        });

    }

    void inputdata(){
        String Rasa = rasa.getText().toString();
        String Harga = harga.getText().toString();
        String Kualitas = kualitas.getText().toString();
        String Kesehatan = kesehatan.getText().toString();
        String Ukuran = ukuran.getText().toString();
        String Komentar = komentar.getText().toString();
        userID = fAuth.getCurrentUser().getUid();

        String url = "https://restoratori.mikolindosorong.id/mobile/insertratingmenu";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent i = new Intent(getApplicationContext(),HistoryReservasi.class);
                        startActivity(i);
                        finish();
                        Toast.makeText(getApplicationContext(),"Berhasil",Toast.LENGTH_LONG).show();
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

                params.put("id_reservasi",getIntent().getStringExtra("id_reservasi"));
                params.put("id_menu",getIntent().getStringExtra("id_menu"));
                params.put("id_konsumen",userID);
                params.put("komentar",Komentar);
                params.put("rasa",Rasa);
                params.put("ukuran",Ukuran);
                params.put("kualitas",Kualitas);
                params.put("harga",Harga);
                params.put("kesehatan",Kesehatan);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
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
