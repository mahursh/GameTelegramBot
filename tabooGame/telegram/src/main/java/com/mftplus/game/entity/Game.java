package com.mftplus.game.entity;

import com.mftplus.game.enums.GameStatus;
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

@Entity(name = "gameEntity")
@Table(name = "game_tbl")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GameStatus status = GameStatus.IN_PROGRESS;

    @ManyToOne
    private  Chat chat ;
}
