package com.example.skirpsii;

public class historyreservasidata {

    String nama_restoran,jadwal_reservasi,jumlah_orang,status_reservasi,jenis_restoran,foto_resto,id_restoran,id_reservasi,id_seluruh_menu;

    public historyreservasidata(){

    }

    public historyreservasidata(String nama_restoran, String jadwal_reservasi, String jumlah_orang,String status_reservasi,String jenis_restoran,String foto_resto,String id_restoran,String id_reservasi,String id_seluruh_menu) {
        this.nama_restoran = nama_restoran;
        this.jadwal_reservasi = jadwal_reservasi;
        this.jumlah_orang = jumlah_orang;
        this.status_reservasi = status_reservasi;
        this.jenis_restoran = jenis_restoran;
        this.foto_resto = foto_resto;
        this.id_restoran = id_restoran;
        this.id_reservasi = id_reservasi;
        this.id_seluruh_menu = id_seluruh_menu;
    }

    public String getId_seluruh_menu() {
        return id_seluruh_menu;
    }

    public void setId_seluruh_menu(String id_seluruh_menu) {
        this.id_seluruh_menu = id_seluruh_menu;
    }

    public String getId_reservasi() {
        return id_reservasi;
    }

    public void setId_reservasi(String id_reservasi) {
        this.id_reservasi = id_reservasi;
    }

    public String getId_restoran() {
        return id_restoran;
    }

    public void setId_restoran(String id_restoran) {
        this.id_restoran = id_restoran;
    }

    public String getJenis_restoran() {
        return jenis_restoran;
    }

    public void setJenis_restoran(String jenis_restoran) {
        this.jenis_restoran = jenis_restoran;
    }

    public String getFoto_resto() {
        return foto_resto;
    }

    public void setFoto_resto(String foto_resto) {
        this.foto_resto = foto_resto;
    }

    public String getStatus_reservasi() {
        return status_reservasi;
    }

    public void setStatus_reservasi(String status_reservasi) {
        this.status_reservasi = status_reservasi;
    }

    public String getNama_restoran() {
        return nama_restoran;
    }

    public void setNama_restoran(String nama_restoran) {
        this.nama_restoran = nama_restoran;
    }

    public String getJadwal_reservasi() {
        return jadwal_reservasi;
    }

    public void setJadwal_reservasi(String jadwal_reservasi) {
        this.jadwal_reservasi = jadwal_reservasi;
    }

    public String getJumlah_orang() {
        return jumlah_orang;
    }

    public void setJumlah_orang(String jumlah_orang) {
        this.jumlah_orang = jumlah_orang;
    }
}
