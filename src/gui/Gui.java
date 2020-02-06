package gui;

import java.util.Optional;
import client.Client;
import client.DatabaseChoice;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * GUI class
 * @author Francesco Raco
 *
 */
public class Gui extends Application
{
	/**
	 * SERVER HOST
	 */
	public static final String HOST = "racomaps.ns0.it";
	
	/**
	 * TCP Connection port
	 */
	public final int PORT = 8080;
	
	/**
	 * Verdana font
	 */
    public static final String FONT = "fonts/Verdana.ttf";
	
	/**
	 * Access point of the GUI application
	 * @param args args
	 */
	public static void main(String[] args)
	{
        launch(args);
    }
	
	/**
	 * Choose action depending on choice of database index of the search
	 * @param status Label which describes the status of execution
	 * @param txtField Text field containing the string query
	 * @param index Choice of database index of the search
	 * @param description Description of server operation
	 * @throws InterruptedException Interrupted Exception
	 */
	private void chooseAction(Label status, TextField txtField, DatabaseChoice index, String description) throws InterruptedException
	{
		//Set status font
		status.setFont(Font.loadFont(ClassLoader.getSystemClassLoader().getResourceAsStream(FONT), 14));
		
		//Show status info
		status.setText("I am working...Please wait...");
		
		//Instantiate new Task overriding the method call():
		//This is useful to separate graphical and logical threads
		Task task = new Task<Void>()
		{
		    @Override public Void call()
		    {
		    	//Get the string query
		    	String input = txtField.getText();
		    
			    //Instantiate the client by server host name and TCP connection port
			    Client client = new Client(HOST, PORT);
		    
		        //Estimated alert header text
			    final String ESTIMATEDHEADERTEXT = "Server reply:";
			
			    //Estimated server output by the string query and the chosen database index of search (without initial and ending white spaces)
			    final String ESTIMATEDSERVEROUTPUT= client.getServerOutput(input.trim(), index);
		    
		        //Run the specified Runnable argument at some unspecified time in the future:
			    //a lambda expression defines it with the specific implementation of the method run();
			    //this is useful for showing the status label meanwhile client waits for server answer
			    Platform.runLater(() ->
		        {
		    	    //Initialize default values of alert header text and server output
		    	    String headerText = ESTIMATEDHEADERTEXT;
		    	    String serverOutput = ESTIMATEDSERVEROUTPUT;
		    	
		    	    //If the client found problems
				    if (ESTIMATEDSERVEROUTPUT.startsWith(Client.MARK))
				    {
					    //Set alert header text specifying that this is a client message
					    headerText = "Client communication:";
					
					    //Remove client mark from the alert content text
					    serverOutput = ESTIMATEDSERVEROUTPUT.replaceFirst(Client.MARK, "").trim();
				    }
		    	
		    	    //Get scrolling alert (containing informations regarding server output) and show it
		    	    getScrollingAlertAndShowIt(AlertType.INFORMATION, description, headerText, serverOutput);
		    	
		    	    //clear the text field
				    txtField.clear();
		    	
		    	    //Reset status info
		    	    status.setText("");
		        });
		    	
		       //Return null
		       return null;
		    };
		}; 		
		
		//Create new Thread object passing task as argument
		Thread thread = new Thread(task);
				
		//Mark this thread as either a daemon thread or a user thread
		thread.setDaemon(true);
		        
		//Begin execution of the thread; method start() invokes run() method implemented before
		thread.start();
	}
	
	/**
	 * Get scrolling alert
	 * @param type Alert type
	 * @param title Alert title string
	 * @param headerText Header text
	 * @param content Alert content string
	 * @return Scrolling alert
	 */
	private Alert getScrollingAlert(AlertType type, String title, String headerText, String content)
	{
		//Create new Alert object by an alert type and set its title
		Alert alert = new Alert(type);
		alert.setTitle(title);
		
		//Set alert header text
		alert.setHeaderText(headerText);
		
		//Set width and height values related to DialogPane alert
		alert.getDialogPane().setPrefSize(330, 225);
		
		//Create a new TextArea object by content string
		TextArea area = new TextArea(content);
		
		//If a run of text exceeds the width of the TextArea,
		//then the text will wrap onto another line
		area.setWrapText(true);
		
		//TextInputControl can't be edited by the user
		area.setEditable(false);
		
		//Assign area to alert dialog pane
		alert.getDialogPane().setContent(area);
		
		//Dialog can be resized by the user
		alert.setResizable(true);
		
		//Return alert
		return alert;
	}
	
