package com.dinus;

public class Jadwal {
    String kelas;
    String kodeMk;
    String namaMk;
    String hari;
    String jam;
    String ruang;
    String dosen;

    Jadwal(String kodeMk, String namaMk, String kelas, String hari, String jam, String ruang, String dosen) {
        this.kelas = kelas;
        this.kodeMk = kodeMk;
        this.namaMk = namaMk;
        this.hari = hari;
        this.jam = jam;
        this.ruang = ruang;
        this.dosen = dosen;

    }

    public String getKodeMk() {
        return kodeMk;
    }

    public String getKelas() {
        return kelas;
    }

    public String getNamaMk() {
        return namaMk;
    }

    public String getHari() {
        return hari;
    }

    public String getJam() {
        return jam;
    }

    public String getRuang() {
        return ruang;
    }

    public String getDosen() {
        return dosen;
    }
}
