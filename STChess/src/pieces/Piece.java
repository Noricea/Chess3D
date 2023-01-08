package pieces;

import util.Vector3i;

import java.io.IOException;

import org.lwjgl.util.vector.Vector3f;

import entities.Object;
import render.Model;
import render.ObjectLoader;
import texture.TextureHandler;

public class Piece {

	private Object figure;
	private Vector3i position;

	public Piece(ObjectLoader objLoader, String figure, String colour) {

		this.figure = new Object(
				new Model(objLoader.loadOBJModel(figure), new TextureHandler(objLoader.loadTexture(colour))),
				new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0f, 0f), 1f);
	}

	public Object getFigure() {
		return figure;
	}

	public Vector3i getPosition() {
		return position;
	}

	public void setPosition(int x, int y, int z) {
		this.position = new Vector3i(x, y, z);
		this.figure.setPos((float) (x * 0.04f) - 0.1f, (float) (y * 0.05f) - 0.1f, (float) (-z * 0.04f) + 0.22f);
	}

	public void setPosition(Vector3f pos) {
		this.position = new Vector3i(Math.round((pos.x + 0.1f) / 0.04f), Math.round((pos.y + 0.1f) / 0.05f),
				Math.round(-(pos.z - 0.22f) / 0.04f));
		this.figure.setPos((float) (position.x * 0.04f) - 0.1f, (float) (position.y * 0.05f) - 0.1f,
				(float) (-position.z * 0.04f) + 0.22f);
	}
}
