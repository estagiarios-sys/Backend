package com.systextil.relatorio.domain.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class JndiTestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test-jndi")
    public List<Map<String, Object>> testJndi() {
        String sql = "SELECT * FROM BASI_010";
        return jdbcTemplate.queryForList(sql);
    }
}