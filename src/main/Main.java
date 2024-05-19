package main;

import dataAccessObject.InMemorySongDAO;
import model.Playlist;
import model.Song;
import service.MusicLibraryService;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InMemorySongDAO songDao = new InMemorySongDAO();
        MusicLibraryService service = new MusicLibraryService(songDao);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nWelcome to the Music Library API");
            System.out.println("1. Add Song");
            System.out.println("2. List Songs");
            System.out.println("3. Remove Song");
            System.out.println("4. Update Song");
            System.out.println("5. Create Playlist");
            System.out.println("6. Add Song to Playlist");
            System.out.println("7. Remove Song from Playlist");
            System.out.println("8. View Playlist");
            System.out.println("9. Delete Playlist");
            System.out.println("10. Search Songs");
            System.out.println("11. Search Playlists");
            System.out.println("12. Shuffle Play");
            System.out.println("13. Exit");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    System.out.print("Enter song title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter artist: ");
                    String artist = scanner.nextLine();
                    System.out.print("Enter genre: ");
                    String genre = scanner.nextLine();
                    service.addSong(new Song(title, artist, genre));
                    System.out.println("Song added successfully!");
                    break;

                case 2:
                    System.out.println("Songs in Library:");
                    for (Song song : service.getAllSongs()) {
                        System.out.println(song);
                    }
                    break;

                case 3:
                    System.out.print("Enter song title to remove: ");
                    String removeTitle = scanner.nextLine();
                    if (service.removeSongByTitle(removeTitle)) {
                        System.out.println("Song removed successfully!");
                    } else {
                        System.out.println("Song not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter current song title: ");
                    String oldTitle = scanner.nextLine();
                    System.out.print("Enter new song title: ");
                    String newTitle = scanner.nextLine();
                    System.out.print("Enter new artist: ");
                    String newArtist = scanner.nextLine();
                    System.out.print("Enter new genre: ");
                    String newGenre = scanner.nextLine();
                    if (service.updateSong(oldTitle, newTitle, newArtist, newGenre)) {
                        System.out.println("Song updated successfully!");
                    } else {
                        System.out.println("Song update failed.");
                    }
                    break;

                case 5:
                    System.out.print("Enter playlist name: ");
                    String playlistName = scanner.nextLine();
                    service.createPlaylist(playlistName);
                    System.out.println("Playlist created successfully!");
                    break;

                case 6:
                    System.out.print("Enter playlist name to add song: ");
                    String playlistToAdd = scanner.nextLine();
                    System.out.print("Enter song title to add to playlist: ");
                    String songTitleToAdd = scanner.nextLine();
                    Song songToAdd = service.findSongByTitle(songTitleToAdd);
                    if (songToAdd != null) {
                        service.addSongToPlaylist(playlistToAdd, songToAdd);
                        System.out.println("Song added to playlist successfully!");
                    } else {
                        System.out.println("Song not found.");
                    }
                    break;

                case 7:
                    System.out.print("Enter playlist name to remove song: ");
                    String playlistToRemove = scanner.nextLine();
                    System.out.print("Enter song title to remove from playlist: ");
                    String songTitleToRemove = scanner.nextLine();
                    Song songToRemove = service.findSongByTitle(songTitleToRemove);
                    if (songToRemove != null && service.removeSongFromPlaylist(playlistToRemove, songToRemove)) {
                        System.out.println("Song removed from playlist successfully!");
                    } else {
                        System.out.println("Song not found in playlist.");
                    }
                    break;

                case 8:
                    System.out.print("Enter playlist name to view: ");
                    String playlistToView = scanner.nextLine();
                    System.out.println("Songs in " + playlistToView + " playlist:");
                    for (Song song : service.getSongsInPlaylist(playlistToView)) {
                        System.out.println(song);
                    }
                    break;

                case 9:
                    System.out.print("Enter playlist name to delete: ");
                    String playlistToDelete = scanner.nextLine();
                    if (service.deletePlaylist(playlistToDelete)) {
                        System.out.println("Playlist deleted successfully!");
                    } else {
                        System.out.println("Playlist not found.");
                    }
                    break;

                case 10:
                    System.out.print("Enter search query for songs: ");
                    String queryForSongs = scanner.nextLine();
                    List<Song> foundSongs = service.searchSongs(queryForSongs);
                    System.out.println("Found Songs:");
                    foundSongs.forEach(song -> System.out.println(song));
                    break;

                case 11:
                    System.out.print("Enter search query for playlists: ");
                    String queryForPlaylists = scanner.nextLine();
                    List<Playlist> foundPlaylists = service.searchPlaylists(queryForPlaylists);
                    System.out.println("Found Playlists:");
                    foundPlaylists.forEach(playlist -> System.out.println(playlist.getName()));
                    break;

                case 12:
                    System.out.println("Shuffled Songs:");
                    List<Song> shuffledSongs = service.getShuffledSongs();
                    shuffledSongs.forEach(song -> System.out.println(song));
                    break;

                case 13:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
