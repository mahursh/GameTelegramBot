package com.mftplus.game.repository;

import com.mftplus.game.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User , Long> {

    User findByTelegramId(Long telegramId);
}
