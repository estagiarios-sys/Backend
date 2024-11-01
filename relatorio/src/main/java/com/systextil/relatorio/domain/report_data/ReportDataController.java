package com.systextil.relatorio.domain.report_data;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

@RestController
@RequestMapping("report-data")
public class ReportDataController {

    private final ReportDataService service;
    
    public ReportDataController(ReportDataService service) {
    	this.service = service;
    }
    
    @PostMapping
    public Object[] getQueryReturn(@RequestBody @Valid QueryData queryData) throws SQLException, ParseException, IOException {
        return service.getQueryReturn(queryData);
    }
    
    @PostMapping("analyze")
    public double getQueryAnalysis(@RequestBody @Valid QueryData queryData) throws SQLException, IOException {
    	return service.getQueryAnalysis(queryData);
    }
}