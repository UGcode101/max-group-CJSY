package org.launchcode.threemix.repository;

import org.launchcode.threemix.model.BlockedSong;
import org.launchcode.threemix.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockedSongRepository extends JpaRepository<BlockedSong, Long> {
    List<BlockedSong> findBlockedSongsByUser(User user);
    BlockedSong findBlockedSongBySongId(String songId);
}