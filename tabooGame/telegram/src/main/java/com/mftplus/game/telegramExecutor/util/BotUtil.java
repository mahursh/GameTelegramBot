package com.mftplus.game.telegramExecutor.util;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Objects;

public class BotUtil {

    public static boolean isPrivateMsg(Message message){
        Long chatId = message.getChatId();
        Long telegramId = message.getFrom().getId();
        return Objects.equals(chatId , telegramId);
    }
}
