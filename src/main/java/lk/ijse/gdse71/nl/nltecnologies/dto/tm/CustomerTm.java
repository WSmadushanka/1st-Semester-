package lk.ijse.gdse71.nl.nltecnologies.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CustomerTm {
    private String custId;
    private String cName;
    private String cAddress;
    private String cNIC;
    private String contactNo;
    private String cEmail;
}
