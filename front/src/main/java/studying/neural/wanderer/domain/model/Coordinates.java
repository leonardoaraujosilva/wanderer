package studying.neural.wanderer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Coordinates {

    private final int x;
    private final int y;

}
