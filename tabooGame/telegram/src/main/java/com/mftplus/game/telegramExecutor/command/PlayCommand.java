package com.mftplus.game.telegramExecutor.command;

import com.mftplus.game.scheduler.RegistrationFinishExecution;
import com.mftplus.game.service.WaitRoomService;
import com.mftplus.game.telegramExecutor.message.MessageBuilder;
import com.mftplus.game.telegramExecutor.message.MessageSender;
import com.mftplus.game.telegramExecutor.util.BotUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PlayCommand implements BotCommand{
    private static final String NOT_A_GROUP_CHAT = "The game should take place in a group chat rather then in a private chat!";

    private final MessageBuilder messageBuilder;
    private final MessageSender messageSender;
    private final WaitRoomService waitRoomService;
    private final RegistrationFinishExecution registrationFinishExecution;

    @Override
    public void handle(Message message) {
        Long chatId = message.getChatId();
        if (BotUtil.isPrivateMsg(message)){
            SendMessage response = messageBuilder.buildTextMsg(message.getChatId() , NOT_A_GROUP_CHAT);
            messageSender.sendMessage(response);
        }else {
            String hash = UUID.randomUUID().toString();

            SendMessage awaitingMsg = messageBuilder.buildAwaitingMsg(chatId , hash);
            Object sendMessage = messageSender.sendMessage(awaitingMsg);
            Integer messageId = extractMessageId(sendMessage);
            waitRoomService.save(chatId , hash ,messageId);
            registrationFinishExecution.scheduleRegistrationFinish(chatId);
        }

    }

    @Override
    public boolean isMatch(String command) {
        return "play".equals(command);
    }


    private Integer extractMessageId(Object sendMessage){
        if (sendMessage instanceof Message message){
            return message.getMessageId();
        }
        throw new IllegalArgumentException("Unknown Object Type : " + sendMessage.getClass().getName());
    }
}
