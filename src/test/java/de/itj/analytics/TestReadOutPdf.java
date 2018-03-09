package de.itj.analytics;

import de.itj.analytics.action.CreateEdictAction;
import de.itj.analytics.objects.Edict;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

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

    @Test
    public void testCreateEdict() {
        File bundesanzeiger = getFile("bgbl118006_76242.pdf");
        CreateEdictAction createEdictAction = new CreateEdictAction(bundesanzeiger);
        createEdictAction.execute();
        List<Edict> result = createEdictAction.getResult();
        assertEquals(8, result.size(), "The count of produced edicts is wrong.");
        assertTrue(result.stream().allMatch(edict -> edict.getRawText().length() > 0), "All edicts must have a raw text");
        List<Edict> testData = createTestEdicts();
        List<String> rawTexts = getRawTexts();
        result.sort(Comparator.comparingInt(Edict::getPage));
        testData.sort(Comparator.comparingInt(Edict::getPage));
        for (int i = 0; i < result.size(); i++) {
            assertEquals(testData.get(i), result.get(i), "Edict is wrong.");
            assertEquals(rawTexts.get(i), result.get(i).getRawText(), "Rawtext is wrong.");
        }
    }

    private List<Edict> createTestEdicts() {
        List<Edict> edicts = new ArrayList<>();

        edicts.add(new Edict("Zweite Verordnung zur Durchführung des Finanzausgleichsgesetzes im Ausgleichsjahr 2015",
                LocalDate.of(2018, Calendar.FEBRUARY, 5), 190));
        edicts.add(new Edict("Zweite Verordnung zur Änderung der Marktordnungswaren-Meldeverordnung",
                LocalDate.of(2018, Calendar.FEBRUARY, 7), 192));
        edicts.add(new Edict("Dreiundzwanzigste Verordnung zur Änderung der Seefischerei-Bußgeldverordnung",
                LocalDate.of(2018, Calendar.FEBRUARY, 7), 196));
        edicts.add(new Edict("Verordnung für eine Übergangsregelung zur Eröffnung des elektronischen Rechtsverkehrs "
                + "mit Bußgeldbehörden im Bereich des Bundesministeriums der Finanzen",
                LocalDate.of(2018, Calendar.FEBRUARY, 8), 197));
        edicts.add(new Edict("Verordnung zur Änderung der Mutterschutz- und Elternzeitverordnung",
                LocalDate.of(2018, Calendar.FEBRUARY, 9), 198));
        edicts.add(new Edict("Verordnung zur Änderung der Elektronischer-Rechtsverkehr-Verordnung",
                LocalDate.of(2018, Calendar.FEBRUARY, 9), 200));
        edicts.add(new Edict("Berichtigung der Metallverfahrenstechnologenausbildungsverordnung",
                LocalDate.of(2018, Calendar.FEBRUARY, 5), 201));
        edicts.add(new Edict("Berichtigung der Verordnung über Verdunstungskühlanlagen, Kühltürme und Nassabscheider",
                LocalDate.of(2018, Calendar.FEBRUARY, 9), 202));
        return edicts;
    }

    private List<String> getRawTexts() {
        List<String> rawTexts = new ArrayList<>();
        rawTexts.add("190 Bundesgesetzblatt Jahrgang 2018 Teil I Nr. 6, ausgegeben zu Bonn am 15. Februar 2018\n" +
                "Zweite Verordnung\n" +
                "zur Durchführung des Finanzausgleichsgesetzes im Ausgleichsjahr 2015\n" +
                "Vom 5. Februar 2018\n" +
                "Auf Grund des § 12 des Finanzausgleichsgesetzes 2. endgültige Ausgleichszuweisungen:\n" +
                "vom 20. Dezember 2001 (BGBl. I S. 3955, 3956) ver-\n" +
                "an Berlin 3 621 856 618,15 Euro\n" +
                "ordnet das Bundesministerium der Finanzen:\n" +
                "an Brandenburg 497 805 893,72 Euro\n" +
                "§ 1 an Bremen 626 734 787,04 Euro\n" +
                "Feststellung der Länderanteile\n" +
                "an der Umsatzsteuer im Ausgleichsjahr 2015 an Mecklenburg-Vorpommern 476 339 096,51 Euro\n" +
                "Für das Ausgleichsjahr 2015 werden als Länder- an Niedersachsen 419 718 957,04 Euro\n" +
                "anteile an der Umsatzsteuer festgestellt: an Nordrhein-Westfalen 1 025 014 547,50 Euro\n" +
                "für Baden-Württemberg 10 880 078 107,45 Euro an Rheinland-Pfalz 350 625 517,44 Euro\n" +
                "für Bayern 12 865 757 672,72 Euro an das Saarland 152 710 373,19 Euro\n" +
                "für Berlin 3 654 244 965,03 Euro an Sachsen 1 029 740 798,52 Euro\n" +
                "für Brandenburg 3 804 150 188,88 Euro an Sachsen-Anhalt 600 770 084,09 Euro\n" +
                "für Bremen 804 726 618,61 Euro an Schleswig-Holstein 249 383 566,03 Euro\n" +
                "für Hamburg 1 787 007 729,50 Euro an Thüringen 585 136 499,95 Euro.\n" +
                "für Hessen 6 174 407 786,53 Euro\n" +
                "§ 3\n" +
                "für Mecklenburg-Vorpommern 2 811 858 599,44 Euro Abschlusszahlungen für 2015\n" +
                "für Niedersachsen 9 940 763 277,72 Euro Zum Ausgleich der Unterschiede zwischen den\n" +
                "für Nordrhein-Westfalen 18 657 936 284,52 Euro vorläufig gezahlten und den endgültig festgestellten\n" +
                "Länderanteilen an der Umsatzsteuer nach § 1, den vor-\n" +
                "für Rheinland-Pfalz 4 613 022 030,26 Euro läufig gezahlten und den endgültig festgestellten Aus-\n" +
                "für das Saarland 1 409 945 749,76 Euro gleichsbeiträgen und Ausgleichszuweisungen nach § 2\n" +
                "werden nach § 15 des Finanzausgleichsgesetzes mit\n" +
                "für Sachsen 7 082 961 923,93 Euro dem Inkrafttreten dieser Verordnung fällig:\n" +
                "für Sachsen-Anhalt 4 012 523 793,83 Euro 1. Überweisungen von zahlungspflichtigen Ländern:\n" +
                "für Schleswig-Holstein 3 183 473 194,13 Euro von Baden-Württemberg 9 905 071,19 Euro\n" +
                "für Thüringen 3 854 188 590,16 Euro. von Bayern 17 953 190,13 Euro\n" +
                "von Hamburg 2 955 300,81 Euro\n" +
                "§ 2 von Hessen 9 466 325,99 Euro,\n" +
                "Abrechnung des Finanzausgleichs 2. Zahlungen an empfangsberechtigte Länder:\n" +
                "unter den Ländern im Ausgleichsjahr 2015\n" +
                "Für das Ausgleichsjahr 2015 wird der Finanzaus- an Berlin 9 055 579,39 Euro\n" +
                "gleich unter den Ländern wie folgt festgestellt: an Brandenburg 3 179 079,31 Euro\n" +
                "1. endgültige Ausgleichsbeiträge: an Bremen 737 247,26 Euro\n" +
                "von Baden-Württemberg 2 323 645 802,52 Euro an Mecklenburg-Vorpommern 3 911 178,94 Euro\n" +
                "von Bayern 5 467 601 474,18 Euro an Niedersachsen 2 164 669,50 Euro\n" +
                "von Hamburg 114 774 295,62 Euro an Nordrhein-Westfalen 5 584 723,37 Euro\n" +
                "von Hessen 1 729 815 166,86 Euro, an Rheinland-Pfalz 1 959 871,51 Euro\n" +
                "Das Bundesgesetzblatt im Internet: www.bundesgesetzblatt.de | Ein Service des Bundesanzeiger Verlag www.bundesanzeiger-verlag.de\n" +
                "Bundesgesetzblatt Jahrgang 2018 Teil I Nr. 6, ausgegeben zu Bonn am 15. Februar 2018 191\n" +
                "an das Saarland 1 219 939,31 Euro § 4\n" +
                "an Sachsen 1 323 711,53 Euro Inkrafttreten, Außerkrafttreten\n" +
                "Diese Verordnung tritt am siebenten Tag nach der\n" +
                "an Sachsen-Anhalt 4 390 172,78 Euro Verkündung in Kraft. Gleichzeitig tritt die Erste Verord-\n" +
                "an Schleswig-Holstein 2 103 910,32 Euro nung zur Durchführung des Finanzausgleichsgesetzes\n" +
                "im Ausgleichsjahr 2015 vom 27. März 2015 (BGBl. I\n" +
                "an Thüringen 4 649 804,91 Euro. S. 365) außer Kraft.\n" +
                "Der Bundesrat hat zugestimmt.\n" +
                "Berlin, den 5. Februar 2018\n" +
                "D e r B u n d e sm i n i s t e r\n" +
                "f ü r b e s o n d e r e A u f g a b e n\n" +
                "M i t d e r Wa h r n e hm u n g d e r G e s c h ä f t e\n" +
                "d e s B u n d e sm i n i s t e r s d e r F i n a n z e n b e a u f t r a g t\n" +
                "P e t e r A l t m a i e r\n" +
                "Das Bundesgesetzblatt im Internet: www.bundesgesetzblatt.de | Ein Service des Bundesanzeiger Verlag www.bundesanzeiger-verlag.de\n");
        rawTexts.add("192 Bundesgesetzblatt Jahrgang 2018 Teil I Nr. 6, ausgegeben zu Bonn am 15. Februar 2018\n" +
                "Zweite Verordnung\n" +
                "zur Änderung der Marktordnungswaren-Meldeverordnung\n" +
                "Vom 7. Februar 2018\n" +
                "Auf Grund des § 15 Absatz 1 Nummer 1 in Verbin- 13. Ethylalkohol landwirtschaftlichen Ursprungs:\n" +
                "dung mit Absatz 2 des Gesetzes über Meldungen über Erzeugnisse im Sinne des Anhangs I Teil XXI\n" +
                "Marktordnungswaren in der Fassung der Bekannt- der Verordnung (EU) Nr. 1308/2013 des\n" +
                "machung vom 26. November 2008 (BGBl. I S. 2260), Europäischen Parlaments und des Rates\n" +
                "von denen § 15 Absatz 1 zuletzt durch Artikel 402 der vom 17. Dezember 2013 über eine gemein-\n" +
                "Verordnung vom 31. August 2015 (BGBl. I S. 1474) same Marktorganisation für landwirtschaft-\n" +
                "geändert worden ist, verordnet das Bundesministerium liche Erzeugnisse und zur Aufhebung der\n" +
                "für Ernährung und Landwirtschaft: Verordnungen (EWG) Nr. 922/72, (EWG)\n" +
                "Nr. 234/79, (EG) Nr. 1037/2001 und (EG)\n" +
                "Artikel 1 Nr. 1234/2007 (ABl. L 347 vom 20.12.2013,\n" +
                "S. 671; L 189 vom 27.6.2014, S. 261; L 130\n" +
                "Änderung der vom 19.5.2016, S. 18; L 34 vom 9.2.2017,\n" +
                "Marktordnungswaren-Meldeverordnung S. 41), die zuletzt durch die Delegierte Ver-\n" +
                "Die Marktordnungswaren-Meldeverordnung vom ordnung (EU) 2016/1226 (ABl. L 202 vom\n" +
                "24. November 1999 (BGBl. I S. 2286), die durch Artikel 1 28.7.2016, S. 5) geändert worden ist, in\n" +
                "der Verordnung vom 2. Dezember 2011 (BGBl. I der jeweils geltenden Fassung; Rohalkohol\n" +
                "S. 2634) geändert worden ist, wird wie folgt geändert: mit einem Alkoholgehalt von unter 96 Volu-\n" +
                "1. § 1 wird wie folgt geändert: menprozent, der nach einer Rektifikation\n" +
                "als neutraler Ethylalkohol landwirtschaft-\n" +
                "a) In Nummer 1 werden die Wörter „Spelz und“ lichen Ursprungs vermarktet wird, gilt eben-\n" +
                "durch die Wörter „Emmer und Einkorn,“ ersetzt. falls als Ethylalkohol landwirtschaftlichen\n" +
                "b) Nummer 5 wird wie folgt gefasst: Ursprungs im Sinne dieser Verordnung,“.\n" +
                "„5. Zucker: Erzeugnisse im Sinne des Anhangs III f) Die bisherige Nummer 12 wird Nummer 14 und\n" +
                "Nummer 2 Buchstabe B Unterabsatz 4 Buch- wie folgt gefasst:\n" +
                "stabe a der Durchführungsverordnung (EU) „14. Molkereien: Unternehmen, die im Durch-\n" +
                "2017/1185 der Kommission vom 20. April 2017 schnitt eines Jahres täglich mindestens\n" +
                "mit Durchführungsbestimmungen zu den Ver- 3 000 Liter Milch verarbeiten oder nach\n" +
                "ordnungen (EU) Nr. 1307/2013 und (EU) einer Bearbeitung zur weiteren Be- und\n" +
                "Nr. 1308/2013 des Europäischen Parlaments Verarbeitung an andere Unternehmen ab-\n" +
                "und des Rates in Bezug auf die Übermittlung geben; als Molkereien im Sinne dieser Ver-\n" +
                "von Informationen und Dokumenten an die ordnung gelten auch Unternehmen, die Er-\n" +
                "Kommission und zur Änderung und Aufhe- zeugnisse im Sinne von Nummer 17 und\n" +
                "bung mehrerer Verordnungen der Kommis- Nummer 18 Buchstabe a bis d herstellen,“.\n" +
                "sion (ABl. L 171 vom 4.7.2017, S. 113) in der\n" +
                "jeweils geltenden Fassung mit Ausnahme des g) Die bisherigen Nummern 13 und 14 werden die\n" +
                "in Buchstabe b genannten Weißzuckers,“. Nummern 15 und 16.\n" +
                "c) Nach Nummer 5 wird folgende Nummer 5a ein- h) Die bisherige Nummer 15 wird Nummer 17 und\n" +
                "gefügt: wie folgt gefasst:\n" +
                "„5a. Isoglucose: Erzeugnisse im Sinne des An- „17. Konsummilch: Milch im Sinne des An-\n" +
                "hangs III Nummer 2 Buchstabe C Unterab- hangs VII Teil IV Ziffer III Unterabsatz 1\n" +
                "satz 4 Satz 1 der Durchführungsverordnung Buchstabe b bis d der Verordnung (EU)\n" +
                "(EU) 2017/1185 in der jeweils geltenden Nr. 1308/2013,“.\n" +
                "Fassung,“. i) Die bisherige Nummer 16 wird Nummer 18.\n" +
                "d) Die bisherige Nummer 5a wird Nummer 5b. j) Die bisherige Nummer 17 wird Nummer 19 und\n" +
                "e) Nach Nummer 11 werden die folgenden Num- wie folgt geändert:\n" +
                "mern 12 und 13 eingefügt: aa) In Buchstabe a werden nach der Angabe\n" +
                "„12. Bioethanol aus Zucker: Ethylalkohol, der „nach § 2“ die Wörter „Absatz 2 bis 9“ einge-\n" +
                "aus einem der Erzeugnisse im Sinne des fügt.\n" +
                "Anhangs III Nummer 2 Buchstabe B Unter- bb) In Buchstabe b werden die Angabe „§§ 4\n" +
                "absatz 4 Buchstabe a der Durchführungs- und 5“ durch die Angabe „§§ 4, 5 und 5a“\n" +
                "verordnung (EU) 2017/1185 gewonnen und der Punkt am Ende durch ein Komma\n" +
                "wurde, ersetzt.\n" +
                "Das Bundesgesetzblatt im Internet: www.bundesgesetzblatt.de | Ein Service des Bundesanzeiger Verlag www.bundesanzeiger-verlag.de\n" +
                "Bundesgesetzblatt Jahrgang 2018 Teil I Nr. 6, ausgegeben zu Bonn am 15. Februar 2018 193\n" +
                "cc) Folgender Buchstabe c wird angefügt: i) Die folgenden Absätze 9 und 10 werden ange-\n" +
                "fügt:\n" +
                "„c) der Meldepflichten nach § 3 die Monate\n" +
                "Oktober bis einschließlich September „(9) Die Meldungen der Hersteller von Ethyl-\n" +
                "des darauffolgenden Jahres.“ alkohol aus Getreide sind\n" +
                "2. § 2 wird wie folgt geändert: 1. im Fall einer jährlichen Herstellung von 1 000\n" +
                "bis unter 10 000 Hektoliter Ethylalkohol jähr-\n" +
                "a) In Absatz 1 wird die Angabe „8“ durch die An- lich nach Maßgabe des § 7 Nummer 1 Buch-\n" +
                "gabe „10“ ersetzt. stabe a,\n" +
                "b) In Absatz 2 Satz 1 Nummer 1 wird nach der An- 2. ab einer jährlichen Herstellung von 10 000\n" +
                "gabe „§ 7 Nummer 1“ die Angabe „Buchstabe a“ Hektoliter Ethylalkohol monatlich nach Maß-\n" +
                "eingefügt. gabe des § 7 Nummer 2\n" +
                "c) In Absatz 3 Satz 1 Nummer 1 wird nach der An- abzugeben. In ihnen sind folgende Angaben zu\n" +
                "gabe „§ 7 Nummer 1“ die Angabe „Buchstabe a“ machen:\n" +
                "eingefügt. 1. für jede Getreideart gesondert, jeweils in\n" +
                "d) Absatz 4 wird wie folgt gefasst: Tonnen, die Angaben nach Absatz 2 Satz 2\n" +
                "Nummer 1,\n" +
                "„(4) Die Meldungen der Hersteller von Stärke\n" +
                "sind 2. für Ethylalkohol jeweils in Hektoliter reiner\n" +
                "Alkohol:\n" +
                "1. im Fall einer jährlichen Herstellung von 1 000 a) die hergestellte Menge, aufgeschlüsselt\n" +
                "bis unter 5 000 Tonnen Stärke jährlich nach nach den verwendeten Getreidearten,\n" +
                "Maßgabe des § 7 Nummer 1 Buchstabe a,\n" +
                "b) die abgegebene Menge nach Verwen-\n" +
                "2. ab einer jährlichen Herstellung von 5 000 Ton- dungszwecken einschließlich der Ausfuhr.\n" +
                "nen Stärke monatlich nach Maßgabe des § 7 (10) Reismühlen mit einer jährlichen Verarbei-\n" +
                "Nummer 2 tungsmenge von mehr als 1 000 Tonnen haben\n" +
                "abzugeben. In ihnen sind folgende Angaben unbeschadet des Absatzes 2 nach Maßgabe des\n" +
                "jeweils in Tonnen zu machen: § 7 Nummer 1 Buchstabe b die Reisbestände\n" +
                "1. für Rohstoffe zur Stärkeherstellung jeweils nach Maßgabe des Anhangs III Nummer 1 Unter-\n" +
                "gesondert nach Stärketräger: absatz 1 Buchstabe b der Durchführungsverord-\n" +
                "nung (EU) 2017/1185 zu melden.“\n" +
                "a) der Bestand am Ende des Meldezeitraums 3. § 3 wird wie folgt geändert:\n" +
                "sowie eine Bestandskorrektur,\n" +
                "a) Absatz 1 wird wie folgt gefasst:\n" +
                "b) der Zugang nach Lieferantengruppen, je-\n" +
                "weils untergliedert nach Inland und Aus- „(1) Die Unternehmen nach Absatz 2, 4, 6\n" +
                "land, oder 7 melden:\n" +
                "a) die Angaben nach Absatz 2 Nummer 1 Buch-\n" +
                "c) der Abgang nach Verwendungszwecken stabe a Doppelbuchstabe aa, Buchstabe b\n" +
                "einschließlich der Ausfuhr, bis d, Nummer 2 und 3 und Absatz 4 nach\n" +
                "2. für stärke- und kohlenhydrathaltige Erzeug- Maßgabe des § 6 Absatz 1 und 3,\n" +
                "nisse gesondert nach dem jeweiligen Erzeug- b) die Angaben nach Absatz 2 Nummer 1 Buch-\n" +
                "nis sowie für Nebenerzeugnisse der Stärke- stabe a Doppelbuchstabe bb, Nummer 4,\n" +
                "herstellung: Absatz 2a und Absatz 5 bis 7 nach Maßgabe\n" +
                "a) der Bestand am Ende des Meldezeitraums des § 6 Absatz 3.\n" +
                "sowie eine Bestandskorrektur, Die Unternehmen nach Absatz 3 Satz 1 melden\n" +
                "b) die Herstellung nach Verwendungszwecken nach Maßgabe des § 6 Absatz 1 Satz 1 und Ab-\n" +
                "sowie der sonstige Zugang aus dem Inland satz 3.“\n" +
                "und Ausland, b) Absatz 2 wird wie folgt geändert:\n" +
                "c) der Abgang nach Verwendungszwecken aa) Nummer 1 Buchstabe a wird wie folgt ge-\n" +
                "einschließlich der Ausfuhr.“ fasst:\n" +
                "„a) nach Maßgabe von Anhang III Nummer 2\n" +
                "e) In Absatz 5 wird im einleitenden Satzteil nach\n" +
                "Buchstabe D Unterabsatz 1 Buchstabe a\n" +
                "der Angabe „§ 7 Nummer 1“ die Angabe „Buch-\n" +
                "der Durchführungsverordnung (EU)\n" +
                "stabe a“ eingefügt.\n" +
                "2017/1185 den Bestand am Ende des\n" +
                "f) In Absatz 6 wird im einleitenden Satzteil nach Meldezeitraums sowie eine Bestands-\n" +
                "der Angabe „§ 7 Nummer 1“ die Angabe „Buch- korrektur, jeweils in Tonnen Weißzucker-\n" +
                "stabe a“ eingefügt. wert, getrennt nach Beständen und\n" +
                "g) In Absatz 7 Satz 1 Nummer 1 wird nach der An- Bestandskorrekturen\n" +
                "gabe „§ 7 Nummer 1“ die Angabe „Buchstabe a“ aa) im Inland und\n" +
                "eingefügt. bb) in anderen Mitgliedstaaten der Euro-\n" +
                "h) In Absatz 8 Satz 1 Nummer 1 und Satz 3 wird päischen Union,“.\n" +
                "jeweils nach der Angabe „§ 7 Nummer 1“ die bb) In Nummer 3 Buchstabe e wird der Punkt am\n" +
                "Angabe „Buchstabe a“ eingefügt. Ende durch ein Komma ersetzt.\n" +
                "Das Bundesgesetzblatt im Internet: www.bundesgesetzblatt.de | Ein Service des Bundesanzeiger Verlag www.bundesanzeiger-verlag.de\n" +
                "194 Bundesgesetzblatt Jahrgang 2018 Teil I Nr. 6, ausgegeben zu Bonn am 15. Februar 2018\n" +
                "cc) Folgende Nummer 4 wird angefügt: d) In Absatz 5 Satz 1 Nummer 1 wird nach der An-\n" +
                "„4. für Weißzucker ab Fabrik nach Maßgabe gabe „§ 7 Nummer 1“ die Angabe „Buchstabe a“\n" +
                "von Anhang II Nummer 1 Unterabsatz 1 eingefügt.\n" +
                "Buchstabe a der Durchführungsverord- 5. § 5 wird wie folgt geändert:\n" +
                "nung (EU) 2017/1185 den durchschnitt- a) Absatz 1 Satz 1 Nummer 1 Buchstabe a wird wie\n" +
                "lichen Verkaufspreis je Tonne sowie die folgt gefasst:\n" +
                "verkaufte Menge in Tonnen Weißzucker-\n" +
                "wert. „a) die Anlieferung von landwirtschaftlichen Be-“\n" +
                "trieben, untergliedert nach Tierarten, jeweils\n" +
                "c) Nach Absatz 2 wird folgender Absatz 2a einge- unter gesonderter Angabe der Anlieferung der\n" +
                "fügt: Milch, die nach unionsrechtlichen Vorschrif-\n" +
                "„(2a) Für den jeweils laufenden Monat haben ten über die ökologische Produktion nach\n" +
                "die Unternehmen nach Absatz 2 nach Maßgabe Maßgabe der Verordnung (EG) Nr. 834/2007\n" +
                "des § 7 Nummer 3 den voraussichtlichen Ver- erzeugt wurde; die Anlieferung von landwirt-\n" +
                "kaufspreis je Tonne für Weißzucker ab Fabrik schaftlichen Betrieben aus dem Ausland ist\n" +
                "nach Maßgabe des Anhangs II Nummer 1 Unter- zusätzlich nach Herkunftsland zu unterglie-\n" +
                "absatz 1 Buchstabe a der Durchführungsverord- dern, die Anlieferung von landwirtschaftlichen\n" +
                "nung (EU) 2017/1185 sowie die voraussichtlich Betrieben aus dem Inland nach Ländern,“.\n" +
                "verkaufte Menge in Tonnen Weißzuckerwert zu b) In Absatz 2 Satz 1 Nummer 1 werden die An-\n" +
                "melden.“ gabe „Nummer 15“ durch die Angabe „Num-\n" +
                "d) Die folgenden Absätze 5 bis 7 werden angefügt: mer 17“ und die Angabe „Nummer 16“ durch\n" +
                "„(5) Die Unternehmen nach Absatz 2 haben die Angabe „Nummer 18“ ersetzt.\n" +
                "jährlich für das vorangegangene Wirtschaftsjahr 6. Nach § 5 wird folgender § 5a eingefügt:\n" +
                "nach Maßgabe des § 7 Nummer 1 Buchstabe c „§ 5a\n" +
                "den von ihnen gezahlten Durchschnittspreis für\n" +
                "Zuckerrüben sowie die entsprechenden Gesamt- Meldepflichten der Alkoholwirtschaft\n" +
                "mengen nach Maßgabe des Anhangs II Nummer 1 (1) Die in den folgenden Absätzen 2 und 3 auf-\n" +
                "Unterabsatz 1 Buchstabe b der Durchführungs- geführten Unternehmen haben jeweils die dort ge-\n" +
                "verordnung (EU) 2017/1185 zu melden. nannten Angaben nach Maßgabe des § 6 Absatz 3\n" +
                "(6) Die Hersteller von Bioethanol aus Zucker und des § 7 Nummer 1 Buchstabe a jährlich zu mel-\n" +
                "mit einer jährlichen Herstellung von mehr als den, soweit die Angaben nicht bereits nach § 2 Ab-\n" +
                "1 000 Hektoliter haben jährlich für das vorange- satz 9 Satz 1 zu melden sind.\n" +
                "gangene Wirtschaftsjahr nach Maßgabe des § 7 (2) Hersteller von Ethylalkohol landwirtschaftli-\n" +
                "Nummer 1 Buchstabe d die erzeugte Menge an chen Ursprungs mit einer jährlichen Herstellung\n" +
                "Bioethanol aus Zucker nach Maßgabe des An- von mehr als 1 000 Hektolitern haben eine Meldung\n" +
                "hangs III Nummer 2 Buchstabe B Unterabsatz 4 nach Maßgabe des Anhangs III Nummer 11 Unter-\n" +
                "Buchstabe e der Durchführungsverordnung (EU) absatz 1 Buchstabe a und b der Durchführungsver-\n" +
                "2017/1185 zu melden. ordnung (EU) 2017/1185 abzugeben.\n" +
                "(7) Die Meldungen der Hersteller von Isoglu- (3) Einführer von Ethylalkohol landwirtschaftli-\n" +
                "cose mit einer jährlichen Herstellung von mehr chen Ursprungs mit einer jährlich gehandelten\n" +
                "als 1 000 Tonnen Isoglucose sind mit folgenden Menge von mehr als 1 000 Hektolitern haben eine\n" +
                "Angaben abzugeben: Meldung nach Maßgabe des Anhangs III Nummer 11\n" +
                "Unterabsatz 1 Buchstabe b der Durchführungsver-\n" +
                "1. monatlich nach Maßgabe des § 7 Nummer 2\n" +
                "ordnung (EU) 2017/1185 abzugeben.“\n" +
                "die Mengen an Isoglucose nach Maßgabe\n" +
                "des Anhangs III Nummer 2 Buchstabe C Un- 7. § 6 wird wie folgt geändert:\n" +
                "terabsatz 1 Buchstabe b der Durchführungs- a) Absatz 1 Satz 1 wird wie folgt gefasst:\n" +
                "verordnung (EU) 2017/1185,\n" +
                "„Soweit auf diese Vorschrift Bezug genommen\n" +
                "2. jährlich nach Maßgabe des § 7 Nummer 1 wird, haben Unternehmen mit mehreren Betrie-\n" +
                "Buchstabe d der Bestand an Isoglucose nach ben für jeden Betrieb gesondert zu melden.“\n" +
                "Maßgabe des Anhangs III Nummer 2 Buch- b) In Absatz 5 werden die Wörter „§ 2 Absatz 2\n" +
                "stabe D Unterabsatz 1 Buchstabe b der bis 8“ durch die Wörter „§ 2 Absatz 2 bis 10“\n" +
                "Durchführungsverordnung (EU) 2017/1185.“ und die Wörter „oder § 5 Abs. 1 oder 2“ durch\n" +
                "4. § 4 wird wie folgt geändert: die Wörter „, § 5 Absatz 1 oder 2 oder § 5a\n" +
                "a) In Absatz 2 Satz 1 Nummer 1 Buchstabe a und Absatz 2 oder 3“ ersetzt.\n" +
                "Nummer 2 Buchstabe a wird jeweils nach der 8. § 7 wird wie folgt gefasst:\n" +
                "Angabe „§ 7 Nummer 1“ die Angabe „Buch-\n" +
                "„§ 7\n" +
                "stabe a“ eingefügt.\n" +
                "Zeitpunkt der Meldungen\n" +
                "b) In Absatz 3 Satz 1 Nummer 1 wird nach der An-\n" +
                "gabe „§ 7 Nummer 1“ die Angabe „Buchstabe a“ Bei der zuständigen Stelle haben einzugehen:\n" +
                "eingefügt. 1. die abzugebenden Jahres- und Halbjahresmel-\n" +
                "c) In Absatz 4 wird im einleitenden Satzteil nach dungen\n" +
                "der Angabe „§ 7 Nummer 1“ die Angabe „Buch- a) nach § 2 Absatz 2 Satz 1 Nummer 1, Absatz 3\n" +
                "stabe a“ eingefügt. Satz 1 Nummer 1, Absatz 4 Satz 1 Nummer 1,\n" +
                "Das Bundesgesetzblatt im Internet: www.bundesgesetzblatt.de | Ein Service des Bundesanzeiger Verlag www.bundesanzeiger-verlag.de\n" +
                "Bundesgesetzblatt Jahrgang 2018 Teil I Nr. 6, ausgegeben zu Bonn am 15. Februar 2018 195\n" +
                "Absatz 5, Absatz 6, Absatz 7 Satz 1 Num- zur Aufhebung der Richtlinie 95/46/EG (Datenschutz-\n" +
                "mer 1, Absatz 8 Satz 1 Nummer 1 und Ab- Grundverordnung) (ABl. L 119 vom 4.5.2016, S. 1;\n" +
                "satz 9 Satz 1 Nummer 1, § 4 Absatz 2 Satz 1 L 314 vom 22.11.2016, S. 72) in der jeweils gelten-\n" +
                "Nummer 1 Buchstabe a, Nummer 2 Buch- den Fassung“ ersetzt.\n" +
                "stabe a, Absatz 3 Satz 1 Nummer 1, Absatz 4\n" +
                "10. In § 9 Satz 1 werden die Wörter „und § 5 Absatz 1\n" +
                "und Absatz 5 Satz 1 Nummer 1, § 5 Absatz 2\n" +
                "oder 2“ durch die Wörter „, § 5 Absatz 1 oder 2 und\n" +
                "Satz 3 und § 5a Absatz 1 spätestens am\n" +
                "§ 5a Absatz 1“ ersetzt.\n" +
                "30. Tag nach Ablauf des jeweiligen Berichts-\n" +
                "zeitraums, 11. In § 10 Nummer 1 werden die Wörter „§ 2 Absatz 1\n" +
                "b) nach § 2 Absatz 10 spätestens am 30. No- oder § 3 Absatz 1 Nummer 1 oder § 4 Absatz 1 oder\n" +
                "vember eines Jahres für den vorangegange- § 5 Absatz 1 Satz 1 oder Absatz 2“ durch die Wör-\n" +
                "nen Berichtszeitpunkt, ter „§ 2 Absatz 1, § 3 Absatz 1 oder Absatz 2a, § 4\n" +
                "Absatz 1, § 5 Absatz 1 Satz 1 oder Absatz 2 oder\n" +
                "c) nach § 3 Absatz 5 spätestens am 31. Mai § 5a Absatz 1“ ersetzt.\n" +
                "eines Jahres für das vorangegangene Wirt-\n" +
                "schaftsjahr, 12. § 11 wird wie folgt gefasst:\n" +
                "d) nach § 3 Absatz 6 und Absatz 7 Nummer 2 „§ 11\n" +
                "spätestens am 30. Oktober für das vorange-\n" +
                "gangene Wirtschaftsjahr, Übergangsregelung\n" +
                "2. die nach § 2 Absatz 2 Satz 1 Nummer 2, Absatz 3 Abweichend von den Vorschriften dieser Verord-\n" +
                "Satz 1 Nummer 2, Absatz 4 Satz 1 Nummer 2, nung\n" +
                "Absatz 7 Satz 1 Nummer 2, Absatz 8 Satz 1 1. gelten die Bestimmungen der Marktordnungs-\n" +
                "Nummer 2 und Absatz 9 Satz 1 Nummer 2, § 3 waren-Meldeverordnung in der bis zum Ablauf\n" +
                "Absatz 2 bis 4, § 4 Absatz 2 Satz 1 Nummer 1 des 15. Februar 2018 geltenden Fassung für Mel-\n" +
                "Buchstabe b, Absatz 2 Satz 1 Nummer 2 Buch- dungen nach § 2 Absatz 4 für vor dem 1. Juli 2018\n" +
                "stabe b, Absatz 3 Satz 1 Nummer 2 und Absatz 5 endende Meldezeiträume weiter und\n" +
                "Satz 1 Nummer 2, § 5 Absatz 1 Satz 1 Nummer 1\n" +
                "bis 3 und Absatz 2 Satz 2 abzugebenden monat- 2. sind die monatlichen Meldungen nach § 2 Ab-\n" +
                "lichen Meldungen spätestens am 20. Tag nach satz 9 Satz 1 Nummer 2 erstmals für den Mel-\n" +
                "Ablauf des Berichtsmonats, dezeitraum Juli 2018 abzugeben; mit der ersten\n" +
                "3. die nach § 3 Absatz 2a abzugebenden monat- Monatsmeldung haben die Meldepflichtigen zu-\n" +
                "lichen Meldungen spätestens am 20. Tag des sätzlich eine zusammenfassende Meldung für\n" +
                "laufenden Monats. das erste Halbjahr 2018 abzugeben.““\n" +
                "9. In § 8 Absatz 1 Satz 1 werden die Wörter „§ 9 und Artikel 2\n" +
                "der Anlage des Bundesdatenschutzgesetzes“\n" +
                "durch die Wörter „Artikel 24, 25 und 32 der Verord- Inkrafttreten\n" +
                "nung (EU) 2016/679 des Europäischen Parlaments\n" +
                "(1) Diese Verordnung tritt vorbehaltlich des Absat-\n" +
                "und des Rates vom 27. April 2016 zum Schutz\n" +
                "zes 2 am Tag nach der Verkündung in Kraft.\n" +
                "natürlicher Personen bei der Verarbeitung perso-\n" +
                "nenbezogener Daten, zum freien Datenverkehr und (2) Artikel 1 Nummer 9 tritt am 25. Mai 2018 in Kraft.\n" +
                "Bonn, den 7. Februar 2018\n" +
                "D e r B u n d e sm i n i s t e r\n" +
                "f ü r E r n ä h r u n g u n d L a n d w i r t s c h a f t\n" +
                "C h r i s t i a n S c hm i d t\n" +
                "Das Bundesgesetzblatt im Internet: www.bundesgesetzblatt.de | Ein Service des Bundesanzeiger Verlag www.bundesanzeiger-verlag.de\n");
        rawTexts.add("196 Bundesgesetzblatt Jahrgang 2018 Teil I Nr. 6, ausgegeben zu Bonn am 15. Februar 2018\n" +
                "Dreiundzwanzigste Verordnung\n" +
                "zur Änderung der Seefischerei-Bußgeldverordnung1\n" +
                "Vom 7. Februar 2018\n" +
                "Auf Grund des § 18 Absatz 6 Satz 1 des Seefischereigesetzes, der zuletzt\n" +
                "durch Artikel 424 der Verordnung vom 31. August 2015 (BGBl. I S. 1474) ge-\n" +
                "ändert worden ist, verordnet das Bundesministerium für Ernährung und Land-\n" +
                "wirtschaft:\n" +
                "Artikel 1\n" +
                "Änderung der\n" +
                "Seefischerei-Bußgeldverordnung\n" +
                "Die Seefischerei-Bußgeldverordnung vom 16. Juni 1998 (BGBl. I S. 1355), die\n" +
                "zuletzt durch Artikel 1 der Verordnung vom 10. November 2017 (BGBl. I S. 3770)\n" +
                "geändert worden ist, wird wie folgt geändert:\n" +
                "1. § 34 wird aufgehoben.\n" +
                "2. Die bisherigen §§ 35 bis 41 werden die §§ 34 bis 40.\n" +
                "3. Nach dem neuen § 40 wird folgender § 41 eingefügt:\n" +
                "„§ 41\n" +
                "Durchsetzung bestimmter\n" +
                "Vorschriften der Verordnung (EU) 2017/1970\n" +
                "Ordnungswidrig im Sinne des § 18 Absatz 2 Nummer 11 Buchstabe a des\n" +
                "Seefischereigesetzes handelt, wer gegen die Verordnung (EU) 2017/1970\n" +
                "des Rates vom 27. Oktober 2017 zur Festsetzung der Fangmöglichkeiten\n" +
                "für bestimmte Fischbestände und Bestandsgruppen in der Ostsee für 2018\n" +
                "und zur Änderung der Verordnung (EU) 2017/127 (ABl. L 281 vom 31.10.2017,\n" +
                "S. 1) verstößt, indem er vorsätzlich oder fahrlässig\n" +
                "1. entgegen Artikel 7 Absatz 1 oder 2 mehr als eine dort genannte Anzahl\n" +
                "Exemplare Dorsch behält oder\n" +
                "2. entgegen Unterabsatz 1 der Fußnote (x) zu der Tabelle „Dorsch Unions-\n" +
                "gewässer der Unterdivisionen 25-32 (COD/3DX32)“ oder Unterabsatz 1 der\n" +
                "Fußnote (x) zu der Tabelle „Dorsch Unterdivisionen 22-24 (COD/3BC+24)“\n" +
                "des Anhangs der Verordnung (EU) 2017/1970 Fischerei betreibt.“\n" +
                "Artikel 2\n" +
                "Inkrafttreten\n" +
                "Diese Verordnung tritt am Tag nach der Verkündung in Kraft.\n" +
                "Bonn, den 7. Februar 2018\n" +
                "D e r B u n d e sm i n i s t e r\n" +
                "f ü r E r n ä h r u n g u n d L a n d w i r t s c h a f t\n" +
                "C h r i s t i a n S c hm i d t\n" +
                "1 Diese Verordnung dient der Durchsetzung der Verordnung (EU) 2017/1970 des Rates vom 27. Okto-\n" +
                "ber 2017 zur Festsetzung der Fangmöglichkeiten für bestimmte Fischbestände und Bestands-\n" +
                "gruppen in der Ostsee für 2018 und zur Änderung der Verordnung (EU) 2017/127 (ABl. L 281 vom\n" +
                "31.10.2017, S. 1).\n" +
                "Das Bundesgesetzblatt im Internet: www.bundesgesetzblatt.de | Ein Service des Bundesanzeiger Verlag www.bundesanzeiger-verlag.de\n");
        rawTexts.add("Bundesgesetzblatt Jahrgang 2018 Teil I Nr. 6, ausgegeben zu Bonn am 15. Februar 2018 197\n" +
                "Verordnung\n" +
                "für eine Übergangsregelung zur Eröffnung\n" +
                "des elektronischen Rechtsverkehrs mit Bußgeldbehörden\n" +
                "im Bereich des Bundesministeriums der Finanzen\n" +
                "Vom 8. Februar 2018\n" +
                "Auf Grund des § 134 Satz 1 und 2 des Gesetzes über Ordnungswidrigkeiten,\n" +
                "der durch Artikel 8 Nummer 14 des Gesetzes vom 5. Juli 2017 (BGBl. I S. 2208)\n" +
                "neu gefasst worden ist, in Verbindung mit § 1 der Elektronischer-Rechtsverkehr-\n" +
                "Bußgeld-Subdelegationsverordnung vom 24. November 2017 (BGBl. I S. 3806)\n" +
                "verordnet das Bundesministerium der Finanzen:\n" +
                "§ 1\n" +
                "Übergangsregelung für den\n" +
                "elektronischen Rechtsverkehr mit Bußgeldbehörden\n" +
                "(1) Im Geschäftsbereich des Bundesministeriums der Finanzen ist die Ein-\n" +
                "reichung elektronischer Dokumente bei den Familienkassen als Bußgeldbehörden\n" +
                "abweichend von § 110c Satz 1 des Gesetzes über Ordnungswidrigkeiten in Ver-\n" +
                "bindung mit § 32a der Strafprozessordnung erst zum 1. Januar 2020 möglich.\n" +
                "(2) § 110a des Gesetzes über Ordnungswidrigkeiten findet in der am 31. De-\n" +
                "zember 2017 geltenden Fassung bis zum 31. Dezember 2019 weiter Anwendung.\n" +
                "§ 2\n" +
                "Inkrafttreten, Außerkrafttreten\n" +
                "Diese Verordnung tritt mit Wirkung vom 1. Januar 2018 in Kraft. Sie tritt am\n" +
                "1. Januar 2020 außer Kraft.\n" +
                "Berlin, den 8. Februar 2018\n" +
                "D e r B u n d e sm i n i s t e r\n" +
                "f ü r b e s o n d e r e A u f g a b e n\n" +
                "M i t d e r W a h r n e hm u n g d e r G e s c h ä f t e\n" +
                "d e s B u n d e sm i n i s t e r s d e r F i n a n z e n b e a u f t r a g t\n" +
                "P e t e r A l t m a i e r\n" +
                "Das Bundesgesetzblatt im Internet: www.bundesgesetzblatt.de | Ein Service des Bundesanzeiger Verlag www.bundesanzeiger-verlag.de\n");
        rawTexts.add("198 Bundesgesetzblatt Jahrgang 2018 Teil I Nr. 6, ausgegeben zu Bonn am 15. Februar 2018\n" +
                "Verordnung\n" +
                "zur Änderung der Mutterschutz- und Elternzeitverordnung\n" +
                "Vom 9. Februar 2018\n" +
                "Auf Grund des § 79 Absatz 1 Satz 1 und Absatz 2 Andere Arbeitsschutzvorschriften bleiben unberührt.\n" +
                "Satz 1 des Bundesbeamtengesetzes, der durch Artikel 2 (2) In jeder Dienststelle, bei der regelmäßig mehr\n" +
                "des Gesetzes vom 23. Mai 2017 (BGBl. I S. 1228) neu als drei Personen tätig sind, sind ein Abdruck des\n" +
                "gefasst worden ist, verordnet die Bundesregierung: Mutterschutzgesetzes sowie ein Abdruck dieser Ver-\n" +
                "ordnung an geeigneter Stelle zur Einsicht auszu-\n" +
                "Artikel 1 legen, auszuhändigen oder in einem elektronischen\n" +
                "Änderung der Informationssystem jederzeit zugänglich zu machen.\n" +
                "Mutterschutz- und Elternzeitverordnung\n" +
                "Die Mutterschutz- und Elternzeitverordnung vom § 3\n" +
                "12. Februar 2009 (BGBl. I S. 320), die zuletzt durch Ar- Besoldung bei\n" +
                "tikel 9 des Gesetzes vom 19. Oktober 2016 (BGBl. I Beschäftigungsverbot, Untersuchungen und Stillen\n" +
                "S. 2362) geändert worden ist, wird wie folgt geändert:\n" +
                "(1) Durch die mutterschutzrechtlichen Beschäf-\n" +
                "1. Abschnitt 1 wird wie folgt gefasst: tigungsverbote wird die Zahlung der Dienst- und An-\n" +
                "„Abschnitt 1 wärterbezüge, mit Ausnahme des Verbots der Mehr-\n" +
                "Mutterschutz arbeit, nicht berührt (§§ 3 bis 6, 10 Absatz 3, § 13\n" +
                "Absatz 1 Nummer 3 und § 16 des Mutterschutz-\n" +
                "§ 1 gesetzes). Dies gilt auch für das Dienstversäumnis\n" +
                "wegen ärztlicher Untersuchungen bei Schwanger-\n" +
                "Allgemeines schaft und Mutterschaft sowie während des Stillens\n" +
                "Für den Mutterschutz von Personen in einem (§ 7 des Mutterschutzgesetzes).\n" +
                "Beamtenverhältnis beim Bund sowie bei bundes- (2) Im Fall der vorzeitigen Beendigung einer\n" +
                "unmittelbaren Körperschaften, Anstalten und Stif- Elternzeit nach § 16 Absatz 3 Satz 3 des Bundes-\n" +
                "tungen des öffentlichen Rechts gelten die §§ 2 bis 5. elterngeld- und Elternzeitgesetzes richtet sich die\n" +
                "Höhe der Dienst- und Anwärterbezüge nach dem\n" +
                "§ 2 Beschäftigungsumfang vor der Elternzeit oder wäh-\n" +
                "Anwendung des Mutterschutzgesetzes rend der Elternzeit, wobei die höheren Bezüge maß-\n" +
                "(1) Die folgenden Vorschriften des Mutterschutz- geblich sind.\n" +
                "gesetzes sind entsprechend anzuwenden: (3) Bemessungsgrundlage für die Zahlung von\n" +
                "1. zu Begriffsbestimmungen (§ 2 Absatz 1 Satz 1, Erschwerniszulagen nach der Erschwerniszulagen-\n" +
                "Absatz 3 Satz 1 und Absatz 4 des Mutterschutz- verordnung sowie für die Vergütung nach der Voll-\n" +
                "gesetzes), streckungsvergütungsverordnung in der Fassung\n" +
                "der Bekanntmachung vom 6. Januar 2003 (BGBl. I\n" +
                "2. zur Gestaltung der Arbeitsbedingungen (§§ 9, 10 S. 8) in der jeweils geltenden Fassung ist der Durch-\n" +
                "Absatz 1 und 2, §§ 11, 12, 13 Absatz 1 Nummer 1 schnitt der Zulagen und der Vergütungen der letzten\n" +
                "des Mutterschutzgesetzes), drei Monate vor Beginn des Monats, in dem die\n" +
                "3. zum Arbeitsplatzwechsel (§ 13 Absatz 1 Num- Schwangerschaft eingetreten ist.\n" +
                "mer 2 des Mutterschutzgesetzes),\n" +
                "4. zur Dokumentation und Information durch den § 4\n" +
                "Arbeitgeber (§ 14 des Mutterschutzgesetzes), Entlassung während der Schwangerschaft,\n" +
                "5. zu Beschäftigungsverboten (§§ 3 bis 6, 10 Ab- nach einer Fehlgeburt und nach der Entbindung\n" +
                "satz 3, § 13 Absatz 1 Nummer 3 und § 16 des (1) Während der Schwangerschaft, bis zum Ab-\n" +
                "Mutterschutzgesetzes), lauf von vier Monaten nach einer Fehlgeburt nach\n" +
                "6. zu Mitteilungen und Nachweisen über die der zwölften Schwangerschaftswoche und bis zum\n" +
                "Schwangerschaft und das Stillen (§ 15 des Mut- Ende der Schutzfrist nach der Entbindung, mindes-\n" +
                "terschutzgesetzes), tens bis zum Ablauf von vier Monaten nach der Ent-\n" +
                "7. zur Freistellung für Untersuchungen und zum bindung, darf die Entlassung von Beamtinnen auf\n" +
                "Stillen (§ 7 des Mutterschutzgesetzes), Probe und von Beamtinnen auf Widerruf gegen ihren\n" +
                "Willen nicht ausgesprochen werden, wenn der oder\n" +
                "8. zu den Mitteilungs- und Aufbewahrungspflichten dem Dienstvorgesetzten die Schwangerschaft, die\n" +
                "des Arbeitgebers (§ 27 Absatz 1 bis 5 des Mutter- Fehlgeburt nach der zwölften Schwangerschafts-\n" +
                "schutzgesetzes) sowie woche oder die Entbindung bekannt ist. Eine ohne\n" +
                "9. zum behördlichen Genehmigungsverfahren für diese Kenntnis ergangene Entlassungsverfügung ist\n" +
                "eine Beschäftigung zwischen 20 und 22 Uhr (§ 28 zurückzunehmen, wenn innerhalb von zwei Wochen\n" +
                "des Mutterschutzgesetzes). nach ihrer Zustellung der oder dem Dienstvorge-\n" +
                "Das Bundesgesetzblatt im Internet: www.bundesgesetzblatt.de | Ein Service des Bundesanzeiger Verlag www.bundesanzeiger-verlag.de\n" +
                "Bundesgesetzblatt Jahrgang 2018 Teil I Nr. 6, ausgegeben zu Bonn am 15. Februar 2018 199\n" +
                "setzten die Schwangerschaft, die Fehlgeburt nach tungen nach Abschnitt 5 des Bundesbesoldungs-\n" +
                "der zwölften Schwangerschaftswoche oder die Ent- gesetzes die Versicherungspflichtgrenze in der ge-\n" +
                "bindung mitgeteilt wird. Das Überschreiten dieser setzlichen Krankenversicherung überschreiten oder\n" +
                "Frist ist unbeachtlich, wenn dies auf einem von der überschreiten würden.“\n" +
                "Beamtin nicht zu vertretenden Grund beruht und die 2. § 6 wird wie folgt gefasst:\n" +
                "Mitteilung über die Schwangerschaft, die Fehlgeburt\n" +
                "oder die Entbindung unverzüglich nachgeholt wird. „§ 6\n" +
                "Die Sätze 1 bis 3 gelten entsprechend für Vorbe- Anwendung des\n" +
                "reitungsmaßnahmen des Dienstherrn, die er im Hin- Bundeselterngeld- und Elternzeitgesetzes\n" +
                "blick auf eine Entlassung einer Beamtin trifft. Beamtinnen und Beamte haben Anspruch auf\n" +
                "(2) Die oberste Dienstbehörde kann in besonde- Elternzeit ohne Dienst- oder Anwärterbezüge ent-\n" +
                "ren Fällen, die nicht mit dem Zustand der Beamtin in sprechend des § 15 Absatz 1 bis 3 sowie der §§ 16\n" +
                "der Schwangerschaft, nach einer Fehlgeburt nach und 27 Absatz 1 Satz 1 des Bundeselterngeld- und\n" +
                "der zwölften Schwangerschaftswoche oder nach Elternzeitgesetzes.“\n" +
                "der Entbindung in Zusammenhang stehen, aus- 3. In § 7 Absatz 1 werden die Wörter „30 Stunden\n" +
                "nahmsweise die Entlassung für zulässig erklären. wöchentlich“ durch die Wörter „30 Wochenstunden\n" +
                "(3) Die §§ 31, 32, 34 Absatz 4, § 35 Satz 1, letz- im Durchschnitt eines Monats“ ersetzt.\n" +
                "terer vorbehaltlich der Fälle des § 24 Absatz 3, sowie 4. § 8 Absatz 2 und 3 wird wie folgt gefasst:\n" +
                "die §§ 36 und 37 Absatz 1 Satz 3 des Bundesbeam-\n" +
                "„(2) In besonderen Fällen kann die oberste Dienst-\n" +
                "tengesetzes bleiben unberührt.\n" +
                "behörde die Entlassung ausnahmsweise für zulässig\n" +
                "erklären.\n" +
                "§ 5\n" +
                "(3) § 4 Absatz 3 gilt entsprechend.“\n" +
                "Zuschuss bei\n" +
                "Beschäftigungsverbot während einer Elternzeit 5. § 11 wird wie folgt geändert:\n" +
                "Beamtinnen erhalten einen Zuschuss von 13 Euro a) Absatz 1 wird aufgehoben.\n" +
                "für jeden Kalendertag eines Beschäftigungsverbots b) Die Absatzbezeichnung „(2)“ wird gestrichen.\n" +
                "in den letzten sechs Wochen vor der Entbindung und\n" +
                "eines Beschäftigungsverbots nach der Entbindung Artikel 2\n" +
                "– einschließlich des Entbindungstages –, der in eine Inkrafttreten\n" +
                "Elternzeit fällt. Dies gilt nicht, wenn sie während\n" +
                "der Elternzeit teilzeitbeschäftigt sind. Der Zuschuss (1) Diese Verordnung tritt vorbehaltlich des Absat-\n" +
                "ist auf 210 Euro begrenzt, wenn die Dienst- oder zes 2 mit Wirkung vom 1. Januar 2018 in Kraft.\n" +
                "Anwärterbezüge ohne die mit Rücksicht auf den (2) § 4 Absatz 2 und 3 sowie § 8 Absatz 2 und 3\n" +
                "Familienstand gewährten Zuschläge und ohne Leis- treten am Tag nach der Verkündung in Kraft.\n" +
                "Berlin, den 9. Februar 2018\n" +
                "D i e B u n d e s k a n z l e r i n\n" +
                "Dr. A n g e l a M e r k e l\n" +
                "D e r B u n d e sm i n i s t e r d e s I n n e r n\n" +
                "T h om a s d e M a i z i è r e\n" +
                "Das Bundesgesetzblatt im Internet: www.bundesgesetzblatt.de | Ein Service des Bundesanzeiger Verlag www.bundesanzeiger-verlag.de\n");
        rawTexts.add("200 Bundesgesetzblatt Jahrgang 2018 Teil I Nr. 6, ausgegeben zu Bonn am 15. Februar 2018\n" +
                "Verordnung\n" +
                "zur Änderung der Elektronischer-Rechtsverkehr-Verordnung*\n" +
                "Vom 9. Februar 2018\n" +
                "Auf Grund des § 32a Absatz 2 Satz 2 und Absatz 4 Maßgabe, dass der Datensatz nach § 2 Absatz 3\n" +
                "Nummer 3 der Strafprozessordnung, der durch Artikel 1 mindestens folgende Angaben enthält:\n" +
                "Nummer 2 des Gesetzes vom 5. Juli 2017 (BGBl. I 1. die Bezeichnung der Strafverfolgungsbehörde\n" +
                "S. 2208) eingefügt worden ist, verordnet die Bundes- oder des Gerichts;\n" +
                "regierung:\n" +
                "2. sofern bekannt, das Aktenzeichen des Verfahrens\n" +
                "Artikel 1 oder die Vorgangsnummer;\n" +
                "Änderung der 3. die Bezeichnung der beschuldigten Personen\n" +
                "Elektronischer-Rechtsverkehr-Verordnung oder der Verfahrensbeteiligten; bei Verfahren ge-\n" +
                "gen Unbekannt enthält der Datensatz anstelle der\n" +
                "Die Elektronischer-Rechtsverkehr-Verordnung vom Bezeichnung der beschuldigten Personen die Be-\n" +
                "24. November 2017 (BGBl. I S. 3803) wird wie folgt zeichnung „Unbekannt“ sowie, sofern bekannt,\n" +
                "geändert: die Bezeichnung der geschädigten Personen;\n" +
                "1. Dem § 1 Absatz 1 wird folgender Satz angefügt: 4. die Angabe der den beschuldigten Personen zur\n" +
                "„Sie gilt ferner nach Maßgabe des Kapitels 4 für die Last gelegten Straftat oder des Verfahrensgegen-\n" +
                "Übermittlung elektronischer Dokumente an Strafver- standes;\n" +
                "folgungsbehörden und Strafgerichte der Länder und 5. sofern bekannt, das Aktenzeichen eines densel-\n" +
                "des Bundes nach § 32a der Strafprozessordnung ben Verfahrensgegenstand betreffenden Verfah-\n" +
                "sowie die Bearbeitung elektronischer Dokumente.“ rens und die Bezeichnung der die Akten führen-\n" +
                "2. Nach § 9 wird folgendes Kapitel 4 eingefügt: den Stelle.\n" +
                "„Kapitel 4 § 11\n" +
                "Elektronischer Rechtsverkehr Sonstige\n" +
                "mit Strafverfolgungsbehörden und Strafgerichten verfahrensbezogene elektronische Dokumente\n" +
                "(1) Sonstige verfahrensbezogene elektronische\n" +
                "§ 10 Dokumente, die an Strafverfolgungsbehörden oder\n" +
                "Schriftlich abzufassende, Strafgerichte übermittelt werden, sollen den Anfor-\n" +
                "zu unterschreibende oder derungen des § 2 entsprechen. Entsprechen sie die-\n" +
                "zu unterzeichnende Dokumente sen Anforderungen nicht und sind sie zur Bearbei-\n" +
                "Die Kapitel 2 und 3 gelten im Bereich des elektro- tung durch die Behörde oder das Gericht aufgrund\n" +
                "nischen Rechtsverkehrs mit Strafverfolgungsbehör- der dortigen technischen Ausstattung oder der dort\n" +
                "den und Strafgerichten für schriftlich abzufassende, einzuhaltenden Sicherheitsstandards nicht geeignet,\n" +
                "zu unterschreibende oder zu unterzeichnende Doku- so liegt ein wirksamer Eingang nicht vor. In der Mit-\n" +
                "mente, die gemäß § 32a Absatz 3 der Strafprozess- teilung nach § 32a Absatz 6 Satz 1 der Strafprozess-\n" +
                "ordnung elektronisch eingereicht werden, mit der ordnung ist auf die in § 2 geregelten technischen\n" +
                "Rahmenbedingungen hinzuweisen.\n" +
                "* Notifiziert gemäß der Richtlinie (EU) 2015/1535 des Europäischen (2) Die Übermittlung kann auch auf anderen als\n" +
                "Parlaments und des Rates vom 9. September 2015 über ein Informa- den in § 32a Absatz 4 der Strafprozessordnung\n" +
                "tionsverfahren auf dem Gebiet der technischen Vorschriften und der\n" +
                "Vorschriften für die Dienste der Informationsgesellschaft (ABl. L 241 genannten Übermittlungswegen erfolgen, wenn ein\n" +
                "vom 17.9.2015, S.1). solcher Übermittlungsweg für die Entgegennahme\n" +
                "Das Bundesgesetzblatt im Internet: www.bundesgesetzblatt.de | Ein Service des Bundesanzeiger Verlag www.bundesanzeiger-verlag.de\n" +
                "Bundesgesetzblatt Jahrgang 2018 Teil I Nr. 6, ausgegeben zu Bonn am 15. Februar 2018 201\n" +
                "verfahrensbezogener elektronischer Dokumente ge- Artikel 2\n" +
                "nerell und ausdrücklich eröffnet ist.“ Inkrafttreten\n" +
                "3. Das bisherige Kapitel 4 wird Kapitel 5. Diese Verordnung tritt am Tag nach der Verkündung\n" +
                "4. Der bisherige § 10 wird § 12. in Kraft.\n" +
                "Der Bundesrat hat zugestimmt.\n" +
                "Berlin, den 9. Februar 2018\n" +
                "D i e B u n d e s k a n z l e r i n\n" +
                "Dr. A n g e l a M e r k e l\n" +
                "D e r B u n d e sm i n i s t e r\n" +
                "d e r J u s t i z u n d f ü r Ve r b r a u c h e r s c h u t z\n" +
                "H e i k o M a a s\n" +
                "2");
        rawTexts.add("Berichtigung\n" +
                "der Metallverfahrenstechnologenausbildungsverordnung\n" +
                "Vom 5. Februar 2018\n" +
                "Die Metallverfahrenstechnologenausbildungsverordnung vom 4. Dezember\n" +
                "2017 (BGBl. I S. 3834) ist wie folgt zu berichtigen:\n" +
                "In § 18 Absatz 2 Satz 2 und 3 ist jeweils das Wort „Arbeitsprobe“ durch das\n" +
                "Wort „Arbeitsaufgabe“ zu ersetzen.\n" +
                "Berlin, den 5. Februar 2018\n" +
                "B u n d e sm i n i s t e r i u m\n" +
                "f ü r W i r t s c h a f t u n d E n e r g i e\n" +
                "Im Auftrag\n" +
                "F r i e d h e l m H o l t e r h o f f\n" +
                "Das Bundesgesetzblatt im Internet: www.bundesgesetzblatt.de | Ein Service des Bundesanzeiger Verlag www.bundesanzeiger-verlag.de\n");
        rawTexts.add("2202 Bundesgesetzblatt Jahrgang 2018 Teil I Nr. 6, ausgegeben zu Bonn am 15. Februar 2018\n" +
                "Berichtigung\n" +
                "der Verordnung über Verdunstungskühlanlagen, Kühltürme und Nassabscheider\n" +
                "Vom 9. Februar 2018\n" +
                "Die Verordnung über Verdunstungskühlanlagen, Kühltürme und Nassabschei-\n" +
                "der vom 12. Juli 2017 (BGBl. I S. 2379) ist wie folgt zu berichtigen:\n" +
                "1. § 4 Absatz 1 ist wie folgt zu berichtigen:\n" +
                "a) Satz 4 ist zu streichen.\n" +
                "b) Nach dem neuen Satz 4 ist folgender Satz einzufügen:\n" +
                "„Der Betreiber hat unverzüglich nach der Inbetriebnahme oder der Wieder-\n" +
                "inbetriebnahme die Art der Bestimmung des Referenzwertes nach den\n" +
                "Sätzen 1 bis 3 festzulegen und im Betriebstagebuch zu dokumentieren.“\n" +
                "2. § 9 Absatz 1 Nummer 1 ist wie folgt zu berichtigen:\n" +
                "a) In den Buchstaben a und b ist jeweils das Wort „pneumophilia“ durch das\n" +
                "Wort „pneumophila“ zu ersetzen.\n" +
                "b) In Buchstabe c ist das Wort „non-pneumophilia“ durch das Wort „non-\n" +
                "pneumophila“ zu ersetzen.\n" +
                "Bonn, den 9. Februar 2018\n" +
                "B u n d e sm i n i s t e r i u m\n" +
                "f ü r Umw e l t , N a t u r s c h u t z , B a u u n d R e a k t o r s i c h e r h e i t\n" +
                "Im Auftrag\n" +
                "H a n s - J o a c h i m H umm e l\n" +
                "Hinweis auf Verkündungen im Bundesanzeiger\n" +
                "Gemäß § 2 Absatz 3 des Verkündungs- und Bekanntmachungsgesetzes in der im Bundesgesetzblatt Teil III,\n" +
                " Gliederungsnummer 114-1, veröffentlichten bereinigten Fassung, der zuletzt durch Artikel 1 Nummer 4 des \n" +
                "Gesetzes vom 22. Dezember 2011 (BGBl. I S. 3044) geändert worden ist, wird auf folgende im Bundesanzeiger\n" +
                "(www.bundesanzeiger.de) verkündete Rechtsverordnung nachrichtlich hingewiesen:\n" +
                "                             \n" +
                "                       Tag des\n" +
                "                            D   a t  u  m     u  n  d    B  e  z  e  i c h  n  u  n  g     d  e  r   V  e  r o  r d  n  u  n  g                                                                                                             F  u  n  d  s  t e  l l e                    Inkrafttretens\n" +
                "23.  1. 2018 Neunundzwanzigste Verordnung zur Änderung der Hundertneun-\n" +
                "undfünfzigsten Durchführungsverordnung zur Luftverkehrs-Ord-\n" +
                "nung (Festlegung von Flugverfahren für An- und Abflüge nach\n" +
                " Instrumentenflugregeln zum und vom Flughafen Saarbrücken) BAnz AT 02.02.2018 V1 1.  3. 2018\n" +
                "FNA: 96-1-2-159\n" +
                "Das Bundesgesetzblatt im Internet: www.bundesgesetzblatt.de | Ein Service des Bundesanzeiger Verlag www.bundesanzeiger-verlag.de\n" +
                "Bundesgesetzblatt Jahrgang 2018 Teil I Nr. 6, ausgegeben zu Bonn am 15. Februar 2018 203\n" +
                "Hinweis auf Rechtsvorschriften der Europäischen Union,\n" +
                "die mit ihrer Veröffentlichung im Amtsblatt der Europäischen Union unmittelbare Rechtswirksamkeit in der Bundes -\n" +
                "republik Deutschland erlangt haben.\n" +
                "Aufgeführt werden nur die Verordnungen, die im Inhaltsverzeichnis des Amtsblattes durch Fettdruck hervorgehoben\n" +
                "sind.\n" +
                "                                                                                                                                            ABl. EU\n" +
                "Datum und Bezeichnung der Rechtsvorschrift                                              – Ausgabe in deutscher Sprache –\n" +
                "                                                                                                                              Nr./Seite                  vom\n" +
                "15. 11. 2017 Verordnung (EU) 2017/2101 des Europäischen Parlaments und des\n" +
                " Rates zur Änderung der Verordnung (EG) Nr. 1920/2006 in Bezug auf den\n" +
                "Informationsaustausch zu neuen psychoaktiven Substanzen und das\n" +
                "Frühwarnsystem und das Risikobewertungsverfahren für neue psycho-\n" +
                "aktive Substanzen L 305/1                    21. 11. 2017\n" +
                "5.  7. 2017 Delegierte Verordnung (EU) 2017/2167 der Kommission zur Änderung\n" +
                "der Delegierten Verordnung (EU) 2016/2374 zur Erstellung eines Rück-\n" +
                "wurfplans für bestimmte Fischereien auf Grundfischarten in den süd-\n" +
                "westlichen Gewässern L 306/2                    22. 11. 2017\n" +
                "20.  9. 2017 Delegierte Verordnung (EU) 2017/2168 der Kommission zur Änderung\n" +
                "der Verordnung (EG) Nr. 589/2008 hinsichtlich der Vermarktungsnormen\n" +
                "für Eier aus Freilandhaltung bei Beschränkungen des Zugangs der\n" +
                " Hennen zu einem Auslauf im Freien L 306/6                    22. 11. 2017\n" +
                "21. 11. 2017 Durchführungsverordnung (EU) 2017/2169 der Kommission betreffend\n" +
                "Format und Modalitäten für die Übermittlung europäischer Erdgas- und\n" +
                "Strompreisstatistiken gemäß der Verordnung (EU) 2016/1952 des Euro-\n" +
                "päischen Parlaments und des Rates (1) L 306/9                    22. 11. 2017\n" +
                "(1) Text von Bedeutung für den EWR.\n" +
                "22. 11. 2017 Durchführungsverordnung (EU) 2017/2177 der Kommission über den\n" +
                " Zugang zu Serviceeinrichtungen und schienenverkehrsbezogenen Leis-\n" +
                "tungen (1) L 307/1                    23. 11. 2017\n" +
                "(1) Text von Bedeutung für den EWR.\n" +
                "22. 11. 2017 Durchführungsverordnung (EU) 2017/2178 der Kommission zur Ände-\n" +
                "rung der Verordnung (EU) Nr. 468/2010 über die EU-Liste der Schiffe,\n" +
                "die illegale, ungemeldete und unregulierte Fischerei betreiben L 307/14                  23. 11. 2017\n" +
                "22. 11. 2017 Durchführungsverordnung (EU) 2017/2179 der Kommission zur Ein- \n" +
                "führung eines endgültigen Antidumpingzolls auf die Einfuhren von\n" +
                " Keramikfliesen mit Ursprung in der Volksrepublik China im Anschluss an\n" +
                "eine Auslaufüberprüfung nach Artikel 11 Absatz 2 der Verordnung (EU)\n" +
                "2016/1036 des Europäischen Parlaments und des Rates L 307/25                  23. 11. 2017\n" +
                "21. 11. 2017 Durchführungsverordnung (EU) 2017/2183 der Kommission zur Ge- \n" +
                "nehmigung einer nicht geringfügigen Änderung der Spezifikation einer\n" +
                "im Register der geschützten Ursprungsbezeichnungen und der ge-\n" +
                "schützten geografischen Angaben eingetragenen Bezeichnung („Arancia\n" +
                "del Gargano“ (g.g.A.)) L 309/3                    24. 11. 2017\n" +
                "22. 11. 2017 Durchführungsverordnung (EU) 2017/2184 der Kommission zur Ände-\n" +
                "rung der Verordnung (EG) Nr. 1484/95 in Bezug auf die Festsetzung der\n" +
                "repräsentativen Preise in den Sektoren Geflügelfleisch und Eier sowie für\n" +
                "Eieralbumin L 309/4                    24. 11. 2017\n" +
                "23. 11. 2017 Durchführungsverordnung (EU) 2017/2185 der Kommission über das\n" +
                "Verzeichnis der Codes und der ihnen entsprechenden Produktarten zur\n" +
                "Bestimmung des Geltungsbereichs der Benennung einer Benannten\n" +
                "Stelle auf dem Gebiet der Medizinprodukte im Rahmen der Verordnung\n" +
                "(EU) 2017/745 des Europäischen Parlaments und des Rates sowie auf\n" +
                "dem Gebiet der In-vitro-Diagnostika im Rahmen der Verordnung (EU)\n" +
                "2017/746 des Europäischen Parlaments und des Rates (1) L 309/7                    24. 11. 2017\n" +
                "(1) Text von Bedeutung für den EWR.\n" +
                "Das Bundesgesetzblatt im Internet: www.bundesgesetzblatt.de | Ein Service des Bundesanzeiger Verlag www.bundesanzeiger-verlag.de\n");
        return rawTexts;
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
