package com.mftplus.game.entity;

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

@Entity(name = "wordEntity")
@Table(name = "word_tbl")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    @ManyToOne
    @JoinColumn(name = "init_form_id")
    private Word initWord;

    @OneToMany(mappedBy = "initWord")
    private List<Word> forms = new ArrayList<>();


}
