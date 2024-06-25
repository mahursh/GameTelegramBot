package com.mftplus.telegram;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component

public class TabooBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(TabooBot.class);

    private final String username;


    public TabooBot(@Value("${app.telegram.username}") String username,
                    @Value("${app.telegram.token}") String botToken
                  ) {
        super(botToken);
        this.username = username;

        logger.warn("TabooBot initialized with username: {}", username);
        logger.warn("TabooBot initialized with token: {}", botToken);
    }

    @Override
    public String getBotUsername() {

        return username;
    }







    @Override
    public void onUpdateReceived(Update update) {
        //changing name of thread for log.
        Thread.currentThread().setName("Telegram - onUpdateReceived");

//


    }




}
