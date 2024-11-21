package lk.ijse.gdse71.nl.nltecnologies.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class SidepanelFormController {


    public AnchorPane childRootNode;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblUserName;


    public AnchorPane rootNode;

    @FXML
    public void initialize(){
        setDate();
        try {
            loadDashboardForm();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void setDate() {
        LocalDate nowDate = LocalDate.now();
        lblDate.setText(String.valueOf(nowDate));
    }

    private void loadDashboardForm() throws IOException {
        AnchorPane dashRootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(dashRootNode);
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) throws IOException {
        AnchorPane dashRootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(dashRootNode);
    }

    @FXML
    void btnCustomerOnAction(ActionEvent event) throws IOException {
        AnchorPane customerRootNode = FXMLLoader.load(this.getClass().getResource("/view/customer_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(customerRootNode);
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) throws IOException {
        AnchorPane placeOrRootNode = FXMLLoader.load(this.getClass().getResource("/view/placeOrder_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(placeOrRootNode);
    }

    @FXML
    void btnItemOnAction(ActionEvent event) throws IOException {
        AnchorPane itemRootNode = FXMLLoader.load(this.getClass().getResource("/view/item_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(itemRootNode);
    }

    @FXML
    void btnEmployeeOnAction(ActionEvent event) throws IOException {
        AnchorPane employeeRootNode = FXMLLoader.load(this.getClass().getResource("/view/employee_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(employeeRootNode);
    }

    @FXML
    void btnSupplierOnAction(ActionEvent event) throws IOException {
        AnchorPane supRootNode = FXMLLoader.load(this.getClass().getResource("/view/supplier_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(supRootNode);
    }

    @FXML
    void btnTransportOnAction(ActionEvent event) throws IOException {
        AnchorPane transRootNode = FXMLLoader.load(this.getClass().getResource("/view/transport_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(transRootNode);
    }

    @FXML
    void btnRepairOnAction(ActionEvent event) throws IOException {
        AnchorPane repairRootNode = FXMLLoader.load(this.getClass().getResource("/view/repair_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(repairRootNode);
    }

    @FXML
    void btnReportOnAction(ActionEvent event) throws IOException {
        AnchorPane reportRootNode = FXMLLoader.load(this.getClass().getResource("/view/report_form.fxml"));
        childRootNode.getChildren().clear();
        childRootNode.getChildren().add(reportRootNode);
    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);

        stage.centerOnScreen();
        stage.setTitle("Login Form");
    }

    public void setUserName(String userName) {
        lblUserName.setText(userName);

    }
}
