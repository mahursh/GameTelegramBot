package com.mftplus.game.service;


import com.mftplus.game.entity.Chat;
import com.mftplus.game.enums.ChatBotStatus;
import com.mftplus.game.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository repository;

    public Chat save (Chat chat){
        Chat existedChat = repository.findByTelegramChatId(chat.getTelegramChatId());
        if (existedChat == null) {
            return repository.save(chat);
        } else {
            existedChat.setChatBotStatus(chat.getChatBotStatus());
            return repository.save(existedChat);
        }
    }


    public Chat convert(ChatMemberUpdated chatMemberUpdated) {
        var chat = new Chat();
        chat.setTelegramChatId(chatMemberUpdated.getChat().getId());
        chat.setTitle(chatMemberUpdated.getChat().getTitle());
        ChatMember newChatMember = chatMemberUpdated.getNewChatMember();
        if (newChatMember != null) {
            chat.setChatBotStatus(convertStatus(newChatMember.getStatus()));
        }
        return chat;
    }

    private ChatBotStatus convertStatus(String status) {
        return switch (status) {
            case "administrator" -> ChatBotStatus.ADMIN;
            case "kicked" -> ChatBotStatus.KICKED;
            default ->  ChatBotStatus.MEMBER;
        };
    }
}
