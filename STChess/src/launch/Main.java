package launch;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import entities.Object;
import pieces.Team;
import render.Model;
import render.ObjectLoader;
import render.RenderHandler;
import render.Renderer;
import shader.StaticShader;
import texture.TextureHandler;
import util.MousePicker;
import window.DisplayManager;

public class Main {

	public static final float LEVEL = 0.05f;
	public static final float TILE = 0.04f;

	public static void main(String[] args) throws Exception {
		// Debug infos
		System.out.print(System.getProperty("os.name") + " ");
		System.out.print(System.getProperty("java.version") + "\n");

		// OpenGL initialisation
		DisplayManager.createDisplay();

		// List of every Object
		List<Object> staticObjects = new ArrayList<Object>();
		List<Object> movableObjects = new ArrayList<Object>();

		int[] startWhite = { 2, 0, 1, // knight
				3, 0, 1, // queen
				1, 0, 1, // bishop
				4, 0, 1, // bishop
				1, 1, 0, // knight
				4, 1, 0, // knight
				0, 1, 0, // rook
				5, 1, 0, // rook
				0, 1, 1, // pawn
				1, 1, 1, // pawn
				1, 0, 2, // pawn
				2, 0, 2, // pawn
				3, 0, 2, // pawn
				4, 0, 2, // pawn
				4, 1, 1, // pawn
				5, 1, 1 // pawn
		};

		int[] startBlack = { 2, 4, 10, // knight
				3, 4, 10, // queen
				1, 4, 10, // bishop
				4, 4, 10, // bishop
				1, 5, 11, // knight
				4, 5, 11, // knight
				0, 5, 11, // rook
				5, 5, 11, // rook
				0, 5, 10, // pawn
				1, 5, 10, // pawn
				1, 4, 9, // pawn
				2, 4, 9, // pawn
				3, 4, 9, // pawn
				4, 4, 9, // pawn
				4, 5, 10, // pawn
				5, 5, 10 // pawn
		};

		Team teamWhite = new Team("white");
		Team teamBlack = new Team("black");

		// Setup
		long cooldown = 0;
		boolean switchTeam = true;
		boolean displayTable = false;

		RenderHandler renderer = new RenderHandler();
		ObjectLoader objLoader = new ObjectLoader();
		Camera camera = new Camera();
		Light light = new Light(new Vector3f(0, 30, 20), new Vector3f(1, 1, 1));
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix());

		Object board = new Object(
				new Model(objLoader.loadOBJModel("board"), new TextureHandler(objLoader.loadTexture("board"))),
				new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0f, 0f), 1f);
		staticObjects.add(board);
		
		if (displayTable) {
			Object table = new Object(
					new Model(objLoader.loadOBJModel("table"), new TextureHandler(objLoader.loadTexture("white"))),
					new Vector3f(0f, -0.2f, -0.7f), new Vector3f(0f, 0f, 0f), 1f);
			Object glass = new Object(
					new Model(objLoader.loadOBJModel("glass"), new TextureHandler(objLoader.loadTexture("white"))),
					new Vector3f(0f, -0.2f, -0.7f), new Vector3f(0f, 0f, 0f), 1f);
			staticObjects.add(table);
			staticObjects.add(glass);
		}



		teamWhite.init(objLoader, startWhite);
		teamBlack.init(objLoader, startBlack);

		while (!Display.isCloseRequested()) {
			// Prepare
			camera.move();
			picker.update();

			for (Object obj : staticObjects) {
				renderer.processObject(obj);
			}

			if (Mouse.isButtonDown(0) && ((System.nanoTime() - cooldown) / 1000000000 >= 0.5f)) {
				cooldown = System.nanoTime();
				if(teamWhite.getPicked()) {
					teamWhite.verifyMove(teamBlack);
				}
				else if (teamBlack.getPicked()) {
					teamBlack.verifyMove(teamWhite);
				}
				else {
					teamWhite.isInFocus(picker);
					teamBlack.isInFocus(picker);
				}
			}

			teamWhite.dragAround(picker);
			teamBlack.dragAround(picker);

			// Render
			teamWhite.render(renderer);
			teamBlack.render(renderer);
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}

		// Closing everything
		renderer.cleanUp();
		objLoader.cleanUp();
		DisplayManager.closeDisplay();
		System.exit(0);
	}
}