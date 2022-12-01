package pieces;

import util.Vector3i;

import org.lwjgl.util.vector.Vector3f;

import entities.Object;
import render.Model;
import render.ObjectLoader;

public abstract class Piece {
	private Object figure;
	private Vector3f position;
	private ObjectLoader obj;
	public Piece(String figure, Vector3f position) {
		super();
		this.figure = new Object(obj.loadOBJModel(figure), position, position, 1f);
		this.position = position;
	}
	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
}
