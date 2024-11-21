package lk.ijse.gdse71.nl.nltecnologies.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString

public class Order {
    private String orderId;
    private String customerId;
    private String trId;
    private Date date;
    private String payment;
}
