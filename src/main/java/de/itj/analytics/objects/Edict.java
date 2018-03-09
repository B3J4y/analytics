package de.itj.analytics.objects;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Edict {
    private final String title;
    private LocalDate date;
    private int page;
    private final static Pattern TITLE_PATTERN = Pattern.compile("([ \\d]?\\d.[ \\d]\\d.\\d{4}) (.*)[\\. ](\\d+)");
    private String rawText;


    public Edict(String title, LocalDate date, int page) {
        this.title = title;
        this.date = date;
        this.page = page;
        rawText = "";
    }

    public static Edict create(String rawTitle) {
        Matcher matcher = TITLE_PATTERN.matcher(rawTitle);
        if (matcher.find()) {
            String dateString = matcher.group(1).trim();
            String[] dateSplitted = dateString.split("\\.");
            int day = Integer.valueOf(dateSplitted[0].trim());
            int month = Integer.valueOf(dateSplitted[1].trim()) - 1;
            int year = Integer.valueOf(dateSplitted[2].trim());
            LocalDate date = LocalDate.of(year, month, day);
            String title = matcher.group(2).split(" \\. \\. ")[0].trim();
            String page = matcher.group(3).trim();
            return new Edict(title, date, Integer.valueOf(page));
        } else {
            throw new IllegalStateException("Cannot match title:" + rawTitle);
        }
    }

    public static boolean isTitlePatter(String rawTitle) {
        return TITLE_PATTERN.matcher(rawTitle).find();
    }

    @Override
    public String toString() {
        String format = "title: %s; date: %s; page: %s";
        return String.format(format, title, date, page);
    }

    public Pattern getPatternFromTitle() {
        String regexPattern = Arrays.stream(title.split(" ")).collect(Collectors.joining("\\s"));
        return Pattern.compile(regexPattern);
    }

    public void addRawText(String pageText) {
        this.rawText += pageText;
    }

    public int getPage() {
        return page;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edict edict = (Edict) o;
        return page == edict.page &&
                Objects.equals(title, edict.title) &&
                Objects.equals(date, edict.date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(title, date, page, rawText);
    }

    public String getRawText() {
        return rawText;
    }
}