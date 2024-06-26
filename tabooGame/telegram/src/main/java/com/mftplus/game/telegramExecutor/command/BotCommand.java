package com.mftplus.game.telegramExecutor.command;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotCommand {

    void handle(Message message);

    boolean isMatch(String command);
}
