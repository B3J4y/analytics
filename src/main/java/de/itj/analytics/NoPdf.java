package de.itj.analytics;

public class NoPdf extends PDFReader {
    @Override
    public String getContent() {
        return "";
    }

    @Override
    public boolean hasContent() {
        return false;
    }
}