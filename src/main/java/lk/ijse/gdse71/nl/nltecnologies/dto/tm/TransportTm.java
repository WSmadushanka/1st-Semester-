package lk.ijse.gdse71.nl.nltecnologies.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class TransportTm {
    private String trId;
    private String vehicleNo;
    private String driverName;
    private String location;
    private double cost;
}
