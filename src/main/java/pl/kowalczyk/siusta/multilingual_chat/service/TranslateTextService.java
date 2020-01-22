package pl.kowalczyk.siusta.multilingual_chat.service;

import org.json.simple.parser.ParseException;
import pl.kowalczyk.siusta.multilingual_chat.exception.NonSupportedLanguageException;
import pl.kowalczyk.siusta.multilingual_chat.model.TranslatedText;

import java.io.IOException;
import java.util.Map;

public interface TranslateTextService {
   TranslatedText translate(String text, String targetLanguage) throws NonSupportedLanguageException;
   Map<String,String> getLanguagesMap() throws IOException, ParseException;
}
