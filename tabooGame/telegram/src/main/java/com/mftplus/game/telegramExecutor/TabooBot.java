package com.mftplus.game.telegramExecutor;


import com.mftplus.game.entity.Chat;
import com.mftplus.game.enums.ChatBotStatus;
import com.mftplus.game.service.ChatService;
import com.mftplus.game.telegramExecutor.command.BotCommandHandler;
import com.mftplus.game.telegramExecutor.message.MessageBuilder;
import com.mftplus.game.telegramExecutor.message.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class TabooBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(TabooBot.class);

    private final String username;
    private final BotCommandHandler commandHandler;
    private final ChatService chatService;
    private final MessageBuilder messageBuilder;
    private final MessageSender messageSender;

//________________________________________________________________________________________

    public TabooBot(@Value("${app.telegram.username}") String username,
                    @Value("${app.telegram.token}") String botToken,
                    BotCommandHandler commandHandler,
                    ChatService chatService,
                    MessageBuilder messageBuilder,
                    MessageSender messageSender
                  ) {
        super(botToken);
        this.username = username;
        this.commandHandler = commandHandler;
        this.chatService = chatService;
        this.messageBuilder = messageBuilder;
        this.messageSender = messageSender;

        logger.warn("TabooBot initialized with username: {}", username);
        logger.warn("TabooBot initialized with token: {}", botToken);
    }
//________________________________________________________________________________________

    @Override
    public String getBotUsername() {

        return username;
    }

//________________________________________________________________________________________

    @SneakyThrows //we can remove it , if we use sendMessage method that is inside  MessageSender class.
    @Override
    public void onUpdateReceived(Update update) {
        Thread.currentThread().setName("Telegram");

        if (update.hasMessage()){
            Message message = update.getMessage();
            String text = message.getText();
            if (text == null) return;
            if(text.startsWith("/")){
                logger.info("Bot receives the command: {}" , text );
                commandHandler.handleCommand(message);
            }
        }else if(update.hasMyChatMember()){
            Chat chat = chatService.convert(update.getMyChatMember());
            chat = chatService.save(chat);
            if(chat.getChatBotStatus() == ChatBotStatus.ADMIN){
               SendMessage message =  messageBuilder.buildTextMsg(chat.getTelegramChatId() , "Hello there!\nLet's play the taboo game");
//                messageSender.sendMessage(message);
                execute(message);
            }
        }


    }




}
