package com.online.stock.repository;

import com.online.stock.model.Vtos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VtosRepository extends JpaRepository<Vtos,String> {
    //@Query("select :column from Vtos")
    //String findValue(@Param("column") String column);
}
