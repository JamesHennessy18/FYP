package com.example.Repo;

import com.example.Model.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Integer> {
    List<SearchHistory> findByUserIdOrderByTimestampDesc(Long userId);
}
