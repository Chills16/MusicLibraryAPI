package local.musiclibrary.main;

// Import necessary classes from other packages to use them in this file.
import local.musiclibrary.dao.InMemorySongDAO;
import local.musiclibrary.model.Playlist;
import local.musiclibrary.model.Song;
import local.musiclibrary.service.MusicLibraryService;
import local.musiclibrary.model.StorageManager;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * This class serves as the entry point for the music library application.
 * It utilizes a console-based interface to interact with users, allowing them
 * to manage songs and playlists through a series of options displayed in a menu.
 */
@SuppressWarnings("all")
public class Main {
    // Scanner object for reading input from the console.
    private static final Scanner scanner = new Scanner(System.in);

    // Main method where the program execution begins.
    public static void main(String[] args) {
        // An instance of InMemorySongDAO to manage songs in memory.
        InMemorySongDAO songDao = new InMemorySongDAO();
        // An instance of MusicLibraryService to handle business logic related to songs and playlists.
        MusicLibraryService service = new MusicLibraryService(songDao);

        // Loading playlists from storage upon startup.
        List<Playlist> loadedPlaylists = StorageManager.loadData(songDao);
        if (loadedPlaylists != null && !loadedPlaylists.isEmpty()) {
            service.setPlaylists(loadedPlaylists);
            System.out.println("Loaded playlists: " + loadedPlaylists.size());
        } else {
            System.out.println("No playlists loaded.");
        }

        // Main loop to process user commands until they choose to exit.
        while (true) {
            displayMenu();
            try {
                int option = scanner.nextInt();
                scanner.nextLine();  // Consume the newline character after reading an integer.
                if (option < 1 || option > 11) {
                    System.out.println("Please enter a valid option (1-11).");
                    continue;
                }
                if (!handleUserOption(option, service)) {
                    break;  // Break the loop if the option is to exit.
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine();  // Consume the rest of the input line to clear invalid input.
            }
        }
    }

    /**
     * Displays the main menu options to the user.
     */
    private static void displayMenu() {
        System.out.println("\nWelcome to the Music Library API");
        System.out.println("1. Add a Song");
        System.out.println("2. List Songs");
        System.out.println("3. Remove a Song");
        System.out.println("4. Create a Playlist");
        System.out.println("5. Add a Song to a Playlist");
        System.out.println("6. Remove a Song from a Playlist");
        System.out.println("7. View a Playlist");
        System.out.println("8. Delete a Playlist");
        System.out.println("9. Search Songs");
        System.out.println("10. Search Playlists");
        System.out.println("11. Exit");
        System.out.print("Choose an option: ");
    }

    /**
     * Handles user input for choosing options from the main menu.
     *
     * @param option The user-selected option.
     * @param service The music library service that will execute actions based on the option.
     * @return boolean Indicates whether the program should continue running (true) or exit (false).
     */
    private static boolean handleUserOption(int option, MusicLibraryService service) {
        switch (option) {
            case 1:
                addSong(service);
                break;
            case 2:
                listSongs(service);
                break;
            case 3:
                removeSong(service);
                break;
            case 4:
                createPlaylist(service);
                break;
            case 5:
                addSongToPlaylist(service);
                break;
            case 6:
                removeSongFromPlaylist(service);
                break;
            case 7:
                viewPlaylist(service);
                break;
            case 8:
                deletePlaylist(service);
                break;
            case 9:
                searchSongs(service);
                break;
            case 10:
                searchPlaylists(service);
                break;
            case 11:
                System.out.println("Exiting...");
                StorageManager.saveData(service.getPlaylists());
                return false; // Stop running after exit
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
        return true;
    }

    /**
     * Prompts the user for song details and adds a new song to the library.
     * This function queries the user for the title, artist, and optional genre of a song,
     * then uses the MusicLibraryService to add the song to the data store.
     *
     * @param service The music library service instance handling the logic.
     */
    private static void addSong(MusicLibraryService service) {
        String title = promptForInput("Enter the song title: ");
        String artist = promptForInput("Enter the artist: ");
        String genre = promptForInput("Enter the genre (optional, press ENTER to skip): ");
        service.addSong(title, artist, genre, scanner);
        System.out.println("Song added successfully!");
    }

    /**
     * Lists all songs currently stored in the music library.
     * Retrieves the songs from the service and prints each one.
     *
     * @param service The music library service instance handling the logic.
     */
    private static void listSongs(MusicLibraryService service) {
        System.out.println("Songs in Library:");
        service.getAllSongs().forEach(song -> System.out.println(song));
    }

    /**
     * Removes a specific song from the music library.
     * Prompts the user to enter the title of the song they wish to remove and
     * attempts to delete it using the MusicLibraryService.
     *
     * @param service The music library service instance handling the logic.
     */
    private static void removeSong(MusicLibraryService service) {
        String title = promptForInput("Enter the song title to remove: ");
        if (service.removeSong(title)) {
            System.out.println("Song removed successfully!");
        } else {
            System.out.println("Song not found.");
        }
    }

    /**
     * Creates a new playlist within the music library.
     * Queries the user for the name of the new playlist and uses the MusicLibraryService to create it.
     *
     * @param service The music library service instance handling the logic.
     */
    private static void createPlaylist(MusicLibraryService service) {
        String name = promptForInput("Enter the playlist name: ");
        service.createPlaylist(name);
        System.out.println("Playlist created successfully!");
    }

    /**
     * Adds a song to a specified playlist.
     * The user is prompted for the playlist's name and the standardized name of the song to add.
     * The service is then called to add the song to the specified playlist.
     *
     * @param service The music library service instance handling the logic.
     */
    private static void addSongToPlaylist(MusicLibraryService service) {
        String playlistName = promptForInput("Enter the playlist name: ");
        String songStandardizedName = promptForInput("Enter the song (Artist - Song): ");
        if (service.addSongToPlaylist(playlistName, songStandardizedName)) {
            System.out.println("Song added to the playlist successfully!");
        } else {
            System.out.println("Song not found or the playlist does not exist.");
        }
    }

    /**
     * Removes a song from a specified playlist.
     * Users are prompted to provide the playlist and song names, and the service attempts to remove the song.
     *
     * @param service The music library service instance handling the logic.
     */
    private static void removeSongFromPlaylist(MusicLibraryService service) {
        String playlistName = promptForInput("Enter the playlist name: ");
        String songStandardizedName = promptForInput("Enter the song (Artist - Song) to remove: ");
        if (service.removeSongFromPlaylist(playlistName, songStandardizedName)) {
            System.out.println("Song removed from the playlist successfully!");
        } else {
            System.out.println("Song not found in the playlist or the playlist does not exist.");
        }
    }

    /**
     * Displays all songs in a specified playlist.
     * The user is prompted for the playlist's name, and the service is used to retrieve and display all songs.
     *
     * @param service The music library service instance handling the logic.
     */
    private static void viewPlaylist(MusicLibraryService service) {
        String playlistName = promptForInput("Enter a playlist name to view: ");
        List<Song> songs = service.viewSongsInPlaylist(playlistName);
        if (!songs.isEmpty()) {
            System.out.println("Songs in " + playlistName + " playlist:");
            songs.forEach(song -> System.out.println(song));
        } else {
            System.out.println("No songs found in this playlist or the playlist does not exist.");
        }
    }

    /**
     * Deletes a specified playlist from the music library.
     * The user is asked to specify which playlist they want to delete, and the service attempts to remove it.
     *
     * @param service The music library service instance handling the logic.
     */
    private static void deletePlaylist(MusicLibraryService service) {
        String playlistName = promptForInput("Enter the playlist name to delete: ");
        if (service.deletePlaylist(playlistName)) {
            System.out.println("Playlist deleted successfully!");
        } else {
            System.out.println("Playlist not found.");
        }
    }

    /**
     * Searches for songs within the music library that match a given query.
     * The user inputs a search term, and the function uses the service to find matching songs.
     *
     * @param service The music library service instance handling the logic.
     */
    private static void searchSongs(MusicLibraryService service) {
        String searchQuery = promptForInput("Enter the song name to search for: ");
        List<Song> foundSongs = service.searchSongs(searchQuery);
        if (!foundSongs.isEmpty()) {
            System.out.println("Found Songs:");
            foundSongs.forEach(song -> System.out.println(song.getStandardizedName() + " - Listen on Spotify: " + song.getSpotifyUrl()));
        } else {
            System.out.println("No songs found matching your query.");
        }
    }

    /**
     * Searches for playlists within the music library based on a user query.
     * This method allows users to find playlists containing specific keywords in their names.
     *
     * @param service The music library service instance handling the logic.
     */
    private static void searchPlaylists(MusicLibraryService service) {
        String queryForPlaylists = promptForInput("Enter a search query for playlists: ");
        List<Playlist> foundPlaylists = service.searchPlaylists(queryForPlaylists);
        if (!foundPlaylists.isEmpty()) {
            System.out.println("Found Playlists:");
            foundPlaylists.forEach(playlist -> System.out.println(playlist.getName()));
        } else {
            System.out.println("No playlists found matching your query.");
        }
    }

    /**
     * Prompts the user for input and returns the entered string.
     * This method is used throughout the application to get user input after displaying a prompt.
     *
     * @param prompt The message displayed to the user prompting for input.
     * @return The string input by the user.
     */
    private static String promptForInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}


