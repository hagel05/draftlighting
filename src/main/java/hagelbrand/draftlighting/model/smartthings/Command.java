package hagelbrand.draftlighting.model.smartthings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Command<T> {
    public String component = "main";
    public String capability;
    public String command;
    private List<T> arguments = new ArrayList<>();

    public void addArgument(T argument) {
        arguments.add(argument);
    }

    public List<T> setArguments(List<T> arguments) {
        return arguments;
    }

    public List<T> getArguments() {
        return arguments;
    }

    Command(String capability, String command, List<T> arguments) {
        this.capability = capability;
        this.command = command;
        setArguments(arguments);
    }

    Command(String capability, String command) {
        this.capability = capability;
        this.command = command;
    }

    Command(String capability, String command, int hue, int saturation) {
        this.capability = capability;
        this.command = command;
        Map<String, Integer> color = new HashMap<>();
        color.put("hue", hue);
        color.put("saturation", saturation);
        addArgument((T) color);
    }
}
