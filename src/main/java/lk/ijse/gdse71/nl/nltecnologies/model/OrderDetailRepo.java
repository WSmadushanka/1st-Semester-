package lk.ijse.gdse71.nl.nltecnologies.model;

import lk.ijse.gdse71.nl.nltecnologies.db.DbConnection;
import lk.ijse.gdse71.nl.nltecnologies.dto.OrderDetail;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailRepo {
    public static boolean save(List<OrderDetail> odList) throws SQLException{
        for (OrderDetail od : odList) {
            boolean isSaved = save(od);
            if(!isSaved) {
                return false;
            }
        }
        return true;
    }

    private static boolean save(OrderDetail od) throws SQLException {
        String sql = "INSERT INTO order_detail VALUES(?, ?, ?, ?)";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setString(1, od.getOrderId());
        pstm.setString(2, od.getItemCode());
        pstm.setInt(3, od.getQty());
        pstm.setDouble(4, od.getUnitPrice());

        return pstm.executeUpdate() > 0;    //false ->  |
    }

}
