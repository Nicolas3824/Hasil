package com.example.skirpsii;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuatReservasi extends AppCompatActivity {

    TextView nama_restoran,rekening,hargatotal;
    public static final String url = "https://restoratori.mikolindosorong.id/mobile/reservasi";
    EditText jumlahpelanggan,tanggal,permintaankhusus,NoTelp,time;
    Button bInput,upload;
    FirebaseAuth fAuth;
    String userID;
    ImageButton calender,waktu;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayofMonth;
    int hour;
    int minute;
    int harga_total;
    ArrayList<CekReservasi> model;
    TextView tes;
    Bitmap bitmap;
    String encodeImageString;
    ArrayList<String> myList;
    String id_seluruh_menu = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_buat_reservasi);

        waktu = findViewById(R.id.time);
        time = findViewById(R.id.waktu);
        calender = findViewById(R.id.calender);
        tanggal = findViewById(R.id.tanggal);
        fAuth = FirebaseAuth.getInstance();
        jumlahpelanggan = (EditText) findViewById(R.id.JumlahPelanggan);
        tanggal = (EditText) findViewById(R.id.tanggal);
        permintaankhusus = (EditText) findViewById(R.id.PermintaanKhusus);
        NoTelp = (EditText) findViewById(R.id.NoTelp);
        bInput = (Button) findViewById(R.id.Reservasi);
        nama_restoran = findViewById(R.id.tvrestoran);
        upload = findViewById(R.id.upload);
        tes = findViewById(R.id.tes);
        rekening = findViewById(R.id.rekening);
        hargatotal = findViewById(R.id.hargatotalmenu);

        myList = (ArrayList<String>) getIntent().getSerializableExtra("id_menu_total");

        for (int i=0;i<myList.size();i++) {
            if(myList.size()-1 == i){
                id_seluruh_menu = id_seluruh_menu.concat(myList.get(i));
            }else {
                id_seluruh_menu = id_seluruh_menu.concat(myList.get(i)+",");
            }
        }

        hargatotal.setText("Rp. "+ getIntent().getStringExtra("harga"));

        rekening.setText("Silahkan Transfer ke " + getIntent().getStringExtra("rekening_pembayaran"));

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(BuatReservasi.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/");
                                startActivityForResult(Intent.createChooser(intent,"BrowseImage"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        nama_restoran.setText(getIntent().getStringExtra("nama_restoran"));

        calender.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayofMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(BuatReservasi.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tanggal.setText(year + "-"+ (month+1)+"-"+day);
                            }
                        },year,month,dayofMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });

        waktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(BuatReservasi.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        hour = i;
                        minute = i1;
                        Calendar calendar = Calendar.getInstance();
                        if(!(TextUtils.isEmpty(tanggal.getText().toString().trim()))){
                            String sDate = tanggal.getText().toString().trim();
                            String[] strings = sDate.split("-");
                            int sDay = Integer.parseInt(strings[2]);
                            calendar.set(Calendar.DAY_OF_MONTH,sDay);
                            calendar.set(Calendar.HOUR_OF_DAY,hour);
                            calendar.set(Calendar.MINUTE,minute);
                            String temp = i + ":" + i1;
                            SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                            try {
                                Date date = f24Hours.parse(temp);
                                time.setText(f24Hours.format(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Silahkan isi tanggal terlebih dahulu",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                },12,0,true);
                timePickerDialog.updateTime(hour,minute);
                timePickerDialog.show();
            }
        });

        bInput.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){

                String jumlahorang = jumlahpelanggan.getText().toString();
                String tanggalreservasi = tanggal.getText().toString();
                String no_telp = NoTelp.getText().toString();
                String waktu = time.getText().toString();
                userID = fAuth.getCurrentUser().getUid();

                Pattern p = Pattern.compile("[0][8][0-9]{10}");
                Matcher m = p.matcher(no_telp);
                Pattern p1 = Pattern.compile("[0][6][0-9]{9}");
                Matcher m1 = p1.matcher(no_telp);
                Pattern p2 = Pattern.compile("[0][8][0-9]{9}");
                Matcher m2 = p2.matcher(no_telp);

                Pattern p3 = Pattern.compile("[0-9]{4}[-][0-9]{1}[-][0-9]{2}");
                Matcher m3 = p3.matcher(tanggalreservasi);
                Pattern p4 = Pattern.compile("[0-9]{4}[-][0-9]{2}[-][0-9]{2}");
                Matcher m4 = p4.matcher(tanggalreservasi);
                Pattern p5 = Pattern.compile("[0-9]{4}[-][0-9]{1}[-][0-9]{1}");
                Matcher m5 = p5.matcher(tanggalreservasi);
                Pattern p6 = Pattern.compile("[0-9]{4}[-][0-9]{2}[-][0-9]{1}");
                Matcher m6 = p6.matcher(tanggalreservasi);

                Pattern p7 = Pattern.compile("[0-9]{2}[:][0-9]{2}");
                Matcher m7 = p7.matcher(waktu);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = (dateFormat.format(calendar.getTime())).toString();

                if(TextUtils.isEmpty(jumlahorang)){
                    jumlahpelanggan.setError("This Field is Required.");
                    return;
                }
                int value=0;
                value=Integer.parseInt(jumlahorang);
                if(value>8 || value<1){
                    jumlahpelanggan.setError("Harus antara 1-8");
                    return;
                }

                if(TextUtils.isEmpty(tanggalreservasi)){
                }
                else {
                    String[] strings1 = date.split("-");
                    String tahun1 = strings1[0];
                    String bulan1 = strings1[1];
                    String[] strings = tanggalreservasi.split("-");
                    String tahun = strings[0];
                    String bulan = strings[1];
                    if(Integer.parseInt(tahun1)==Integer.parseInt(tahun)){
                        if(Integer.parseInt(bulan1)==Integer.parseInt(bulan) || Integer.parseInt(bulan1)+1==Integer.parseInt(bulan)){
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Tanggal Reservasi melewati 1 bulan",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else {
                        if(Integer.parseInt(bulan) == 1){
                        }else {
                            Toast.makeText(getApplicationContext(),"Tanggal Reservasi melewati 1 bulan",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                if(!(TextUtils.equals(tes.getText().toString(),"Sudah Terupload"))) {
                    Toast.makeText(getApplicationContext(), "Silahkan Upload Bukti Reservasi", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(m7.matches()){
                }
                else {
                    time.setError("Format Waktu Salah");
                    return;
                }

                if(m3.matches() || m4.matches() || m5.matches() || m6.matches()){
                }
                else {
                    tanggal.setError("Format Tanggal Salah");
                    return;
                }

                if(m.matches() || m1.matches() || m2.matches()){
                }
                else {
                    NoTelp.setError("Harus Nomor Telepon");
                    return;
                }

                if(TextUtils.isEmpty(tanggalreservasi)){
                    tanggal.setError("This Field is Required.");
                    return;
                }
                if(TextUtils.isEmpty(no_telp)){
                    NoTelp.setError("This Field is Required.");
                    return;
                }
                if(TextUtils.isEmpty(waktu)){
                    time.setError("This Field is Required.");
                    return;
                }
                String url = "https://restoratori.mikolindosorong.id/mobile/cariIdreservasi.php?data="+userID.toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayList<String> mylist = new ArrayList<String>();
                            String hasil = "";
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray getHasil = jsonObject.getJSONArray("hasil");
                            model = new ArrayList<>();
                            for (int i=0;i<getHasil.length();i++) {
                                JSONObject getData = getHasil.getJSONObject(i);
                                String jadwal_reservasi = getData.getString("jadwal_reservasi");
                                String[] strings = jadwal_reservasi.split(" ");
                                mylist.add(strings[0]);
                                mylist.add(strings[1]);
                                model.add(new CekReservasi(jadwal_reservasi));
                            }
                            ArrayList<String> tahun = new ArrayList<String >();
                            ArrayList<String> bulan = new ArrayList<String>();
                            ArrayList<String> hari = new ArrayList<String>();
                            for(int i = 0;i<mylist.size();i++){
                                if(i%2 == 0){
                                    String[] strings = mylist.get(i).split("-");
                                    tahun.add(strings[0]);
                                    bulan.add(strings[1]);
                                    hari.add(strings[2]);
                                }
                            }
                            String[] strings = tanggalreservasi.split("-");
                            int x = Integer.parseInt(strings[1]);
                            int y = Integer.parseInt(strings[2]);
                            int z = Integer.parseInt(strings[0]);
                            boolean c = false;
                            for(int i = 0;i<tahun.size();i++){
                                if(z == Integer.parseInt(tahun.get(i))){
                                    c = true;
                                }
                            }
                            boolean a = false;
                            if(c){
                                for(int i = 0;i<bulan.size();i++){
                                    if(x == Integer.parseInt(bulan.get(i))){
                                        a = true;
                                    }
                                }
                            }
                            boolean b = false;
                            if(a){
                                for(int i = 0;i<hari.size();i++){
                                    if(y == Integer.parseInt(hari.get(i))){
                                        b = true;
                                    }
                                }
                            }
                            if(b){
                                Toast.makeText(getApplicationContext(),"Anda sudah melakukan reservasi di hari yang sama",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                inputData();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
                );
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!= null && data.getData() != null){
            Uri filePath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                encodeBitmapImage(bitmap);
                tes.setText("Sudah Terupload");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
        encodeImageString = android.util.Base64.encodeToString(bytesofimage,Base64.DEFAULT);
    }

    public void inputData(){

        String jumlah_orang = jumlahpelanggan.getText().toString();
        String jadwal_reservasi = tanggal.getText().toString();
        String permintaan_khusus = permintaankhusus.getText().toString();
        String no_kontak = NoTelp.getText().toString();
        String waktu = time.getText().toString();
        userID = fAuth.getCurrentUser().getUid();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"Berhasil",Toast.LENGTH_SHORT).show();
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

                params.put("id_restoran",getIntent().getStringExtra("id_restoran"));
                params.put("nama_restoran",getIntent().getStringExtra("nama_restoran"));
                params.put("id_konsumen",userID);
                params.put("id_seluruh_menu",id_seluruh_menu);
                params.put("total_harga","Rp. "+ getIntent().getStringExtra("harga"));
                params.put("jumlah_orang",jumlah_orang);
                params.put("jadwal_reservasi",jadwal_reservasi+" "+waktu+":00");
                params.put("permintaan_khusus",permintaan_khusus);
                params.put("no_kontak",no_kontak);
                params.put("bukti_pembayaran",encodeImageString);

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