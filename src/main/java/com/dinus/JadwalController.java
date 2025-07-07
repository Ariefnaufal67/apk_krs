package com.dinus;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class JadwalController implements Initializable {
    ObservableList<Jadwal> listJadwal;
    boolean flagEdit;

    @FXML
    private ComboBox<String> cbDosen;
    @FXML
    private Button btnAdd, btnCancel, btnDelete, btnEdit, btnUpdate, btnPilih;
    @FXML
    private TextField tfCari, tfHari, tfJam, tfKelas, tfKodematkul, tfNmMatkul, tfRuang;
    @FXML
    private TableView<Jadwal> tbJadwal;
    @FXML
    private TableColumn<Jadwal, String> hari, jam, kelas, kodeMk, namaMk, ruang, dosen;

    @FXML
    void pilihMatkul(ActionEvent event) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(MatkulSearchController.class.getResource("fmatkulSearch.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Daftar Matakuliah");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.showAndWait();
            Matakuliah m = (Matakuliah) stage.getUserData();
            tfKodematkul.setText(m.getKodeMk());
            tfNmMatkul.setText(m.getNamaMk());
        } catch (IOException ex) {
            Logger.getLogger(JadwalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void add(ActionEvent event) {
        flagEdit = false;
        teksAktif(true);
        buttonAktif(true);
        tfKodematkul.requestFocus();
    }

    @FXML
    void cancel(ActionEvent event) {
        teksAktif(false);
        clearTeks();
        buttonAktif(false);
    }

    @FXML
    void delete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "hapus data ?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            int idx = tbJadwal.getSelectionModel().getSelectedIndex();
            String vKodeMk = tbJadwal.getItems().get(idx).getKodeMk();
            String vKelas = tbJadwal.getItems().get(idx).getKelas();
            AksesDB.delDataJadwal(vKodeMk, vKelas);
            new Alert(Alert.AlertType.INFORMATION, "Delete Data Sukses..").show();
            loadData();
        }
    }

    @FXML
    void edit(ActionEvent event) {
        buttonAktif(true);
        teksAktif(true);
        flagEdit = true;
        int idx = tbJadwal.getSelectionModel().getSelectedIndex();
        Jadwal j = tbJadwal.getItems().get(idx);
        tfKodematkul.setText(j.getKodeMk());
        tfKelas.setText(j.getKelas());
        tfNmMatkul.setText(j.getNamaMk());
        tfHari.setText(j.getHari());
        tfJam.setText(j.getJam());
        tfRuang.setText(j.getRuang());
        cbDosen.setValue(j.getDosen());
        tfKodematkul.requestFocus();
    }

    @FXML
    void update(ActionEvent event) {
        String vKelas = tfKelas.getText();
        String vKodeMk = tfKodematkul.getText();
        String vHari = tfHari.getText();
        String vJam = tfJam.getText();
        String vRuang = tfRuang.getText();
        String vDosen = cbDosen.getValue();

        if (!flagEdit) {
            AksesDB.addDataJadwal(vKodeMk, vKelas, vHari, vJam, vRuang, vDosen);
            new Alert(Alert.AlertType.INFORMATION, "Insert Data Sukses..").show();
        } else {
            int idx = tbJadwal.getSelectionModel().getSelectedIndex();
            String oldKelas = tbJadwal.getItems().get(idx).getKelas();
            AksesDB.updateDataJadwal(vKodeMk, oldKelas, vHari, vJam, vRuang, vDosen);
            new Alert(Alert.AlertType.INFORMATION, "Update Data Berhasil").show();
        }

        loadData();
        clearTeks();
        teksAktif(false);
        buttonAktif(false);
        flagEdit = false;
    }

    void buttonAktif(boolean nonAktif) {
        btnAdd.setDisable(nonAktif);
        btnEdit.setDisable(nonAktif);
        btnDelete.setDisable(nonAktif);
        btnUpdate.setDisable(!nonAktif);
        btnCancel.setDisable(!nonAktif);
    }

    void teksAktif(boolean aktif) {
        tfKelas.setEditable(aktif);
        tfKodematkul.setEditable(aktif);
        tfNmMatkul.setEditable(aktif);
        tfHari.setEditable(aktif);
        tfJam.setEditable(aktif);
        tfRuang.setEditable(aktif);
        cbDosen.setDisable(!aktif);
    }

    void clearTeks() {
        tfKelas.setText("");
        tfKodematkul.setText("");
        tfNmMatkul.setText("");
        tfHari.setText("");
        tfJam.setText("");
        tfRuang.setText("");
        cbDosen.setValue(null);
    }

    void initTabel() {
        kelas.setCellValueFactory(new PropertyValueFactory<>("kelas"));
        kodeMk.setCellValueFactory(new PropertyValueFactory<>("kodeMk"));
        namaMk.setCellValueFactory(new PropertyValueFactory<>("namaMk"));
        hari.setCellValueFactory(new PropertyValueFactory<>("hari"));
        jam.setCellValueFactory(new PropertyValueFactory<>("jam"));
        ruang.setCellValueFactory(new PropertyValueFactory<>("ruang"));
        dosen.setCellValueFactory(new PropertyValueFactory<>("dosen"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listJadwal = FXCollections.observableArrayList();
        initTabel();
        loadData();
        setFilter();
        loadDosen();
        buttonAktif(false);
        teksAktif(false);
        flagEdit = false;
    }

    void loadDosen() {
        ObservableList<String> listDosen = FXCollections.observableArrayList();
        try (Connection conn = KoneksiDB.getConn();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT kode_dsn FROM dosen")) {
            while (rs.next()) {
                listDosen.add(rs.getString("kode_dsn"));
            }
            cbDosen.setItems(listDosen);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Gagal memuat daftar dosen.").show();
        }
    }

    void loadData() {
        listJadwal = AksesDB.getDataJadwal();
        tbJadwal.setItems(listJadwal);
    }

    void setFilter() {
        FilteredList<Jadwal> filterData = new FilteredList<>(listJadwal, b -> true);
        tfCari.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(j -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String keyword = newValue.toLowerCase();
                return j.getNamaMk().toLowerCase().contains(keyword)
                        || j.getKodeMk().toLowerCase().contains(keyword);
            });
        });
        SortedList<Jadwal> sortedData = new SortedList<>(filterData);
        sortedData.comparatorProperty().bind(tbJadwal.comparatorProperty());
        tbJadwal.setItems(sortedData);
    }
}
