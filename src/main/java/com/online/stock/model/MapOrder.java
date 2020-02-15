package com.online.stock.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MAP_ORDER")
public class MapOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ODTRANA")
    @SequenceGenerator(name="SEQ_ODTRANA", sequenceName = "SEQ_ODTRANA", allocationSize=50)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;
    @Column(name = "ODERID")
    private String orderId;
    @Column(name = "CLOSEDORDERID")
    private String closedOrderId;

    public MapOrder(String orderId,String closedOrderId) {
        this.orderId = orderId;
        this.closedOrderId = closedOrderId;
    }
}
