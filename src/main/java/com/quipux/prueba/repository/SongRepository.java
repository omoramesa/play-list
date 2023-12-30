package com.quipux.prueba.repository;

import com.quipux.prueba.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
}