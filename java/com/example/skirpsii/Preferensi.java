package com.example.skirpsii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class Preferensi extends AppCompatActivity {

    Spinner prioritas,kasta,porsi,manis,asam,asin,pahit,pedas,umami,starchy,budget;
    CheckBox toilet,wifi,musik,smoking,spotfoto,tempatibadah,tempatparkir;
    String toilett = "",wifii = "" ,musikk = "" ,smokingg = "" ,spotfotoo = "" ,tempatibadahh = "" ,tempatparkirr = "";
    RadioGroup resfav;
    RadioButton resfavv;
    FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_preferensi);

        prioritas = findViewById(R.id.prioritas);
        kasta = findViewById(R.id.kasta);
        porsi = findViewById(R.id.porsi);
        manis = findViewById(R.id.manis);
        asam = findViewById(R.id.asam);
        asin = findViewById(R.id.asin);
        pahit = findViewById(R.id.pahit);
        pedas = findViewById(R.id.pedas);
        umami = findViewById(R.id.umami);
        starchy = findViewById(R.id.starchy);
        budget = findViewById(R.id.budget);
        toilet = findViewById(R.id.toilet);
        wifi = findViewById(R.id.wifi);
        musik = findViewById(R.id.musik);
        smoking = findViewById(R.id.smoking);
        spotfoto = findViewById(R.id.spotfoto);
        tempatibadah = findViewById(R.id.tempatibadah);
        tempatparkir = findViewById(R.id.tempatparkir);
        resfav = findViewById(R.id.resfav);
        fAuth = FirebaseAuth.getInstance();

        toilet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    toilett = toilet.getText().toString();
                }else {
                    toilett = "";
                }
            }
        });

        wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    wifii = wifi.getText().toString();
                }else {
                    wifii = "";
                }
            }
        });

        musik.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    musikk = musik.getText().toString();
                }else {
                    musikk = "";
                }
            }
        });

        spotfoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    spotfotoo = spotfoto.getText().toString();
                }else {
                    spotfotoo = "";
                }
            }
        });

        tempatibadah.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    tempatibadahh = tempatibadah.getText().toString();
                }else {
                    tempatibadahh = "";
                }
            }
        });

        smoking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    smokingg = smoking.getText().toString();
                }else {
                    smokingg = "";
                }
            }
        });

        tempatparkir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    tempatparkirr = tempatparkir.getText().toString();
                }else {
                    tempatparkirr = "";
                }
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.prioritas);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Prioritas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner spinner1 = (Spinner) findViewById(R.id.kasta);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.Kasta, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        Spinner spinner2 = (Spinner) findViewById(R.id.porsi);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.Porsi, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        Spinner spinner3 = (Spinner) findViewById(R.id.manis);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.Angka, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        Spinner spinner4 = (Spinner) findViewById(R.id.asam);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.Angka, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter4);

        Spinner spinner5 = (Spinner) findViewById(R.id.asin);
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this,
                R.array.Angka, android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(adapter5);

        Spinner spinner6 = (Spinner) findViewById(R.id.pahit);
        ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(this,
                R.array.Angka, android.R.layout.simple_spinner_item);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner6.setAdapter(adapter6);

        Spinner spinner7 = (Spinner) findViewById(R.id.pedas);
        ArrayAdapter<CharSequence> adapter7 = ArrayAdapter.createFromResource(this,
                R.array.Angka, android.R.layout.simple_spinner_item);
        adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner7.setAdapter(adapter7);

        Spinner spinner8 = (Spinner) findViewById(R.id.umami);
        ArrayAdapter<CharSequence> adapter8 = ArrayAdapter.createFromResource(this,
                R.array.Angka, android.R.layout.simple_spinner_item);
        adapter8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner8.setAdapter(adapter8);

        Spinner spinner9 = (Spinner) findViewById(R.id.starchy);
        ArrayAdapter<CharSequence> adapter9 = ArrayAdapter.createFromResource(this,
                R.array.Angka, android.R.layout.simple_spinner_item);
        adapter9.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner9.setAdapter(adapter9);

        Spinner spinner10 = (Spinner) findViewById(R.id.budget);
        ArrayAdapter<CharSequence> adapter10 = ArrayAdapter.createFromResource(this,
                R.array.Budget, android.R.layout.simple_spinner_item);
        adapter10.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner10.setAdapter(adapter10);

    }

    public void Simpan(View view) {
        inputData();
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

    void inputData(){
        userID = fAuth.getCurrentUser().getUid();
        String Prioritas = prioritas.getSelectedItem().toString();
        String Kasta = kasta.getSelectedItem().toString();
        String Porsi = porsi.getSelectedItem().toString();
        String Manis = manis.getSelectedItem().toString();
        String Asam = asam.getSelectedItem().toString();
        String Asin = asin.getSelectedItem().toString();
        String Pahit = pahit.getSelectedItem().toString();
        String Pedas = pedas.getSelectedItem().toString();
        String Starchy = starchy.getSelectedItem().toString();
        String Umami = umami.getSelectedItem().toString();
        String Budget = budget.getSelectedItem().toString();
        if (resfav.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(),"Silahkan Memilih Jenis Restoran Favorit",Toast.LENGTH_SHORT).show();
            return;
        } else {
        }
        int resfavitem = resfav.getCheckedRadioButtonId();
        resfavv = (RadioButton) findViewById(resfavitem);
        String restoran_favorit = resfavv.getText().toString();
        String temp = "";
        if(temp.isEmpty()){
            if(toilett.isEmpty()){
            }else {
                temp = toilett;
            }
        }
        if(TextUtils.isEmpty(temp)){
            if(wifii.isEmpty()){
            }else {
                temp = wifii;
            }
        }else {
            if(wifii.isEmpty()){
            }else {
                temp = temp + "," + wifii;
            }
        }
        if(temp.isEmpty()){
            if(musikk.isEmpty()){
            }else {
                temp = musikk;
            }
        }else {
            if(musikk.isEmpty()){
            }else {
                temp = temp + "," + musikk;
            }
        }
        if(temp.isEmpty()){
            if(tempatparkirr.isEmpty()){
            }else {
                temp = tempatparkirr;
            }
        }else {
            if(tempatparkirr.isEmpty()){
            }else {
                temp = temp + "," + tempatparkirr;;
            }
        }
        if(temp.isEmpty()){
            if(tempatibadahh.isEmpty()){
            }else {
                temp = tempatibadahh;
            }
        }else {
            if(tempatibadahh.isEmpty()){
            }else {
                temp = temp + "," + tempatibadahh;
            }
        }
        if(temp.isEmpty()){
            if(smokingg.isEmpty()){
            }else {
                temp = smokingg;
            }
        }else {
            if(smokingg.isEmpty()){
            }else {
                temp = temp + "," + smokingg;
            }
        }
        if(temp.isEmpty()){
            if(spotfotoo.isEmpty()){
            }else {
                temp = spotfotoo;
            }
        }else {
            if(spotfotoo.isEmpty()){
            }else {
                temp = temp + "," + spotfotoo;
            }
        }
        String url = "https://restoratori.mikolindosorong.id/mobile/simpanpreferensi";
        String finalTemp = temp;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"Tersimpan Preferensi",Toast.LENGTH_SHORT).show();
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
                params.put("id_konsumen",userID);
                params.put("prioritas",Prioritas);
                params.put("kasta_harga",Kasta);
                params.put("porsi_makan",Porsi);
                params.put("tingkat_rasa_manis",Manis);
                params.put("tingkat_rasa_asam",Asam);
                params.put("tingkat_rasa_asin",Asin);
                params.put("tingkat_rasa_pahit",Pahit);
                params.put("tingkat_rasa_pedas",Pedas);
                params.put("tingkat_rasa_starchy",Starchy);
                params.put("tingkat_rasa_umami",Umami);
                params.put("budget_makan",Budget);
                params.put("fasilitas_diinginkan", finalTemp);
                params.put("jenis_restoran_favorit",restoran_favorit);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}