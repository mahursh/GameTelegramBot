package com.mftplus.game.entity;

//import com.vladmihalcea.hibernate.type.array.ListArrayType;
//import org.hibernate.annotations.TypeDefs;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
//import org.hibernate.annotations.Type;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity(name = "cardEntity")
@Table(name = "card_tbl")

//@TypeDefs({@TypeDef(name = "list-array" , typeClass = ListArrayType.class)})
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answer;

    //@Type(type = "list-array")
    @ElementCollection
    private List<String> taboos;

    //@Type(type = "list-array")
    @ElementCollection
    private List<String> allTaboos;
}
