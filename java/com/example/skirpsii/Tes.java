package com.example.skirpsii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Tes extends AppCompatActivity {

    public static final String url = "https://restoratori.mikolindosorong.id/mobile/insert.php";
    EditText tnim,tnama,talamat;
    TextView tket;
    Button bInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tes);
        tnim = (EditText) findViewById(R.id.inNim);
        tnama = (EditText) findViewById(R.id.inNama);
        talamat = (EditText) findViewById(R.id.inAlamat);
        tket = (TextView) findViewById(R.id.txtKet);
        bInput = (Button) findViewById(R.id.btInput);

        bInput.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                inputData();
            }
        });
    }

    public void inputData(){
        String nim = tnim.getText().toString();
        String nama = tnama.getText().toString();
        String alamat = talamat.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tket.setText("Data Berhasil disimpan");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tket.setText(error.getMessage());
            }
        })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<String, String>();

                params.put("nim",nim);
                params.put("nama",nama);
                params.put("alamat",alamat);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

}