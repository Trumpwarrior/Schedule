package com.technologies.stankin.schedule.ParsersAndStuff;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import com.tom_roush.pdfbox.text.TextPosition;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataFromPDF extends PDFTextStripper {

    private static List<TextPosition> textPositions = new ArrayList<>();
    private PDDocument document;
    private boolean devilTrigger = true;
    URL url;

    private DataFromPDF() throws IOException {

    }

    public DataFromPDF(final String url) throws IOException {
        textPositions.clear();
                try {
                    this.url = new URL(url);
                }catch (MalformedURLException e) { }catch (IOException e) { }
    }

    public DataFromPDF(final URL url) throws IOException {
        textPositions.clear();
        this.url = url;
    }

    public String getText() throws IOException {
        final PDFTextStripper pdfStripper = new DataFromPDF();
        String str = "";

            new Thread() {
                @Override
                public void run() {
                    try {
                        document = PDDocument.load(url.openStream());
                        pdfStripper.getText(document);
                        devilTrigger = false;
                    }catch (MalformedURLException e) {

                    }catch (IOException e) {

                    }

                }
            }.start();
        while (devilTrigger){}

        for(TextPosition pos : textPositions) {
            str += pos.getUnicode();
        }

        return str;
    }

    public List<Float> getTextPositions() throws IOException {

        final PDFTextStripper pdfStripper = new DataFromPDF();

            new Thread() {
                @Override
                public void run() {
                    try {
                        document = PDDocument.load(url.openStream());
                        pdfStripper.getText(document);
                        devilTrigger = false;
                    }catch (MalformedURLException e) {

                    }catch (IOException e) {

                    }

                }
            }.start();
        while (devilTrigger){}

        List<Float> xPositions = new ArrayList<>();

        for(TextPosition pos : textPositions) {
            xPositions.add(pos.getX());
        }

        return xPositions;
    }

    public boolean getDevilTrigger() {
        return devilTrigger;
    }

    @Override
    protected void writeString(String string, List<TextPosition> textPos) throws IOException {
        textPositions.addAll(textPos);
    }

}