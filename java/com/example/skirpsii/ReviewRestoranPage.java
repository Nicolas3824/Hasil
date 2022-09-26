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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ReviewRestoranPage extends AppCompatActivity {

    public static final String url = "https://restoratori.mikolindosorong.id/mobile/ratingrestoran";

    TextView nama_restoran,jenis_restoran;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    EditText Variasi_menu,Higienis,Pelayanan,Suasana,Komentar,Kelayakan;
    Button Konfirmasi;
    FirebaseAuth fAuth;
    String userID,date;
    ImageView foto_resto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_review_restoran_page);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = (dateFormat.format(calendar.getTime())).toString();
        Variasi_menu = findViewById(R.id.VariasiMenuRestoran);
        Higienis = findViewById(R.id.HigienisRestoran);
        Pelayanan = findViewById(R.id.PelayananRestoran);
        Suasana = findViewById(R.id.SuasanaRestoran);
        Komentar = findViewById(R.id.KomentarRestoran);
        Kelayakan = findViewById(R.id.kelayakanharga);
        Konfirmasi = findViewById(R.id.Konfirmasi);
        fAuth = FirebaseAuth.getInstance();

        nama_restoran = findViewById(R.id.namarestoran);
        jenis_restoran = findViewById(R.id.jenisrestoran);
        foto_resto = findViewById(R.id.dtlimgres);

        Glide.with(this).load(getIntent().getStringExtra("foto_resto")).into(foto_resto);
        nama_restoran.setText(getIntent().getStringExtra("nama_restoran"));
        jenis_restoran.setText(getIntent().getStringExtra("jenis_restoran"));

        Konfirmasi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){

                String variasi_menu = Variasi_menu.getText().toString();
                String higienis = Higienis.getText().toString();
                String pelayanan = Pelayanan.getText().toString();
                String suasana = Suasana.getText().toString();
                String kelayakan = Kelayakan.getText().toString();

                if(TextUtils.isEmpty(variasi_menu)){
                    Variasi_menu.setError("This Field is Required.");
                    return;
                }
                int value=0;
                value=Integer.parseInt(variasi_menu);
                if(value>5 || value<1){
                    Variasi_menu.setError("Harus antara 1-5");
                    return;
                }
                if(TextUtils.isEmpty(higienis)){
                    Higienis.setError("This Field is Required.");
                    return;
                }
                int value1=0;
                value1=Integer.parseInt(higienis);
                if(value1>5 || value1<1){
                    Higienis.setError("Harus antara 1-5");
                    return;
                }
                if(TextUtils.isEmpty(suasana)){
                    Suasana.setError("This Field is Required.");
                    return;
                }
                int value2=0;
                value2=Integer.parseInt(suasana);
                if(value2>5 || value2<1){
                    Suasana.setError("Harus antara 1-5");
                    return;
                }
                if(TextUtils.isEmpty(pelayanan)){
                    Pelayanan.setError("This Field is Required.");
                    return;
                }
                int value3=0;
                value3=Integer.parseInt(pelayanan);
                if(value3>5 || value3<1){
                    Pelayanan.setError("Harus antara 1-5");
                    return;
                }
                if(TextUtils.isEmpty(kelayakan)){
                    Kelayakan.setError("This Field is Required.");
                    return;
                }
                int value4=0;
                value4=Integer.parseInt(kelayakan);
                if(value4>5 || value4<1){
                    Kelayakan.setError("Harus antara 1-5");
                    return;
                }

                inputData();
            }
        });

    }

    void inputData(){
        String variasi_menu = Variasi_menu.getText().toString();
        String higienis = Higienis.getText().toString();
        String pelayanan = Pelayanan.getText().toString();
        String suasana = Suasana.getText().toString();
        String komentar = Komentar.getText().toString();
        String kelayakan = Kelayakan.getText().toString();
        userID = fAuth.getCurrentUser().getUid();

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
                params.put("id_restoran",getIntent().getStringExtra("id_restoran"));
                params.put("id_konsumen",userID);
                params.put("komentar",komentar);
                params.put("suasana",suasana);
                params.put("pelayanan",pelayanan);
                params.put("fasilitas",variasi_menu);
                params.put("kelayakan_harga",kelayakan);
                params.put("higienis",higienis);
                params.put("tanggal_review",date);

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
