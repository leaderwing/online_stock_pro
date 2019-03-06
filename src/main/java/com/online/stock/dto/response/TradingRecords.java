package com.online.stock.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class TradingRecords {
    private List<TradingRow> rowList;
}
