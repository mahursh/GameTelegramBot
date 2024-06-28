package com.mftplus.game.scheduler;


import com.mftplus.game.entity.WaitRoom;
import com.mftplus.game.enums.WaitRoomStatus;
import com.mftplus.game.service.WaitRoomService;
import com.mftplus.game.telegramExecutor.TabooBot;
import com.mftplus.game.telegramExecutor.message.MessageBuilder;
import com.mftplus.game.telegramExecutor.message.MessageSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@Transactional
@RequiredArgsConstructor
public class RegistrationFinishExecution {

    private final ThreadPoolTaskScheduler taskScheduler;
    private final WaitRoomService waitRoomService;
    private final MessageBuilder messageBuilder;
    private final MessageSender messageSender;
    private static final Logger logger = LoggerFactory.getLogger(TabooBot.class);
    @Lazy
    @Autowired
    private RegistrationFinishExecution self;

    public void scheduleRegistrationFinish(Long telegramId){

        logger.warn("INSIDE scheduleRegistrationFinish METHOD  - START!");
        taskScheduler.schedule(
                () -> self.registrationFinished(telegramId),
                Instant.now().plus(1, ChronoUnit.MINUTES)
        );

        logger.warn("INSIDE scheduleRegistrationFinish METHOD  - END!");

    }

    public void registrationFinished(Long telegramChatId){
        logger.warn("INSIDE registrationFinished METHOD !");

        logger.error("bef");
        //TODO: this part seems to cause the error !
        WaitRoom waitRoom = waitRoomService.findAwaitingByChatId(telegramChatId);
        logger.error("aft");

        if (waitRoom == null) return;
        logger.info("waitRoom Founded: {}" , waitRoom.toString());

        if (waitRoom.getUsers().size() < 2){
            EditMessageText notEnoughUserMsg = messageBuilder.editNotEnoughUserMsg(waitRoom.getMessageId() , telegramChatId);
            messageSender.sendMessage(notEnoughUserMsg);
        }else {
            SendMessage gameStartedMsg = messageBuilder.buildTextMsg(telegramChatId , "The Game Has Started!");
            messageSender.sendMessage(gameStartedMsg);
        }

        waitRoom.setStatus(WaitRoomStatus.REGISTRATION_FINISHED);


    }
}
