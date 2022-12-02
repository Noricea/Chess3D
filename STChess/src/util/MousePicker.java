package util;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Playfield;

public class MousePicker {

	private Vector3f currentRay;

	private Matrix4f projectionMatrix, viewMatrix;
	private Camera camera;

	public MousePicker(Camera camera, Matrix4f projectionMatrix) {
		this.camera = camera;
		this.projectionMatrix = projectionMatrix;
		this.viewMatrix = TransMatrices.createViewMatrix(camera);
	}

	public Vector3f getCurrentRay() {
		return currentRay;
	}

	public void update() {
		viewMatrix = TransMatrices.createViewMatrix(camera);
		currentRay = calculateMouseRay();

	};

	public boolean pointsAt(Vector3f position) {
		// position ?= viewMatrix + REACH * currentRay;
		float distance = getDistance(camera.getPosition(), position);

		float x = camera.getPosition().x + (distance * currentRay.x);
		float y = camera.getPosition().y + (distance * currentRay.y);
		float z = camera.getPosition().z + (distance * currentRay.z);

		if (x < (position.x + 0.011f) && x > (position.x - 0.011f)) {
			if (y < (position.y + 0.0065f) && y > (position.y)) {
				if (z < (position.z + 0.011f) && z > (position.z - 0.011f)) {
					return true;
				}
			}
		}
		return false;
	}

	public Vector3f getField() {
		Playfield pf = new Playfield();
		float lambda = 0f;
		Vector3f ray;
		
		for (float height = 0.15f; height >= -0.1f; height -= 0.05f) {
			lambda = (height - camera.getPosition().y) / currentRay.y;
			
			ray = new Vector3f(camera.getPosition().x + (lambda * currentRay.x), camera.getPosition().y + (lambda * currentRay.y),
					camera.getPosition().z + (lambda * currentRay.z));			
			if(pf.onField(ray)) {
				return ray;
			}
		}
		
		return new Vector3f(
				camera.getPosition().x + (currentRay.x * 0.5f),
				camera.getPosition().y + (currentRay.y * 0.5f), 
				camera.getPosition().z + (currentRay.z * 0.5f));
	}

	public float getDistance(Vector3f pos1, Vector3f pos2) {
		return (float) Math
				.sqrt(Math.pow((pos2.x - pos1.x), 2) + Math.pow((pos2.y - pos1.y), 2) + Math.pow((pos2.z - pos1.z), 2));
	}

	private Vector3f calculateMouseRay() {
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
		Vector4f eyeCoords = getEyeCoords(clipCoords);
		Vector3f worldRay = getWorldCoords(eyeCoords);
		return worldRay;
	}

	private Vector3f getWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}

	private Vector4f getEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

	private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY) {
		float x = (2f * mouseX) / Display.getWidth() - 1f;
		float y = (2f * mouseY) / Display.getHeight() - 1f;
		return new Vector2f(x, y);
	}
}
