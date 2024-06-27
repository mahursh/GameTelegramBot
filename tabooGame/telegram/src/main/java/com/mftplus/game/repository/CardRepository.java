package com.mftplus.game.repository;

import com.mftplus.game.entity.Card;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card , Long> {
}
