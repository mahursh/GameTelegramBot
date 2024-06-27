package com.mftplus.game.service;

import com.mftplus.game.entity.Chat;
import com.mftplus.game.entity.User;
import com.mftplus.game.entity.WaitRoom;
import com.mftplus.game.repository.ChatRepository;
import com.mftplus.game.repository.UserRepository;
import com.mftplus.game.repository.WaitRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WaitRoomService {

    private final WaitRoomRepository waitRoomRepository ;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;




    public WaitRoom save(Long telegramChatId , String hash){
        var waitRoom = new WaitRoom();
        Chat chat = chatRepository.findByTelegramChatId(telegramChatId);
        waitRoom.setChat(chat);
        waitRoom.setHash(hash);
        return waitRoomRepository.save(waitRoom);
    }

    public WaitRoom join(String hash , Long telegramId){

        WaitRoom waitRoom = waitRoomRepository.findByHash(hash);
        User user = userRepository.findByTelegramId(telegramId);
        waitRoom.getUsers().add(user);
        return waitRoom;
    }
}
