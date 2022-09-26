package com.example.skirpsii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Page_Akun_Detail extends AppCompatActivity {

    ImageButton calender;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    EditText Tanggal,Suku,Alamat,Pekerjaan;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayofMonth;
    Calendar calendar;
    String userID;
    Spinner Agama , Status,EstimasiPenghasilan;
    Button Simpan;
    RadioGroup Jeniskelaminitem;
    DocumentReference documentReference;
    RadioButton Jeniskelamin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_page_akun_detail);

        calender = findViewById(R.id.calender);
        Tanggal = findViewById(R.id.tanggalupdate);
        Suku = findViewById(R.id.Sukuupdate);
        Alamat = findViewById(R.id.Alamatupdate);
        EstimasiPenghasilan = findViewById(R.id.EstimasiPenghasilanupdate);
        Pekerjaan = findViewById(R.id.Pekerjaanupdate);
        Agama = findViewById(R.id.Agamaupdate);
        Status = findViewById(R.id.Statusupdate);
        Jeniskelaminitem = findViewById(R.id.jeniskelaminupdate);
        Simpan = findViewById(R.id.SimpanUpdate);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Updateprofile();
            }
        });

        calender.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayofMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Page_Akun_Detail.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                Tanggal.setText(day + "/"+ (month+1)+"/"+year);
                            }
                        },year,month,dayofMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.Agamaupdate);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Agama, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner spinnerr = (Spinner) findViewById(R.id.Statusupdate);
        ArrayAdapter<CharSequence> adapterr = ArrayAdapter.createFromResource(this,
                R.array.Status, android.R.layout.simple_spinner_item);
        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerr.setAdapter(adapterr);

        Spinner spinnerrr = (Spinner) findViewById(R.id.EstimasiPenghasilanupdate);
        ArrayAdapter<CharSequence> adapterrr = ArrayAdapter.createFromResource(this,
                R.array.EstimasiPenghasilan, android.R.layout.simple_spinner_item);
        adapterrr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerrr.setAdapter(adapterrr);

    }

    private void Updateprofile() {
        String tanggal = Tanggal.getText().toString();
        String suku = Suku.getText().toString();
        String alamat = Alamat.getText().toString();
        String penghasilan = EstimasiPenghasilan.getSelectedItem().toString();
        String pekerjaan = Pekerjaan.getText().toString();
        String agama = Agama.getSelectedItem().toString();
        String status = Status.getSelectedItem().toString();
        int jeniskelaminitem = Jeniskelaminitem.getCheckedRadioButtonId();
        Jeniskelamin = (RadioButton) findViewById(jeniskelaminitem);
        String jeniskelamin = Jeniskelamin.getText().toString();
        userID = fAuth.getCurrentUser().getUid();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = (dateFormat.format(calendar.getTime())).toString();
        String[] strings = date.split("-");
        int year = Integer.parseInt(strings[0]);
        int month = Integer.parseInt(strings[1]);
        int day = Integer.parseInt(strings[2]);
        String[] strings1 = tanggal.split("/");
        int year1 = Integer.parseInt(strings1[2]);
        int month1 = Integer.parseInt(strings1[1]);
        int day1 = Integer.parseInt(strings1[0]);

        int umur = year - year1;
        if((month-month1)<0){
            umur = umur -1;
        }else if ((day-day1)<0){
            umur = umur -1;
        }

        String url = "https://restoratori.mikolindosorong.id/mobile/simpanpersonalisasi";
        int finalUmur = umur;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"Tersimpan Personalisasi",Toast.LENGTH_SHORT).show();
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
                params.put("umur",String.valueOf(finalUmur));
                params.put("suku_etnis",suku);
                params.put("daerah_tinggal",alamat);
                params.put("estimasi_penghasilan",penghasilan);
                params.put("profesi",pekerjaan);
                params.put("agama",agama);
                params.put("status",status);
                params.put("jenis_kelamin",jeniskelamin);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
        Map<String,Object> user = new HashMap<>();
        user.put("Tanggal Lahir",tanggal);
        user.put("Kota Kelahiran",suku);
        user.put("Alamat",alamat);
        user.put("Penghasilan",penghasilan);
        user.put("Pekerjaan",pekerjaan);
        user.put("Agama",agama);
        user.put("Status",status);
        user.put("Jenis Kelamin",jeniskelamin);

        fStore.collection("users profile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()){
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String documentID = documentSnapshot.getId();
                    fStore.collection("users profile").document(documentID).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Sucessfully Updated",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Some Error",Toast.LENGTH_SHORT);
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
                }
            }
        });

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
