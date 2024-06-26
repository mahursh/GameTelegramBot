package com.mftplus.game.entity;

import com.mftplus.game.enums.GameRole;
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

@Entity(name = "userGameEntity")
@Table(name = "user_game_tbl")
public class UserGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private  User user;

    @ManyToOne
    private Game game;

    @Enumerated(EnumType.STRING)
    private GameRole gameRole ;

    private int score;

    private boolean explained;
}
