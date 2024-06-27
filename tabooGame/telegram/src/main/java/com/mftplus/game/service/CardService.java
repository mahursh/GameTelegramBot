package com.mftplus.game.service;

import com.mftplus.game.entity.Card;
import com.mftplus.game.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository repository;
    private final WordService wordService;

    public Card save (List<String> answerAndTaboos){
        var card = new Card();
        card.setAnswer(answerAndTaboos.get(0));
        card.setTaboos(answerAndTaboos.subList(1 ,answerAndTaboos.size()));

        card.setAllTaboos(
                answerAndTaboos
                        .stream()
                        .flatMap(w -> wordService.getAllWordForm(w).stream())
                        .toList()
        );

        return repository.save(card);
    }

    public boolean isTableEmpty(){
        return repository.count() == 0;
    }
}
