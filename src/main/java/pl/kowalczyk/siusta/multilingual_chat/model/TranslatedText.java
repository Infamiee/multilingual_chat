package pl.kowalczyk.siusta.multilingual_chat.model;

import java.util.Objects;

public class TranslatedText {
    private final String text;
    private final String sourceLanguage;

    public TranslatedText(String text, String sourceLanguage) {
        this.text = text;
        this.sourceLanguage = sourceLanguage;
    }

    public String getText() {
        return text;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TranslatedText that = (TranslatedText) o;
        return Objects.equals(text, that.text) &&
                Objects.equals(sourceLanguage, that.sourceLanguage);
    }

}
