package com.quipux.prueba.controller;
import com.quipux.prueba.contoller.PlayListController;
import com.quipux.prueba.entity.PlayList;
import com.quipux.prueba.service.PlayListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PlayListControllerTest {

    @InjectMocks
    private PlayListController playListController;

    @Mock
    private PlayListService playListService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddPlaylistWithSongsSuccess() {
        PlayList playlist = new PlayList();
        playlist.setNombre("MyPlaylist");

        when(playListService.createPlayListWithSongs(any())).thenReturn(new ResponseEntity<>(playlist, HttpStatus.CREATED));

        ResponseEntity<Object> response = playListController.addPlaylistWithSongs(playlist);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(playlist, response.getBody());
        verify(playListService, times(1)).createPlayListWithSongs(any());
    }

    @Test
    public void testAddPlaylistWithSongsEmptyName() {
        PlayList playlist = new PlayList();
        playlist.setNombre("");  // Lista con nombre vacío

        when(playListService.createPlayListWithSongs(any())).thenReturn(new ResponseEntity<>("El nombre de la lista no puede estar vacío.", HttpStatus.BAD_REQUEST));

        ResponseEntity<Object> response = playListController.addPlaylistWithSongs(playlist);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El nombre de la lista no puede estar vacío.", response.getBody());
        verify(playListService, times(1)).createPlayListWithSongs(any());
    }

    @Test
    public void testGetAllPlaylistsSuccess() {
        PlayList playlist = new PlayList();
        when(playListService.getAllPlaylists()).thenReturn(new ResponseEntity<>(Collections.singletonList(playlist), HttpStatus.OK));

        ResponseEntity<Object> response = playListController.getAllPlaylists();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.singletonList(playlist), response.getBody());
        verify(playListService, times(1)).getAllPlaylists();
    }

    @Test
    public void testGetAllPlaylistsEmpty() {
        when(playListService.getAllPlaylists()).thenReturn(new ResponseEntity<>("No hay listas de reproducción disponibles", HttpStatus.NOT_FOUND));

        ResponseEntity<Object> response = playListController.getAllPlaylists();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No hay listas de reproducción disponibles", response.getBody());
        verify(playListService, times(1)).getAllPlaylists();
    }

    @Test
    public void testGetPlaylistByNameSuccess() {
        String listName = "MyPlaylist";
        PlayList playlist = new PlayList();
        when(playListService.getPlaylistByName(listName)).thenReturn(new ResponseEntity<>(playlist, HttpStatus.OK));

        ResponseEntity<Object> response = playListController.getPlaylistByName(listName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(playlist, response.getBody());
        verify(playListService, times(1)).getPlaylistByName(listName);
    }

    @Test
    public void testGetPlaylistByNameNotFound() {
        String listName = "NonExistentPlaylist";
        when(playListService.getPlaylistByName(listName)).thenReturn(new ResponseEntity<>("No se encontró la lista de reproducción", HttpStatus.NOT_FOUND));

        ResponseEntity<Object> response = playListController.getPlaylistByName(listName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No se encontró la lista de reproducción", response.getBody());
        verify(playListService, times(1)).getPlaylistByName(listName);
    }

    @Test
    public void testDeletePlaylistSuccess() {
        String listName = "MyPlaylist";
        when(playListService.deletePlaylist(listName)).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        ResponseEntity<Object> response = playListController.deletePlaylist(listName);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(playListService, times(1)).deletePlaylist(listName);
    }

    @Test
    public void testDeletePlaylistNotFound() {
        String listName = "NonExistentPlaylist";
        when(playListService.deletePlaylist(listName)).thenReturn(new ResponseEntity<>("No se encontró la lista de reproducción", HttpStatus.NOT_FOUND));

        ResponseEntity<Object> response = playListController.deletePlaylist(listName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No se encontró la lista de reproducción", response.getBody());
        verify(playListService, times(1)).deletePlaylist(listName);
    }
}
