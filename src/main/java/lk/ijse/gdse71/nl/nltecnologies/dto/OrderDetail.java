package lk.ijse.gdse71.nl.nltecnologies.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
@AllArgsConstructor

public class OrderDetail {
    private String orderId;
    private String itemCode;
    private int qty;
    private double unitPrice;
}
