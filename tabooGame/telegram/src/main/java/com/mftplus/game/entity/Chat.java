package com.mftplus.game.entity;

import com.mftplus.game.enums.ChatBotStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity(name = "chatEntity")
@Table(name = "chat_tbl")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String Title;
    private Long telegramChatId;

    @Enumerated(EnumType.STRING)
    private ChatBotStatus chatBotStatus;
}
