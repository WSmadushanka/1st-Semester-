package lk.ijse.gdse71.nl.nltecnologies.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString

public class ItemSupplierDetail {
    private String itemId;
    private String supId;
    private int qty;
    private double unitPrice;
}
