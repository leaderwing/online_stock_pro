package com.online.stock.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYSVAR")
@Data
public class SysVar {
    @Column(name = "GRNAME")
    private String grname;
    @Id
    @Column(name = "VARNAME")
    private String varname;
    @Column(name = "VARVALUE")
    private String varvalue;
    @Column(name = "VARDESC")
    private String vardesc;
    @Column(name = "EN_VARDESC")
    private String en_vardesc;
}
