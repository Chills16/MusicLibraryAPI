package model;

/**
 * This class currently includes methods that are
 * not used but will be necessary for when we run the program.
 */
@SuppressWarnings("unused")
public class Song {
    private String title;
    private String artist;
    private String genre;
    private String spotifyUrl;

    public Song(String title, String artist, String genre, String spotifyUrl) {
        this.title = title;
        this.artist = artist;
        this.genre = (genre == null ? "" : genre);
        this.spotifyUrl = spotifyUrl;
    }

    public String getStandardizedName() {
        return artist + " - " + title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }

    @Override
    public String toString() {
        return getStandardizedName() + " - Listen on Spotify: " + spotifyUrl;
    }
}

