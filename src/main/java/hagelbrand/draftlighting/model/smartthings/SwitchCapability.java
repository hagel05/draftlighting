package hagelbrand.draftlighting.model.smartthings;

import lombok.Data;

@Data
public class SwitchCapability {
    public Command command;
    public enum Switch {ON, OFF};

    public SwitchCapability(Switch switchState) {
        this.command = new Command( "switch", switchState.name().toLowerCase());
    }
}
