package com.dinus;

public class Krs {
    private String nim;
    private String namaMhs;
    private String kodeMk;
    private String namaMk;
    private String kelas;
    private String status;

    public Krs(String nim, String namaMhs, String kodeMk, String namaMk, String kelas, String status) {
        this.nim = nim;
        this.namaMhs = namaMhs;
        this.kodeMk = kodeMk;
        this.namaMk = namaMk;
        this.kelas = kelas;
        this.status = status;
    }

    public String getNim() {
        return nim;
    }

    public String getNamaMhs() {
        return namaMhs;
    }

    public String getKodeMk() {
        return kodeMk;
    }

    public String getNamaMk() {
        return namaMk;
    }

    public String getKelas() {
        return kelas;
    }

    public String getStatus() {
        return status;
    }
}
