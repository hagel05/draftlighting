package hagelbrand.draftlighting.clients;

import java.util.ArrayList;
import java.util.List;

public class Arguments<T> {
    private List<T> arguments;

    public Arguments() {
        this.arguments = new ArrayList<>();
    }

    public void addArgument(T argument) {
        arguments.add(argument);
    }

    public List<T> getArguments() {
        return arguments;
    }
}
