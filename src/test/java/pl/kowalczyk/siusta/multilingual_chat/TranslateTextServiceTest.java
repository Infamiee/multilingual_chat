package pl.kowalczyk.siusta.multilingual_chat;


import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import pl.kowalczyk.siusta.multilingual_chat.exception.NonSupportedLanguageException;
import pl.kowalczyk.siusta.multilingual_chat.model.TranslatedText;
import pl.kowalczyk.siusta.multilingual_chat.service.TranslateTextServiceImpl;

import java.io.IOException;
import java.util.Map;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class TranslateTextServiceTest {

    @Spy
    TranslateTextServiceImpl translateTextService;

    @Test
    public void should_translate() {
        try {
            assertEquals(translateTextService.translate("hello", "pl"), new TranslatedText("Witaj", "en"));
            assertEquals(translateTextService.translate("donde esta la biblioteca", "de"), new TranslatedText("Wo ist die Bibliothek?", "es"));
            assertEquals(translateTextService.translate("Wo ist die Bibliothek?", "es"), new TranslatedText("¿Dónde está la biblioteca?", "de"));
            assertEquals(translateTextService.translate("עפּשטיין האָט זיך ניט דערהרגעט", "en"), new TranslatedText("Epstein was not killed", "yi"));
        }catch (NonSupportedLanguageException e){
            fail();
        }

    }

    @Test
    public void should_not_translate() {
        try {
            translateTextService.translate("hello", "not an actual supported language");
            fail();
        } catch (NonSupportedLanguageException e) {
            final String expected = "Language is not supported";
            assertEquals(expected, e.getMessage());
        }

        try {
            translateTextService.translate("hello", "");
            fail();
        } catch (NonSupportedLanguageException e) {
            final String expected = "Language is not supported";
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    public void should_get_value_from_map() throws IOException, ParseException {
        Map<String, String> map = translateTextService.getLanguagesMap();
        assertEquals(map.get("Polish"), "pl");
        assertEquals(map.get("English"), "en");
        assertEquals(map.get("Tajik"), "tg");
        assertEquals(map.get("gdfsgfdsgsdf"), null);
    }


}
