package lk.ijse.gdse71.nl.nltecnologies.controller;

import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.gdse71.nl.nltecnologies.dto.Location;
import lk.ijse.gdse71.nl.nltecnologies.dto.Transport;
import lk.ijse.gdse71.nl.nltecnologies.dto.tm.TransportTm;
import lk.ijse.gdse71.nl.nltecnologies.model.LocationRepo;
import lk.ijse.gdse71.nl.nltecnologies.model.TransportRepo;
import lk.ijse.gdse71.nl.nltecnologies.util.Regex;
import org.controlsfx.control.textfield.TextFields;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TransportFormController {

    @FXML
    private TableColumn<?, ?> colCost;

    @FXML
    private TableColumn<?, ?> colDriverName;

    @FXML
    private TableColumn<?, ?> colLocation;

    @FXML
    private TableColumn<?, ?> colTransportId;

    @FXML
    private TableColumn<?, ?> colVehicleNo;

    @FXML
    private AnchorPane mapRootNode;

    @FXML
    private TableView<TransportTm> tblTransport;

    @FXML
    private TextField txtCost;

    @FXML
    private TextField txtDriverName;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtLocation;

    @FXML
    private TextField txtSearchLocation;

    @FXML
    private TextField txtVehicleNo;

    private MapPoint eiffelPoint = new MapPoint(6.711053811499971, 79.9097716129893);

    @FXML
    public void initialize() {
        setCellValueFactory();
        loadAllTransport();
        getPlaces();
        getLocation();
        getCurrentId();

        try {
            MapView mapView = crateMapView();
            mapRootNode.getChildren().add(mapView);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colTransportId.setCellValueFactory(new PropertyValueFactory<>("trId"));
        colVehicleNo.setCellValueFactory(new PropertyValueFactory<>("vehicleNo"));
        colDriverName.setCellValueFactory(new PropertyValueFactory<>("driverName"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
    }

    private void loadAllTransport() {
        ObservableList<TransportTm> obList = FXCollections.observableArrayList();

        try {
            List<Transport> transportList = TransportRepo.getAll();
            for (Transport transport : transportList) {
                TransportTm tm = new TransportTm(
                        transport.getTrId(),
                        transport.getVehicleNo(),
                        transport.getDriverName(),
                        transport.getLocation(),
                        transport.getCost()
                );

                obList.add(tm);
            }

            tblTransport.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String trId = txtId.getText();
        String vehicleNo = txtVehicleNo.getText();
        String driverName = txtDriverName.getText();
        String location = txtLocation.getText();
        double cost = Double.parseDouble(txtCost.getText());

        Transport transport = new Transport(trId,vehicleNo,driverName,location,cost);

        //if(isValidate()) {
        try {

            boolean isSaved = TransportRepo.save(transport);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Transport saved!").show();
                clearFields();
                initialize();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        /*}else{
            new Alert(Alert.AlertType.INFORMATION, "The data you entered is incorrect").show();
        }*/
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Update Transport?", yes, no).showAndWait();

        if (type.orElse(no) == yes) {
            String trId = txtId.getText();
            String vehicalNo = txtVehicleNo.getText();
            String driverName = txtDriverName.getText();
            String location = txtLocation.getText();
            double cost = Double.parseDouble(txtCost.getText());

            Transport transport = new Transport(trId, vehicalNo, driverName, location, cost);

            try {
                //if(isValidate()) {
                boolean isSaved = TransportRepo.update(transport);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Transport updated!").show();
                    clearFields();
                    initialize();
                }
            /*}else{
                new Alert(Alert.AlertType.INFORMATION, "The data you entered is incorrect").show();
            }*/
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Delete Supplier?", yes, no).showAndWait();

        if (type.orElse(no) == yes) {
            String id = txtId.getText();

            try {
                boolean isDeleted = TransportRepo.delete(id);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Transport deleted!").show();
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
        txtDriverName.setText("");
        txtVehicleNo.setText("");
        txtLocation.setText("");
        txtCost.setText("");
        txtSearchLocation.setText("");
    }

    private String getCurrentId() {
        String nextId = "";

        try {
            String currentId = TransportRepo.getLastId();

            nextId = generateNextId(currentId);
            txtId.setText(nextId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nextId;
    }

    private String generateNextId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("T");  //" ", "2"
            int idNum = Integer.parseInt(split[1]);

            if(idNum >= 1){
                return "T" + 0 + 0 + ++idNum;
            }else if(idNum >= 9){
                return "T" + 0 + ++idNum;
            } else if(idNum >= 99){
                return "T" + ++idNum;
            }
        }
        return "T001";
    }

    private void getPlaces() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> placeList = LocationRepo.getPlace();

            for(String place : placeList) {
                obList.add(place);
            }

            TextFields.bindAutoCompletion(txtLocation, obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private MapView crateMapView() throws SQLException {
        MapView mapView = new MapView();
        List<String> validLocations = LocationRepo.getPlace();
        if (validLocations.contains(txtLocation.getText())) {
            Location location = LocationRepo.searchByPath(txtLocation.getText());
            System.out.println(location.getLatitude());
            System.out.println(location.getLongitude());
            eiffelPoint = new MapPoint(location.getLatitude(), location.getLongitude());

            mapView.setPrefSize(446, 487);
            mapView.setZoom(15);
            mapView.flyTo(0, eiffelPoint, 0.1);

            return mapView;

        }else if (!(txtLocation.getText().equals(null))) {

            mapView.setPrefSize(446, 487);
            mapView.setZoom(15);
            mapView.flyTo(0, eiffelPoint, 0.1);

            System.out.println("!null");
            return mapView;
        }
        return mapView;
    }

    @FXML
    void txtSearchLocationOnAction(ActionEvent event) {
        try {
            btnSearchLocationOnAction();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getLocation() { // Transport Table Location Loads
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> locationList = TransportRepo.getlocation();

            for (String location : locationList) {
                obList.add(location);
            }
            TextFields.bindAutoCompletion(txtSearchLocation, obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSearchLocationOnAction() throws SQLException {
        String location = txtSearchLocation.getText();

        Transport tr = TransportRepo.searchByLocation(location);
        if (tr != null) {
            txtId.setText(tr.getTrId());
            txtVehicleNo.setText(tr.getVehicleNo());
            txtDriverName.setText(tr.getDriverName());
            txtLocation.setText(tr.getLocation());
            txtCost.setText(String.valueOf(tr.getCost()));
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Location not found!").show();
        }
    }

    @FXML
    void txtDriverNameOnAction(ActionEvent event) {
        txtLocation.requestFocus();
    }

    @FXML
    void txtVehicleNoOnAction(ActionEvent event) {
        txtDriverName.requestFocus();
    }

    @FXML
    void txtTrIdOnAction(ActionEvent event) {
        txtVehicleNo.requestFocus();
    }

    @FXML
    void txtLocationOnAction(ActionEvent event) {
        try {
            MapView mapView = crateMapView();
            mapRootNode.getChildren().add(mapView);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        txtCost.requestFocus();
    }

    @FXML
    void txtTrIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.TID,txtId);
    }

    @FXML
    void txtVehicleNoOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.VEHICALNO,txtVehicleNo);
    }

    @FXML
    void txtCostOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.PRICE,txtCost);
    }

}
