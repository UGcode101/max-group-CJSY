package org.launchcode.threemix.repository;

import org.launchcode.threemix.model.BlockedArtist;
import org.launchcode.threemix.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockedArtistRepository extends JpaRepository<BlockedArtist, Long> {
    List<BlockedArtist> findBlockedArtistByUser(User user);
}