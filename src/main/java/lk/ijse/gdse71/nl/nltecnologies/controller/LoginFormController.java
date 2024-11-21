package lk.ijse.gdse71.nl.nltecnologies.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.gdse71.nl.nltecnologies.db.DbConnection;
import lk.ijse.gdse71.nl.nltecnologies.util.Regex;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {

    @FXML
    private AnchorPane rootNode;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUserName;

    @FXML
    void btnLoginOnAction() throws IOException {
      try{
         /* AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();*/
        String userName = txtUserName.getText();
        String pw = txtPassword.getText();


            if(isValid()) { // Validated
                checkCredential(userName, pw);
            }else{
                new Alert(Alert.AlertType.INFORMATION, "" +
                        "").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void checkCredential(String user_name, String pw) throws SQLException, IOException {
          String sql = "SELECT user_name,password FROM users WHERE user_name = ?";

           Connection connection = DbConnection.getInstance().getConnection();
           PreparedStatement pstm = connection.prepareStatement(sql);
           pstm.setObject(1, user_name);

           ResultSet resultSet = pstm.executeQuery();
           if (resultSet.next()) {
               String dbPw = resultSet.getString("password");

               if (pw.equals(dbPw)) {
                   navigateToTheDashboard(user_name);
               } else {
                   new Alert(Alert.AlertType.ERROR, "Sorry! Password is Incorrect!").show();
               }
           } else {
               new Alert(Alert.AlertType.INFORMATION, "Sorry! User id can't be find!").show();
           }

    }
   private void navigateToTheDashboard(String user_name) throws IOException {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/sidepanel_form.fxml"));
       Parent dashboardRoot = loader.load();
       SidepanelFormController controller = loader.getController();
       controller.setUserName(user_name); // Pass the username to the DashboardFormController

       Scene scene = new Scene(dashboardRoot);

       Stage stage = (Stage) rootNode.getScene().getWindow();
       stage.setScene(scene);
       stage.centerOnScreen();
       stage.setTitle("Home Page");
   }

    @FXML
    void hyperRegistrationOnAction(ActionEvent event) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/registration_form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage)this.rootNode.getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Registration");
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void txtPasswordOnAction()throws IOException {
        btnLoginOnAction();
    }

    @FXML
    void txtUserNameOnAction(ActionEvent event) {
        txtUserName.requestFocus();
    }

    @FXML
    void txtPasswordOnActionKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.PASSWORD,txtPassword);
    }

    @FXML
    void txtUserNameOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.NAME,txtUserName);
    }

    private boolean isValid() {
        if (!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.NAME, txtUserName)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.PASSWORD, txtPassword)) return false;
        return true;
    }
}
