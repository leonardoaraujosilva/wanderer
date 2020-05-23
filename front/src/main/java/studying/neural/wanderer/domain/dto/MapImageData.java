package studying.neural.wanderer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import studying.neural.wanderer.domain.model.Map;

import java.awt.image.BufferedImage;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapImageData {

    private Map map;
    private BufferedImage image;

}
