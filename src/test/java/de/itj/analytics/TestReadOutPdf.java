package de.itj.analytics;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestReadOutPdf {
    @Test
    public void testFileConversion() throws Exception {
        File speiseKarte = getFile("Speisekarte.pdf");
        File speiseKarteTxt = getFile("Speisekarte.txt");
        PDFReader pdfReader = PDFReader.createPDFReader(speiseKarte);
        assertTrue(pdfReader.hasContent(), "Speisekarte has not been loaded");
        compareTextes(pdfReader.getContent().trim(), getTextFromFile(speiseKarteTxt));

        File bundesanzeiger = getFile("bgbl118006_76242.pdf");
        File bundesanzeigerTxt = getFile("bgbl118006_76242.txt");
        PDFReader pdfReaderBA = PDFReader.createPDFReader(bundesanzeiger);
        assertTrue(pdfReaderBA.hasContent(), "Speisekarte has not been loaded");
        compareTextes(pdfReaderBA.getContent().trim(), getTextFromFile(bundesanzeigerTxt));
        System.out.println("First test is done");
    }



    private void compareTextes(String text1, String text2) {
        String[] splittedText1 = text1.split("\n");
        String[] splittedText2 = text2.split("\n");
        assertTrue(splittedText1.length == splittedText2.length, "Both texts have to be the same size");
        for (int i = 0; i < splittedText1.length; i++) {
            assertEquals(splittedText1[i].trim(), splittedText2[i].trim(), "There is a difference in this line.");
        }
    }

    private String getTextFromFile(File file) {
        StringBuilder concatLines = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                concatLines.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return concatLines.toString();
    }

    private File getFile(String filename) {
        URL resource = this.getClass().getClassLoader().getResource(filename);
        if (resource != null) {
            return new File(resource.getFile());
        } else {
            String PATH_TO_RESOURCES = "src/test/resources/";
            return new File(PATH_TO_RESOURCES, filename);
        }
    }
}
