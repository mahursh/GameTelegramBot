package com.mftplus.game.repository;

import com.mftplus.game.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat , Long> {

    Chat findByTelegramChatId(Long telegramChatId);
}
