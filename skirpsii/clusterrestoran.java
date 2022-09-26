package com.example.skirpsii;

public class clusterrestoran {

    String id_restoran,nama_restoran,jenis_restoran,alamat,foto_resto,status_restoran,rekening_pembayaran;

    public clusterrestoran(){

    }

    public clusterrestoran(String id_restoran, String nama_restoran, String jenis_restoran, String alamat, String foto_resto, String status_restoran, String rekening_pembayaran) {
        this.id_restoran = id_restoran;
        this.nama_restoran = nama_restoran;
        this.jenis_restoran = jenis_restoran;
        this.alamat = alamat;
        this.foto_resto = foto_resto;
        this.status_restoran = status_restoran;
        this.rekening_pembayaran = rekening_pembayaran;
    }

    public String getId_restoran() {
        return id_restoran;
    }

    public void setId_restoran(String id_restoran) {
        this.id_restoran = id_restoran;
    }

    public String getNama_restoran() {
        return nama_restoran;
    }

    public void setNama_restoran(String nama_restoran) {
        this.nama_restoran = nama_restoran;
    }

    public String getJenis_restoran() {
        return jenis_restoran;
    }

    public void setJenis_restoran(String jenis_restoran) {
        this.jenis_restoran = jenis_restoran;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getFoto_resto() {
        return foto_resto;
    }

    public void setFoto_resto(String foto_resto) {
        this.foto_resto = foto_resto;
    }

    public String getStatus_restoran() {
        return status_restoran;
    }

    public void setStatus_restoran(String status_restoran) {
        this.status_restoran = status_restoran;
    }

    public String getRekening_pembayaran() {
        return rekening_pembayaran;
    }

    public void setRekening_pembayaran(String rekening_pembayaran) {
        this.rekening_pembayaran = rekening_pembayaran;
    }
}
