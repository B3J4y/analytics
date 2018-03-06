package de.itj.analytics;

public class SingleColumnPdf extends PDFReader {
    private final String content;

    public SingleColumnPdf(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public boolean hasContent() {
        return true;
    }
}
