package model;

/**
 * This class represents a song in the music library. It holds information about the song such as its title,
 * artist, genre, and a URL to listen to it on Spotify. Methods are provided to modify and retrieve these details.
 */
@SuppressWarnings("unused")  // This annotation tells the compiler to ignore warnings about unused methods, as they may be used later.
public class Song {
    // Fields to hold the song's properties.
    private String title;       // The title of the song.
    private String artist;      // The artist who performed the song.
    private String genre;       // The genre of the song.
    private String spotifyUrl;  // A URL to the song on the Spotify platform.

    /**
     * Constructor to create a new song instance. Requires all the fields to be set at the time of object creation.
     *
     * @param title      The title of the song.
     * @param artist     The artist of the song.
     * @param genre      The genre of the song; if not provided, defaults to an empty string.
     * @param spotifyUrl The Spotify URL where the song can be played.
     */
    public Song(String title, String artist, String genre, String spotifyUrl) {
        this.title = title;
        this.artist = artist;
        this.genre = (genre == null ? "" : genre);  // If genre is null, set it to an empty string.
        this.spotifyUrl = spotifyUrl;
    }

    /**
     * Returns a standardized name for the song that combines the artist and title.
     * This can be useful for displaying the song in a list or for sorting.
     *
     * @return A string in the format "Artist - Title".
     */
    public String getStandardizedName() {
        return artist + " - " + title;
    }

    // Getter method for the title of the song.
    public String getTitle() {
        return title;
    }

    // Setter method for the title of the song. Allows updating the title after the song object has been created.
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter method for the artist of the song.
    public String getArtist() {
        return artist;
    }

    // Setter method for the artist of the song. Allows updating the artist name.
    public void setArtist(String artist) {
        this.artist = artist;
    }

    // Getter method for the genre of the song.
    public String getGenre() {
        return genre;
    }

    // Setter method for the genre of the song. Allows changing the genre.
    public void setGenre(String genre) {
        this.genre = genre;
    }

    // Getter method for the Spotify URL of the song.
    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    // Setter method for the Spotify URL of the song. Allows updating the URL where the song can be played.
    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }

    /**
     * Returns a string representation of the song, which includes its standardized name and Spotify URL.
     * This is useful for logging and displaying the song in a readable format.
     *
     * @return A string representation of the song.
     */
    @Override
    public String toString() {
        return getStandardizedName() + " - Listen on Spotify: " + spotifyUrl;
    }
}


