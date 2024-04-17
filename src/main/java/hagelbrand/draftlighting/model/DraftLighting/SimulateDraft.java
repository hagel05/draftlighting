package hagelbrand.draftlighting.model.DraftLighting;

import lombok.Data;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Data
public class SimulateDraft {
    int year = Year.now().getValue();
    ArrayList<String> deviceIds = new ArrayList<>();
}
