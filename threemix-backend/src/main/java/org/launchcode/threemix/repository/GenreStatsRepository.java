package org.launchcode.threemix.repository;

import org.launchcode.threemix.model.GenreStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreStatsRepository extends JpaRepository<GenreStats, Long> {
}