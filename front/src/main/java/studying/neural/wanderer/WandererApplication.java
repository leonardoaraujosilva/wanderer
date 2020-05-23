package studying.neural.wanderer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class WandererApplication extends Application {

    @Override
    @SneakyThrows
    public void start(Stage stage) {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/main.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch();
    }
}
