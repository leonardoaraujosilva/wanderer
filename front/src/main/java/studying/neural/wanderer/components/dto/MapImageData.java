package studying.neural.wanderer.components.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import studying.neural.wanderer.components.model.Map;

import java.awt.image.BufferedImage;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapImageData {

    private Map map;
    private BufferedImage image;

}
