package lk.ijse.gdse71.nl.nltecnologies.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data

public class BrandNewItem {
    private String itemId;
    private String Name;
    private String category;
    private String brand;
    private LocalDate stockDate;
    private String description;
    private String warranty;
    private String type;
    private String path;
}
