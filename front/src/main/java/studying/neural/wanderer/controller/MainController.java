package studying.neural.wanderer.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import studying.neural.wanderer.Game;
import studying.neural.wanderer.service.CreateTooManyGames;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private final CreateTooManyGames createTooManyGames = new CreateTooManyGames();
    private boolean started = false;

    @FXML
    private ImageView imageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void startNextGeneration() {
        if(!started) {
            createTooManyGames.createAndStart(imageView);
            started = true;
        } else {
            Game.sleep = Game.sleep == 0 ? 25 : 0;

        }
    }
}
