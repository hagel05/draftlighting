package hagelbrand.draftlighting.model.espn;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Status {
    public int id;
    // TODO: convert to enum
    public String name;
    public String description;
}
