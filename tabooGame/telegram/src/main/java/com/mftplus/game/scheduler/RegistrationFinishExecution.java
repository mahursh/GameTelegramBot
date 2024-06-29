package com.mftplus.game.scheduler;


import com.mftplus.game.entity.*;
import com.mftplus.game.enums.GameRole;
import com.mftplus.game.enums.WaitRoomStatus;
import com.mftplus.game.service.GameService;
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
    private final GameService gameService;
    private final TimeIsUpExecution timeIsUpExecution;
    private static final Logger logger = LoggerFactory.getLogger(TabooBot.class);

   //TODO: remove @Autowired .
    @Lazy
    @Autowired
    private RegistrationFinishExecution self;

    /*
    Self Injection Approach:

    this self will not be injected during bean initialization ,
    it will cause circular dependency .
    instead we will inject this dependency  ,once we need it . to
    tell spring about it,  we will apply @Lazy  annotation .
    now we will call the registrationFinished() methode inside  scheduleRegistrationFinish constructor
    using self.registrationFinished() .
    and note that the registrationFinished() method must be public.
    in this way spring proxy will be aware of method call and create the transaction before executing method
    */


    public void scheduleRegistrationFinish(Long telegramId){

        taskScheduler.schedule(
                () -> self.registrationFinished(telegramId),
                Instant.now().plus(1, ChronoUnit.MINUTES)
        );


    }

    public void registrationFinished(Long telegramChatId){

        WaitRoom waitRoom = waitRoomService.findAwaitingByChatId(telegramChatId); //This part is the reason we put @Transactional upon the class.

        if (waitRoom == null) return;

        if (waitRoom.getUsers().size() < 2){
            EditMessageText notEnoughUserMsg = messageBuilder.editNotEnoughUserMsg(waitRoom.getMessageId() , telegramChatId);
            messageSender.sendMessage(notEnoughUserMsg);
        }else {
            //TODO: Test It With Two Accounts.


            Game game = gameService.save(waitRoom);
            messageSender.sendNextTurnMsg(telegramChatId, game);
            timeIsUpExecution.scheduleTimeIsUp(telegramChatId);


        }

        waitRoom.setStatus(WaitRoomStatus.REGISTRATION_FINISHED);

    }


}
