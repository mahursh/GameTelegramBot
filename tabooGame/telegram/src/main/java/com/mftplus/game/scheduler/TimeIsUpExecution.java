package com.mftplus.game.scheduler;

import com.mftplus.game.entity.Game;
import com.mftplus.game.entity.User;
import com.mftplus.game.enums.GameStatus;
import com.mftplus.game.service.GameService;
import com.mftplus.game.telegramExecutor.message.MessageBuilder;
import com.mftplus.game.telegramExecutor.message.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@Transactional
@RequiredArgsConstructor
public class TimeIsUpExecution {
    private final GameService gameService;
    private final MessageBuilder messageBuilder;
    private final MessageSender messageSender;
    private final ThreadPoolTaskScheduler taskScheduler;


    @Lazy
    @Autowired
    private TimeIsUpExecution self;


    public void timeIsUpHandler(Long telegramChatId) {
        Game game = gameService.timeIsUp(telegramChatId);
        User nextUser = gameService.assignExplainer(game);
        if (nextUser == null) {
            SendMessage resultMsg = messageBuilder.buildGameResultMsg(telegramChatId, game);
            messageSender.sendMessage(resultMsg);
            game.setStatus(GameStatus.TIME_IS_UP);
        } else {
            messageSender.sendNextTurnMsg(telegramChatId, game);
            scheduleTimeIsUp(telegramChatId);
        }
    }

    public void scheduleTimeIsUp(Long telegramChatId) {

        taskScheduler.schedule(
                () -> self.timeIsUpHandler(telegramChatId),
                Instant.now().plus(1 , ChronoUnit.MINUTES)
        );
    }
}
