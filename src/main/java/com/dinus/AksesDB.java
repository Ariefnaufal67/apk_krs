package com.dinus;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AksesDB {

   // ---------- Koneksi ----------
   public static Connection getConn() {
      try {
         return KoneksiDB.getConn();
      } catch (Exception e) {
         throw new RuntimeException("Koneksi gagal: " + e.getMessage());
      }
   }

   // ---------- CRUD MHS ----------
   public static ObservableList<Mhs> getDataMhs() {
      ObservableList<Mhs> list = FXCollections.observableArrayList();
      try {
         Connection c = getConn();
         String sql = "SELECT * FROM mhs";
         PreparedStatement ps = c.prepareStatement(sql);
         ResultSet rs = ps.executeQuery();
         while (rs.next()) {
            list.add(new Mhs(
                  rs.getString("nim"),
                  rs.getString("nama"),
                  rs.getString("alamat")));
         }
      } catch (SQLException ex) {
         Logger.getLogger(AksesDB.class.getName()).log(Level.SEVERE, null, ex);
      }
      return list;
   }

   public static void addDataMhs(String nim, String nama, String alamat) {
      String sql = "INSERT INTO mhs(nim, nama, alamat) VALUES (?, ?, ?)";
      try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
         ps.setString(1, nim);
         ps.setString(2, nama);
         ps.setString(3, alamat);
         ps.executeUpdate();
      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }

   public static void updateDataMhs(String nimBaru, String nama, String nimLama, String alamat) {
      String sql = "UPDATE mhs SET nim=?, nama=?, alamat=? WHERE nim=?";
      try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
         ps.setString(1, nimBaru);
         ps.setString(2, nama);
         ps.setString(3, alamat);
         ps.setString(4, nimLama);
         ps.executeUpdate();
      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }

   public static void delDataMhs(String nim) {
      String sql = "DELETE FROM mhs WHERE nim=?";
      try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
         ps.setString(1, nim);
         ps.executeUpdate();
      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }

   // ---------- CRUD MATAKULIAH ----------
   public static ObservableList<Matakuliah> getDataMatakuliah() {
      ObservableList<Matakuliah> list = FXCollections.observableArrayList();
      try {
         Connection c = getConn();
         String sql = "SELECT * FROM matakuliah";
         PreparedStatement ps = c.prepareStatement(sql);
         ResultSet rs = ps.executeQuery();
         while (rs.next()) {
            list.add(new Matakuliah(
                  rs.getString("kode_mk"),
                  rs.getString("nama_mk"),
                  rs.getInt("sks")));
         }
      } catch (SQLException ex) {
         Logger.getLogger(AksesDB.class.getName()).log(Level.SEVERE, null, ex);
      }
      return list;
   }

   public static void addDataMatakuliah(String kodeMk, String namaMk, int sks) {
      String sql = "INSERT INTO matakuliah(kode_mk, nama_mk, sks) VALUES (?, ?, ?)";
      try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
         ps.setString(1, kodeMk);
         ps.setString(2, namaMk);
         ps.setInt(3, sks);
         ps.executeUpdate();
      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }

   public static void updateDataMatakuliah(String kodeBaru, String namaMk, int sks, String kodeLama) {
      String sql = "UPDATE matakuliah SET kode_mk=?, nama_mk=?, sks=? WHERE kode_mk=?";
      try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
         ps.setString(1, kodeBaru);
         ps.setString(2, namaMk);
         ps.setInt(3, sks);
         ps.setString(4, kodeLama);
         ps.executeUpdate();
      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }

   public static void delDataMatakuliah(String kodeMk) {
      String sql = "DELETE FROM matakuliah WHERE kode_mk=?";
      try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
         ps.setString(1, kodeMk);
         ps.executeUpdate();
      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }

   // ---------- CRUD JADWAL ----------
   public static ObservableList<Jadwal> getDataJadwal() {
      ObservableList<Jadwal> list = FXCollections.observableArrayList();
      try {
         Connection c = getConn();
         String sql = "SELECT j.kode_mk, j.kelas, m.nama_mk, j.hari, j.jam, j.ruang, j.kode_dsn " +
               "FROM jadwal j " +
               "JOIN matakuliah m ON j.kode_mk = m.kode_mk";
         PreparedStatement ps = c.prepareStatement(sql);
         ResultSet rs = ps.executeQuery();
         while (rs.next()) {
            list.add(new Jadwal(
                  rs.getString("kode_mk"),
                  rs.getString("nama_mk"),
                  rs.getString("kelas"),
                  rs.getString("hari"),
                  rs.getString("jam"),
                  rs.getString("ruang"),
                  rs.getString("kode_dsn") // Tambahan kode dosen
            ));
         }
      } catch (SQLException ex) {
         Logger.getLogger(AksesDB.class.getName()).log(Level.SEVERE, null, ex);
      }
      return list;
   }

   public static void addDataJadwal(String kodeMk, String kelas, String hari, String jam, String ruang,
         String kodeDsn) {
      String sql = "INSERT INTO jadwal(kode_mk, kelas, hari, jam, ruang,  kode_dsn) VALUES (?, ?, ?, ?, ?,?)";
      try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
         ps.setString(1, kodeMk);
         ps.setString(2, kelas);
         ps.setString(3, hari);
         ps.setString(4, jam);
         ps.setString(5, ruang);
         ps.setString(6, kodeDsn);
         ps.executeUpdate();
      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }

   public static void updateDataJadwal(String kodeMk, String kelas, String hari, String jam, String ruang,
         String kodeDsn) {
      String sql = "UPDATE jadwal SET hari=?, jam=?, ruang=?, kode_dsn=? WHERE kode_mk=? AND kelas=?";
      try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
         ps.setString(1, hari); // SET hari
         ps.setString(2, jam); // SET jam
         ps.setString(3, ruang); // SET ruang
         ps.setString(4, kodeDsn); // SET kode_dsn
         ps.setString(5, kodeMk); // WHERE kode_mk = ?
         ps.setString(6, kelas); // AND kelas = ?
         ps.executeUpdate();
      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }

   public static void delDataJadwal(String kodeMk, String kelas) {
      String sql = "DELETE FROM jadwal WHERE kode_mk=? AND kelas=?";
      try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
         ps.setString(1, kodeMk);
         ps.setString(2, kelas);
         ps.executeUpdate();
      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }

   // ---------- USER AUTH ----------
   public static void signUpUser(ActionEvent event, String userName, String password) {
      try (Connection conn = getConn()) {
         String checkSql = "SELECT * FROM user WHERE username=?";
         PreparedStatement checkSt = conn.prepareStatement(checkSql);
         checkSt.setString(1, userName);
         ResultSet rs = checkSt.executeQuery();
         if (rs.isBeforeFirst()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username sudah terdaftar", ButtonType.OK);
            alert.show();
            return;
         }
         String insertSql = "INSERT INTO user(username, password) VALUES (?, ?)";
         PreparedStatement insertSt = conn.prepareStatement(insertSql);
         insertSt.setString(1, userName);
         insertSt.setString(2, password);
         insertSt.executeUpdate();
         Alert alert = new Alert(Alert.AlertType.INFORMATION, "User berhasil dibuat", ButtonType.OK);
         alert.show();
         DBUtil.changeScene(event, "fMenu.fxml", "SignUp", userName);
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public static void loginUser(ActionEvent event, String userName, String password) {
      try (Connection conn = getConn()) {
         String sql = "SELECT * FROM user WHERE username=?";
         PreparedStatement st = conn.prepareStatement(sql);
         st.setString(1, userName);
         ResultSet rs = st.executeQuery();
         if (!rs.isBeforeFirst()) {
            new Alert(Alert.AlertType.ERROR, "User tidak ditemukan").show();
            return;
         }
         while (rs.next()) {
            String rpassword = rs.getString("password");
            if (rpassword.equals(password)) {
               DBUtil.changeScene(event, "fMenu.fxml", "Login Sistem KRS", userName);
            } else {
               new Alert(Alert.AlertType.ERROR, "Password salah").show();
            }
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   // ---------- KRS ----------

   public static ObservableList<Krs> getDataKrs() {
      ObservableList<Krs> list = FXCollections.observableArrayList();
      try {
         Connection c = getConn();
         String sql = "SELECT k.nim, m.nama, k.kode_mk, mk.nama_mk, k.kelas, k.status " +
               "FROM krs k " +
               "JOIN mhs m ON k.nim = m.nim " +
               "JOIN matakuliah mk ON k.kode_mk = mk.kode_mk";
         PreparedStatement ps = c.prepareStatement(sql);
         ResultSet rs = ps.executeQuery();
         while (rs.next()) {
            list.add(new Krs(
                  rs.getString("nim"),
                  rs.getString("nama"),
                  rs.getString("kode_mk"),
                  rs.getString("nama_mk"),
                  rs.getString("kelas"),
                  rs.getString("status")));
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return list;
   }

   public static void addDataKrs(String nim, String kodeMk, String kelas, String status) {
      try {
         Connection c = getConn();
         String sql = "INSERT INTO krs(nim, kode_mk, kelas, status) VALUES (?, ?, ?, ?)";
         PreparedStatement ps = c.prepareStatement(sql);
         ps.setString(1, nim);
         ps.setString(2, kodeMk);
         ps.setString(3, kelas);
         ps.setString(4, status);
         ps.executeUpdate();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public static void updateDataKrs(String nim, String kodeMk, String kelas, String status) {
      try {
         Connection c = getConn();
         String sql = "UPDATE krs SET kelas=?, status=? WHERE nim=? AND kode_mk=?";
         PreparedStatement ps = c.prepareStatement(sql);
         ps.setString(1, kelas);
         ps.setString(2, status);
         ps.setString(3, nim);
         ps.setString(4, kodeMk);
         ps.executeUpdate();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public static void delDataKrs(String nim, String kodeMk) {
      try {
         Connection c = getConn();
         String sql = "DELETE FROM krs WHERE nim=? AND kode_mk=?";
         PreparedStatement ps = c.prepareStatement(sql);
         ps.setString(1, nim);
         ps.setString(2, kodeMk);
         ps.executeUpdate();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public static ObservableList<String> getKelasByKodeMk(String kodeMk) {
      ObservableList<String> list = FXCollections.observableArrayList();
      String sql = "SELECT DISTINCT kelas FROM jadwal WHERE kode_mk = ?";
      try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
         ps.setString(1, kodeMk);
         ResultSet rs = ps.executeQuery();
         while (rs.next()) {
            list.add(rs.getString("kelas"));
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return list;
   }

   // ---------- Dosen ----------
   // Ambil semua dosen
   public static ObservableList<Dosen> getDataDosen() {
      ObservableList<Dosen> list = FXCollections.observableArrayList();
      String sql = "SELECT * FROM dosen";
      try (Connection c = getConn(); Statement s = c.createStatement(); ResultSet r = s.executeQuery(sql)) {
         while (r.next()) {
            list.add(new Dosen(r.getString("kode_dsn"), r.getString("nama_dsn")));
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return list;
   }

   // Tambah dosen
   public static void addDataDosen(String kode, String nama) {
      String sql = "INSERT INTO dosen (kode_dsn, nama_dsn) VALUES (?, ?)";
      try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
         ps.setString(1, kode);
         ps.setString(2, nama);
         ps.executeUpdate();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   // Update dosen
   public static void updateDataDosen(String kode, String nama) {
      String sql = "UPDATE dosen SET nama_dsn = ? WHERE kode_dsn = ?";
      try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
         ps.setString(1, nama);
         ps.setString(2, kode);
         ps.executeUpdate();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   // Hapus dosen
   public static void delDataDosen(String kode) {
      String sql = "DELETE FROM dosen WHERE kode_dsn = ?";
      try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
         ps.setString(1, kode);
         ps.executeUpdate();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

}
