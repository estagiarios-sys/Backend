package com.systextil.relatorio.notaFiscal;

import com.systextil.relatorio.entity.NotaFiscal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaFiscalRepository extends JpaRepository<NotaFiscal, Long> {
}
