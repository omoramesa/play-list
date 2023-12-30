package com.quipux.prueba.contoller;

import com.quipux.prueba.entity.PlayList;
import com.quipux.prueba.service.PlayListService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/playLists")
public class PlayListController {
    private static final Logger logger = LoggerFactory.getLogger(PlayListController.class);
    @Autowired
    private PlayListService playListService;

    /**
     * Método para crear una nueva lista de reproducción
     * @param playlist
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<Object> addPlaylistWithSongs(@RequestBody PlayList playlist) {
        try {
            // Se valida que el nombre de la lista no esté vacía
            if (playlist == null || playlist.getNombre() == null || playlist.getNombre().trim().isEmpty()) {
                return new ResponseEntity<>("El nombre de la lista no puede estar vacío.", HttpStatus.BAD_REQUEST);
            }

            ResponseEntity<Object> createdPlaylist = playListService.createPlayListWithSongs(playlist);
            logger.error("Se creo La lista con exito");
            return new ResponseEntity<>(createdPlaylist, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear la lista de reproducción", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Método para consultar todas las listas de reproducción
     * @return
     */
    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllPlaylists() {
        try {
            ResponseEntity<Object> response = playListService.getAllPlaylists();

            if (response.getStatusCode() != HttpStatus.OK) {
                // Imprimir la respuesta en caso de error
                logger.error("Error en la respuesta: {}", response.getBody());
            }

            return response;

        } catch (Exception e) {
            // Imprimir la traza de la excepción
            logger.error("Error en la respuesta: {}", e);
            return new ResponseEntity<>("Error al recuperar las listas de reproducción", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Método para consultar una lista de reproducción por nombre
     * @param listName
     * @return
     */
    @GetMapping("/{listName}")
    public ResponseEntity<Object> getPlaylistByName(@PathVariable String listName) {
        try {
            ResponseEntity<Object> playlist = playListService.getPlaylistByName(listName);

            if (playlist == null) {
                return new ResponseEntity<>("No se encontró la lista de reproducción", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(playlist, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al recuperar la lista de reproducción por nombre", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Método para eliminar una lista de reproducción por nombre
     * @param listName
     * @return
     */
    @DeleteMapping("/{listName}")
    public ResponseEntity<Object> deletePlaylist(@PathVariable String listName) {
        try {
            ResponseEntity<Object> response = playListService.deletePlaylist(listName);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                return new ResponseEntity<>("Lista de reproducción eliminada con éxito", HttpStatus.NO_CONTENT);
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new ResponseEntity<>("No se encontró la lista de reproducción", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("Error al eliminar la lista de reproducción", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar la lista de reproducción", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
