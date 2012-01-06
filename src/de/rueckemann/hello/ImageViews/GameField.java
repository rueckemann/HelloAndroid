package de.rueckemann.hello.ImageViews;

import de.rueckemann.hello.R;
import android.content.Context;
import android.widget.ImageView;

public class GameField extends ImageView {

	private Position pos;
	private GameFieldType type;
	private boolean blocked;
	private boolean movable;

	public GameField(Context context, int row, int col, GameFieldType type) {
		super(context);
		this.pos = new Position(row,col);
		this.type = type;
		setImageResource(type);
	}


	
	private void setImageResource(GameFieldType type2) {
		switch (type) {
		case PLAYER : 
			setImageResource(R.drawable.player1);
			break;
	    case BRICK :
	    	setImageResource(R.drawable.brick);
	    	blocked = true;
	    	break;
	    case GRASS :
			setImageResource(R.drawable.grass);
			break;
	    case BRIDGE :
			setImageResource(R.drawable.bridge);
			movable = true;
			break;
	    case WATER :
			setImageResource(R.drawable.water);
			blocked = true;
			break;

	    default : 
	    	break;
	    } 
		setAdjustViewBounds(true); // set the ImageView bounds to match the Drawable's dimensions
	}



	public Position getPosition() {
		return pos;
	}

	public GameFieldType getType() {
		return type;
	}
	
	public boolean isBlocked() {
		return blocked;
	}

	public boolean isMovable() {
		return movable;
	}
	

	public void setFixed() {
		movable=false;
	}

}
