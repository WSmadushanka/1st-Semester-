package lk.ijse.gdse71.nl.nltecnologies.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class DailyOrders {
    Date date;
    int countOr;
    int countQty;
}
