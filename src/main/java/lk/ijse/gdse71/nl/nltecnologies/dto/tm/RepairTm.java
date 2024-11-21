package lk.ijse.gdse71.nl.nltecnologies.dto.tm;

import lk.ijse.gdse71.nl.nltecnologies.dto.Repair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RepairTm extends Repair {
    private String repId;
    private String itemName;
    private String description;
    private String custId;
    private double cost;
    private LocalDate reciveDate;
    private LocalDate reternDate;
}
