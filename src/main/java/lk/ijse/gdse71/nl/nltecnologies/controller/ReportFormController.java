package lk.ijse.gdse71.nl.nltecnologies.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import lk.ijse.gdse71.nl.nltecnologies.QrReader;
import lk.ijse.gdse71.nl.nltecnologies.db.DbConnection;
import lk.ijse.gdse71.nl.nltecnologies.dto.QrResult;
import lk.ijse.gdse71.nl.nltecnologies.dto.Repair;
import lk.ijse.gdse71.nl.nltecnologies.model.BrandNewItemRepo;
import lk.ijse.gdse71.nl.nltecnologies.model.CustomerRepo;
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

import static net.sf.jasperreports.engine.xml.JRXmlLoader.load;

public class ReportFormController {

    @FXML
    private TextField txtSearchCustomerTel;

    @FXML
    private TextField txtSearchItemStockDate;

    public ReportFormController(){
        qrResultModel = new QrResult();
    }
    private QrReader qr;
    private QrResult qrResultModel;

    @FXML
    public void initialize(){
        getCustomerTel();
        getItemDate();
    }

    @FXML
    void btnCustomerReportOnAction() throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/report/CustomerReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            Map<String, Object> data = new HashMap<>();

            if (isValidateNum()) {
                data.put("custTel", txtSearchCustomerTel.getText());
            } else {
                new Alert(Alert.AlertType.INFORMATION, "The data you entered is incorrect").show();
            }
            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);

            clearTel();
    }

    @FXML
    void btnStockOnAction() throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/report/supplierDateVise.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();

        if(isValidateDate()) {
            data.put("date", txtSearchItemStockDate.getText());
        }else{
            new Alert(Alert.AlertType.INFORMATION, "The data you entered is incorrect").show();
        }

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);

        clearDate();
    }

    @FXML
    void btnProfitOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = load("src/main/resources/report/Monthly_Profit.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, null, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }

    private void clearTel() {
        txtSearchCustomerTel.setText("");
    }

    private void getCustomerTel() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> telList = CustomerRepo.getTel();

            for(String tel : telList) {
                obList.add(tel);
            }
            TextFields.bindAutoCompletion(txtSearchCustomerTel,obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getItemDate() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> dateList = BrandNewItemRepo.getDate();

            for (String date : dateList) {
                obList.add(date);
            }
            TextFields.bindAutoCompletion(txtSearchItemStockDate,obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearDate() {
        txtSearchItemStockDate.setText("");
    }

    private boolean isValidateDate() {
        if (!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.DATE,txtSearchItemStockDate)) return false;
        return true;
    }

    private boolean isValidateNum() {
        if (!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.PHONENO,txtSearchCustomerTel)) return false;
        return true;
    }


    @FXML
    void txtCustTelOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.PHONENO,txtSearchCustomerTel);
    }

    @FXML
    void txtStockDateOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.DATE,txtSearchItemStockDate);
    }

    @FXML
    void btnScanOnAction(ActionEvent event) {
        qr = new QrReader(qrResultModel);
        new Thread(() -> {
            while (qrResultModel.getResult() == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            txtSearchCustomerTel.setText(qrResultModel.getResult());
        }).start();

        txtSearchCustomerTel.requestFocus();
    }
}
