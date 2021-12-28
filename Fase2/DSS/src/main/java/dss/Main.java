package dss;

import dss.gui.Frame;
import dss.gui.LogIn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    // Estamos a usar JavaFX, portanto este é o método principal da aplicação
    @Override
    public void start(Stage stage) throws Exception {
        SGR sgr = new SGR();
        Frame frame = new Frame(sgr);
        Scene mainScene = frame.getScene();

        stage.setScene(mainScene);
        stage.setTitle("Sistema de Gestão de Reparações");
        stage.show();
    }
}
