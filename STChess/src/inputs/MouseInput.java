package inputs;

import org.lwjgl.input.Mouse;

import util.Vector2d;

public class MouseInput {
	private Vector2d previousPos, currentPos;
	
	public MouseInput(Vector2d previousPos, Vector2d currentPos) {
		this.previousPos = previousPos;
		this.currentPos = currentPos;
	}
	
	public void init() {
		
	}
	
	public void update() {
		previousPos = currentPos;
		currentPos = new Vector2d(Mouse.getDX(), Mouse.getDY());
	}

	public Vector2d getPreviousPos() {
		return previousPos;
	}

	public Vector2d getCurrentPos() {
		return currentPos;
	}
	
	public Vector2d getChangeInPos() {
		return currentPos.subtract(previousPos);
	}
}
