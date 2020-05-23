package studying.neural.wanderer.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import studying.neural.wanderer.service.CreateTooManyGames;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private final CreateTooManyGames createTooManyGames = new CreateTooManyGames();

    @FXML
    private ImageView imageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void startNextGeneration() {
        createTooManyGames.createAndStart(imageView);
    }
}
