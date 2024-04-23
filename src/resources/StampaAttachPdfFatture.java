package resources;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StampaAttachPdfFatture extends Application {
	
	public static final String title = "Software srl";
	public static String css = "src/resources/application.css";
	public static String[] fonts = new String[] {"Arial", "Arial Black", "Comic sans MS", "Courier New", 
			"Impact", "Lucida Console", "Modern", "Tahoma", "Times New Roman", "Verdana"};
	
	
	public static void main(String[] args) {
        launch(args);
    }

	
	public void start(Stage primaryStage) throws IOException, InterruptedException {
		primaryStage.setTitle(StampaAttachPdfFatture.title);
		
		File f = new File("src/fxml/GestioneConvenevoli.fxml");
		if(f.exists() && !f.isDirectory()) { 
			System.out.println("file trovato");
		}
		
		FXMLLoader loader = new FXMLLoader(StampaAttachPdfFatture.class.getResource("/fxml/GestioneConvenevoli.fxml"));
        Parent root = loader.load();
        
        primaryStage.setScene(new Scene(root, 1100, 640));
        primaryStage.show();
        primaryStage.setMaximized(true);
	}

}
