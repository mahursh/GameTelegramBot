package com.mftplus.game.service;

import com.mftplus.game.entity.Word;
import com.mftplus.game.repository.WordRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class WordService {

    private final WordRepository repository;

    public boolean isTableEmpty() {
        return repository.count() == 0;
    }

    public Word save(String word) {
        return repository.save(build(word));
    }


    public Word save(String word, Word initWord) {
        Word entity = build(word);
        entity.setInitWord(initWord);
        return repository.save(entity);
    }


    public Word build(String word) {
        var entity = new Word();
        entity.setWord(word);
        return entity;
    }

    public List<String> getAllWordForm(String word) {

        return getOrCreate(word)
                .stream()
                .map(w -> w.getInitWord() == null ? w : w.getInitWord())
                .flatMap(w -> Stream.concat(Stream.of(w), w.getForms().stream()))
                .map(Word::getWord)
                .toList();

    }

    public List<Word> getOrCreate(String word) {
        List<Word> words = repository.findByWord(word);
        if (words.isEmpty()) {
            return List.of(save(word));
        }

        return words;
    }
}
