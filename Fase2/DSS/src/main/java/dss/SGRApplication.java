package dss;

import dss.business.SGR.SGR;
import dss.business.SGR.SGRInterface;
import dss.gui.Frame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SGRApplication extends Application {
    // Estamos a usar JavaFX, portanto este é o método principal da aplicação
    @Override
    public void start(Stage stage) throws Exception {
        SGRInterface sgr = new SGR();
        Frame frame = new Frame(sgr);
        frame.setStage(stage);
        Scene mainScene = frame.getScene();

        stage.setScene(mainScene);
        stage.setTitle("Sistema de Gestão de Reparações");
        stage.show();
    }
}
