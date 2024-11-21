package lk.ijse.gdse71.nl.nltecnologies.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class SupplierTm {
    private String supId;
    private String companyName;
    private String personName;
    private String tel;
    private String location;
    private String email;
}
