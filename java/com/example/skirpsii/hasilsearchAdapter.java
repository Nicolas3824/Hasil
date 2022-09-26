package com.example.skirpsii;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class hasilsearchAdapter extends BaseAdapter {

    Activity activity;
    List<hasilsearch> item;
    private LayoutInflater inflater ;

    public hasilsearchAdapter(Activity activity, List<hasilsearch> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int i) {
        return item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) view = inflater.inflate(R.layout.searchlayout, null);

        TextView nama_restoran = (TextView) view.findViewById(R.id.namarestoran);
        TextView jenis_restoran = (TextView) view.findViewById(R.id.jenisrestoran);
        TextView alamat = (TextView) view.findViewById(R.id.alamatrestoran);

        hasilsearch hasilsearch = item.get(i);

        nama_restoran.setText(hasilsearch.getNama_restoran());
        jenis_restoran.setText(hasilsearch.getJenis_restoran());
        alamat.setText(hasilsearch.getAlamat());

        return view;
    }


}
