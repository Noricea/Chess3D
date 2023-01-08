package entities;

import org.lwjgl.util.vector.Vector3f;

import render.Model;

public class Object {
	private Model model;
	private Vector3f pos, rotation;
	private float scale;
	private boolean inHand = false;
	
	public Object(Model model, Vector3f pos, Vector3f rotation, float scale) {
		super();
		this.model = model;
		this.pos = pos;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public void incPos(float x, float y, float z) {
		this.pos.x += x;
		this.pos.y += y;
		this.pos.z += z;
	}
	
	public void setPos(float x, float y, float z) {
		this.pos.x = x;
		this.pos.y = y;
		this.pos.z = z;
	}
	
	public void setPos(Vector3f pos) {
		this.pos.x = pos.x;
		this.pos.y = pos.y;
		this.pos.z = pos.z;
	}
	
	public void incRotation(float x, float y, float z) {
		this.rotation.x += x;
		this.rotation.y += y;
		this.rotation.z += z;
	}
	
	public void setRotation(float x, float y, float z) {
		this.rotation.x = x;
		this.rotation.y = y;
		this.rotation.z = z;
	}

	public Model getModel() {
		return model;
	}

	public Vector3f getPos() {
		return pos;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}
	
	public boolean getInHand() {
		return inHand;
	}
	
	public void setInHand(boolean inHand) {
		this.inHand = inHand;
	}
	
	public void changeInHand() {
		inHand = !inHand;
	}
}
