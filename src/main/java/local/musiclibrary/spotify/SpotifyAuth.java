// SpotifyAuth.java
// This Java file contains the SpotifyAuth class responsible for handling authentication with the Spotify API.
// It uses client credentials to obtain an access token that allows the application to make requests to the Spotify Web API.
// The class manages the setup of these credentials and the actual process of fetching the token using HTTP requests.


package local.musiclibrary.spotify;

import okhttp3.*;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Properties;

/**
 * Handles authentication with the Spotify API by fetching an access token using client credentials.
 */
public class SpotifyAuth {
    private static final String clientId;
    private static final String clientSecret;

    static {
        Properties props = new Properties();
        try {
            // Load properties from the config file located in src/main/resources
            props.load(SpotifyAuth.class.getClassLoader().getResourceAsStream("config.properties"));
            clientId = props.getProperty("spotify.clientId");
            clientSecret = props.getProperty("spotify.clientSecret");

            if (clientId == null || clientSecret == null) {
                throw new IllegalStateException("Client ID and Client Secret must not be null. Check your config.properties.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration properties", e);
        }
    }
    /**
     * Retrieves the access token from Spotify's authorization server.
     * @return The access token as a String.
     * @throws IOException If there is an issue with the network or if the server response is unexpected.
     */
    public static String getAccessToken() throws IOException {
        OkHttpClient client = new OkHttpClient();
        String credentials = Credentials.basic(clientId, clientSecret);

        RequestBody body = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build();
        Request request = new Request.Builder()
                .url("https://accounts.spotify.com/api/token")
                .post(body)
                .header("Authorization", credentials)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Gson gson = new Gson();
            assert response.body() != null;
            SpotifyTokenResponse tokenResponse = gson.fromJson(response.body().string(), SpotifyTokenResponse.class);
            // Parse the JSON response to extract the access token
            return tokenResponse.access_token;  // Return the access token obtained from Spotify
        }
    }

    /**
     * A helper class to deserialize JSON response from Spotify's API for authentication.
     */
    @SuppressWarnings("unused")
    private static class SpotifyTokenResponse {
        String access_token;  // Field to hold the access token
        String token_type;    // Token type, typically "Bearer"
        int expires_in;       // Duration in seconds for which the token is valid
        String scope;         // Scopes of access granted by the token
    }
}


