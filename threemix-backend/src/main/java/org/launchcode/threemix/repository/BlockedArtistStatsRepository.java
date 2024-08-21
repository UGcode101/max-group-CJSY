package org.launchcode.threemix.repository;

import org.launchcode.threemix.model.BlockedArtistStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockedArtistStatsRepository extends JpaRepository<BlockedArtistStats, Long> {
}