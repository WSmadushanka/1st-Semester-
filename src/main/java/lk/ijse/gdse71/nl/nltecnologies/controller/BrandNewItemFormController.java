package lk.ijse.gdse71.nl.nltecnologies.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import lk.ijse.gdse71.nl.nltecnologies.dto.BrandNewItem;
import lk.ijse.gdse71.nl.nltecnologies.dto.ItemSupplierDetail;
import lk.ijse.gdse71.nl.nltecnologies.dto.SaveBrandNewItem;
import lk.ijse.gdse71.nl.nltecnologies.dto.Supplier;
import lk.ijse.gdse71.nl.nltecnologies.dto.tm.BrandNewItemTm;
import lk.ijse.gdse71.nl.nltecnologies.model.BrandNewItemRepo;
import lk.ijse.gdse71.nl.nltecnologies.model.ItemSupplierDetailRepo;
import lk.ijse.gdse71.nl.nltecnologies.model.SaveBrandNewItemRepo;
import lk.ijse.gdse71.nl.nltecnologies.model.SupplierRepo;
import lk.ijse.gdse71.nl.nltecnologies.util.Regex;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class BrandNewItemFormController {

    @FXML
    private JFXComboBox<String> cmbType;

    @FXML
    private Label lblSupCompany;

    @FXML
    private TableColumn<?, ?> colBrand;

    @FXML
    private TableColumn<?, ?> colCategory;

    @FXML
    private TableColumn<?, ?> colItemId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colSupCompany;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private AnchorPane imgRootNode;

    @FXML
    private ImageView itemImage;

    @FXML
    private Label lblSupCompanyName;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<BrandNewItemTm> tableBrandNewItem;

    @FXML
    private TextField txtBrand;

    @FXML
    private TextField txtCategory;

    @FXML
    private DatePicker txtDate;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtItemId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtSearchItemName;

    @FXML
    private TextField txtSupplierId;

    @FXML
    private TextField txtUnitPrice;

    @FXML
    private TextField txtWarranty;

    private Image image;

    @FXML
    public void initialize() {
        getSupplierId();
        getBrand();
        getCategory();
        getType();
        setCellValueFactory();
        loadAllItems();
        getItemName();
        getCurrentItemId();
    }

    private void setCellValueFactory() {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colSupCompany.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
    }

    private void loadAllItems() {
        ObservableList<BrandNewItemTm> obList = FXCollections.observableArrayList();

        try {
            List<BrandNewItem> itemList = BrandNewItemRepo.getAll();
            List<ItemSupplierDetail> supplierDetail = ItemSupplierDetailRepo.getAll();
            List<Supplier> suppliers = SupplierRepo.getAll();
            BrandNewItemTm tm;
            for(ItemSupplierDetail itemSupplier : supplierDetail) {
                String item_name = null;
                String item_category = null;
                String item_brand = null;
                String supplierCompany = null;

                for (BrandNewItem brandNewItemTm : itemList){
                    if (brandNewItemTm.getItemId().equals(itemSupplier.getItemId())){
                        item_name =   brandNewItemTm.getName();
                        item_category = brandNewItemTm.getCategory();
                        item_brand =brandNewItemTm.getBrand();
                    }
                }
                for(Supplier supplier: suppliers){
                    if(supplier.getSupId().equals(itemSupplier.getSupId())){
                        supplierCompany = supplier.getCompanyName();
                    }
                }


                tm = new BrandNewItemTm(
                        itemSupplier.getItemId(),
                        item_name,
                        item_category,
                        item_brand,
                        itemSupplier.getUnitPrice(),
                        itemSupplier.getQty(),
                        supplierCompany
                );
                obList.add(tm);
            }
            tableBrandNewItem.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSaveItemOnAction(ActionEvent event) {
        String itemId = txtItemId.getText();
        String name = txtName.getText();
        String category = txtCategory.getText();
        String brand = txtBrand.getText();
        LocalDate date = txtDate.getValue();
        String warranty = txtWarranty.getText();
        String description = txtDescription.getText();
        String type = String.valueOf(cmbType.getValue());
        String path = image.getUrl();

        var item = new BrandNewItem(itemId, name, category, brand, date, description, warranty, type, path);

        itemId = txtItemId.getText();
        String supId = txtSupplierId.getText();
        int handOnQty = Integer.parseInt(txtQty.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());

        var itemSupplier = new ItemSupplierDetail(itemId,supId,handOnQty,unitPrice);
        SaveBrandNewItem si = new SaveBrandNewItem(item, itemSupplier);
        try {
            if(isValid()) {
                boolean isPlaced = SaveBrandNewItemRepo.saveBrandNewItem(si);
                if (isPlaced) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Save Item!").show();
                    clearFields();
                    initialize();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Item Save Unsuccessfully!").show();
                }
            }else{
                new Alert(Alert.AlertType.INFORMATION, "The data you entered is incorrect").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateItemOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> bType = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Update Item?", yes, no).showAndWait();

        if (bType.orElse(no) == yes) {
            String itemId = txtItemId.getText();
            String name = txtName.getText();
            String category = txtCategory.getText();
            String brand = txtBrand.getText();
            LocalDate date = txtDate.getValue();
            String warranty = txtWarranty.getText();
            String type = String.valueOf(cmbType.getValue());
            String description = txtDescription.getText();
            String path = image.getUrl();

            var item = new BrandNewItem(itemId, name, category, brand, date, description, warranty, type, path);

            itemId = txtItemId.getText();
            String supId = txtSupplierId.getText();
            int handOnQty = Integer.parseInt(txtQty.getText());
            double unitPrice = Double.parseDouble(txtUnitPrice.getText());

            var itemSupplier = new ItemSupplierDetail(itemId, supId, handOnQty, unitPrice);

            SaveBrandNewItem si = new SaveBrandNewItem(item, itemSupplier);
            try {
                if (isValid()) {
                    boolean isPlaced = SaveBrandNewItemRepo.updateBrandNewItem(si);
                    if (isPlaced) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Update Item!").show();
                        clearFields();
                        initialize();
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Item Update Unsuccessfully!").show();
                    }
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "The data you entered is incorrect").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private void clearFields() {
        txtItemId.setText("");
        txtName.setText("");
        txtDescription.setText("");
        txtDate.setValue(null);
        txtQty.setText("");
        txtUnitPrice.setText("");
        txtWarranty.setText("");
        txtBrand.setText("");
        cmbType.setValue(null);
        txtCategory.setText("");
        txtSupplierId.setText("");
        lblSupCompanyName.setText("");
        itemImage.setImage(null);
        txtSearchItemName.setText("");

        txtItemId.requestFocus();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnDeleteItemOnAction(ActionEvent event) {
        ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION,"Are you sure to Delete Item?", yes, no).showAndWait();

        if (type.orElse(no) == yes){
            String id = txtItemId.getText();

            try {
                boolean isItemDelete = BrandNewItemRepo.delete(id);
                if (isItemDelete){
                    new Alert(Alert.AlertType.CONFIRMATION,"item deleted!").show();
                    clearFields();
                    initialize();
                }
            }catch (SQLException e){
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private String getCurrentItemId() {
        String nextId = "";

        try {
            String currentId = BrandNewItemRepo.getLastId();
            nextId = generateNextOrderId(currentId);
            txtItemId.setText(nextId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nextId;
    }

    private String generateNextOrderId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("I");  //" ", "2"
            int idNum = Integer.parseInt(split[1]);

            if(idNum >= 0){
                return "I" + 0 + 0 + ++idNum;
            }else if(idNum >= 9){
                return "I" + 0 + ++idNum;
            } else if(idNum >= 99){
                return "I" + ++idNum;
            }
        }
        return "I001";
    }

    private void getType(){
       ObservableList<String> obList = FXCollections.observableArrayList();

        obList.add("Brand New");
        obList.add("Used");

        cmbType.setItems(obList);

    }

    private void getBrand() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        obList.add("Hp");
        obList.add("Msi");
        obList.add("Asus");
        obList.add("Acer");
        obList.add("Toshiba");
        obList.add("Dell");
        obList.add("Lenovo");
        obList.add("Huawei");
        obList.add("Adata");
        obList.add("Corsair");
        obList.add("T-Force");
        obList.add("Cooler Master");
        obList.add("Armaggeddon");
        obList.add("Gamdias");
        obList.add("Fantech");
        obList.add("ProLink");
        obList.add("Jadel");
        obList.add("Logitech");
        obList.add("AMD");
        obList.add("INTEL");

        TextFields.bindAutoCompletion(txtBrand,obList);
    }

    private void getCategory() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        obList.add("Laptop");
        obList.add("Monitor");
        obList.add("Keyboard & Mouse");
        obList.add("Casing");
        obList.add("Headset & Speaker");
        obList.add("Processor");
        obList.add("Motherboard");
        obList.add("Memory");
        obList.add("Storage");
        obList.add("Graphic Card");
        obList.add("Combo Pack");

        TextFields.bindAutoCompletion(txtCategory,obList);
    }

    @FXML
    void btnAddNewSupplierOnAction(ActionEvent event) throws IOException {
        AnchorPane supRootNode = FXMLLoader.load(this.getClass().getResource("/view/supplier_form.fxml"));
        rootNode.getChildren().clear();
        rootNode.getChildren().add(supRootNode);
    }

    @FXML
    void btnPicImportOnAction() {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*png", "*jpg"));

        File file = openFile.showOpenDialog(imgRootNode.getScene().getWindow());

        if (file != null) {
            image = new Image(file.toURI().toString(), 200, 200, false, true);
            itemImage.setImage(image);
        }
    }

    private void getSupplierId() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> idList = SupplierRepo.getId();

            for(String id : idList) {
                obList.add(id);
            }

            TextFields.bindAutoCompletion(txtSupplierId, obList); //Assign

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtSearchItemNameOnAction(ActionEvent event) {
        try {
            btnSearchItemNameOnAction();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSearchItemNameOnAction() throws SQLException {
        String name = txtSearchItemName.getText();

        BrandNewItem item = BrandNewItemRepo.searchByName(name);
        ItemSupplierDetail isd = ItemSupplierDetailRepo.searchById(item.getItemId());
        Supplier supplier = SupplierRepo.searchById(isd.getSupId());

        if (item != null) {
            txtItemId.setText(item.getItemId());
            txtName.setText(item.getName());
            txtDescription.setText(item.getDescription());
            txtDate.setValue(item.getStockDate());
            txtCategory.setText(item.getCategory());
            txtBrand.setText(item.getBrand());
            if(isd != null) {
                txtSupplierId.setText(isd.getSupId());
                txtQty.setText(String.valueOf(isd.getQty()));
                txtUnitPrice.setText(String.valueOf(isd.getUnitPrice()));
            }
            if(supplier != null) {
                lblSupCompany.setText(supplier.getCompanyName());
            }
            cmbType.setValue(item.getType());
            txtWarranty.setText(item.getWarranty());
            image = new Image(item.getPath(), 153, 176, false, true);
            itemImage.setImage(image);


        } else {
            new Alert(Alert.AlertType.INFORMATION, "Item not found!").show();
        }
    }

    private void getItemName() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> telList = BrandNewItemRepo.getName();

            for (String tel : telList) {
                obList.add(tel);
            }
            TextFields.bindAutoCompletion(txtSearchItemName, obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void cmbtypeOnAction(ActionEvent event) {
        txtDate.requestFocus();
    }

    @FXML
    void txtBrandOnAction(ActionEvent event) {
        txtDescription.requestFocus();
    }

    @FXML
    void txtCategoryOnAction(ActionEvent event) {
        txtBrand.requestFocus();
    }

    @FXML
    void txtDateOnAction(ActionEvent event) {
        txtWarranty.requestFocus();
    }

    @FXML
    void txtDescriptionOnAction(ActionEvent event) {
        txtSupplierId.requestFocus();
    }

    @FXML
    void txtItemId(ActionEvent event) {
        txtName.requestFocus();
    }

    @FXML
    void txtNameOnAction(ActionEvent event) {
        txtCategory.requestFocus();
    }

    @FXML
    void txtQtyOnAction(ActionEvent event) {
        txtUnitPrice.requestFocus();
    }

    @FXML
    void txtWarrantyOnAction(ActionEvent event) {
        txtWarranty.requestFocus();
    }

    @FXML
    void txtSupplierIdOnAction(ActionEvent event) {
        String id = String.valueOf(txtSupplierId.getText());
        Supplier supplier;
        try {
            supplier = SupplierRepo.searchById(id);

            lblSupCompanyName.setText(supplier.getCompanyName());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        cmbType.requestFocus();
    }

    @FXML
    void txtUnitPriceOnAction(ActionEvent event) {
        btnPicImportOnAction();
    }

    @FXML
    void txtQtyOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.QTY,txtQty);
    }

    @FXML
    void txtUnitPriceOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.PRICE,txtUnitPrice);
    }

    @FXML
    void txtItemIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.IID,txtItemId);
    }

    private boolean isValid() {
        if(!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.IID,txtItemId))return false;
        if (!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.QTY,txtQty)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse71.nl.nltecnologies.util.TextField.PRICE,txtUnitPrice)) return false;
        return true;
    }
}
