package lk.ijse.gdse71.nl.nltecnologies.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.gdse71.nl.nltecnologies.db.DbConnection;
import lk.ijse.gdse71.nl.nltecnologies.dto.BrandNewItem;
import lk.ijse.gdse71.nl.nltecnologies.dto.Customer;
import lk.ijse.gdse71.nl.nltecnologies.dto.DailyOrders;
import lk.ijse.gdse71.nl.nltecnologies.dto.Order;
import lk.ijse.gdse71.nl.nltecnologies.dto.tm.MostSellItemTm;
import lk.ijse.gdse71.nl.nltecnologies.model.BrandNewItemRepo;
import lk.ijse.gdse71.nl.nltecnologies.model.CustomerRepo;
import lk.ijse.gdse71.nl.nltecnologies.model.DashboardRepo;
import lk.ijse.gdse71.nl.nltecnologies.model.OrderRepo;
import org.controlsfx.control.textfield.TextFields;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static lk.ijse.gdse71.nl.nltecnologies.model.DashboardRepo.orderDaily;

public class DashboardFormController {

    @FXML
    private BarChart<?, ?> barChart;

    @FXML
    private TableColumn<?, ?> colItemName;

    @FXML
    private TableColumn<?, ?> colOrderCount;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private Label labCutCount;

    @FXML
    private Label lblOrderCount;

    @FXML
    private PieChart pieChart;

    @FXML
    private Label lblOrderCountlab;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView tblMostSellItems;

    @FXML
    private TextField txtOrderDate;

    @FXML
    public void initialize(){
        loadCustomerCount();
        loadOrderCount();
        loadMostSellItemTable();
        loadAll();
        getOrderDate();


       /* try {
            barChart();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }*/

        try {
            pieChartConnect();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void loadMostSellItemTable() {
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colOrderCount.setCellValueFactory(new PropertyValueFactory<>("orderCount"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("sumQty"));
    }

    private  void loadAll(){
        ObservableList<MostSellItemTm> obList = FXCollections.observableArrayList();

        try {
            List<MostSellItemTm> itmeList = DashboardRepo.getMostSellItem();
            BrandNewItem item;
            for (MostSellItemTm sellItem : itmeList){
                item = BrandNewItemRepo.searchById(sellItem.getItemId());
                MostSellItemTm tm = new MostSellItemTm(
                        item.getName(),
                        sellItem.getOrderCount(),
                        sellItem.getSumQty()
                );
                obList.add(tm);
            }
            tblMostSellItems.setItems(obList);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void loadCustomerCount() {
        int count = 0;
        try {
            List<Customer> customerList = CustomerRepo.getAll();
            for (Customer cust : customerList){
                count ++;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        labCutCount.setText(String.valueOf(count));
    }

    private void loadOrderCount() {
        int count = 0;
        try {
            List<Order> orderList = OrderRepo.getAll();
            for (Order order : orderList){

                count ++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        lblOrderCount.setText(String.valueOf(count));
    }

    // Pie Chart --------------------------------------------------------------------------------------------------------------------------------------------------
    private void pieChartConnect() throws SQLException {
        List<MostSellItemTm> itemList = DashboardRepo.getMostSellItem();
        BrandNewItem item;
        for (MostSellItemTm sellItem : itemList) {
            item = BrandNewItemRepo.searchById(sellItem.getItemId());

            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data(item.getName(), sellItem.getSumQty())
                    );
            pieChartData.forEach(data ->
                    data.nameProperty().bind(
                            Bindings.concat(
                                    data.getName(), "  amount: ", data.pieValueProperty()
                            )
                    )
            );

            pieChart.getData().addAll(pieChartData);
        }
    }

    // Bar Chart -----------------------------------------------------------------------------------------------------------------------------------------------------
    private void barChart() throws SQLException {
        XYChart.Series chart = new XYChart.Series();
        chart.setName("NL-technologies");

        String sql = "SELECT\n" +
                "   DATE_FORMAT(MIN(o.order_date), '%Y-%m-%d') AS WeekStartDate,\n" +
                "     DATE_FORMAT(MAX(o.order_date), '%Y-%m-%d') AS WeekEndDate,\n" +
                "    COUNT(DISTINCT o.order_id) AS WeeklyOrders,\n" +
                "    SUM(od.qty * od.unit_price) AS TotalAmount\n" +
                "FROM\n" +
                "    orders o\n" +
                "JOIN \n" +
                "    order_detail od ON o.order_id = od.order_id\n" +
                "WHERE\n" +
                "    o.order_date BETWEEN (SELECT MIN(order_date) FROM orders) AND (SELECT MAX(order_date) FROM orders)\n" +
                "GROUP BY\n" +
                "    YEARWEEK(o.order_date, 1)\n" +
                "ORDER BY\n" +
                "    WeekStartDate;";

        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet rst  = stm.executeQuery();

        while (true) {
            if (!rst.next()) break;

            String date = rst.getString(2);

            int count  = rst.getInt(4);
            chart.getData().add(new XYChart.Data<>(date, count));
        }
       /* barChart.getData().addAll(chart);*/
    }

    private void getOrderDate() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> dateList = OrderRepo.getAllDate();

            for (String date : dateList){
                obList.add(date);
            }
            TextFields.bindAutoCompletion(txtOrderDate, obList);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSearchOrderDateOnAction() throws SQLException{
        Date date = java.sql.Date.valueOf(txtOrderDate.getText());
        DailyOrders dailyOrders = orderDaily(date);

        lblOrderCountlab.setText(String.valueOf(dailyOrders.getCountOr()));
        lblOrderCount.setText(String.valueOf(dailyOrders.getCountQty()));
    }

    @FXML
    void txtOrderDateOnAction(ActionEvent event) throws SQLException {
        btnSearchOrderDateOnAction();
    }
}
