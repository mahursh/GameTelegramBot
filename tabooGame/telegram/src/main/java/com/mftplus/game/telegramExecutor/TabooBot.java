package com.mftplus.game.telegramExecutor;


import com.mftplus.game.telegramExecutor.command.BotCommandHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component

public class TabooBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(TabooBot.class);

    private final String username;
    private final BotCommandHandler commandHandler;

//________________________________________________________________________________________

    public TabooBot(@Value("${app.telegram.username}") String username,
                    @Value("${app.telegram.token}") String botToken,
                    BotCommandHandler commandHandler
                  ) {
        super(botToken);
        this.username = username;
        this.commandHandler = commandHandler;

        logger.warn("TabooBot initialized with username: {}", username);
        logger.warn("TabooBot initialized with token: {}", botToken);
    }
//________________________________________________________________________________________

    @Override
    public String getBotUsername() {

        return username;
    }

//________________________________________________________________________________________

    @Override
    public void onUpdateReceived(Update update) {
        Thread.currentThread().setName("Telegram");

        if (update.hasMessage()){
            Message message = update.getMessage();
            String text = message.getText();
            if (text == null) return;
            if(text.startsWith("/")){
                commandHandler.handleCommand(message);
            }
        }






    }




}
