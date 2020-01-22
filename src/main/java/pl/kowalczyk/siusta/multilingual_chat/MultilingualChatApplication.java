package pl.kowalczyk.siusta.multilingual_chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.kowalczyk.siusta.multilingual_chat.model.ChatMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@SpringBootApplication
public class MultilingualChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultilingualChatApplication.class, args);
    }

    @Bean
    Flux<ChatMessage> chatMessages(UnicastProcessor<ChatMessage> messageDistributor) {
        return messageDistributor.replay(20).autoConnect();
    }

    @Bean
    UnicastProcessor<ChatMessage> messageDistributor() {
        return UnicastProcessor.create();
    }


}
