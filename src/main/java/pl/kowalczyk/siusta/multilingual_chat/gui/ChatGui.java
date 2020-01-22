package pl.kowalczyk.siusta.multilingual_chat.gui;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import org.github.legioth.accessor.Accessor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.vaadin.marcus.shortcut.Shortcut;
import pl.kowalczyk.siusta.multilingual_chat.service.TranslateTextService;
import pl.kowalczyk.siusta.multilingual_chat.model.ChatMessage;
import pl.kowalczyk.siusta.multilingual_chat.model.MessageList;
import pl.kowalczyk.siusta.multilingual_chat.model.TranslatedText;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Route("")
@Push
public class ChatGui extends VerticalLayout {
    private MessageList messages = new MessageList();
    private String name;
    private Map<String, String> languagesMap;
    private String language;
    private TranslateTextService translator;
    private UnicastProcessor<ChatMessage> messageDistributor;
    private Flux<ChatMessage> chatMessages;
    private Div headers = new Div();

    @Autowired
    public ChatGui(UnicastProcessor<ChatMessage> messageDistributor,
                   Flux<ChatMessage> chatMessages, TranslateTextService translator) throws Exception {
        this.translator = translator;
        this.chatMessages = chatMessages;
        this.messageDistributor = messageDistributor;

        setSizeFull();
        this.languagesMap = translator.getLanguagesMap();
        Dialog credentialsDialog = addCredentialsDialog();
        credentialsDialog.open();
        add(credentialsDialog);


        H1 header = new H1("Multilingual chat");
        headers.getElement().getThemeList().add("dark");
        headers.getElement().getStyle().set("width", "100%");
        headers.add(header);


        add(headers, messages, addInputComponent());
        System.out.println(System.getProperty("user.dir"));
    }

    private void addMessage(ChatMessage message) {
        try {
            TranslatedText translatedText = translator.translate(message.getMessage(), this.language);
            if (!translatedText.getSourceLanguage().equals(this.language)) {
                Paragraph translatedParagraph = new Paragraph(message.getTime().format(DateTimeFormatter.ofPattern("h:mm")) + " - " + message.getFrom() + " : " + translatedText.getText());
                messages.add(new Paragraph(message.getMessage() + " {" + translatedText.getSourceLanguage() + "}"), translatedParagraph);
            } else {
                messages.add(new Paragraph(message.getTime().format(DateTimeFormatter.ofPattern("h:mm")) + " - " + message.getFrom() + " : " + message.getMessage()));
            }

        } catch (Exception e) {
            e.printStackTrace();
            messages.add(new Paragraph("Something went wrong"));
        }
    }

    private Dialog addCredentialsDialog() {
        TextField nameField = new TextField("Name");
        ComboBox<String> lanuageChoice = new ComboBox<>("Language");
        lanuageChoice.setItems(languagesMap.keySet().stream().sorted().collect(Collectors.toList()));
        Dialog dialog = new Dialog();
        Button joinButton = new Button("Join");
        joinButton.addClickListener(click -> {
            if (nameField.getValue().trim().length() > 3 && this.languagesMap.keySet().contains(lanuageChoice.getValue())) {
                dialog.close();
                this.name = nameField.getValue();
                this.language = this.languagesMap.get(lanuageChoice.getValue());


                chatMessages.subscribe(this::addMessage);
                headers.add(new H2("Name: " + this.name));
                headers.add(new H3("Language: " + lanuageChoice.getValue()));
            } else {
                Notification notification = new Notification("Username should be at least 3 characters long and language choosen from list", 5 * 1000);
                notification.open();
            }
        });

        Shortcut.add(nameField, Key.ENTER, joinButton::click);
        dialog.add(new HorizontalLayout(nameField,lanuageChoice),joinButton);
        return dialog;

    }

    private HorizontalLayout addInputComponent() {
        TextField input = new TextField();
        Button send = new Button("Send");
        send.addClickListener(click -> {
            if (!(input.getValue().trim().isEmpty()) && input.getValue().trim().length() < 100) {
                ChatMessage message = new ChatMessage(name, input.getValue());
                input.clear();
                input.focus();
                messageDistributor.onNext(message);
            } else {
                Notification notification = new Notification("text can't be empty and have more than 100 characters ");
                notification.open();
                input.focus();
            }
        });

        Shortcut.add(input, Key.ENTER, send::click);
        return new HorizontalLayout(input, send);
    }



}



