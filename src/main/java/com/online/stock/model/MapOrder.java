package com.online.stock.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MAP_ORDER")
public class MapOrder {
    @Id
    @Column(name = "ORDERID")
    private String orderId;
    @Column(name = "CLOSEDORDERID")
    private String closedOrderId;
}
