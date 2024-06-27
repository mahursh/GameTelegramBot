package com.mftplus.game.telegramExecutor.command;

import com.mftplus.game.telegramExecutor.message.MessageBuilder;
import com.mftplus.game.telegramExecutor.message.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BotCommandHandler {

    private final List<BotCommand> commands;
    private final MessageSender messageSender;
    private final MessageBuilder messageBuilder;

    public void handleCommand(Message message){
        BotCommand botCommand =
                commands.stream()
                        .filter(c -> c.isMatch(extractCommand(message)))
                        .findFirst()
                        .orElse(null);

        if (botCommand == null){
            SendMessage response = messageBuilder.buildCommandNotFoundMsg(message);
            messageSender.sendMessage(response);
        }else {
            botCommand.handle(message);
        }
    }

    private String extractCommand(Message message){
        String text = message.getText();
        if (text.startsWith("/")){
          return text.substring(1);
        }
        throw new IllegalArgumentException("Could Not Extract A Command From Text : " + text);
    }
}
