package com.mftplus.game.service;


import com.mftplus.game.entity.User;
import com.mftplus.game.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User save(User user){
        User existedUser = repository.findByTelegramId(user.getTelegramId());
        if (existedUser == null){
            existedUser = repository.save(user);

        }
        return existedUser;
    }
}
