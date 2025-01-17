package lk.ijse.gdse71.nl.nltecnologies.model;

import lk.ijse.gdse71.nl.nltecnologies.db.DbConnection;
import lk.ijse.gdse71.nl.nltecnologies.dto.ItemSupplierDetail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemSupplierDetailRepo {
    public static List<ItemSupplierDetail> getAll() throws SQLException {
        String sql = "SELECT * FROM item_supplier_detail";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<ItemSupplierDetail> itemSupplierList = new ArrayList<>();

        while (resultSet.next()) {
            String itemId = resultSet.getString(1);
            String supId = resultSet.getString(2);
            int handOnQty = resultSet.getInt(3);
            double unitPrice = resultSet.getDouble(4);

            ItemSupplierDetail itemSupplier = new ItemSupplierDetail(itemId,supId,handOnQty,unitPrice);
            itemSupplierList.add(itemSupplier);
        }
        return itemSupplierList;
    }

    public static ItemSupplierDetail searchById(String code) throws SQLException {
        String sql = "SELECT * FROM item_supplier_detail WHERE item_id = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        System.out.println(code);
        pstm.setObject(1, code);
        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return new ItemSupplierDetail(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4)
            );
        }
        return null;
    }

    public static boolean save(ItemSupplierDetail is) throws SQLException {
        System.out.println("start Item supper detail");
        String sql = "INSERT INTO item_supplier_detail VALUES(?, ?, ?, ?)";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);
        pstm.setString(1, is.getItemId());
        pstm.setString(2, is.getSupId());
        pstm.setInt(3, is.getQty());
        pstm.setDouble(4, is.getUnitPrice());
        System.out.println("end Item supper detail");

        return pstm.executeUpdate() > 0;    //false ->  |
    }

    public static boolean update(ItemSupplierDetail is) throws SQLException {
        System.out.println("start Item supper detail");
        String sql = "UPDATE item_supplier_detail SET sup_id = ?, qty = ?, unit_price = ? WHERE item_id = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setString(1, is.getSupId());
        pstm.setInt(2, is.getQty());
        pstm.setDouble(3, is.getUnitPrice());
        pstm.setString(4, is.getItemId());
        System.out.println("end Item supper detail");

        return pstm.executeUpdate() > 0;    //false ->  |
    }
}
