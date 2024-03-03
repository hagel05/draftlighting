package hagelbrand.draftlighting.clients;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hagelbrand.draftlighting.config.SmartThingsConfig;
import hagelbrand.draftlighting.model.smartthings.ColorControlCapability;
import hagelbrand.draftlighting.model.smartthings.Commands;
import hagelbrand.draftlighting.model.smartthings.SwitchCapability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

interface SmartThingsLighting {
    void turnOn() throws IOException, InterruptedException;
    void turnOff() throws IOException, InterruptedException;
    void setColor() throws IOException, InterruptedException;
}
@Import(SmartThingsConfig.class)
public class SmartThingsClient implements SmartThingsLighting {
    private final SmartThingsConfig smartThingsConfig;

    @Autowired
    public SmartThingsClient(SmartThingsConfig config) {
        this.smartThingsConfig = config;
    }
    // TODO: Device api is /devices/{deviceId}/commands

    private final HttpClient client = HttpClient.newBuilder().build();

    @Override
    public void turnOn() throws IOException, InterruptedException {
        // TODO: Refactor to take a device list and send a list of commands
        String deviceId = "088796e1-4072-4528-a854-1a1d8b9c097d";
        Commands commands = new Commands(List.of(new SwitchCapability(SwitchCapability.Switch.ON).getCommand()));
        sendDeviceCommands(deviceId, commands);
    }
    @Override
    public void turnOff() throws IOException, InterruptedException {
        String deviceId = "088796e1-4072-4528-a854-1a1d8b9c097d";
        Commands commands = new Commands(List.of(new SwitchCapability(SwitchCapability.Switch.OFF).getCommand()));
        sendDeviceCommands(deviceId, commands);
    }
    @Override
    public void setColor() throws IOException, InterruptedException {
        String deviceId = "088796e1-4072-4528-a854-1a1d8b9c097d";
        Commands commands = new Commands(List.of(new ColorControlCapability(0, 100).getCommand()));
        sendDeviceCommands(deviceId, commands);
    }

    private void sendDeviceCommands(String deviceId, Commands commands) throws InterruptedException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String commandJson = objectMapper.writeValueAsString(commands);
        System.out.println(commandJson);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(smartThingsConfig.getUrl() + "devices/" + deviceId + "/commands"))
                .header("authorization", "Bearer " + smartThingsConfig.getPat())
                .POST(HttpRequest.BodyPublishers.ofString(commandJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);
    }

}
