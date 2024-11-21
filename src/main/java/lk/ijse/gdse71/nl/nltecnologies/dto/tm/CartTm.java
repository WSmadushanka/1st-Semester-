package lk.ijse.gdse71.nl.nltecnologies.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class CartTm {
    private String itemName;
    private int qty;
    private double unitPrice;
    private double total;
    private Button btnRemove;
}
