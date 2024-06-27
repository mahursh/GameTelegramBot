package com.mftplus.game.service;

import com.mftplus.game.entity.Chat;
import com.mftplus.game.entity.WaitRoom;
import com.mftplus.game.repository.ChatRepository;
import com.mftplus.game.repository.WaitRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitRoomService {

    private final WaitRoomRepository waitRoomRepository ;
    private final ChatRepository chatRepository;




    public WaitRoom save(Long telegramChatId , String hash){
        var waitRoom = new WaitRoom();
        Chat chat = chatRepository.findByTelegramChatId(telegramChatId);
        waitRoom.setChat(chat);
        waitRoom.setHash(hash);
        return waitRoomRepository.save(waitRoom);
    }
}
