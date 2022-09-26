package com.example.skirpsii;

public class UserNE {

    String id_konsumen,komentar,totalrating;

    public UserNE(String id_konsumen, String komentar, String totalrating) {
        this.id_konsumen = id_konsumen;
        this.komentar = komentar;
        this.totalrating = totalrating;
    }

    public String getId_konsumen() {
        return id_konsumen;
    }

    public void setId_konsumen(String id_konsumen) {
        this.id_konsumen = id_konsumen;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public String getTotalrating() {
        return totalrating;
    }

    public void setTotalrating(String totalrating) {
        this.totalrating = totalrating;
    }

    public UserNE() {

    }

}
