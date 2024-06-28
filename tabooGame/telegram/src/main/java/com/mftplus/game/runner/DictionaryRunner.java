package com.mftplus.game.runner;

import com.mftplus.game.entity.Word;
import com.mftplus.game.service.WordService;
import com.mftplus.game.telegramExecutor.TabooBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Order(1)
@Component
@RequiredArgsConstructor
public class DictionaryRunner implements ApplicationRunner {
    private final WordService service;
    private static final Logger logger = LoggerFactory.getLogger(TabooBot.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (service.isTableEmpty()){
            Resource resource = new ClassPathResource("/files/dictionary.txt");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))){
                String line;
                while ((line = br.readLine()) != null){
                    insertWord(line);
                }

            }catch (IOException e){
                logger.error("Error Reading File: " + e.getMessage());
            }


        }

    }

    private void insertWord(String line){
        String[] words = line.split(",");
        Word initWord = service.save(words[0]);
        for (int i = 1 ;i < words.length ; i++){
            String word = words[i];
            service.save(word , initWord);
        }
    }
}
