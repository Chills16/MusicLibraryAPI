package spotify;

import okhttp3.*;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Properties;

public class SpotifyAuth {
    private static final String clientId;
    private static final String clientSecret;

    static {
        Properties props = new Properties();
        try {
            // Load properties from the config file located in src/main/resources
            props.load(SpotifyAuth.class.getClassLoader().getResourceAsStream("config.properties"));
            clientId = props.getProperty("clientId");
            clientSecret = props.getProperty("clientSecret");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration properties", e);
        }
    }
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
            return tokenResponse.access_token;
        }
    }

    @SuppressWarnings("unused")
    private static class SpotifyTokenResponse {
        String access_token;
        String token_type;
        int expires_in;
        String scope;
    }
}

