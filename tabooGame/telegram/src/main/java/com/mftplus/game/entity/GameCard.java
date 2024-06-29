package com.mftplus.game.entity;

import com.mftplus.game.enums.CardStatus;
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

@Entity(name = "gameCardEntity")
@Table(name = "game_card_tbl")
public class GameCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Card card;

    @ManyToOne
    private Game game;

    @Enumerated(EnumType.STRING)
    private CardStatus status = CardStatus.IN_PROGRESS;
}
