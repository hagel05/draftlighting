package hagelbrand.draftlighting.controller;

import hagelbrand.draftlighting.clients.ESPNSportsCoreClient;
import hagelbrand.draftlighting.clients.SmartThingsClient;
import hagelbrand.draftlighting.config.ESPNConfig;
import hagelbrand.draftlighting.config.SmartThingsConfig;
import hagelbrand.draftlighting.model.DraftLighting.SimulateDraft;
import hagelbrand.draftlighting.model.PickResponse;
import hagelbrand.draftlighting.model.espn.Draft.Draft;
import hagelbrand.draftlighting.model.espn.Draft.Picks;
import hagelbrand.draftlighting.model.espn.Team.Team;
import hagelbrand.draftlighting.service.DraftLightingService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@Getter
@RequiredArgsConstructor
@RequestMapping("/draftlighting")
public class DraftLightingController {
    private final SmartThingsConfig smartThingsConfig;
    private final ESPNConfig espnConfig;

    @GetMapping(value = "/config", produces = MediaType.TEXT_PLAIN_VALUE)
    public String retrieveConfig() {
        return smartThingsConfig.getUrl() + " :: " + smartThingsConfig.getPat() + " :: " + espnConfig.getUrl();
    }

    @GetMapping(value = "/on")
    public void turnOn() throws IOException, InterruptedException {
        SmartThingsClient smartThingsClient = new SmartThingsClient(smartThingsConfig);
        smartThingsClient.turnOn();
    }

    @GetMapping(value = "/off")
    public void turnOff() throws IOException, InterruptedException {
        SmartThingsClient smartThingsClient = new SmartThingsClient(smartThingsConfig);
        smartThingsClient.turnOff();
    }

    @GetMapping(value = "/draft")
    public ResponseEntity<Draft> getDraft() throws IOException, InterruptedException {
        ESPNSportsCoreClient espnSportsCoreClient = new ESPNSportsCoreClient(espnConfig);
        Draft result;
        try {
            result = espnSportsCoreClient.getDraft(2024);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/overallPick")
    public ResponseEntity<PickResponse> getOverall(@RequestParam(defaultValue = "1") int pick,
                                                   @RequestParam(defaultValue = "2024") int year) throws IOException, InterruptedException {
        ESPNSportsCoreClient espnSportsCoreClient = new ESPNSportsCoreClient(espnConfig);
        PickResponse result;
        try {
            result = espnSportsCoreClient.getSpecificOverallPick(year, pick);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
    @GetMapping(value = "/overallPickTeam")
    public ResponseEntity<Team> getOverallPickTeam(@RequestParam(defaultValue = "1") int pick,
                                                   @RequestParam(defaultValue = "2024") int year) throws IOException, InterruptedException {
        Team result;
        ESPNSportsCoreClient espnSportsCoreClient = new ESPNSportsCoreClient(espnConfig);
        try {
            result = espnSportsCoreClient.getTeamForSpecificPick(year, pick);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/onTheClock")
    public ResponseEntity<PickResponse> getOnTheClockPick(@RequestParam(defaultValue = "1") int pick,
                                                   @RequestParam(defaultValue = "2024") int year) throws IOException, InterruptedException {
        ESPNSportsCoreClient espnSportsCoreClient = new ESPNSportsCoreClient(espnConfig);
        PickResponse result;
        try {
            result = espnSportsCoreClient.getOnTheClockPick(year);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/startDraft")
    public ResponseEntity<Void> startDraft(@RequestBody SimulateDraft draft) throws IOException, InterruptedException {
        DraftLightingService service = new DraftLightingService(espnConfig, smartThingsConfig);
        try {
            service.startDraft(draft.getYear(), draft.getDeviceIds());
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/simulateDraft")
    public ResponseEntity<Void> simulateDraft(@RequestBody SimulateDraft draft) throws IOException, InterruptedException {
        DraftLightingService service = new DraftLightingService(espnConfig, smartThingsConfig);
        service.simulateDraft(draft.getYear(), draft.getDeviceIds());
        return ResponseEntity.noContent().build();
    }
}
