package hagelbrand.draftlighting.model.smartthings;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Data
public class ColorControlCapability {
    public Command command;
    public Map<String, Integer> color = new HashMap<>();
    public ColorControlCapability(int hue, int saturation) {
        this.command = new Command("colorControl", "setColor", hue, saturation);
    }

}
