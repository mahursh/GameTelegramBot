package com.mftplus.game.telegramExecutor.command;

import com.mftplus.game.telegramExecutor.message.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
@RequiredArgsConstructor
public class StartCommand implements BotCommand{

    private final MessageSender messageSender;


    @Override
    public void handle(Message message) {
        var response = new SendMessage();
        response.setChatId(message.getChatId());
        response.setText("Welcome Friend !");
        messageSender.sendMessage(response);

    }

    @Override
    public boolean isMatch(String command) {
        return "start".equals(command);
    }


}
