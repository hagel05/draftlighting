package hagelbrand.draftlighting.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import hagelbrand.draftlighting.config.ESPNConfig;
import hagelbrand.draftlighting.model.PickResponse;
import hagelbrand.draftlighting.model.espn.Draft.Draft;
import hagelbrand.draftlighting.model.espn.Draft.Picks;
import hagelbrand.draftlighting.model.espn.Team.Team;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ESPNSportsCoreClient {
    private final ESPNConfig espnConfig;

    @Autowired
    public ESPNSportsCoreClient(ESPNConfig config) {
        this.espnConfig = config;
    }
    // TODO: Device api is /devices/{deviceId}/commands

    private final HttpClient client = HttpClient.newBuilder().build();

    public Draft getDraft(int season) throws InterruptedException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(espnConfig.getUrl() + "sports/football/leagues/nfl/seasons/" + season + "/draft/rounds"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return parseResponse(response, Draft.class);
    }

    public PickResponse getOnTheClockPick(int season) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(espnConfig.getUrl() + "sports/football/leagues/nfl/seasons/" + season + "/draft/rounds"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Draft draft = parseResponse(response, Draft.class);

        Optional<Picks> overallPick = draft.getItems().stream()
                .flatMap(rounds -> rounds.getPicks().stream())
                .filter(picks -> picks.getStatus().getName().contains("ON_THE_CLOCK"))
                .findFirst();
        overallPick.orElseThrow(() -> new NoSuchElementException("Overall pick not found in this draft"));
        String teamUrl = overallPick.get().getTeam().$ref;
        request = HttpRequest.newBuilder()
                .uri(URI.create(String.valueOf(teamUrl)))
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Team team = parseResponse(response, Team.class);
        return new PickResponse(overallPick.get(), team);
    }

    public PickResponse getLastPick(int season) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(espnConfig.getUrl() + "sports/football/leagues/nfl/seasons/" + season + "/draft/rounds"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Draft draft = parseResponse(response, Draft.class);

        Optional<Picks> lastPick = draft.getItems().stream()
                .flatMap(rounds -> rounds.getPicks().stream())
                .filter(picks -> picks.getStatus().getName().contains("ON_THE_CLOCK"))
                .reduce((first, last) -> last);
        lastPick.orElseThrow(() -> new NoSuchElementException("Overall pick not found in this draft"));
        String teamUrl = lastPick.get().getTeam().$ref;
        request = HttpRequest.newBuilder()
                .uri(URI.create(String.valueOf(teamUrl)))
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Team team = parseResponse(response, Team.class);
        return new PickResponse(lastPick.get(), team);
    }

    public PickResponse getSpecificOverallPick(int season, int pick) throws IOException, InterruptedException {
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
        overallPick.orElseThrow(() -> new NoSuchElementException("Overall pick not found in this draft"));
        String teamUrl = overallPick.get().getTeam().$ref;
        request = HttpRequest.newBuilder()
                .uri(URI.create(String.valueOf(teamUrl)))
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Team team = parseResponse(response, Team.class);
        System.out.println(team.displayName);
        return new PickResponse(overallPick.get(), team);
    }
    public Team getTeamForSpecificPick(int season, int pick) throws IOException, InterruptedException {
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
        overallPick.orElseThrow(() -> new NoSuchElementException("Overall pick not found in this draft"));
        String teamUrl = overallPick.get().getTeam().$ref;
        request = HttpRequest.newBuilder()
                .uri(URI.create(String.valueOf(teamUrl)))
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Team team = parseResponse(response, Team.class);
        return team;
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
