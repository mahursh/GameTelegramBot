package com.mftplus.game.configs;

import com.mftplus.game.entity.Chat;
import com.mftplus.game.entity.User;
import com.mftplus.game.enums.WaitRoomStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity(name = "waitRoomEntity")
@Table(name = "wait_room_tbl")
public class WaitRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer messageId;

    private String hash;

    private WaitRoomStatus status;

    @ManyToOne
    private Chat chat;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "wait_room_user" ,
            joinColumns = @JoinColumn(name = "wait_room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users = new ArrayList<>();
}
