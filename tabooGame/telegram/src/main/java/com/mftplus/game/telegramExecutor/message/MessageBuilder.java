package com.mftplus.game.telegramExecutor.message;

import com.mftplus.game.entity.User;
import com.mftplus.game.entity.WaitRoom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;


@Component
public class MessageBuilder {
    @Value("${app.telegram.username}")
    private String username;
    private static final String JOIN_GAME_INSTRUCTION = "To join the game press the \"start\" command\nwithin a private chat with the bot.\n\nThe game starts in two minutes";

    public SendMessage buildWelcomeMessage(Long chatId , User user){
        SendMessage message = new SendMessage();
        String txt = """
                Welcome %s! \uD83D\uDC4B
                        This bot lets you play 🎮 the Taboo game with your friends.
                        Here's how:
                        1. <a href="https://t.me/%s?startgroup" >Add this</a> bot to a chat with other players.
                        2. In that chat, send the /play command to start a new game.
                       \s
                        Use the /rules command to view the game rules
                
                """.formatted(user.getUsername() , username);

        message.setText(txt);
        message.setChatId(chatId);
        message.setParseMode(ParseMode.HTML);
        return message;
    }

    public SendMessage buildCommandNotFoundMsg(Message message){
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("You Tried To Perform an Unknown Command !");
        return sendMessage;
    }

    public SendMessage buildRulesMessage(Long chatId){
        SendMessage message = new SendMessage();

        String txt=
                """
                There are two roles: \uD83D\uDC42Guesser and \uD83D\uDDE3 Explainer.
                
                The Explainer receives a card in a private chat that includes a word for others to guess and a list of taboo words \uD83D\uDEAB
                ✅ The objective is to assist other players in guessing the word while avoiding the use of any of the taboo words
                
                If any of the Guessers correctly guess a word, both the Explainer and Guesser earn a score\uD83C\uDFAF
                
                The Explainer role changes every five minutes ⌛, and the game continues until everyone has taken a turn explaining.       
                """;

        message.setText(txt);
        message.setChatId(chatId);
        return message;
    }

    public SendMessage buildTextMsg(Long chatId , String text){
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        //TODO: Get ParsMode As Parameter .
        message.setParseMode(ParseMode.HTML);
        return message;
    }

    public SendMessage buildAwaitingMsg(Long chatId , String hash){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(JOIN_GAME_INSTRUCTION);


        sendMessage.setReplyMarkup(joinInlineKeyboard(hash));
        return sendMessage;
    }

    private String buildJoinToGameLink(String hash){
        return "https://t.me/%s?start=%s".formatted(username , hash);
    }

    public EditMessageText editAwaitingMsg(WaitRoom waitRoom){
        EditMessageText editMessageText = new EditMessageText();
        String text = JOIN_GAME_INSTRUCTION;
        if(waitRoom.getUsers().size() > 0){
            text += "\n\nJoined players:\n";
            text += addListOfPlayers(waitRoom.getUsers());
        }
        editMessageText.setText(text);
        editMessageText.setParseMode(ParseMode.HTML);
        editMessageText.setChatId(waitRoom.getChat().getTelegramChatId());
        editMessageText.setMessageId(waitRoom.getMessageId());
        editMessageText.setReplyMarkup(joinInlineKeyboard(waitRoom.getHash()));
        return editMessageText;
    }

    public InlineKeyboardMarkup joinInlineKeyboard(String hash){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();

        button.setText("Join the game");
        button.setUrl(buildJoinToGameLink(hash));
        row1.add(button);
        inlineKeyboardMarkup.setKeyboard(List.of(row1));
        return inlineKeyboardMarkup;
    }

    private String addListOfPlayers(List<User> users){
        var textBuilder = new StringBuilder();
        for (int i = 0 ; i < users.size() ; i++){
            User user = users.get(i);
            textBuilder.append("%d. <a href=\"tg://user?id=%d\">%s</a>\n".formatted(i + 1 , user.getTelegramId() ,user.getFirstName()));
        }
        return textBuilder.toString();
    }

    public EditMessageText editNotEnoughUserMsg(Integer messageId , Long telegramChatId){
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(messageId);
        editMessageText.setChatId(telegramChatId);
        editMessageText.setParseMode(ParseMode.HTML);
        String txt = "Not Enough User ⚠";
        txt += "\nThe minimum number of players is <b>2</b>";
        editMessageText.setText(txt);
        return editMessageText;
    }
}
