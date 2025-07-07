package com.dinus;

import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;

public class MatkulController implements Initializable {
    ObservableList<Matakuliah> listMk;
    boolean flagEdit;

    @FXML
    private TextField tfKode;
    @FXML
    private TextField tfNama;
    @FXML
    private TextField tfSks;
    @FXML
    private TextField tfCari;
    @FXML
    private TableView<Matakuliah> tvMk;
    @FXML
    private TableColumn<Matakuliah, String> colKode;
    @FXML
    private TableColumn<Matakuliah, String> colNama;
    @FXML
    private TableColumn<Matakuliah, Integer> colSks;
    @FXML
    private Button btnAdd, btnEdit, btnDel, btnUpdate, btnCancel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listMk = FXCollections.observableArrayList();
        initTabel();
        loadData();
        setFilter();
        flagEdit = false;
        teksAktif(false);
        buttonAktif(false);
    }

    public void initTabel() {
        colKode.setCellValueFactory(new PropertyValueFactory<>("kodeMk"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaMk"));
        colSks.setCellValueFactory(new PropertyValueFactory<>("sks"));
    }

    public void loadData() {
        listMk = AksesDB.getDataMatakuliah();
        tvMk.setItems(listMk);
    }

    public void buttonAktif(boolean aktif) {
        btnAdd.setDisable(aktif);
        btnEdit.setDisable(aktif);
        btnDel.setDisable(aktif);
        btnUpdate.setDisable(!aktif);
        btnCancel.setDisable(!aktif);
    }

    public void teksAktif(boolean aktif) {
        tfKode.setEditable(aktif);
        tfNama.setEditable(aktif);
        tfSks.setEditable(aktif);
    }

    public void clearTeks() {
        tfKode.clear();
        tfNama.clear();
        tfSks.clear();
    }

    public void setFilter() {
        FilteredList<Matakuliah> filterData = new FilteredList<>(listMk, b -> true);
        tfCari.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(mk -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String keyword = newValue.toLowerCase();
                return mk.getNamaMk().toLowerCase().contains(keyword) ||
                        mk.getKodeMk().toLowerCase().contains(keyword);
            });
        });
        SortedList<Matakuliah> sorted = new SortedList<>(filterData);
        sorted.comparatorProperty().bind(tvMk.comparatorProperty());
        tvMk.setItems(sorted);
    }

    @FXML
    void add(ActionEvent e) {
        flagEdit = false;
        teksAktif(true);
        buttonAktif(true);
        tfKode.requestFocus();
    }

    @FXML
    void cancel(ActionEvent e) {
        clearTeks();
        teksAktif(false);
        buttonAktif(false);
    }

    @FXML
    void delete(ActionEvent e) {
        int idx = tvMk.getSelectionModel().getSelectedIndex();
        String kode = tvMk.getItems().get(idx).getKodeMk();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Hapus data?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            AksesDB.delDataMatakuliah(kode);
            loadData();
        }
    }

    @FXML
    void edit(ActionEvent e) {
        flagEdit = true;
        teksAktif(true);
        buttonAktif(true);
        int idx = tvMk.getSelectionModel().getSelectedIndex();
        tfKode.setText(tvMk.getItems().get(idx).getKodeMk());
        tfNama.setText(tvMk.getItems().get(idx).getNamaMk());
        tfSks.setText(String.valueOf(tvMk.getItems().get(idx).getSks()));
        tfKode.requestFocus();
    }

    @FXML
    void update(ActionEvent e) {
        String kode = tfKode.getText();
        String nama = tfNama.getText();
        int sks = Integer.parseInt(tfSks.getText());
        if (flagEdit) {
            String kodeLama = tvMk.getSelectionModel().getSelectedItem().getKodeMk();
            AksesDB.updateDataMatakuliah(kode, nama, sks, kodeLama);
            new Alert(Alert.AlertType.INFORMATION, "Data berhasil diperbarui").show();
        } else {
            AksesDB.addDataMatakuliah(kode, nama, sks);
            new Alert(Alert.AlertType.INFORMATION, "Data berhasil ditambahkan").show();
        }
        loadData();
        flagEdit = false;
        clearTeks();
        teksAktif(false);
        buttonAktif(false);
    }
}
