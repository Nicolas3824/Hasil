package com.example.skirpsii;

import java.io.Serializable;

public class daftarmenurestoran implements Serializable {

    String id_menu,id_restoran , nama_menu , foto_menu , harga, tag_menu ,deskripsi;

    public daftarmenurestoran(){

    }

    public daftarmenurestoran(String id_menu,String id_restoran, String nama_menu, String foto_menu, String harga, String tag_menu, String deskripsi) {
        this.id_restoran = id_restoran;
        this.nama_menu = nama_menu;
        this.foto_menu = foto_menu;
        this.harga = harga;
        this.tag_menu = tag_menu;
        this.deskripsi = deskripsi;
        this.id_menu = id_menu;
    }

    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public String getId_restoran() {
        return id_restoran;
    }

    public void setId_restoran(String id_restoran) {
        this.id_restoran = id_restoran;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public String getFoto_menu() {
        return foto_menu;
    }

    public void setFoto_menu(String foto_menu) {
        this.foto_menu = foto_menu;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getTag_menu() {
        return tag_menu;
    }

    public void setTag_menu(String tag_menu) {
        this.tag_menu = tag_menu;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
