package com.quipux.prueba.service;

import com.quipux.prueba.entity.PlayList;
import com.quipux.prueba.entity.Song;
import com.quipux.prueba.repository.PlayListRepository;
import com.quipux.prueba.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayListService {

    @Autowired
    private PlayListRepository playListRepository;

    @Autowired
    private SongRepository songRepository;

    public ResponseEntity<Object> createPlayListWithSongs(PlayList playlist) {
        try {
            if (playlist == null || playlist.getNombre() == null || playlist.getNombre().trim().isEmpty()) {
                return new ResponseEntity<>("El nombre de la lista no puede estar vacío.", HttpStatus.BAD_REQUEST);
            }
            // Validar que el nombre de la lista sea único
            if (isPlaylistNameUnique(playlist.getNombre())) {
                // Guardar la lista de reproducción
                PlayList savedPlaylist = playListRepository.save(playlist);

                // Asignar las canciones a la lista de reproducción y guardarlas
                if (playlist.getCanciones() != null && !playlist.getCanciones().isEmpty()) {
                    for (Song song : playlist.getCanciones()) {
                        song.setPlaylist(savedPlaylist);
                        songRepository.save(song);
                    }
                }

                return new ResponseEntity<>(savedPlaylist, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("El nombre de la lista ya existe. Por favor, elige otro.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear la lista de reproducción con canciones.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private boolean isPlaylistNameUnique(String playlistName) {
        Optional<PlayList> existingPlaylist = playListRepository.findByNombre(playlistName);
        return existingPlaylist.isEmpty();
    }
    public ResponseEntity<Object> getAllPlaylists() {
        try {
            List<PlayList> playlists = playListRepository.findAll();

            if (playlists.isEmpty()) {
                return new ResponseEntity<>("No hay listas de reproducción disponibles", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(playlists, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace(); // Esto imprime la traza de la excepción en la consola (puedes personalizarlo según tus necesidades)
            return new ResponseEntity<>("Error al recuperar las listas de reproducción", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<Object> getPlaylistByName(String listName) {
        try {
            Optional<PlayList> playlist = playListRepository.findByNombre(listName);

            if (!playlist.isPresent()) {
                return new ResponseEntity<>("No se encontró la lista de reproducción", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(playlist.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al recuperar la lista de reproducción por nombre", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    public ResponseEntity<Object> deletePlaylist(String listName) {
        try {
            Optional<PlayList> playlist = playListRepository.findByNombre(listName);

            if (!playlist.isPresent()) {
                return new ResponseEntity<>("No se encontró la lista de reproducción", HttpStatus.NOT_FOUND);
            }

            playListRepository.delete(playlist.get());
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar la lista de reproducción", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
