package studying.neural.wanderer.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import lombok.SneakyThrows;
import studying.neural.wanderer.components.dto.MapImageData;
import studying.neural.wanderer.tools.car.CarManager;
import studying.neural.wanderer.tools.map.MapLoader;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private ImageView image;

    @FXML
    private TextField file;

    @FXML
    private BorderPane pane;

    private MapImageData data;

    @Override
    @SneakyThrows
    public void initialize(URL location, ResourceBundle resources) {

        var image = new File(getClass().getClassLoader().getResource("maps/complex.png").toURI());
        data = new MapLoader().loadFromImage(image);
        var car = new CarManager().create(data.getMap());
        this.image.setImage(SwingFXUtils.toFXImage(data.getImage(), null));
        file.setText(image.getAbsolutePath());

    }

    @FXML
    public void onAction() {

    }

}
