import java.io.IOException;
import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ServerGUI extends Application
{
	//Message from other clients and server
	public static TextArea textArea = new TextArea();
	private Server server;
	
	
	@Override
	public void start(Stage primaryStage)
	{
		try 
		{
			//Input textField
			TextField inputField = new TextField();
			
			
			//Setting the size
			textArea.setLayoutX(10);
			textArea.setLayoutY(10);
			textArea.setMaxSize(380, 400);
			textArea.setMinHeight(340);
			
			textArea.setEditable(false);
			textArea.setMouseTransparent(true);
			textArea.setFocusTraversable(false);
			
			
			Pane root = new Pane();
			Scene scene = new Scene(root,403,400);
	
			inputField.setPrefColumnCount(33);
			inputField.setOnAction(new EventHandler<ActionEvent>() 
			{
				public void handle(ActionEvent event) 
				{
					//send message to server
					try 
					{
						//client.sendMessage(inputField.getText());
						String buf = "SERVER: ";
						buf += inputField.getText();
						//buf += '\n';
						server.getCS().sendMessageToAllClients(buf);
			
						
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
					
					//clear
					inputField.clear();
				}
			});

			//Add objects to the scene
			primaryStage.setScene(scene);
			primaryStage.show();
			inputField.setLayoutX(10);
			inputField.setLayoutY(360);
			root.getChildren().addAll(inputField, textArea);
			server = new Server();
			
			primaryStage.setTitle("SERVER RUNNING AT " + Server.port);
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		launch(args);
		//new Thread(new ServerGUI()).start();
	}



	
}
