package com.quipux.prueba.service;

import com.quipux.prueba.entity.PlayList;
import com.quipux.prueba.entity.Song;
import com.quipux.prueba.repository.PlayListRepository;
import com.quipux.prueba.repository.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PlayListServiceTest {

    @InjectMocks
    private PlayListService playListService;

    @Mock
    private PlayListRepository playListRepository;

    @Mock
    private SongRepository songRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePlayListWithSongsSuccess() {
        PlayList playlist = new PlayList();
        playlist.setNombre("MyPlaylist");
        playlist.setCanciones(Collections.singletonList(new Song()));

        when(playListRepository.save(any())).thenReturn(playlist);
        when(songRepository.save(any())).thenReturn(new Song());

        ResponseEntity<Object> response = playListService.createPlayListWithSongs(playlist);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(playlist, response.getBody());
        verify(playListRepository, times(1)).save(any());
        verify(songRepository, times(1)).save(any());
    }

    @Test
    public void testCreatePlayListWithSongsEmptyName() {
        PlayList playlist = new PlayList();
        playlist.setNombre("");  // Lista con nombre vacío

        ResponseEntity<Object> response = playListService.createPlayListWithSongs(playlist);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El nombre de la lista no puede estar vacío.", response.getBody());
        verify(playListRepository, never()).save(any());
        verify(songRepository, never()).save(any());
    }

    @Test
    public void testGetAllPlaylistsSuccess() {
        when(playListRepository.findAll()).thenReturn(Collections.singletonList(new PlayList()));

        ResponseEntity<Object> response = playListService.getAllPlaylists();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.singletonList(new PlayList()), response.getBody());
        verify(playListRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllPlaylistsEmpty() {
        when(playListRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<Object> response = playListService.getAllPlaylists();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No hay listas de reproducción disponibles", response.getBody());
        verify(playListRepository, times(1)).findAll();
    }

    @Test
    public void testGetPlaylistByNameSuccess() {
        String listName = "MyPlaylist";
        PlayList playlist = new PlayList();
        playlist.setNombre(listName);

        when(playListRepository.findByNombre(listName)).thenReturn(Optional.of(playlist));

        ResponseEntity<Object> response = playListService.getPlaylistByName(listName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(playlist, response.getBody());
        verify(playListRepository, times(1)).findByNombre(listName);
    }

    @Test
    public void testGetPlaylistByNameNotFound() {
        String listName = "NonExistentPlaylist";

        when(playListRepository.findByNombre(listName)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = playListService.getPlaylistByName(listName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No se encontró la lista de reproducción", response.getBody());
        verify(playListRepository, times(1)).findByNombre(listName);
    }

    @Test
    public void testDeletePlaylistSuccess() {
        String listName = "MyPlaylist";
        PlayList playlist = new PlayList();
        playlist.setNombre(listName);

        when(playListRepository.findByNombre(listName)).thenReturn(Optional.of(playlist));

        ResponseEntity<Object> response = playListService.deletePlaylist(listName);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(playListRepository, times(1)).findByNombre(listName);
        verify(playListRepository, times(1)).delete(playlist);
    }

    @Test
    public void testDeletePlaylistNotFound() {
        String listName = "NonExistentPlaylist";

        when(playListRepository.findByNombre(listName)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = playListService.deletePlaylist(listName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No se encontró la lista de reproducción", response.getBody());
        verify(playListRepository, times(1)).findByNombre(listName);
        verify(playListRepository, never()).delete(any());
    }
}
