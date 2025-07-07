package com.dinus;

public class Dosen {
    private String kodeDsn;
    private String namaDsn;

    public Dosen(String kodeDsn, String namaDsn) {
        this.kodeDsn = kodeDsn;
        this.namaDsn = namaDsn;
    }

    public String getKodeDsn() {
        return kodeDsn;
    }

    public void setKodeDsn(String kodeDsn) {
        this.kodeDsn = kodeDsn;
    }

    public String getNamaDsn() {
        return namaDsn;
    }

    public void setNamaDsn(String namaDsn) {
        this.namaDsn = namaDsn;
    }
}
