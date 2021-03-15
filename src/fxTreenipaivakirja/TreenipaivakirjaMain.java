package fxTreenipaivakirja;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import treenipaivakirja.Treenipaivakirja;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * Pääohjelma treenipäiväkirjan käynnistämiseksi.
 * 
 * @author Kristian Koronen
 * @version 1.2.2018
 * 
 */
public class TreenipaivakirjaMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("TreenipaivakirjaView.fxml"));
            final Pane root = ldr.load();
            final TreenipaivakirjaController treenipaivakirjaCtrl = (TreenipaivakirjaController) ldr.getController();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("treenipaivakirja.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Treenipaivakirja");
            
            Treenipaivakirja treenipaivakirja = new Treenipaivakirja();
            treenipaivakirja.lueTiedostosta("treenipaivakirja_tiedostot");
            treenipaivakirjaCtrl.setTreenipaivakirja(treenipaivakirja);
            
            primaryStage.setOnCloseRequest((event) -> {
                if (!treenipaivakirjaCtrl.voikoSulkea()) event.consume();                
            });
            primaryStage.show();
            if (!treenipaivakirjaCtrl.avaa()) Platform.exit();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args Ei käytössä
     */
    public static void main(String[] args) {
        launch(args);
    }
}