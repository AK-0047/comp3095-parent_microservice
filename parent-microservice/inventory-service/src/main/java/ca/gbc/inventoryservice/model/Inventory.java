package ca.gbc.inventoryservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Table(name =  "t_inventory")
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String skuCode;
    private Integer quantity;
}
