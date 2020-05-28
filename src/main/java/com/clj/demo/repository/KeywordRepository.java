package com.clj.demo.repository;
import com.clj.demo.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Integer> {
    Optional<Keyword> findKeywordByWord(String word);
}