	/**
	 * Show scrolling alert
	 * @param alert Alert
	 * @return Optional<ButtonType>
	 */
	private Optional<ButtonType> showScrollingAlert(Alert alert)
	{
		return alert.showAndWait();
	}
	
	/**
	 * Get scrolling alert and show it
	 * @param type Alert type
	 * @param title Alert title string
	 * @param headerText Header text
	 * @param content Alert content string
	 * @return Optional<ButtonType>
	 */
	private Optional<ButtonType> getScrollingAlertAndShowIt(AlertType type, String title, String headerText, String content)
	{
		Alert alert = getScrollingAlert(type, title, headerText, content);
		return showScrollingAlert(alert);
	}
	
	@Override
    public void start(Stage primaryStage)
	{
		//Instantiate text field containing the string name of the query
		TextField text = new TextField();
		
		//Create label object which describes the status of execution
		//and set its style
		Label status = new Label("");
		status.setStyle("-fx-text-fill: red");
		
        //Create new toggle button related to full scan button, then set its text and action
		ToggleButton fullScanButton = new ToggleButton();
        fullScanButton.setText("Full Scan");
        fullScanButton.setOnAction(new EventHandler<ActionEvent>()
        {
 
            @Override
            public void handle(ActionEvent event)
            {
            	//Try execution and handle eventual exceptions
            	try
               {
            		//Invoke chooseAction specifying full scan as database index of the search
            		chooseAction(status, text, DatabaseChoice.FULLSCAN, "Full Scan");
               }
               catch (InterruptedException e)
               {
				
            	   e.printStackTrace();
               }
			}
        });
        
        //Create new toggle button related to quick scan, then set its text and action
        ToggleButton quickScanButton = new ToggleButton();
        quickScanButton.setText("Quick Scan");
        quickScanButton.setOnAction(new EventHandler<ActionEvent>()
        {
 
            @Override
            public void handle(ActionEvent event)
            {
            	//Try execution and handle eventual exceptions
            	try
            	{
            		//Invoke chooseAction specifying quick scan as database index of the search
            		chooseAction(status, text, DatabaseChoice.QUICKSCAN, "Quick Scan");
				}
            	catch (InterruptedException e)
            	{
					e.printStackTrace();
				}
            }
        });
        
        //Create a toggle group object
        ToggleGroup group = new ToggleGroup();
        
        //Add to group the full and quick scan buttons
        fullScanButton.setToggleGroup(group);
        quickScanButton.setToggleGroup(group);
        
        //Create a new flexible grid of rows and columns and set center-left alignment
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER_LEFT);
       
        //Create label by program title string
        Label title = new Label("   Type Query");
        
        //Set title font
        title.setFont(Font.loadFont(ClassLoader.getSystemClassLoader().getResourceAsStream(FONT), 16));
        
        //Create label by a text string which tells the user to write the query
        //in the following text field
        Label queryString = new Label("Here:");
        
        //Set query string font
        queryString.setFont(Font.loadFont(ClassLoader.getSystemClassLoader().getResourceAsStream(FONT), 14));
        
        //Add all labels and button created to gridPane specifying column and row index (last 2
        //colspan and rowspan arguments are optional)
        gridPane.add(title, 6, 0, 2, 1);
        gridPane.add(queryString, 4, 1, 2, 2);
        gridPane.add(text, 6, 1, 2, 2);
        gridPane.add(fullScanButton, 6, 3, 1, 1);
        gridPane.add(quickScanButton, 7, 3, 1, 1);
        gridPane.add(status, 6, 4, 2, 2);
        
        //Set gridPane vertical and horizontal gap between nodes
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        
        //Create the container for all content, assign gridPane to it and
        //specifying width and height values
        Scene scene = new Scene(gridPane, 330, 160);

        //Set application title
        primaryStage.setTitle("Question Answering");
        
        //Set scene as application scene 
        primaryStage.setScene(scene);
        
        //Set not resizable window
        primaryStage.setResizable(false);
        
        //Show application window
        primaryStage.show();
    }
}
