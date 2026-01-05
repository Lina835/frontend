package fr.java;

import fr.java.frontend.HomeView;
import fr.java.frontend.Router;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Router.init(stage);
        Router.setScene(HomeView.build());
    }

    public static void main(String[] args) {
        launch();
    }
}
