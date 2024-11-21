package lk.ijse.gdse71.nl.nltecnologies.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class OrderDetailTm {
    private String code;
    private String model;
    private int qty;
    private double unitPrice;
    private double total;
    private JFXButton btnRemove;
}
