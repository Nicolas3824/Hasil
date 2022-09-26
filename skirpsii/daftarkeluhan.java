package com.example.skirpsii;

public class daftarkeluhan {

    String tanggal_keluhan,judul_keluhan,isi_keluhan,status_keluhan,nota_admin;

    public daftarkeluhan(){

    }

    public daftarkeluhan(String tanggal_keluhan, String judul_keluhan, String isi_keluhan, String status_keluhan,String nota_admin) {
        this.tanggal_keluhan = tanggal_keluhan;
        this.judul_keluhan = judul_keluhan;
        this.isi_keluhan = isi_keluhan;
        this.status_keluhan = status_keluhan;
        this.nota_admin = nota_admin;
    }

    public String getNota_admin() {
        return nota_admin;
    }

    public void setNota_admin(String nota_admin) {
        this.nota_admin = nota_admin;
    }

    public String getTanggal_keluhan() {
        return tanggal_keluhan;
    }

    public void setTanggal_keluhan(String tanggal_keluhan) {
        this.tanggal_keluhan = tanggal_keluhan;
    }

    public String getJudul_keluhan() {
        return judul_keluhan;
    }

    public void setJudul_keluhan(String judul_keluhan) {
        this.judul_keluhan = judul_keluhan;
    }

    public String getIsi_keluhan() {
        return isi_keluhan;
    }

    public void setIsi_keluhan(String isi_keluhan) {
        this.isi_keluhan = isi_keluhan;
    }

    public String getStatus_keluhan() {
        return status_keluhan;
    }

    public void setStatus_keluhan(String status_keluhan) {
        this.status_keluhan = status_keluhan;
    }
}
