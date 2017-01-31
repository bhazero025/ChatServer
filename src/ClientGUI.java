import java.io.IOException;
import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ClientGUI extends Application
{
	Client client = null;
	//Message from other clients and server
	public static TextArea textArea = new TextArea();

	@Override
	public void start(Stage primaryStage)
	{
		try 
		{
			client = new Client();
			//Ask for username
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Username");
			dialog.setHeaderText("Please enter a username");
			dialog.setContentText("Username: ");
			
			//Assign username to Client.username
			Optional<String> result = dialog.showAndWait();
			result.ifPresent(name -> Client.userName = name + ":");
			
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
						client.sendMessage(inputField.getText());
					} 
					catch (IOException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//clear
					inputField.clear();
				}
			});

			primaryStage.setScene(scene);
			primaryStage.show();
			inputField.setLayoutX(10);
			inputField.setLayoutY(360);
			root.getChildren().addAll(inputField, textArea);
			
			String title = Client.userName;
			//Remove :
			title = title.substring(0, Client.userName.length() - 1);
			
			primaryStage.setTitle("Client " + title);
			
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
}
