package hagelbrand.draftlighting.model.espn.Team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {
        public String location;
        public String abbreviation;
        public String displayName;
        public String color;
        public String alternateColor;
}
