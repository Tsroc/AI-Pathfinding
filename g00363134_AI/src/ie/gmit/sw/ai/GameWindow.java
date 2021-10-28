package ie.gmit.sw.ai;

import java.util.List;

import ie.gmit.sw.ai.node.Node;
import ie.gmit.sw.ai.node.NodePos;
import ie.gmit.sw.ai.node.NodeTile;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
 * Main UI for the game. You should not have to alter anything in this class.
 * 
 */
public class GameWindow extends Application{
	private static final char PLAYER_ID = '1';
	private static final int DEFAULT_SIZE = 60;
	private static final int IMAGE_COUNT = 6;
	private GameView view;
	private Node pos = new NodePos();
	private Node tempPos = new NodePos();
	private GameModel model = GameModel.getInstance();
	private List<NodeTile> neighbours; 
	private boolean alive = true;

	@Override
    public void start(Stage stage) throws Exception {
    	view = new GameView(model); //Create a view of the model

    	stage.setTitle("GMIT - B.Sc. in Computing (Software Development) - AI Assignment 2021");
		stage.setWidth(600);
		stage.setHeight(630);
		stage.setOnCloseRequest((e) -> model.tearDown()); //Shut down the executor service
    	
		VBox box = new VBox();
		Scene scene = new Scene(box);
		scene.setOnKeyPressed(e -> keyPressed(e)); //Add a key listener
		stage.setScene(scene);
		
    	Sprite[] sprites = getSprites(); //Load the sprites from the res directory
    	view.setSprites(sprites); //Add the sprites to the view
    	placePlayer(); //Add the player

    	// NOTE: This has been changed because the player was not being seen with previous design
    	// Need to make sure that the player cannot be replaced by the enemies
    	model.addGameCharacters();
    	box.getChildren().add(view);
		
    	view.draw(); //Paint the view
    	
		//Display the window
		stage.show();
		stage.centerOnScreen();
	}
	
    public void keyPressed(KeyEvent e) { //Handle key events
    	KeyCode key = e.getCode(); 
		tempPos.setPos(pos.getRow(), pos.getCol());
    	
		if(alive) {
			// Changing controls to allow for WASD.
			if ((key == KeyCode.D && pos.getCol() < DEFAULT_SIZE - 1)|| 
				(key == KeyCode.RIGHT && pos.getCol() < DEFAULT_SIZE - 1)){
				if (model.isValidMove(pos.getRow(), pos.getCol(), pos.getRow(), pos.getCol() + 1, PLAYER_ID)) { 
					tempPos.setCol(tempPos.getCol() + 1);
					pos.setPos(tempPos.getRow(), tempPos.getCol());
				}
			}else if ((key == KeyCode.A && pos.getCol() > 0)||
					(key == KeyCode.LEFT && pos.getCol() < DEFAULT_SIZE - 1)){
				if (model.isValidMove(pos.getRow(), pos.getCol(), pos.getRow(), pos.getCol() - 1, PLAYER_ID)) {
					tempPos.setCol(tempPos.getCol() - 1);
					pos.setPos(tempPos.getRow(), tempPos.getCol());
				}
			}else if ((key == KeyCode.W && pos.getRow() > 0)||
					(key == KeyCode.UP && pos.getRow() < DEFAULT_SIZE - 1)){
				if (model.isValidMove(pos.getRow(), pos.getCol(), pos.getRow() - 1, pos.getCol(), PLAYER_ID)) {
					tempPos.setRow(tempPos.getRow() - 1);
					pos.setPos(tempPos.getRow(), tempPos.getCol());
				}        	
			}else if ((key == KeyCode.S && pos.getRow() < DEFAULT_SIZE - 1)||
					(key == KeyCode.DOWN && pos.getRow() < DEFAULT_SIZE - 1)){
				if (model.isValidMove(pos.getRow(), pos.getCol(), pos.getRow() + 1, pos.getCol(), PLAYER_ID)) {
					tempPos.setRow(tempPos.getRow() + 1);
					pos.setPos(tempPos.getRow(), tempPos.getCol());
				}        	
			}else if (key == KeyCode.Z){
				view.toggleZoom();
			}else{
				return;
			}
		} else {
			if (key == KeyCode.Z){
				view.toggleZoom();
			} else {
				System.out.println("You are dead!");
			}
		}
        
        updateView();       
    }
	
	private void placePlayer(){  //Place the main player character	
		// Making sure the player does not spawn at the edge of the map,
		// This causes problems with the A* implementation. 
    	pos.setRow((int) ((DEFAULT_SIZE-6) * Math.random())+3);
    	pos.setCol((int) ((DEFAULT_SIZE-6) * Math.random())+3);
    	model.set(pos.getRow(), pos.getCol(), PLAYER_ID); //Player is at index 1
    	updateView(); 		
	}
	
	private void updateView(){ 
		if(alive) {
			neighbours = model.getNeighbours(model.get(pos.getRow(), pos.getCol()));
			for (int i = 0; i < neighbours.size(); i++) {
				if(neighbours.get(i).getTile() == '5') {
					model.set(pos.getRow(), pos.getCol(), '\u0020');
					alive = false;
				}
			}
		}
		
		view.setCurrentRow(pos.getRow());
		view.setCurrentCol(pos.getCol());
	}
	
	private Sprite[] getSprites() throws Exception{
		/*
		 * Read in the images from the resources directory as sprites. Each sprite is
		 * referenced by its index in the array, e.g. a 3 implies a Pink Enemy... Ideally, 
		 * the array should dynamically created from the images... 
		 */
		Sprite[] sprites = new Sprite[IMAGE_COUNT];
		sprites[0] = new Sprite("Player", "/res/player-0.png", "/res/player-1.png", "/res/player-2.png", "/res/player-3.png", "/res/player-4.png", "/res/player-5.png", "/res/player-6.png", "/res/player-7.png");
		sprites[1] = new Sprite("Red Enemy", "/res/red-0.png", "/res/red-1.png", "/res/red-2.png", "/res/red-3.png", "/res/red-4.png", "/res/red-5.png", "/res/red-6.png", "/res/red-7.png");
		sprites[2] = new Sprite("Pink Enemy", "/res/pink-0.png", "/res/pink-1.png", "/res/pink-2.png", "/res/pink-3.png", "/res/pink-4.png", "/res/pink-5.png", "/res/pink-6.png", "/res/pink-7.png");
		sprites[3] = new Sprite("Blue Enemy", "/res/blue-0.png", "/res/blue-1.png", "/res/blue-2.png", "/res/blue-3.png", "/res/blue-4.png", "/res/blue-5.png", "/res/blue-6.png", "/res/blue-7.png");
		sprites[4] = new Sprite("Red Green Enemy", "/res/gred-0.png", "/res/gred-1.png", "/res/gred-2.png", "/res/gred-3.png", "/res/gred-4.png", "/res/gred-5.png", "/res/gred-6.png", "/res/gred-7.png");
		sprites[5] = new Sprite("Orange Enemy", "/res/orange-0.png", "/res/orange-1.png", "/res/orange-2.png", "/res/orange-3.png", "/res/orange-4.png", "/res/orange-5.png", "/res/orange-6.png", "/res/orange-7.png");		return sprites;
	}
}