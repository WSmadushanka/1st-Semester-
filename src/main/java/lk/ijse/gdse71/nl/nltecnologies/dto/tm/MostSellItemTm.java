package lk.ijse.gdse71.nl.nltecnologies.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
@NoArgsConstructor
@Data

public class MostSellItemTm {
    private String itemId;
    private int orderCount;
    private double sumQty;

}
