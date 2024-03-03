package hagelbrand.draftlighting.controller;

import hagelbrand.draftlighting.clients.ESPNSportsCoreClient;
import hagelbrand.draftlighting.clients.SmartThingsClient;
import hagelbrand.draftlighting.config.ESPNConfig;
import hagelbrand.draftlighting.config.SmartThingsConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
    public void getDraft() throws IOException, InterruptedException {
        ESPNSportsCoreClient espnSportsCoreClient = new ESPNSportsCoreClient(espnConfig);
        espnSportsCoreClient.getDraft(2024);
    }

    @GetMapping(value = "/overallPick")
    public void getOverall() throws IOException, InterruptedException {
        ESPNSportsCoreClient espnSportsCoreClient = new ESPNSportsCoreClient(espnConfig);
        espnSportsCoreClient.getSpecificOverallPick(2024, 2);
    }
}
