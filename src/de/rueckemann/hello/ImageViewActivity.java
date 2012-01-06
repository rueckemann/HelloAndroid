package de.rueckemann.hello;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import de.rueckemann.hello.ImageViews.GameField;
import de.rueckemann.hello.ImageViews.GameFieldType;
import de.rueckemann.hello.ImageViews.Position;

public class ImageViewActivity extends MenuActivity {
	private static final String TAG = "ImageViewActivity";	
	
	enum Direction {
		RIGHT, LEFT, UP, DOWN
	}

	LinearLayout baseLayout;
	TableLayout gameBoard;
	
	int cols = 15;
	int rows = 20;
	Position playerPosition = null;
	Map<Position, GameField> originalFields = new HashMap<Position, GameField>();
	
	//private OnClickListener gameFieldClickListener;

	private OnClickListener moveClickListener;

	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	    
	    
	    initListeners();
	    baseLayout = new LinearLayout(this);
	    baseLayout.setOrientation(LinearLayout.VERTICAL);
	    
	    gameBoard = new TableLayout(this);
	    baseLayout.addView(gameBoard);
	    
	    baseLayout.addView(prepareButtons());
	    
		drawGameBoard();
	    drawLevel();
		this.setContentView(baseLayout);
	    
	    
	}


	private void drawLevel() {
		replaceField(5,5,GameFieldType.BRICK);
		replaceField(15,5,GameFieldType.BRICK);
		
		replaceField(5,6,GameFieldType.WATER);
		replaceField(5,7,GameFieldType.WATER);
		replaceField(5,8,GameFieldType.WATER);
		replaceField(5,9,GameFieldType.WATER);
		replaceField(6,6,GameFieldType.WATER);
		replaceField(6,7,GameFieldType.WATER);
		replaceField(6,8,GameFieldType.WATER);
		replaceField(6,9,GameFieldType.WATER);
		replaceField(7,6,GameFieldType.WATER);
		replaceField(5,6,GameFieldType.WATER);
		
		replaceField(12,4,GameFieldType.BRIDGE);
		replaceField(12,5,GameFieldType.BRIDGE);
		replaceField(12,6,GameFieldType.BRIDGE);
		replaceField(10,6,GameFieldType.BRIDGE);
		
		setPlayer(10,5);		
	}


	private LinearLayout prepareButtons() {
		LinearLayout buttonBar = new LinearLayout(this);
		
		final ImageButton buttonRight = new ImageButton(this);
		buttonRight.setTag(Direction.RIGHT);
		buttonRight.setImageResource(R.drawable.move_right);
		buttonRight.setOnClickListener(moveClickListener);
		buttonBar.addView(buttonRight);
		
		final ImageButton buttonLeft = new ImageButton(this);
		buttonLeft.setTag(Direction.LEFT);
		buttonLeft.setImageResource(R.drawable.move_left);
		buttonLeft.setOnClickListener(moveClickListener);
		buttonBar.addView(buttonLeft);

		final ImageButton buttonUp = new ImageButton(this);
		buttonUp.setTag(Direction.UP);
		buttonUp.setImageResource(R.drawable.move_up);
		buttonUp.setOnClickListener(moveClickListener);
		buttonBar.addView(buttonUp);

		final ImageButton buttonDown = new ImageButton(this);
		buttonDown.setTag(Direction.DOWN);
		buttonDown.setImageResource(R.drawable.move_down);
		buttonDown.setOnClickListener(moveClickListener);
		buttonBar.addView(buttonDown);

		return buttonBar;
	}



	private void initListeners() {
//		gameFieldClickListener = new OnClickListener() {
//			public void onClick(View arg0) {
//				GameField field = ((GameField)arg0);
//				Toast.makeText(ImageViewActivity.this.getApplicationContext(), "I'm a " + field.getType() + " at Pos: " + field.getPosition(), Toast.LENGTH_SHORT).show();
//			}
//		};
		
		moveClickListener = new OnClickListener() {
	          public void onClick(View v) {
	        	    final Direction direction = (Direction)((ImageButton)v).getTag();  
	        	    move(direction);
		          }
		      }; 		
	}
	
	
	private void drawGameBoard() {
		for (int row = 0; row < this.rows; row++) {
		    final TableRow tableRow = new TableRow(this);
		    GameField field =  null;
		    for (int col = 0; col < this.cols; col++) {
				GameFieldType type;
		    	if(row == 0 || row == this.rows-1 || col == 0 || col == this.cols-1) {
		    	  type = GameFieldType.BRICK;
				} else {
				  type = GameFieldType.GRASS;
				}
		    	field = new GameField(this, row, col, type);
		    	//field.setOnClickListener(gameFieldClickListener);
				originalFields.put(new Position(row,col), field);
		    	tableRow.addView(field);
			}			
		    gameBoard.addView(tableRow);
		}
	}
	
	/**
	 * initially set the player to a GameField on in the board
	 * @param row
	 * @param col
	 */
	private void setPlayer(int row, int col) {
		Position pos = new Position(row,col);
		this.playerPosition = pos;
		this.originalFields.put(pos,getFieldAt(pos));
		replaceField(row,col,GameFieldType.PLAYER);
	}

	private void move(Direction direction) {
		final Position newPlayerPosition = getNewPosition(playerPosition, direction);
		
		final GameField fieldAtNewPlayerPos = getFieldAt(newPlayerPosition);
		
		if(fieldAtNewPlayerPos != null) {
			if(fieldAtNewPlayerPos.isBlocked()) {
				Toast.makeText(this, "Can't go this way!", Toast.LENGTH_SHORT).show();
			} else if(fieldAtNewPlayerPos.isMovable()) {
				// player is pushing something
				final Position newPushPosition = getNewPosition(newPlayerPosition, direction);
				final GameField fieldAtNewPushPosition = getFieldAt(newPushPosition);
				
				// bridge can be moved into water
				if(fieldAtNewPushPosition.getType().equals(GameFieldType.WATER) && fieldAtNewPlayerPos.getType().equals(GameFieldType.BRIDGE)) {
					moveAndPush(newPlayerPosition, fieldAtNewPlayerPos, newPushPosition);
			    } else if(fieldAtNewPushPosition.isBlocked() || fieldAtNewPushPosition.isMovable() || getFieldAt(getNewPosition(newPushPosition,direction)).isMovable()) {
					Toast.makeText(this, "The way is blocked!", Toast.LENGTH_SHORT).show();
				} else {
					moveAndPush(newPlayerPosition, fieldAtNewPlayerPos, newPushPosition);
				}
			} else {	
				//player is just moving
				movePlayer(newPlayerPosition, fieldAtNewPlayerPos);
			}
		}
	}


	private void moveAndPush(final Position newPlayerPosition, final GameField fieldAtNewPlayerPos, final Position newPushPosition) {
		GameField originalFieldAtPushTarget = getFieldAt(newPushPosition);
		// @TODO strange: this should work with just fieldAtNewPos 
		// this might be due to a reference counter on the View object which is not null when the view is stored in a HashMap
		replaceField(newPushPosition, fieldAtNewPlayerPos.getType());
		movePlayer(newPlayerPosition, fieldAtNewPlayerPos);
		
		// when we push something into water it gets unmovable
		if(originalFieldAtPushTarget.getType().equals(GameFieldType.WATER)) {
			getFieldAt(newPushPosition).setFixed();
		}
	}


	private void movePlayer(final Position newPosition, final GameField fieldAtNewPos) {
		// restore the original field at to current player position
		GameField player = getFieldAt(playerPosition);
		replaceField(playerPosition, originalFields.get(playerPosition));
		// move the player to the new position
		playerPosition = newPosition;
		replaceField(newPosition, player);
	}


	/**
	 * calculate the new positon dependand on the direction the player wants to move
	 * @param direction
	 * @return
	 */
	private Position getNewPosition(Position sourcePosition, Direction direction) {
		int row = sourcePosition.getRow();
		int col = sourcePosition.getCol();
		
		switch (direction) {
		case RIGHT : 
			col++;
			break;
		case LEFT : 
			col--;
			break;
		case UP :
			row--;
			break;
		case DOWN :
			row++;
			break;
		}
		return new Position(row, col);
	}

	private void replaceField(Position pos, GameField field) {
		((TableRow)((TableLayout)gameBoard).getChildAt(pos.getRow())).removeViewAt(pos.getCol());
		((TableRow)((TableLayout)gameBoard).getChildAt(pos.getRow())).addView(field, pos.getCol());
	}

	private GameField getFieldAt(Position pos) {
		return getFieldAt(pos.getRow(), pos.getCol());
	}
	
	private GameField getFieldAt(int row, int col) {
		return ((GameField)((TableRow)((TableLayout)gameBoard).getChildAt(row)).getChildAt(col));
	}
	
	private void replaceField(int row, int col, GameFieldType type) {
		((TableRow)((TableLayout)gameBoard).getChildAt(row)).removeViewAt(col);
		addField(row, col, type);
	}

	private void replaceField(Position pos, GameFieldType type) {
		replaceField(pos.getRow(), pos.getCol(), type);
	}

	private void addField(int row, int col, GameFieldType type) {
		GameField view = new GameField(this,row,col,type);
		((TableRow)((TableLayout)gameBoard).getChildAt(row)).addView(view, col);
	}
}

