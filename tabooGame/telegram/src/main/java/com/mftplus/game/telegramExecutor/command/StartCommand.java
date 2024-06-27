package com.mftplus.game.telegramExecutor.command;

import com.mftplus.game.entity.User;
import com.mftplus.game.service.UserService;
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


    @Override
    public void handle(Message message) {
        var response = new SendMessage();
        response.setChatId(message.getChatId());
        response.setText("Welcome Friend !");
        User user = convert(message.getFrom());
        service.save(user);
        messageSender.sendMessage(response);

    }

    @Override
    public boolean isMatch(String command) {
        return "start".equals(command);
    }


    private User convert(org.telegram.telegrambots.meta.api.objects.User telegramUser) {
        var user = new User();
        user.setFirstname(telegramUser.getFirstName());
        user.setLastname(telegramUser.getLastName());
        user.setUsername(telegramUser.getUserName());
        user.setTelegramId(telegramUser.getId());

        return user;
    }
}
