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

public class SpotifySearch {
    public static Map<String, String> searchSong(String accessToken, String query) throws IOException, URISyntaxException {
        Map<String, String> trackDetails = new HashMap<>();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URI uri = new URIBuilder("https://api.spotify.com/v1/search")
                    .addParameter("q", query)
                    .addParameter("type", "track")
                    .addParameter("limit", "10")
                    .build();

            HttpGet request = new HttpGet(uri);
            request.setHeader("Authorization", "Bearer " + accessToken);

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





