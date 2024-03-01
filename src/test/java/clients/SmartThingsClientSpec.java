package clients;

import hagelbrand.draftlighting.config.SmartThingsConfig;
import hagelbrand.draftlighting.clients.SmartThingsClient;
import org.junit.jupiter.api.Test;

public class SmartThingsClientSpec {
    SmartThingsConfig config = new SmartThingsConfig();
    @Test
    public void testTurnOnSmartLight() throws Exception {
        SmartThingsClient smartThingsClient = new SmartThingsClient(config);
        smartThingsClient.turnOn();
    }

    @Test
    public void testTurnOffSmartLight() throws Exception {
        SmartThingsClient smartThingsClient = new SmartThingsClient(config);
        smartThingsClient.turnOff();
    }

    @Test
    public void testSetColorSmartLight() throws Exception {
        SmartThingsClient smartThingsClient = new SmartThingsClient(config);
        smartThingsClient.setColor();
    }
}
