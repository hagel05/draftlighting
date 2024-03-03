package hagelbrand.draftlighting.model.espn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Picks {
    public Status status;
    public int pick;
    public int overall;
    public int round;
    public boolean traded;
    public Athlete athlete;
    public Team team;
}
