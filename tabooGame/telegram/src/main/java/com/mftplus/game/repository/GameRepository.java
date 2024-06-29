package com.mftplus.game.repository;

import com.mftplus.game.entity.Game;
import com.mftplus.game.enums.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game , Long> {

Game findByChat_telegramChatIdAndStatus(Long telegramChatId , GameStatus status);
}
