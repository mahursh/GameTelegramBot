package com.mftplus.game.telegramExecutor.message;

import com.mftplus.game.entity.Card;
import com.mftplus.game.entity.Game;
import com.mftplus.game.entity.User;
import com.mftplus.game.service.GameService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class MessageSender extends DefaultAbsSender {

    private final GameService gameService;
    private final MessageBuilder messageBuilder;


    public MessageSender(@Value("${app.telegram.token}") String botToken,
                         GameService gameService,
                         MessageBuilder messageBuilder) {
        super(new DefaultBotOptions(), botToken);
        this.gameService =gameService;
        this.messageBuilder =messageBuilder;
    }

    @SneakyThrows
    public Object sendMessage(BotApiMethod<?> message){
        return execute(message);
    }

    public void sendNextTurnMsg(Long telegramChatId, Game game) {
        Card nextCard = gameService.getNextCard(game);
        User explainer = gameService.getExplainer(game);
        SendMessage nextTurnMsg = messageBuilder.buildNextTurnMsg(telegramChatId , explainer , nextCard.getId());
        SendMessage cardMsg = messageBuilder.buildCardMsg(explainer.getTelegramId() , nextCard);
        sendMessage(nextTurnMsg);
        sendMessage(cardMsg);
    }
}
