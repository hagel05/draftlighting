package hagelbrand.draftlighting.clients;

import java.util.HashMap;
import java.util.Map;

public class ColorControlCapability {
    Command command;
    Map<String, Integer> color = new HashMap<>();
    ColorControlCapability(int hue, int saturation) {
        this.command = new Command("colorControl", "setColor", hue, saturation);
    }

}
