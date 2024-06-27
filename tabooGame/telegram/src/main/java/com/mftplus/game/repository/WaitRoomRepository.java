package com.mftplus.game.repository;

import com.mftplus.game.entity.WaitRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitRoomRepository extends JpaRepository<WaitRoom , Long> {

    WaitRoom findByHash(String hash);
}
