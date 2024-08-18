package org.launchcode.threemix.repository;

import org.launchcode.threemix.model.BlockedSongStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockedSongStatsRepository extends JpaRepository<BlockedSongStats, Long> {
}