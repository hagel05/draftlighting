package hagelbrand.draftlighting.model;

import hagelbrand.draftlighting.model.espn.Draft.Picks;
import hagelbrand.draftlighting.model.espn.Team.Team;
import lombok.Data;

@Data
public class PickResponse {
    Picks pick;
    Team team;

    public PickResponse(Picks pick, Team team) {
        this.pick = pick;
        this.team = team;
    }
}
