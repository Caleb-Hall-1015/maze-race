/*
 * Class: GUI
 * 
 * Purpose: Display the maze and players
 * 
 * Attributes: StackPane stackPane
 *			   VBox flowPane
 *			   Button startBT
 *			   Button easyDifficultyBT
 *			   Button mediumDifficultyBT
 *		       Button hardDifficultyBT
 *			   GameController gameController
 *			   Circle player
 *			   Circle AI
 *			   ObservableList<Node> stackPaneChildren
 *			   ObservableList<Node> flowPaneChildren
 *			   Label winLabel
 *			   Label lossLabel
 *			   Label ratioLabel
 *			   Label timerLabel
 *			   Timer timer
 *			   long startTime
 * 
 * Methods: main(String[] args): void
 * 			start(Stage primaryStage): void
 * 			setPlayerLocation(): void
 * 			setAILocation(): void
 * 			game(Stage primaryStage): void
 * 			setWin(): void
 * 
 * Created 11/3/21
 * Updated 12/12/21
 */

import javafx.util.Duration;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.collections.*;
import javafx.geometry.Pos;

public class GUI extends Application
{
	private StackPane stackPane = new StackPane();
	private VBox flowPane = new VBox();
	private Button startBT = new Button();
	private Button easyDifficultyBT = new Button();
	private Button hardDifficultyBT = new Button();
	private GameController gameController = new GameController();
	private Circle player = new Circle(5);
	private Circle AI = new Circle(5);
	private ObservableList<Node> stackPaneChildren = stackPane.getChildren();
	private ObservableList<Node> flowPaneChildren = flowPane.getChildren();
	private Label winLabel = new Label();
	private Label lossLabel = new Label();
	private Label ratioLabel = new Label();
	private Label timerLabel = new Label();
	private Timer timer = new Timer();
	private long startTime;
	private TextField textField = new TextField();
	private Label nameLabel = new Label();
	private int playerNumber;
	private Button submitBT = new Button("Submit player number");
	private Label winnerLabel = new Label();
	private HBox winPane = new HBox();
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception
	{
		gameController.makeMaze();
		Scene MenuScene = new Scene(flowPane, 863, 663);
		startBT.setText("Click here to start!");
		easyDifficultyBT.setText("Select this button to begin the game with difficultly level: Easy");
		hardDifficultyBT.setText("Select this button to begin the game with difficultly level: Hard");
		startBT.setVisible(false);
		startBT.setOnMousePressed(j -> {
			easyDifficultyBT.setVisible(true);
			hardDifficultyBT.setVisible(true);
			startBT.setVisible(false);
		});
		
		easyDifficultyBT.setOnMousePressed(a -> {
			gameController.setDifficultyLevel(1);
			game(primaryStage);
		});
		
		hardDifficultyBT.setOnMousePressed(b -> {
			gameController.setDifficultyLevel(2);
			game(primaryStage);
		});
		
		submitBT.setOnMousePressed(d -> {
			//lets player put an int as their player number, but anything other than an int will cause an error
			//program catches that error and then defaults the player number to 1
			try
			{
				playerNumber = Integer.parseInt(textField.getText());
			} catch (NumberFormatException e)
			{
				playerNumber = 1;
			}
			
			startBT.setVisible(true);
			nameLabel.setVisible(false);
			textField.setVisible(false);
			submitBT.setVisible(false);
		});
		
		flowPane.setSpacing(50);
		
		startBT.setLayoutX(300);
		startBT.setLayoutY(300);
		
		easyDifficultyBT.setVisible(false);
		hardDifficultyBT.setVisible(false);

		primaryStage.setScene(MenuScene);
		
		flowPane.setAlignment(Pos.CENTER);
		
		nameLabel.setText("Enter a player number:");
		
		flowPaneChildren.addAll(nameLabel, textField, submitBT, startBT, easyDifficultyBT, hardDifficultyBT);
		
		primaryStage.setTitle("Maze Race");
		primaryStage.setResizable(false);
		primaryStage.show();
		gameController.resetMaze();
	}
	
