package ma.enset.packetinterceptor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InterceptorApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InterceptorApplication.class.getResource("capture-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 760, 400);
        stage.setTitle("Intrusion Detection System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}