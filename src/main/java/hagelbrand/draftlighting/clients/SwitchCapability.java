package hagelbrand.draftlighting.clients;

public class SwitchCapability {
    Command command;
    enum Switch {ON, OFF};

    SwitchCapability(Switch switchState) {
        this.command = new Command( "switch", switchState.name().toLowerCase());
    }
}
