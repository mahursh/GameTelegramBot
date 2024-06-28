package com.mftplus.game.repository;

import com.mftplus.game.entity.WaitRoom;
import com.mftplus.game.enums.WaitRoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitRoomRepository extends JpaRepository<WaitRoom , Long> {

    WaitRoom findByHash(String hash);

    //TODO: Thiiiis iissss hoooow  weeee dooo iiittttt !!!!!!  :)))))
    WaitRoom findByChat_telegramChatIdAndStatus(Long telegramChatId , WaitRoomStatus status);
}
