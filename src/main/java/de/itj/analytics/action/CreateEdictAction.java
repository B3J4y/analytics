package de.itj.analytics.action;

import de.itj.analytics.PDFReader;
import de.itj.analytics.objects.Edict;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CreateEdictAction implements Action<List<Edict>> {
    private Pattern titlePattern = Pattern.compile(".* (Berichtigung der )?((\\w*v)|V)erordnung (zur (Änderung|Durchführung)|(für eine Übergangsregelung))?.*");
    private File pdf;
    private List<Edict> createdEdicts;

    public CreateEdictAction(File pdf) {
        this.pdf = pdf;
    }

    @Override
    public void execute() {
        createdEdicts = createEdictsFromIndex();
    }

    private List<Edict> createEdictsFromIndex() {
        List<Edict> edicts = new ArrayList<>();
        String textFromPage;
        try (PDDocument document = PDDocument.load(pdf)){
            int firstPage = 1;
            textFromPage = PDFReader.getTextFromPage(document, firstPage);

            //get information from content page
            StringBuilder titleBuilder = new StringBuilder("");
            for (String text : textFromPage.split("\n")) {
                titleBuilder.append(text);
                String title = titleBuilder.toString();
                if (titlePattern.matcher(title).find()) {
                    if (Edict.isTitlePatter(title)) {
                        Edict ed = Edict.create(title);
                        edicts.add(ed);
                    } else {
                        continue;
                    }
                }
                titleBuilder = new StringBuilder("");
            }

            //connect raw text to edict
            int numberOfPages = document.getNumberOfPages();
            Edict currentEdict = null;
            for (int i = firstPage + 1; i < numberOfPages; i++) {
                String pageText = PDFReader.getTextFromPage(document, i);
                Optional<Edict> optionalEdict = edicts.stream()
                        .filter(edict -> edict.getPatternFromTitle().matcher(pageText).find())
                        .findFirst();

                if (optionalEdict.isPresent()) {
                    Edict foundEdict = optionalEdict.get();
                    if (currentEdict != null) {
                        List<Pair<Integer, String>> textSegments = foundEdict.getPatternFromTitle()
                                .matcher(pageText)
                                .results()
                                .mapToInt(MatchResult::start)
                                .mapToObj(j -> Pair.of(j, pageText.substring(0, j)))
                                .collect(Collectors.toList());
                        if (textSegments.size() > 1) {
                            throw new UnsupportedOperationException("Not yet integrated");
                        }

                        Pair<Integer, String> textBeforeTitle = textSegments.get(0);
                        //the header is just one line. The assumption is that if there are more than a single line
                        //then there is text which belongs to the edict before
                        if (textBeforeTitle.getRight().split("\n").length > 1) {
                            currentEdict.addRawText(textBeforeTitle.getRight().trim());
                            currentEdict = foundEdict;
                            currentEdict.addRawText(pageText.substring(textBeforeTitle.getLeft()));
                            continue;
                        }
                    }
                    currentEdict = foundEdict;
                } else if (currentEdict == null) {
                    System.out.println(pageText);
                    throw new IllegalStateException("Cannot find an edict to this page");
                }
                currentEdict.addRawText(pageText);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return edicts;
    }

    @Override
    public List<Edict> getResult() {
        return createdEdicts;
    }
}