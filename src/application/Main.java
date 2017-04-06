package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
		      Parent root = FXMLLoader.load(getClass().getResource("any.fxml"));  
		        Scene scene = new Scene(root, 800, 600);  
		        stage.initStyle(StageStyle.DECORATED);  
		        stage.setScene(scene);
		        stage.setWidth(610);
		        stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
