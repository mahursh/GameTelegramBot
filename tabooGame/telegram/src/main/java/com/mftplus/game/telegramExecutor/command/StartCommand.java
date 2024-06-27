package com.mftplus.game.telegramExecutor.command;

import com.mftplus.game.entity.User;
import com.mftplus.game.service.UserService;
import com.mftplus.game.telegramExecutor.message.MessageBuilder;
import com.mftplus.game.telegramExecutor.message.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
@RequiredArgsConstructor
public class StartCommand implements BotCommand {

    private final UserService service;
    private final MessageSender messageSender;
    private final MessageBuilder messageBuilder;


    @Override
    public void handle(Message message) {

        User user = convert(message.getFrom());
        user = service.save(user);
        SendMessage response = messageBuilder.buildWelcomeMessage(message.getChatId(), user);
        messageSender.sendMessage(response);

    }

    @Override
    public boolean isMatch(String command) {
        return "start".equals(command);
    }


    private User convert(org.telegram.telegrambots.meta.api.objects.User telegramUser) {
        var user = new User();
        user.setFirstName(telegramUser.getFirstName());
        user.setLastName(telegramUser.getLastName());
        user.setUsername(telegramUser.getUserName());
        user.setTelegramId(telegramUser.getId());

        return user;
    }
}
