package com.dinus;

import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class KrsController implements Initializable {
    ObservableList<Krs> listKrs;
    boolean flagEdit;

    @FXML
    private TextField tfNamaMhs, tfNamaMk;
    @FXML
    private ComboBox<String> cbNim, cbKodeMk, cbKelas, cbStatus;
    @FXML
    private Button btnAdd, btnEdit, btnDelete, btnUpdate, btnCancel;
    @FXML
    private TableView<Krs> tvKrs;
    @FXML
    private TableColumn<Krs, String> colNim, colNamaMhs, colKodeMk, colNamaMk, colKelas, colStatus;

    private ObservableList<Mhs> listMhs;
    private ObservableList<Matakuliah> listMk;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listKrs = FXCollections.observableArrayList();
        listMhs = AksesDB.getDataMhs();
        listMk = AksesDB.getDataMatakuliah();

        ObservableList<String> nimItems = FXCollections.observableArrayList();
        for (Mhs m : listMhs)
            nimItems.add(m.getNim());
        cbNim.setItems(nimItems);

        ObservableList<String> mkItems = FXCollections.observableArrayList();
        for (Matakuliah m : listMk)
            mkItems.add(m.getKodeMk());
        cbKodeMk.setItems(mkItems);
        loadKelas();
        cbStatus.setItems(FXCollections.observableArrayList("baru", "ulang"));

        initTabel();
        loadData();
        teksAktif(false);
        buttonAktif(false);
        tfNamaMhs.setEditable(false);
        tfNamaMk.setEditable(false);
    }

    public void initTabel() {
        colNim.setCellValueFactory(new PropertyValueFactory<>("nim"));
        colNamaMhs.setCellValueFactory(new PropertyValueFactory<>("namaMhs"));
        colKodeMk.setCellValueFactory(new PropertyValueFactory<>("kodeMk"));
        colNamaMk.setCellValueFactory(new PropertyValueFactory<>("namaMk"));
        colKelas.setCellValueFactory(new PropertyValueFactory<>("kelas"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void loadData() {
        listKrs = AksesDB.getDataKrs();
        tvKrs.setItems(listKrs);
    }

    public void teksAktif(boolean aktif) {
        cbNim.setDisable(!aktif);
        cbKodeMk.setDisable(!aktif);
        cbKelas.setDisable(!aktif);
        cbStatus.setDisable(!aktif);
    }

    public void buttonAktif(boolean aktif) {
        btnAdd.setDisable(aktif);
        btnEdit.setDisable(aktif);
        btnDelete.setDisable(aktif);
        btnUpdate.setDisable(!aktif);
        btnCancel.setDisable(!aktif);
    }

    public void clearTeks() {
        cbNim.setValue(null);
        cbKodeMk.setValue(null);
        cbKelas.setValue(null);
        cbStatus.setValue(null);
        tfNamaMhs.clear();
        tfNamaMk.clear();
    }

    private void loadKelas() {
        ObservableList<String> listKelas = FXCollections.observableArrayList();
        try (Connection conn = KoneksiDB.getConn();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT DISTINCT kelas FROM jadwal")) {

            while (rs.next()) {
                listKelas.add(rs.getString("kelas"));
            }
            cbKelas.setItems(listKelas);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Gagal memuat daftar kelas.").show();
        }
    }

    @FXML
    void add(ActionEvent e) {
        flagEdit = false;
        teksAktif(true);
        buttonAktif(true);
        cbNim.requestFocus();
    }

    @FXML
    void cancel(ActionEvent e) {
        clearTeks();
        teksAktif(false);
        buttonAktif(false);
    }

    @FXML
    void delete(ActionEvent e) {
        int idx = tvKrs.getSelectionModel().getSelectedIndex();
        if (idx < 0)
            return;
        Krs k = tvKrs.getItems().get(idx);
        AksesDB.delDataKrs(k.getNim(), k.getKodeMk());
        loadData();
        new Alert(Alert.AlertType.INFORMATION, "Data berhasil dihapus!").show();
    }

    @FXML
    void edit(ActionEvent e) {
        int idx = tvKrs.getSelectionModel().getSelectedIndex();
        if (idx < 0)
            return;

        flagEdit = true;
        teksAktif(true);
        buttonAktif(true);

        Krs k = tvKrs.getItems().get(idx);
        cbNim.setValue(k.getNim());
        cbKodeMk.setValue(k.getKodeMk());
        cbKelas.setValue(k.getKelas());
        cbStatus.setValue(k.getStatus());
        tfNamaMk.setText(k.getNamaMk());
        tfNamaMhs.setText(k.getNamaMhs());
    }

    @FXML
    void update(ActionEvent e) {
        String nim = cbNim.getValue();
        String kodeMk = cbKodeMk.getValue();
        String kelas = cbKelas.getValue();
        String status = cbStatus.getValue();

        if (nim == null || kodeMk == null || kelas == null || status == null) {
            new Alert(Alert.AlertType.WARNING, "Semua field wajib diisi!").show();
            return;
        }

        if (flagEdit) {
            AksesDB.updateDataKrs(nim, kodeMk, kelas, status);
            new Alert(Alert.AlertType.INFORMATION, "Data berhasil diperbarui!").show();
        } else {
            AksesDB.addDataKrs(nim, kodeMk, kelas, status);
            new Alert(Alert.AlertType.INFORMATION, "Data berhasil ditambahkan!").show();
        }

        loadData();
        clearTeks();
        teksAktif(false);
        buttonAktif(false);
        flagEdit = false;
    }

    @FXML
    void onPilihMhs() {
        String nim = cbNim.getValue();
        for (Mhs m : listMhs) {
            if (m.getNim().equals(nim)) {
                tfNamaMhs.setText(m.getNama());
                break;
            }
        }
    }

    @FXML
    void onPilihMk() {
        String kode = cbKodeMk.getValue();
        for (Matakuliah m : listMk) {
            if (m.getKodeMk().equals(kode)) {
                tfNamaMk.setText(m.getNamaMk());
                break;
            }
        }

        // Isi kelas secara dinamis
        ObservableList<String> kelasList = AksesDB.getKelasByKodeMk(kode);
        cbKelas.setItems(kelasList);
        cbKelas.setValue(null); // reset value sebelumnya
    }

}