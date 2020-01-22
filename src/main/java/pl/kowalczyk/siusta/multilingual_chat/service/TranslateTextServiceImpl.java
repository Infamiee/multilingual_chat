package pl.kowalczyk.siusta.multilingual_chat.service;

import com.google.auth.Credentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import pl.kowalczyk.siusta.multilingual_chat.exception.NonSupportedLanguageException;
import pl.kowalczyk.siusta.multilingual_chat.model.TranslatedText;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class TranslateTextServiceImpl implements TranslateTextService {
    private Translate translate;
    private Map<String,String> languageMap;
    public TranslateTextServiceImpl() throws IOException, ParseException {
        this.translate =  TranslateOptions.newBuilder().build().getService();
        this.languageMap = getLanguagesMap();

    }

    @Override
    public TranslatedText translate(String text, String targetLanguage) throws NonSupportedLanguageException {
        if (languageMap.containsValue(targetLanguage)) {
            String originalText = text;
            Translation translation = this.translate.translate(originalText, TranslateOption.targetLanguage(targetLanguage));
            return new TranslatedText(translation.getTranslatedText(),translation.getSourceLanguage());
        }
        else {
            throw new NonSupportedLanguageException("Language is not supported");
        }

    }

    @Override
    public Map<String,String>  getLanguagesMap() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("src/main/resources/static/supported languages.json"));
        Map<String,String> lanuages = new HashMap<>();
        JSONObject jsonObject =  (JSONObject) obj;

        jsonObject.keySet().forEach(key -> {
            Object value = jsonObject.get(key);
            lanuages.put(value.toString(),key.toString());
        });

        return lanuages;


    }
}