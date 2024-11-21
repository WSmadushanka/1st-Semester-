package lk.ijse.gdse71.nl.nltecnologies.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class PlaceOrder {
    private Order order;
    private List<OrderDetail> odList;
}
