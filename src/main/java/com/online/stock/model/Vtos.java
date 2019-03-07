package com.online.stock.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "VTOS")
@Data
public class Vtos {
    @Id
    @GeneratedValue
    private String id;
    private String A1;
    private String A2;
    private String A3;
    private String A4;
    private String A5;
    private String A6;
    private String A7;
    private String B1;
    private String B2;
    private String B3;
    private String B4;
    private String B5;
    private String B6;
    private String B7;
    private String C1;
    private String C2;
    private String C3;
    private String C4;
    private String C5;
    private String C6;
    private String C7;
    private String D1;
    private String D2;
    private String D3;
    private String D4;
    private String D5;
    private String D6;
    private String D7;
    private String E1;
    private String E2;
    private String E3;
    private String E4;
    private String E5;
    private String E6;
    private String E7;
    private String F1;
    private String F2;
    private String F3;
    private String F4;
    private String F5;
    private String F6;
    private String F7;
    private String G1;
    private String G2;
    private String G3;
    private String G4;
    private String G5;
    private String G6;
    private String G7;

}
