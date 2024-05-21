// SpotifySearch.java
// This Java file contains the SpotifySearch class, which provides functionality to search for songs on Spotify using their API.
// It constructs HTTP requests to query Spotify's track database based on user input, parses the JSON response, and returns a map of song details.
// This class is crucial for integrating Spotify's extensive music database with the application, allowing users to search and retrieve music information.


package spotify;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides functionality to search for songs on Spotify using the Spotify Web API.
 */
public class SpotifySearch {
    /**
     * Searches Spotify for tracks based on a given query.
     * @param accessToken The access token for authentication with Spotify API.
     * @param query The search query for the song.
     * @return A map of song details with artist and song name as key, and Spotify URL as value.
     * @throws IOException If there is a problem with the network or server.
     * @throws URISyntaxException If the URI built for API request is incorrect.
     */
    public static Map<String, String> searchSong(String accessToken, String query) throws IOException, URISyntaxException {
        Map<String, String> trackDetails = new HashMap<>();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URI uri = new URIBuilder("https://api.spotify.com/v1/search")
                    .addParameter("q", query) // Query parameter for the song search.
                    .addParameter("type", "track") // Specifies the type of search (tracks).
                    .addParameter("limit", "10") // Limits the number of results to 10.
                    .build();

            HttpGet request = new HttpGet(uri);
            request.setHeader("Authorization", "Bearer " + accessToken); // Sets the Authorization header with the access token.

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new IOException("Unexpected response status: " + response.getStatusLine());
                }

                String jsonData = EntityUtils.toString(response.getEntity());
                JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
                JsonArray tracks = jsonObject.getAsJsonObject("tracks").getAsJsonArray("items");

                for (JsonElement element : tracks) {
                    JsonObject track = element.getAsJsonObject();
                    String name = track.getAsJsonPrimitive("name").getAsString();
                    String artist = track.getAsJsonArray("artists").get(0).getAsJsonObject().getAsJsonPrimitive("name").getAsString();
                    String spotifyUrl = track.getAsJsonObject("external_urls").getAsJsonPrimitive("spotify").getAsString();

                    trackDetails.put(artist + " - " + name, spotifyUrl);
                }
            }
        }
        return trackDetails;
    }
}





