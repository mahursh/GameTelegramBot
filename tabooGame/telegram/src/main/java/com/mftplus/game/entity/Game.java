package com.mftplus.game.entity;

import com.mftplus.game.enums.GameStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

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

    @OneToMany(mappedBy = "game" , cascade = CascadeType.ALL)
    private List<UserGame> users;

    public void addUserGame(UserGame userGame){
        userGame.setGame(this);
        users.add(userGame);
    }
}
