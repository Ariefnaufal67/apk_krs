package com.dinus;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DosenController {
    @FXML
    private TextField tfKodeDsn, tfNamaDsn;
    @FXML
    private Button btnAdd, btnEdit, btnDelete, btnUpdate, btnCancel;
    @FXML
    private TableView<Dosen> tbDosen;
    @FXML
    private TableColumn<Dosen, String> colKode, colNama;

    private ObservableList<Dosen> listDosen;
    private boolean flagEdit = false;

    @FXML
    void initialize() {
        colKode.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getKodeDsn()));
        colNama.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNamaDsn()));
        loadData();
        teksAktif(false);
        tombolAktif(false);
    }

    void loadData() {
        listDosen = AksesDB.getDataDosen();
        tbDosen.setItems(listDosen);
    }

    void teksAktif(boolean aktif) {
        tfKodeDsn.setEditable(aktif && !flagEdit);
        tfNamaDsn.setEditable(aktif);
    }

    void tombolAktif(boolean aktif) {
        btnAdd.setDisable(aktif);
        btnEdit.setDisable(aktif);
        btnDelete.setDisable(aktif);
        btnUpdate.setDisable(!aktif);
        btnCancel.setDisable(!aktif);
    }

    void clearTeks() {
        tfKodeDsn.clear();
        tfNamaDsn.clear();
    }

    @FXML
    void add(ActionEvent e) {
        flagEdit = false;
        teksAktif(true);
        tombolAktif(true);
        tfKodeDsn.requestFocus();
    }

    @FXML
    void edit(ActionEvent e) {
        Dosen d = tbDosen.getSelectionModel().getSelectedItem();
        if (d == null)
            return;

        tfKodeDsn.setText(d.getKodeDsn());
        tfNamaDsn.setText(d.getNamaDsn());
        flagEdit = true;
        teksAktif(true);
        tombolAktif(true);
    }

    @FXML
    void delete(ActionEvent e) {
        Dosen d = tbDosen.getSelectionModel().getSelectedItem();
        if (d == null)
            return;

        AksesDB.delDataDosen(d.getKodeDsn());
        loadData();
        clearTeks();
    }

    @FXML
    void update(ActionEvent e) {
        String kode = tfKodeDsn.getText();
        String nama = tfNamaDsn.getText();

        if (flagEdit) {
            AksesDB.updateDataDosen(kode, nama);
        } else {
            AksesDB.addDataDosen(kode, nama);
        }

        loadData();
        clearTeks();
        tombolAktif(false);
        teksAktif(false);
        flagEdit = false;
    }

    @FXML
    void cancel(ActionEvent e) {
        clearTeks();
        teksAktif(false);
        tombolAktif(false);
    }
}