	public void setPlayerLocation()
	{
		StackPane.setAlignment(player, Pos.TOP_LEFT);
		double[] locations = gameController.wherePlayer();
		player.setTranslateX(locations[0]);
		player.setTranslateY(locations[1]);
	}
	
	//https://stackoverflow.com/questions/14983706/javafx-stackpane-x-y-coordinates
	public void setAILocation()
	{
		StackPane.setAlignment(AI, Pos.TOP_LEFT);
		double[] locations = gameController.whereAI();
		AI.setTranslateX(locations[0]);
		AI.setTranslateY(locations[1]);
	}
	
	//plays the game using the timeline
	public void game(Stage primaryStage)
	{
		startTime = System.nanoTime();
		TimerTask task = new TimerTask()
		{
		        public void run()
		        {
		            gameController.makeMoves();      
		        }
		};
		//https://stackoverflow.com/questions/35512648/adding-a-timer-to-my-program-javafx
		timer.scheduleAtFixedRate(task, 0, 150l);
		
		StackPane.setAlignment(timerLabel, Pos.TOP_LEFT);
		timerLabel.setText("Time elapsed: " + (long) (System.nanoTime() - startTime));
		timerLabel.setTranslateX(725);
		timerLabel.setTranslateY(0);
		
		StackPane.setAlignment(winLabel, Pos.TOP_LEFT);
		winLabel.setText("Number of wins: " + gameController.getWins());
		winLabel.setTranslateX(725);
		winLabel.setTranslateY(100);
		
		StackPane.setAlignment(lossLabel, Pos.TOP_LEFT);
		lossLabel.setText("Number of losses: " + gameController.getLosses());
		lossLabel.setTranslateX(725);
		lossLabel.setTranslateY(300);
		
		StackPane.setAlignment(ratioLabel, Pos.TOP_LEFT);
		ratioLabel.setText("Win/loss ratio: " + gameController.getRatio() + "%");
		ratioLabel.setTranslateX(725);
		ratioLabel.setTranslateY(500);
		
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		Scene MazeScene = new Scene(stackPane, 863, 663);
		KeyFrame keyframe = new KeyFrame(Duration.millis(1), e -> 
		{
			timerLabel.setText("Time elapsed: " + (double) (System.nanoTime() - startTime) / 1000000000);
			//https://stackoverflow.com/questions/33224161/how-do-i-run-a-function-on-a-specific-key-press-in-javafx
			//https://discourse.processing.org/t/solution-for-repeating-keypressed-while-holding-key/3989
			MazeScene.setOnKeyReleased(w -> {
			    if (w.getCode() == KeyCode.UP) 
			    {
			        gameController.getPlayer().move(gameController.possibleMoves((player.getTranslateX() - 3.5) / 17, (player.getTranslateY() - 3.5) / 17), 0);
			        Rectangle r = new Rectangle(gameController.wherePlayer()[0], gameController.wherePlayer()[1], 17, 17);
					StackPane.setAlignment(r, Pos.TOP_LEFT);
			        r.setFill(Color.LIGHTBLUE);
			        r.setTranslateX(gameController.wherePlayer()[0] - 3.5);
			        r.setTranslateY(gameController.wherePlayer()[1] - 3.5);
			        stackPaneChildren.add(r);
			        player.toFront();
			    }
			    
			    if (w.getCode() == KeyCode.RIGHT) 
			    {
			        gameController.getPlayer().move(gameController.possibleMoves((player.getTranslateX() - 3.5) / 17, (player.getTranslateY() - 3.5) / 17), 1);
			        Rectangle r = new Rectangle(gameController.wherePlayer()[0], gameController.wherePlayer()[1], 17, 17);
					StackPane.setAlignment(r, Pos.TOP_LEFT);
			        r.setFill(Color.LIGHTBLUE);
			        r.setTranslateX(gameController.wherePlayer()[0] - 3.5);
			        r.setTranslateY(gameController.wherePlayer()[1] - 3.5);
			        stackPaneChildren.add(r);
			        player.toFront();
			    }
			    
			    if (w.getCode() == KeyCode.DOWN) 
			    {
			        gameController.getPlayer().move(gameController.possibleMoves((player.getTranslateX() - 3.5) / 17, (player.getTranslateY() - 3.5) / 17), 2);
			        Rectangle r = new Rectangle(gameController.wherePlayer()[0], gameController.wherePlayer()[1], 17, 17);
					StackPane.setAlignment(r, Pos.TOP_LEFT);
			        r.setFill(Color.LIGHTBLUE);
			        r.setTranslateX(gameController.wherePlayer()[0] - 3.5);
			        r.setTranslateY(gameController.wherePlayer()[1] - 3.5);
			        stackPaneChildren.add(r);
			        player.toFront();
			    }
			    
			    if (w.getCode() == KeyCode.LEFT) 
			    {
			        gameController.getPlayer().move(gameController.possibleMoves((player.getTranslateX() - 3.5) / 17, (player.getTranslateY() - 3.5) / 17), 3);
			        Rectangle r = new Rectangle(gameController.wherePlayer()[0], gameController.wherePlayer()[1], 17, 17);
					StackPane.setAlignment(r, Pos.TOP_LEFT);
			        r.setFill(Color.LIGHTBLUE);
			        r.setTranslateX(gameController.wherePlayer()[0] - 3.5);
			        r.setTranslateY(gameController.wherePlayer()[1] - 3.5);
			        stackPaneChildren.add(r);
			        player.toFront();
			    }
			});
			
			if (gameController.gameOver())
			{
				setWin();
				timeline.stop();
			}
			setAILocation();
			setPlayerLocation();
			Rectangle r = new Rectangle(gameController.whereAI()[0], gameController.whereAI()[1], 17, 17);
			StackPane.setAlignment(r, Pos.TOP_LEFT);
	        r.setFill(Color.PEACHPUFF);
	        r.setTranslateX(gameController.whereAI()[0] - 3.5);
	        r.setTranslateY(gameController.whereAI()[1] - 3.5);
	        stackPaneChildren.add(r);
	        AI.toFront();
	        player.toFront();
		});
		
		AI.setFill(Color.ORANGE);
		player.setFill(Color.BLUE);
		
		//39 cells to account for walls between the 20x20 maze, 17x17 pixels each
		//sets colors for the cells
		for (int y = 0; y < 663; y += 17)
		{
			for (int x = 0; x < 663; x += 17)
			{
				Rectangle r = new Rectangle(x, y, 17, 17);
				if (x == 0 && y == 0)
				{
					r.setFill(Color.GREEN);
				} else
				{
					if (x == 646 && y == 646)
					{
						r.setFill(Color.RED);
					} else
					{
						if (gameController.isCellWall(x / 17, y / 17))
						{
							r.setFill(Color.BLACK);
						} else
						{
							r.setFill(Color.WHITE);
						}
					}
				}
				StackPane.setAlignment(r, Pos.TOP_LEFT);
				r.setTranslateX(x);
				r.setTranslateY(y);
				stackPaneChildren.add(r);
			}
		}
		
		primaryStage.setScene(MazeScene);
		stackPaneChildren.add(timerLabel);
		stackPaneChildren.add(winLabel);
		stackPaneChildren.add(lossLabel);
		stackPaneChildren.add(ratioLabel);
		stackPaneChildren.add(AI);
		stackPaneChildren.add(player);
		timeline.getKeyFrames().add(keyframe);
		timeline.play();
	}
	
	public void setWin()
	{
		Stage winStage = new Stage();
		
		if (((gameController.whereAI()[0] - 3.5) / 17) == 38 && ((gameController.whereAI()[1] - 3.5) / 17) == 38)
		{
			winnerLabel.setText("GAME OVER! AI wins!");
		} else
		{
			winnerLabel.setText("GAME OVER! Player number " + playerNumber + " wins!");
		}
		
		winPane.setAlignment(Pos.CENTER);
		winPane.getChildren().add(winnerLabel);

		Scene winScene = new Scene(winPane, 250, 50);
		winStage.setScene(winScene);
		winStage.setTitle("Winner Announcement");
		winStage.show();
	}
}