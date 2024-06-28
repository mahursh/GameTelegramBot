package com.mftplus.game.runner;

import com.mftplus.game.service.CardService;
import com.mftplus.game.telegramExecutor.TabooBot;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Order(2)
@Component
@RequiredArgsConstructor
public class TabooCardRunner implements ApplicationRunner {
    private final CardService service;
    private static final Logger logger = LoggerFactory.getLogger(TabooBot.class);
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (service.isTableEmpty()){
            ClassPathResource resource = new ClassPathResource("/files/taboo-cards.txt");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))){
                String line;
                while ((line = br.readLine()) != null){
                    List<String> answerAndTaboos = Arrays.stream(line.split(",")).toList();
                    service.save(answerAndTaboos);
                }

            }catch (IOException e){
                logger.error("Error Reading File: " + e.getMessage());
            }
        }

    }
}
