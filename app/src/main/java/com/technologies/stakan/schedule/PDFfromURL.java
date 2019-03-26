package com.technologies.stakan.schedule;

import android.support.annotation.NonNull;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class PDFfromURL {

    private URL pdfurl;
    private PDDocument mainDocument = new PDDocument();

    PDFfromURL(@NonNull URL url) {
        try {
            pdfurl = new URL(url.getRef());
        }catch (MalformedURLException e) {
            //ОБРАБОТАТЬ КОГДА ПРИДУМАЮ КАК
        }
    }

    PDFfromURL(String adressString) {
        try {
            pdfurl = new URL(adressString);
        }catch (MalformedURLException e) {
            //ОБРАБОТАТЬ КОГДА ПРИДУМАЮ КАК
        }
    }

    void setURL(@NonNull URL url) throws MalformedURLException {
            pdfurl = new URL(url.getRef());
    }

    void setURL(String adressString) throws MalformedURLException {
            pdfurl = new URL(adressString);
    }

    void loadDocument() throws IOException {
        mainDocument = PDDocument.load(pdfurl.openStream());
    }

    PDDocument getPDF() {
        return mainDocument;
    }
}
