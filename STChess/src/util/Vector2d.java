package util;

public class Vector2d {
	public int x, y;

	public Vector2d(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2d add(Vector2d vec2) {
		return new Vector2d(this.x + vec2.x, this.y + vec2.y);
	}
	
	public Vector2d subtract(Vector2d vec2) {
		return new Vector2d(this.x - vec2.x, this.y - vec2.y);
	}
}
