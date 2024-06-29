package com.mftplus.game.telegramExecutor.message;

import com.mftplus.game.entity.Card;
import com.mftplus.game.entity.Game;
import com.mftplus.game.entity.GameCard;
import com.mftplus.game.entity.UserGame;
import com.mftplus.game.enums.CardStatus;
import com.mftplus.game.enums.GameRole;
import com.mftplus.game.service.GameService;
import com.mftplus.game.telegramExecutor.util.BotUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Set;

@Component
@Transactional
@RequiredArgsConstructor
public class UserMessageHandler {

    private final GameService gameService;
    private final MessageBuilder messageBuilder;
    private final MessageSender messageSender;


    public void handle(Message message){
        Long telegramChatId = message.getChatId();
        if (BotUtil.isPrivateMsg(message)){
            String txt = "A game tasks place in a group chat, and you might want to send your message to some of them.";
            SendMessage wrongChatMsg = messageBuilder.buildTextMsg(telegramChatId , txt);
            messageSender.sendMessage(wrongChatMsg);
        }else{
            Game game = gameService.findGameInProgress(telegramChatId);
            if (game == null) return;
            User from = message.getFrom();
            Long telegramId = from.getId();
            String text = message.getText();
            GameCard gameCard = gameService.getGameCardInProgress(game);
            UserGame userGame = gameService.whoseMessage(telegramId , game);
            if (userGame.getGameRole() == GameRole.EXPLAINER){
                Set<String> tabooUsed = gameService.findTabooUsed(gameCard.getCard() , text);
                if (! tabooUsed.isEmpty()){

                    gameCard.setStatus(CardStatus.TIME_IS_UP);
                    SendMessage tabooUsedMsg = messageBuilder.buildTabooUsedMsg(telegramChatId , tabooUsed);
                    messageSender.sendMessage(tabooUsedMsg);

                    sendNextCardMessage(telegramChatId,game);

                }
            }else {
                if (gameService.isGuessed(gameCard.getCard(), text)){
                    gameService.cardWasGuessed(gameCard , userGame);
                    SendMessage scoreEarnedMsg = messageBuilder.buildScoreEarnedMsg(telegramChatId , from.getFirstName());
                    messageSender.sendMessage(scoreEarnedMsg);
                    sendNextCardMessage(telegramChatId ,game);
                }
            }
        }
    }


    private void sendNextCardMessage(Long chatId , Game game){
        Card nextCard = gameService.getNextCard(game);
        SendMessage nextTurnMsg = messageBuilder.buildNextCardMsg(chatId , nextCard.getId());
        messageSender.sendMessage(nextTurnMsg);

        com.mftplus.game.entity.User explainer = gameService.getExplainer(game);
        SendMessage cardMsg = messageBuilder.buildCardMsg(explainer.getTelegramId(), nextCard);
        messageSender.sendMessage(cardMsg);
    }
}
