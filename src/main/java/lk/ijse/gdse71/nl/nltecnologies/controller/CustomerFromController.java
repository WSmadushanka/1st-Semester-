package lk.ijse.gdse71.nl.nltecnologies.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.gdse71.nl.nltecnologies.QrGenerateor;
import lk.ijse.gdse71.nl.nltecnologies.dto.Customer;
import lk.ijse.gdse71.nl.nltecnologies.dto.tm.CustomerTm;
import lk.ijse.gdse71.nl.nltecnologies.model.CustomerRepo;
import lk.ijse.gdse71.nl.nltecnologies.util.Regex;
import org.controlsfx.control.textfield.TextFields;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CustomerFromController {

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colNIC;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colTel;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<CustomerTm> tblCustomer;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNIC;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSearchCustomers;

    @FXML
    private TextField txtTel;

    @FXML
    public void initialize(){
        setCellValueFactory();
        loadAllCustomers();
        getCustomerTel();
        getCurrentId();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("cName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("cAddress"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("cNIC"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("cEmail"));
    }

    private void loadAllCustomers() {
        ObservableList<CustomerTm> obList = FXCollections.observableArrayList();

        try {
            List<Customer> customerList = CustomerRepo.getAll();
            for (Customer customer : customerList){
                CustomerTm tm = new CustomerTm(
                        customer.getCustId(),
                        customer.getCName(),
                        customer.getCAddress(),
                        customer.getCNIC(),
                        customer.getContactNo(),
                        customer.getCEmail()
                );
                obList.add(tm);
            }
            tblCustomer.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String nic = txtNIC.getText();
        String contact = txtTel.getText();
        String email = txtEmail.getText();

        Customer customer = new Customer(id,name,address,nic,contact,email);

        try {
            if (isValidate()){
                boolean isSaved = CustomerRepo.save(customer);
                if (isSaved){
                    QrGenerateor.setData(contact,email,name);
                    new Alert(Alert.AlertType.CONFIRMATION,"Customer Saved!").show();
                    initialize();
                    clearFields();
                }
            }else {
                new Alert(Alert.AlertType.INFORMATION,"The data you entered is Incorrect!").show();
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Update Customer?", yes, no).showAndWait();

        if (type.orElse(no) == yes) {
            String id = txtId.getText();
            String name = txtName.getText();
            String address = txtAddress.getText();
            String nic = txtNIC.getText();
            String contact = txtTel.getText();
            String email = txtEmail.getText();

            Customer customer = new Customer(id, name, address, nic, contact, email); // Set Customer Data

            try {
                if (isValidate()) { // Add Validation
                    boolean isUpdated = CustomerRepo.update(customer);
                    if (isUpdated) {
                        new Alert(Alert.AlertType.CONFIRMATION, "customer updated!").show();
                        initialize();
                        clearFields();
                    }
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "The data you entered is incorrect").show();
                }

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Delete Customer?", yes, no).showAndWait();

        if (type.orElse(no) == yes) {
            String id = txtId.getText();

            try {
                boolean isDeleted = CustomerRepo.delete(id);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "customer deleted!").show();
                    clearFields();
                    initialize();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtNIC.setText("");
        txtTel.setText("");
        txtEmail.setText("");
        txtSearchCustomers.setText("");
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private String getCurrentId() {
        String nextId = "";

        try {
            String currentId = CustomerRepo.getLastId();
            nextId = generateNextId(currentId);
            txtId.setText(nextId);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return nextId;
    }

    private String generateNextId(String currentId) {
        if (currentId != null){
            String[] splits = currentId.split("C");
            int idNum = Integer.parseInt(splits[1]);

            if (idNum >= 1){
                return "C" + 0 + 0 + ++idNum;
            }else if (idNum >= 9){
                return "C" + 0 + ++idNum;
            } else if (idNum >= 99) {
                return "C" + ++idNum;
            }
        }
        return "C001";
    }

    @FXML
    void txtSearchCustomersOnAction(ActionEvent event) throws SQLException {
        btnSearchCustomersOnAction();
    }

    private void getCustomerTel() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> telList = CustomerRepo.getTel();

            for (String tel : telList){
                obList.add(tel);
            }
            TextFields.bindAutoCompletion(txtSearchCustomers,obList);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSearchCustomersOnAction() throws SQLException{
        String tel = txtSearchCustomers.getText();

        Customer customer = CustomerRepo.searchByTel(String.valueOf(tel));
        if (customer != null) {
            txtId.setText(customer.getCustId());
            txtName.setText(customer.getCName());
            txtNIC.setText(customer.getCNIC());
            txtAddress.setText(customer.getCAddress());
            txtEmail.setText(customer.getCEmail());
            txtTel.setText(customer.getContactNo());
        } else {
            new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
        }
    }

    // Focus Actions -------------------------------------------------------
    @FXML
    void addressOnAction(ActionEvent event) {
        txtNIC.requestFocus();
    }

    @FXML
    void nameOnAction(ActionEvent event) {
        txtAddress.requestFocus();
    }

    @FXML
    void nicOnAction(ActionEvent event) {
        txtTel.requestFocus();
    }

    @FXML
    void telOnAction(ActionEvent event) {
        txtEmail.requestFocus();
    }

    @FXML
    void txtIdOnAction(ActionEvent event) {
        txtName.requestFocus();
    }

    @FXML
    void txtAddressOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.ADDRESS,txtAddress);
    }

    //Validation---------------------------------------------------------------------------------------------------------------------------------
    @FXML
    void txtCustIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.CID,txtId);
    }

    @FXML
    void txtEmailOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.EMAIL,txtEmail);
    }

    @FXML
    void txtNicOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.NIC,txtNIC);
    }

    @FXML
    void txtTelOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.PHONENO,txtTel);
    }

    private boolean isValidate() {
        if (!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.CID,txtId))return false;
        if (!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.NIC,txtNIC))return false;
        if (!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.ADDRESS,txtAddress))return false;
        if (!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.PHONENO,txtTel))return false;
        if (!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.EMAIL,txtEmail))return false;

        return true;
    }
}
