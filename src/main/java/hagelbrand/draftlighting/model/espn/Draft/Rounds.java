package hagelbrand.draftlighting.model.espn.Draft;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class Rounds {
    public int number;
    public String displayName;
    public String shortDisplayName;
    public List<Picks> picks;

}
