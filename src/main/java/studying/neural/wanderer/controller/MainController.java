package studying.neural.wanderer.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import lombok.SneakyThrows;
import studying.neural.wanderer.domain.dto.MapImageData;
import studying.neural.wanderer.service.map.MapLoader;

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
        image.setFitWidth(pane.getCenter().getLayoutX());
        image.setFitHeight(pane.getCenter().getLayoutY());

        var image = new File(getClass().getClassLoader().getResource("maps/complex.png").toURI());
        data = new MapLoader().loadFromImage(image);
        this.image.setImage(SwingFXUtils.toFXImage(data.getImage(), null));
        file.setText(image.getAbsolutePath());
    }

    @FXML
    public void onAction() {

    }

}
