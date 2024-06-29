package com.mftplus.game.service;

import com.mftplus.game.entity.*;
import com.mftplus.game.enums.CardStatus;
import com.mftplus.game.enums.GameRole;
import com.mftplus.game.enums.GameStatus;
import com.mftplus.game.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final CardService cardService;

    public Game save(WaitRoom waitRoom){
        var game = new Game();
        game.setChat(waitRoom.getChat());
        waitRoom.getUsers()
                .stream()
                .map(this :: buildUserGame)
                .forEach(game :: addUserGame);

        assignExplainer(game);
       return gameRepository.save(game);
    }



    public Card getNextCard(Game game){
        Card nextCard = cardService.getNextCard(game);
        var gameCard = new GameCard();
        gameCard.setCard(nextCard);
        game.addGameCard(gameCard);
        return nextCard;
    }



    public User assignExplainer(Game game){
        UserGame nextExplainer =
                game.getUsers()
                .stream()
                        .filter(ug -> !ug.isExplained())
                        .findFirst()
                        .orElse(null);

        if (nextExplainer != null){
            nextExplainer.setGameRole(GameRole.EXPLAINER);
            return nextExplainer.getUser();
        }
        return null;
    }

    private UserGame buildUserGame(User user){
        var userGame = new UserGame();
        userGame.setUser(user);
        return userGame;
    }

    public Game findGameInProgress(Long telegramChatId){
        return gameRepository.findByChat_telegramChatIdAndStatus(telegramChatId , GameStatus.IN_PROGRESS);
    }

    public GameCard getGameCardInProgress(Game game){
        return game.getCards()
                .stream()
                .filter(gc -> gc.getStatus().equals(CardStatus.IN_PROGRESS))
                .findFirst()
                .orElseThrow();
    }

    public UserGame whoseMessage(Long telegramId , Game game){
        return game.getUsers()
                .stream()
                .filter(ug -> ug.getUser().getTelegramId().equals(telegramId))
                .findFirst()
                .orElseThrow();
    }

    public Set<String> findTabooUsed(Card card ,String text){
        return extractWords(text.toLowerCase())
                .stream()
                .filter(w -> card.getAllTaboos().contains(w))
                .collect(Collectors.toSet());
    }

    private List<String> extractWords(String text){
        return Arrays.stream(text.split("[^a-zA-Z-]+")).toList();
    }

    public boolean isGuessed(Card card , String text){
        return extractWords(text.toLowerCase()).contains(card.getAnswer());
    }

    public void cardWasGuessed(GameCard gameCard , UserGame guesser){
        guesser.setScore(guesser.getScore() + 1) ;
        Game game = gameCard.getGame();
        UserGame explainer = getUserGameExplainer(game);
        explainer.setScore(explainer.getScore() + 1);
        gameCard.setStatus(CardStatus.COMPLETED);
    }

    public UserGame getUserGameExplainer(Game game){
        return  game.getUsers()
                .stream()
                .filter(ug -> ug.getGameRole() == GameRole.EXPLAINER)
                .findFirst()
                .orElseThrow();
    }
    public User getExplainer(Game game){
        return game.getUsers()
                .stream()
                .filter(ug -> ug.getGameRole() == GameRole.EXPLAINER)
                .findFirst()
                .map(UserGame::getUser)
                .orElseThrow();
    }

    public Game timeIsUp(Long telegramChatId) {
        Game game = findGameInProgress(telegramChatId);
        GameCard gameCard = getGameCardInProgress(game);
        gameCard.setStatus(CardStatus.TIME_IS_UP);
        UserGame userGame = getUserGameExplainer(game);
        userGame.setGameRole(GameRole.GUESSER);
        userGame.setExplained(true);
        return game;
    }
}
