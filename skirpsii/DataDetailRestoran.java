package com.example.skirpsii;

public class DataDetailRestoran {

    String status_restoran="",kontak="",tingkat_harga="",fasilitas_restoran="",alamat="",daerah_restoran="",jam_buka="",deskripsi="";

    public DataDetailRestoran(String status_restoran, String kontak, String tingkat_harga, String fasilitas_restoran, String alamat, String daerah_restoran, String jam_buka, String deskripsi) {
        this.status_restoran = status_restoran;
        this.kontak = kontak;
        this.tingkat_harga = tingkat_harga;
        this.fasilitas_restoran = fasilitas_restoran;
        this.alamat = alamat;
        this.daerah_restoran = daerah_restoran;
        this.jam_buka = jam_buka;
        this.deskripsi = deskripsi;
    }

    public DataDetailRestoran(){

    }

    public String getStatus_restoran() {
        return status_restoran;
    }

    public void setStatus_restoran(String status_restoran) {
        this.status_restoran = status_restoran;
    }

    public String getKontak() {
        return kontak;
    }

    public void setKontak(String kontak) {
        this.kontak = kontak;
    }

    public String getTingkat_harga() {
        return tingkat_harga;
    }

    public void setTingkat_harga(String tingkat_harga) {
        this.tingkat_harga = tingkat_harga;
    }

    public String getFasilitas_restoran() {
        return fasilitas_restoran;
    }

    public void setFasilitas_restoran(String fasilitas_restoran) {
        this.fasilitas_restoran = fasilitas_restoran;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getDaerah_restoran() {
        return daerah_restoran;
    }

    public void setDaerah_restoran(String daerah_restoran) {
        this.daerah_restoran = daerah_restoran;
    }

    public String getJam_buka() {
        return jam_buka;
    }

    public void setJam_buka(String jam_buka) {
        this.jam_buka = jam_buka;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
