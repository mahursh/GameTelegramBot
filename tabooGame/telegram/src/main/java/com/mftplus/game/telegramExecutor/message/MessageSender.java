package com.mftplus.game.telegramExecutor.message;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class MessageSender extends DefaultAbsSender {
    public MessageSender(@Value("${app.telegram.token}") String botToken) {
        super(new DefaultBotOptions(), botToken);
    }

    @SneakyThrows
    public Object sendMessage(BotApiMethod<?> message){
        return execute(message);
    }
}
