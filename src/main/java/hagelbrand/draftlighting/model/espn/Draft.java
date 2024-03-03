package hagelbrand.draftlighting.model.espn;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Draft {
    public int count;
    public int pageIndex;
    public int pageSize;
    public int pageCount;
    public List<Rounds> items;
}
