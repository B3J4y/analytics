package de.itj.analytics;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public abstract class PDFReader {

    public abstract String getContent();

    public abstract boolean hasContent();

    public static PDFReader createPDFReader(File file) {
        try (PDDocument document = PDDocument.load(file)){
            return new SingleColumnPdf(getTextFromWholeDoc(document));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new NoPdf();
    }

    public static String getTextFromWholeDoc(PDDocument pdDocument) {
        try {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            return stripper.getText(pdDocument);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}

