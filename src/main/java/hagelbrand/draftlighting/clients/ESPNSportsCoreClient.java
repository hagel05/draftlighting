package hagelbrand.draftlighting.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import hagelbrand.draftlighting.config.ESPNConfig;
import hagelbrand.draftlighting.model.espn.Draft;
import hagelbrand.draftlighting.model.espn.Picks;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ESPNSportsCoreClient {
    private final ESPNConfig espnConfig;

    @Autowired
    public ESPNSportsCoreClient(ESPNConfig config) {
        this.espnConfig = config;
    }
    // TODO: Device api is /devices/{deviceId}/commands

    private final HttpClient client = HttpClient.newBuilder().build();

    public void getDraft(int season) throws InterruptedException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(espnConfig.getUrl() + "sports/football/leagues/nfl/seasons/" + season + "/draft/rounds"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        System.out.println(response);
    }

    public void getOnTheClockPick(int season) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(espnConfig.getUrl() + "sports/football/leagues/nfl/seasons/" + season + "/draft/rounds"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void getSpecificOverallPick(int season, int pick) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(espnConfig.getUrl() + "sports/football/leagues/nfl/seasons/" + season + "/draft/rounds"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Draft draft = parseResponse(response, Draft.class);

        Optional<Picks> overallPick = draft.getItems().stream()
                .flatMap(rounds -> rounds.getPicks().stream())
                .filter(picks -> picks.getOverall() == pick)
                .findFirst();
        overallPick.ifPresentOrElse(
                foundPick -> System.out.println(foundPick.overall + " :: Round " + foundPick.round + " :: Pick " + foundPick.pick),
                () -> System.out.println("Not found!")
        );
    }



    private <T> T parseResponse(HttpResponse<String> response, Class<T> targetType) {
        ObjectMapper objectMapper = new ObjectMapper();
        // Check the HTTP status code
        int statusCode = response.statusCode();
        if (statusCode >= 200 && statusCode < 300) {
            // If the response is successful, parse the JSON to a Java object
            try {
                return objectMapper.readValue(response.body(), targetType);
            } catch (Exception e) {
                throw new RuntimeException("Error parsing JSON response", e);
            }
        } else {
            // Handle error scenario
            System.out.println("Error response: " + response.body());
            return null;
        }
    }
}
