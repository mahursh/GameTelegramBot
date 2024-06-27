package com.mftplus.game.telegramExecutor.command;

import com.mftplus.game.entity.User;
import com.mftplus.game.entity.WaitRoom;
import com.mftplus.game.service.UserService;
import com.mftplus.game.service.WaitRoomService;
import com.mftplus.game.telegramExecutor.message.MessageBuilder;
import com.mftplus.game.telegramExecutor.message.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
@RequiredArgsConstructor
public class StartCommand implements BotCommand {

    private final UserService userService;
    private final MessageSender messageSender;
    private final MessageBuilder messageBuilder;
    private final WaitRoomService waitRoomService;


    @Override
    public void handle(Message message) {

        Long chatId = message.getChatId();
        User user = convert(message.getFrom());
        user = userService.save(user);
        String hash = extractHash(message.getText());
       if (hash != null){
           WaitRoom waitRoom = waitRoomService.join(hash , message.getFrom().getId());
           String txt = "You've joined the game in the <b>%s</b> group".formatted(waitRoom.getChat().getTitle());
           SendMessage joinMessage = messageBuilder.buildTextMsg(chatId, txt);
           messageSender.sendMessage(joinMessage);

       }else{
           SendMessage response = messageBuilder.buildWelcomeMessage(message.getChatId(), user);
           messageSender.sendMessage(response);
       }

    }

    private String extractHash(String text){
        String[] split = text.split(" ");
        if (split.length > 1){
            return split[1];
        }
        return null;
    }

    @Override
    public boolean isMatch(String command) {

        return command.startsWith("start");
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
