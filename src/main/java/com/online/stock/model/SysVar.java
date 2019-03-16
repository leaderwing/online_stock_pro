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
    private String GRNAME;
    @Id
    @Column(name = "VARNAME")
    private String VARNAME;
    @Column(name = "VARVALUE")
    private String VARVALUE;
    @Column(name = "VARDESC")
    private String VARDESC;
    @Column(name = "EN_VARDESC")
    private String EN_VARDESC;
}
