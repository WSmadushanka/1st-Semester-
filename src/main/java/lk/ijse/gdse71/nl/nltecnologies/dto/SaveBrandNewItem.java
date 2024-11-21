package lk.ijse.gdse71.nl.nltecnologies.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString

public class SaveBrandNewItem {
    private BrandNewItem brandNewItem;
    private ItemSupplierDetail itemSupplier;
}
