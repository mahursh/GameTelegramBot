package com.mftplus.game.service;

import com.mftplus.game.entity.Card;
import com.mftplus.game.entity.Game;
import com.mftplus.game.entity.GameCard;
import com.mftplus.game.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    private final static Random NEXT_CARD_ID = new Random();

    private final CardRepository cardRepository;
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

        return cardRepository.save(card);
    }

    public boolean isTableEmpty(){
        return cardRepository.count() == 0;
    }

    public Card getNextCard(Game game){

        Set<Long> usedCardIds = getCardIds(game);
        List<Long> allIds = cardRepository.getAllIds();
        Long nextCardId;
        do {
            int index = NEXT_CARD_ID.nextInt(allIds.size());
            nextCardId = allIds.get(index);
        }while (usedCardIds.contains(nextCardId));

        return cardRepository.findById(nextCardId).get();
    }

    private Set<Long> getCardIds(Game game){
        return game.getCards()
                .stream()
                .map(GameCard :: getCard)
                .map(Card :: getId)
                .collect(Collectors.toSet());
    }
}
