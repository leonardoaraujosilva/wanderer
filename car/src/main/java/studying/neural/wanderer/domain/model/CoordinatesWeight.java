package studying.neural.wanderer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CoordinatesWeight {

    private final Coordinates coordinates;
    private final int weight;

}
