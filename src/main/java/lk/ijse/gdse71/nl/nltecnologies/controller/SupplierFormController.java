package lk.ijse.gdse71.nl.nltecnologies.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import lk.ijse.gdse71.nl.nltecnologies.db.DbConnection;
import lk.ijse.gdse71.nl.nltecnologies.dto.Supplier;
import lk.ijse.gdse71.nl.nltecnologies.dto.tm.SupplierTm;
import lk.ijse.gdse71.nl.nltecnologies.model.SupplierRepo;
import lk.ijse.gdse71.nl.nltecnologies.util.Regex;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.textfield.TextFields;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SupplierFormController {

    @FXML
    private TableColumn<?, ?> colCompanyName;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colLocation;

    @FXML
    private TableColumn<?, ?> colPersonName;

    @FXML
    private TableColumn<?, ?> colSupId;

    @FXML
    private TableColumn<?, ?> coltel;

    @FXML
    private TableView<SupplierTm> tblSupplier;

    @FXML
    private TextField txtCompanyName;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtLocation;

    @FXML
    private TextField txtPersonName;

    @FXML
    private TextField txtSearchSupplier;

    @FXML
    private TextField txtSupId;

    @FXML
    public void initialize() {
       setCellValueFactory();
       loadAllSuppliers();
       supplierCompanyName();
       getCurrentId();
    }

    private void setCellValueFactory() {
        colSupId.setCellValueFactory(new PropertyValueFactory<>("supId"));
        colCompanyName.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        colPersonName.setCellValueFactory(new PropertyValueFactory<>("personName"));
        coltel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadAllSuppliers() {
        ObservableList<SupplierTm> obList = FXCollections.observableArrayList();

        try {
            List<Supplier> suppliersList = SupplierRepo.getAll();
            for (Supplier supplier : suppliersList){
                SupplierTm tm = new SupplierTm(
                        supplier.getSupId(),
                        supplier.getCompanyName(),
                        supplier.getPersonName(),
                        supplier.getTel(),
                        supplier.getLocation(),
                        supplier.getEmail()
                );
                obList.add(tm);
            }
            tblSupplier.setItems(obList);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String supId = txtSupId.getText();
        String companyName = txtCompanyName.getText();
        String personName = txtPersonName.getText();
        String tel = txtContact.getText();
        String location = txtLocation.getText();
        String email = txtEmail.getText();

        Supplier suppler = new Supplier(supId, companyName, personName, tel, location , email );

        try {
            if(isValidate()) {
                boolean isSaved = SupplierRepo.save(suppler);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "supplier saved!").show();
                    clearFields();
                    initialize();
                }
            }else{
                new Alert(Alert.AlertType.INFORMATION, "The data you entered is incorrect").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Update Supplier?", yes, no).showAndWait();

        if (type.orElse(no) == yes) {
            String supId = txtSupId.getText();
            String companyName = txtCompanyName.getText();
            String personName = txtPersonName.getText();
            String tel = txtContact.getText();
            String location = txtLocation.getText();
            String email = txtEmail.getText();

            Supplier suppler = new Supplier(supId, companyName, personName, tel, location, email);

            try {
                if (isValidate()) {
                    boolean isSaved = SupplierRepo.update(suppler);
                    if (isSaved) {
                        new Alert(Alert.AlertType.CONFIRMATION, "supplier update!").show();
                        clearFields();
                        initialize();
                    }
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "The data you entered is incorrect").show();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtSupId.setText("");
        txtCompanyName.setText("");
        txtPersonName.setText("");
        txtContact.setText("");
        txtLocation.setText("");
        txtEmail.setText("");
        txtSearchSupplier.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION,"Are you sure to Delete Supplier?",yes,no).showAndWait();

        if (type.orElse(no) ==  yes){
            String id = txtSupId.getText();

            try {
                boolean isDeleted = SupplierRepo.delete(id);
                if (isDeleted){
                    new Alert(Alert.AlertType.CONFIRMATION,"Supplier Deleted!").show();
                    clearFields();
                    initialize();
                }
            }catch (SQLException e){
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private String getCurrentId() {
        String nextId = "";

        try {
            String currentId = SupplierRepo.getLastId();

            nextId = generateNextId(currentId);
            txtSupId.setText(nextId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nextId;
    }

    private String generateNextId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("S");  //" ", "2"
            int idNum = Integer.parseInt(split[1]);

            if(idNum >= 1){
                return "S" + 0 + 0 + ++idNum;
            }else if(idNum >= 9){
                return "S" + 0 + ++idNum;
            } else if(idNum >= 99){
                return "S" + ++idNum;
            }
        }
        return "S001";
    }

    private void supplierCompanyName() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> nameList = SupplierRepo.getName();

            for (String name : nameList) {
                obList.add(name);
            }
            TextFields.bindAutoCompletion(txtSearchSupplier, obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtSearchSupplierOnAction(ActionEvent event) {
        try {
            btnSearchSupplierOnAction();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSearchSupplierOnAction() throws SQLException {
        String name = txtSearchSupplier.getText();

        Supplier supplier = SupplierRepo.searchByName(name);
        if (supplier != null) {
            txtSupId.setText(supplier.getSupId());
            txtCompanyName.setText(supplier.getCompanyName());
            txtPersonName.setText(supplier.getPersonName());
            txtLocation.setText(supplier.getLocation());
            txtEmail.setText(supplier.getEmail());
            txtContact.setText(supplier.getTel());
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Customer not found!").show();
        }
    }

    @FXML
    void txtCompanyNameOnAction(ActionEvent event) {
        txtPersonName.requestFocus();
    }

    @FXML
    void txtLocationOnAction(ActionEvent event) {
        txtEmail.requestFocus();
    }

    @FXML
    void txtPersonNameOnAction(ActionEvent event) {
        txtContact.requestFocus();
    }

    @FXML
    void txtSupIdOnAction(ActionEvent event) {
        txtCompanyName.requestFocus();
    }

    @FXML
    void txtContactNameOnAction(ActionEvent event) {
        txtLocation.requestFocus();
    }

    @FXML
    void txtSupIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.SID,txtSupId);
    }

    @FXML
    void txtContactNoOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.PHONENO,txtContact);
    }

    @FXML
    void txtEmailOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.EMAIL,txtEmail);
    }

    private boolean isIdValidate() {
        if (!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.SID,txtSupId)) return false;
        return true;
    }

    private boolean isValidate() {
        if (!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.SID,txtSupId)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.EMAIL,txtEmail)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.PHONENO,txtContact)) return false;

        return true;
    }

    // Supplier Report -----------------------------------------------------------------------------------------------------------
    @FXML
    void btnSupplierReportOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("report/SupplerItemReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();
        if(isIdValidate()) {
            data.put("supId", txtSupId.getText());
        }else{
            new Alert(Alert.AlertType.INFORMATION, "Supplier Id you entered is incorrect").show();
        }

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }

}
