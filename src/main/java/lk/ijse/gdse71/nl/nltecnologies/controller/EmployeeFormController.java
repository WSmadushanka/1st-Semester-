package lk.ijse.gdse71.nl.nltecnologies.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import lk.ijse.gdse71.nl.nltecnologies.QrGenerateor;
import lk.ijse.gdse71.nl.nltecnologies.QrReader;
import lk.ijse.gdse71.nl.nltecnologies.db.DbConnection;
import lk.ijse.gdse71.nl.nltecnologies.dto.Employee;
import lk.ijse.gdse71.nl.nltecnologies.dto.QrResult;
import lk.ijse.gdse71.nl.nltecnologies.dto.tm.EmployeeTm;
import lk.ijse.gdse71.nl.nltecnologies.model.EmployeeRepo;
import lk.ijse.gdse71.nl.nltecnologies.util.Regex;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EmployeeFormController {

    @FXML
    private ImageView ImgView;

    @FXML
    private Pane main_pain;

    @FXML
    private TableColumn<?, ?> colDOR;

    @FXML
    private TableColumn<?, ?> colDob;

    @FXML
    private TableColumn<?, ?> colEid;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colNic;

    @FXML
    private TableColumn<?, ?> colPosition;

    @FXML
    private TableColumn<?, ?> colTel;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<EmployeeTm> tblCustomer;

    @FXML
    private TextField txtAddress;

    @FXML
    private DatePicker txtDOB;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker txtEnrollDate;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNIC;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPosition;

    @FXML
    private TextField txtSalary;

    @FXML
    private TextField txtSearchEmployee;

    @FXML
    private TextField txtTel;

    private Image image;
    private QrResult qrResultModel;
    private QrReader qr;

    public EmployeeFormController(){
        qrResultModel = new QrResult();
    }

    @FXML
    public void initialize(){
        setCellValueFactory();
        loadAllEmployee();
        getEmpId();
        getCurrentId();
    }

    private void setCellValueFactory() {
        colEid.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("empName"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("empNic"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("empTel"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("empEmail"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colDOR.setCellValueFactory(new PropertyValueFactory<>("dateRegister"));
    }

    private void loadAllEmployee() {
        ObservableList<EmployeeTm> obList = FXCollections.observableArrayList();

        try {
            List<Employee> employeeList = EmployeeRepo.getAll();
            for (Employee employee : employeeList) {
                EmployeeTm tm = new EmployeeTm(
                        employee.getEmpId(),
                        employee.getEmpName(),
                        employee.getEmpNic(),
                        employee.getPosition(),
                        employee.getEmpTel(),
                        employee.getDob(),
                        employee.getDateRegister(),
                        employee.getEmpEmail(),
                        employee.getSalary()
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
        String position = txtPosition.getText();
        String contact = txtTel.getText();
        Date dob = Date.valueOf(txtDOB.getValue());
        Date dateRegistration = Date.valueOf(txtEnrollDate.getValue());
        String email = txtEmail.getText();
        double salary = Double.parseDouble(txtSalary.getText());
        String path = image.getUrl();

        Employee employee = new Employee(id, name, address, nic, position, contact , dob, dateRegistration, email, salary, path);

        try {
            if (isValidate()){
                boolean isSaved = EmployeeRepo.save(employee);
                if (isSaved){
                    QrGenerateor.setData(id, email, name);
                    new Alert(Alert.AlertType.CONFIRMATION,"Employee Saved!").show();
                    clearFields();
                    initialize();
                }
            }else {
                new Alert(Alert.AlertType.INFORMATION,"The data you entered is incorrect.").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Update Employee?", yes, no).showAndWait();

        if (type.orElse(no) == yes) {
            String id = txtId.getText();
            String name = txtName.getText();
            String address = txtAddress.getText();
            String nic = txtNIC.getText();
            String position = txtPosition.getText();
            String contact = txtTel.getText();
            Date dob = Date.valueOf(txtDOB.getValue());
            Date dateRegistration = Date.valueOf(txtEnrollDate.getValue());
            String email = txtEmail.getText();
            double salary = Double.parseDouble(txtSalary.getText());
            String path = image.getUrl();

            Employee employee = new Employee(id, name, address, nic, position, contact, dob, dateRegistration, email, salary, path); // Set Employee Data

            try {
                if (isValidate()) {
                    boolean isUpdated = EmployeeRepo.update(employee);
                    if (isUpdated) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Employee updated!").show();
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

        Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Delete Employee?", yes, no).showAndWait();

        if (type.orElse(no) == yes) {
            String id = txtId.getText();

            try {
                boolean isDeleted = EmployeeRepo.delete(id); // Delete Employee Data
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Employee deleted!").show();
                    clearFields();
                    initialize();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtNIC.setText("");
        txtPosition.setText("");
        txtTel.setText("");
        txtDOB.setValue(null);
        txtEnrollDate.setValue(null);
        txtEmail.setText("");
        txtSalary.setText("");
        ImgView.setImage(null);
        txtSearchEmployee.setText("");
    }

    private String getCurrentId() {
        String nextId = "";

        try {
            String currentId = EmployeeRepo.getLastId();

            nextId = generateNextId(currentId);
            txtId.setText(nextId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nextId;
    }

    private String generateNextId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("E");  //" ", "2"
            int idNum = Integer.parseInt(split[1]);

            if(idNum >= 1){
                return "E" + 0 + 0 + ++idNum;
            }else if(idNum >= 9){
                return "E" + 0 + ++idNum;
            } else if(idNum >= 99){
                return "E" + ++idNum;
            }
        }
        return "E001";
    }

    @FXML
    void btnImportImgOnAction() {  // Search Image Path in Your PC
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*png", "jpg"));

        File file = openFile.showOpenDialog(main_pain.getScene().getWindow());

        if (file != null){
            image = new Image(file.toURI().toString(),153,176,false,true);
            ImgView.setImage(image);
        }
    }

    private void getEmpId() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> telList = EmployeeRepo.getId();

            for(String tel : telList) {
                obList.add(tel);
            }
            TextFields.bindAutoCompletion(txtSearchEmployee,obList); // Load Employee Ids In Text Field

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtSearchEmployeeOnAction(ActionEvent event) { // Search Employee Button Call
        try {
            btnSearchEmployeeOnAction();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSearchEmployeeOnAction() throws SQLException {  // Search Employees
        String tel = txtSearchEmployee.getText();

        Employee emp = EmployeeRepo.searchById(String.valueOf(tel)); // Search Employees In Employee Id
        if (emp != null) {
            txtId.setText(emp.getEmpId());
            txtName.setText(emp.getEmpName());
            txtAddress.setText(emp.getEmpAddress());
            txtNIC.setText(emp.getEmpNic());
            txtPosition.setText(emp.getPosition());
            txtTel.setText(emp.getEmpTel());
            txtDOB.setValue(emp.getDob().toLocalDate());
            txtEnrollDate.setValue(emp.getDateRegister().toLocalDate());
            txtEmail.setText(emp.getEmpEmail());
            txtSalary.setText(String.valueOf(emp.getSalary()));
            image = new Image(emp.getPath(), 153, 176, false, true);
            ImgView.setImage(image);

        } else {
            new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
        }
    }


   public void btnScanOnAction(ActionEvent event) {
        qr = new QrReader(qrResultModel);
        new Thread(() -> {
            while (qrResultModel.getResult() == null){
                try {
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            txtSearchEmployee.setText(qrResultModel.getResult());
        }).start();
        txtSearchEmployee.requestFocus();
    }

    // Employee Report Generate ----------------------------------------------------------------------------
    @FXML
    void btnEmployeeReportOnAction(ActionEvent event) throws SQLException, JRException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/report/EmployeeReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();
        if(isIdValidate()) {
            data.put("emp_id", txtId.getText());
        } else{
            new Alert(Alert.AlertType.INFORMATION, "Employee Id you entered is incorrect").show();
        }
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }

    private boolean isIdValidate() {
        if (!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.EID,txtId)) return false;
        return true;
    }

    private boolean isValidate() {
        if(!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.EID,txtId))return false;
        if(!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.NIC,txtNIC))return false;
        if(!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.ADDRESS,txtAddress))return false;
        if(!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.PHONENO,txtTel))return false;
        if(!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.EMAIL,txtEmail))return false;
        if(!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.PRICE,txtSalary))return false;

        return true;
    }

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
        txtDOB.requestFocus();
    }

    @FXML
    void txtDOBOnAction(ActionEvent event) {
        txtEnrollDate.requestFocus();
    }

    @FXML
    void txtEmailOnAction(ActionEvent event) {
        txtEmail.requestFocus();
    }

    @FXML
    void txtIdOnAction(ActionEvent event) {
        txtSalary.requestFocus();
    }

    @FXML
    void txtPositionOnAction(ActionEvent event) {
        btnImportImgOnAction();
    }

    @FXML
    void txtRegDateOnAction(ActionEvent event) {
        txtEmail.requestFocus();
    }

    @FXML
    void txtSalOnAction(ActionEvent event) {
        txtPosition.requestFocus();
    }

    @FXML
    void txtAddressOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.ADDRESS,txtAddress);
    }

    @FXML
    void txtEmailOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.EMAIL,txtEmail);
    }

    @FXML
    void txtEmpIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.EID,txtId);
    }

    @FXML
    void txtNicOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.NIC,txtNIC);
    }

    @FXML
    void txtSalOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.PRICE,txtSalary);
    }

    @FXML
    void txtTelOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.PHONENO,txtTel);
    }
}
