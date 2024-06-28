package com.mftplus.game.repository;

import com.mftplus.game.entity.Card;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card , Long> {

    @Query("SELECT c.id FROM cardEntity c")
    List<Long> getAllIds();
}
