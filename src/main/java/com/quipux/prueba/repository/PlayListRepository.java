package com.quipux.prueba.repository;


import com.quipux.prueba.entity.PlayList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayListRepository extends JpaRepository<PlayList, Long> {
    Optional<PlayList> findByNombre(String nombre);



}

