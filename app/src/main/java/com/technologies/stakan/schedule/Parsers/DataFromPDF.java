package com.technologies.stakan.schedule.Parsers;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataFromPDF extends PDFTextStripper {

    private List<TextPosition> textPositions = new ArrayList<>();
    private PDDocument document;
    private URL url;

    private DataFromPDF() throws IOException {

    }

    public DataFromPDF(String url) throws IOException {
        this.url = new URL(url);
    }

    public DataFromPDF(URL url) throws IOException {
        this.url = url;
    }

    public String getText() throws IOException {

        if(document == null) {
            document = PDDocument.load(url.openStream());
        }

        PDFTextStripper pdfStripper = new PDFTextStripper();
        String str;

        str = pdfStripper.getText(document);

        return str;
    }

    public List<TextPosition> getTextPositions() throws IOException {

        if(document == null) {
            document = PDDocument.load(url.openStream());
        }

        PDFTextStripper pdfStripper = new DataFromPDF();
        String str;

        pdfStripper.getText(document);

        return textPositions;
    }

    @Override
    protected void writeString(String string, List<TextPosition> textPos) throws IOException {
        textPositions.addAll(textPos);
    }

}
