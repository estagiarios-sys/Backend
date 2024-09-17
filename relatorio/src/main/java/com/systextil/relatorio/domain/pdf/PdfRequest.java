package com.systextil.relatorio.domain.pdf;

public class PdfRequest {
    private String fullTableHTML;
    private String titlePDF;
    private String imgPDF;

    // Getters e Setters
    public String getFullTableHTML() {
        return fullTableHTML;
    }

    public void setFullTableHTML(String fullTableHTML) {
        this.fullTableHTML = fullTableHTML;
    }

    public String getTitlePDF() {
        return titlePDF;
    }

    public void setTitlePDF(String titlePDF) {
        this.titlePDF = titlePDF;
    }

    public String getImgPDF() {
        return imgPDF;
    }

    public void setImgPDF(String imgPDF) {
        this.imgPDF = imgPDF;
    }
}