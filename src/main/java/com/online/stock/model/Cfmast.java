package com.online.stock.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "CFMAST")
public class Cfmast {
    @Id
    @Column(name = "CUSTID")
    private String custid;
    @Column(name = "FULLNAME")
    private String fullname;
}
