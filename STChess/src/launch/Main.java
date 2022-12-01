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
		List<Object> objects = new ArrayList<Object>();

		// Setup
		RenderHandler renderer = new RenderHandler();
		ObjectLoader objLoader = new ObjectLoader();
		Camera camera = new Camera();
		Light light = new Light(new Vector3f(0, 30, 20), new Vector3f(1, 1, 1));
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix());

		Object board = new Object(
				new Model(objLoader.loadOBJModel("board"), new TextureHandler(objLoader.loadTexture("board"))),
				new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0f, 0f), 1f);
		Object t1_k = new Object(
				new Model(objLoader.loadOBJModel("king"), new TextureHandler(objLoader.loadTexture("white"))),
				new Vector3f(-0.1f, -0.1f, 0.22f), new Vector3f(0f, 0f, 0f), 1f);
		Object t2_k = new Object(
				new Model(objLoader.loadOBJModel("king"), new TextureHandler(objLoader.loadTexture("black"))),
				new Vector3f(-0.1f, -0.1f, 0.22f), new Vector3f(0f, 0f, 0f), 1f);

		t1_k.incPos(2 * TILE, 0, -TILE);
		t2_k.incPos(3 * TILE, 0, -TILE);

		objects.add(board);
		objects.add(t1_k);
		objects.add(t2_k);
		long cooldown = 0;

		while (!Display.isCloseRequested()) {
			// Prepare
			camera.move();
			picker.update();

			for (Object obj : objects) {

				if (obj != objects.get(0) && Mouse.isButtonDown(0) && ((System.nanoTime() - cooldown) / 1000000000 >= 1)) {
					if (obj.getInHand()) {
						obj.changeInHand();
						cooldown = System.nanoTime();
					} else if (!obj.getInHand() && picker.pointsAt(obj.getPos())) {
						obj.changeInHand();
						cooldown = System.nanoTime();
					}
				}
				if (obj.getInHand() && obj != objects.get(0)) {
					obj.setPos(camera.getPosition().x + picker.getCurrentRay().x,
							camera.getPosition().y + picker.getCurrentRay().y,
							camera.getPosition().z + picker.getCurrentRay().z);
				}
				renderer.processObject(obj);
			}
			renderer.render(light, camera);

			// Implement

			// Render
			DisplayManager.updateDisplay();
		}

		// Closing everything
		renderer.cleanUp();
		objLoader.cleanUp();
		DisplayManager.closeDisplay();
		System.exit(0);
	}

	/*
	 * List<Object> team = new ArrayList<Object>();
	 * 
	 * ObjectLoader objLoader = new ObjectLoader(); StaticShader shader = new
	 * StaticShader(); Renderer renderer = new Renderer(shader); Camera camera = new
	 * Camera(); Light light = new Light(new Vector3f(0, 30, 20), new Vector3f(1, 1,
	 * 1));
	 * 
	 * Object board = new Object( new Model(objLoader.loadOBJModel("board"), new
	 * TextureHandler(objLoader.loadTexture("board"))), new Vector3f(0f, 0f, 0f),
	 * new Vector3f(0f, 0f, 0f), 1f);
	 * 
	 * //White Object wking = new Object(objLoader.loadOBJModel("king"), new
	 * Vector3f(-0.1f, -0.1f, 0.22f), new Vector3f(0f, 0f, 0f), 1f); Object wqueen =
	 * new Object(objLoader.loadOBJModel("queen"), new Vector3f(-0.1f, -0.1f,
	 * 0.22f), new Vector3f(0f, 0f, 0f), 1f); Object[] wpawn = new Object[8];
	 * Object[] wrook = new Object[2]; Object[] wbishop = new Object[2]; Object[]
	 * wknight = new Object[2]; for (int i = 0; i < 8; i++) { if(i < 2) { wrook[i] =
	 * new Object(objLoader.loadOBJModel("rook"), new Vector3f(-0.1f, -0.1f, 0.22f),
	 * new Vector3f(0f, 0f, 0f), 1f); wbishop[i] = new
	 * Object(objLoader.loadOBJModel("bishop"), new Vector3f(-0.1f, -0.1f, 0.22f),
	 * new Vector3f(0f, 0f, 0f), 1f); wknight[i] = new
	 * Object(objLoader.loadOBJModel("knight"), new Vector3f(-0.1f, -0.1f, 0.22f),
	 * new Vector3f(0f, 180f, 0f), 1f); } wpawn[i] = new Object(new
	 * Model(objLoader.loadOBJModel("pawn"), new
	 * TextureHandler(objLoader.loadTexture("pawn"))), new Vector3f(-0.1f, -0.1f,
	 * 0.22f), new Vector3f(0f, 0f, 0f), 1f); if(i < 2) { wpawn[i].incPos(i*TILE,
	 * LEVEL, -TILE); } else if(i > 5) { wpawn[i].incPos((i-2)*TILE, LEVEL, -TILE);
	 * } else { wpawn[i].incPos((i-1)*TILE, 0, -2*TILE); } } wrook[0].incPos(0,
	 * LEVEL, 0); wrook[1].incPos(5*TILE, LEVEL, 0); wbishop[0].incPos(1*TILE, 0,
	 * -TILE); wbishop[1].incPos(4*TILE, 0, -TILE); wknight[0].incPos(TILE, LEVEL,
	 * 0); wknight[1].incPos(4*TILE, LEVEL, 0); wking.incPos(3*TILE, 0, -TILE);
	 * wqueen.incPos(2*TILE, 0, -TILE);
	 * 
	 * //Black Object bking = new Object(objLoader.loadOBJModel("king"), new
	 * Vector3f(-0.1f, -0.1f, 0.22f), new Vector3f(0f, 0f, 0f), 1f); Object bqueen =
	 * new Object(objLoader.loadOBJModel("queen"), new Vector3f(-0.1f, -0.1f,
	 * 0.22f), new Vector3f(0f, 0f, 0f), 1f); Object[] bpawn = new Object[8];
	 * Object[] brook = new Object[2]; Object[] bbishop = new Object[2]; Object[]
	 * bknight = new Object[2]; for (int i = 0; i < 8; i++) { if(i < 2) { brook[i] =
	 * new Object(objLoader.loadOBJModel("rook"), new Vector3f(-0.1f, -0.1f, 0.22f),
	 * new Vector3f(0f, 0f, 0f), 1f); bbishop[i] = new
	 * Object(objLoader.loadOBJModel("bishop"), new Vector3f(-0.1f, -0.1f, 0.22f),
	 * new Vector3f(0f, 0f, 0f), 1f); bknight[i] = new
	 * Object(objLoader.loadOBJModel("knight"), new Vector3f(-0.1f, -0.1f, 0.22f),
	 * new Vector3f(0f, 0f, 0f), 1f); } bpawn[i] = new Object(new
	 * Model(objLoader.loadOBJModel("pawn"), new
	 * TextureHandler(objLoader.loadTexture("pawn"))), new Vector3f(-0.1f, -0.1f,
	 * 0.22f), new Vector3f(0f, 0f, 0f), 1f); if(i < 2) { bpawn[i].incPos(i*TILE,
	 * 5*LEVEL, -10*TILE); } else if(i > 5) { bpawn[i].incPos((i-2)*TILE, 5*LEVEL,
	 * -10*TILE); } else { bpawn[i].incPos((i-1)*TILE, 4*LEVEL, -9*TILE); } }
	 * brook[0].incPos(0, 5*LEVEL, -11*TILE); brook[1].incPos(5*TILE, 5*LEVEL,
	 * -11*TILE); bbishop[0].incPos(1*TILE, 4*LEVEL, -10*TILE);
	 * bbishop[1].incPos(4*TILE, 4*LEVEL, -10*TILE); bknight[0].incPos(TILE,
	 * 5*LEVEL, -11*TILE); bknight[1].incPos(4*TILE, 5*LEVEL, -11*TILE);
	 * bking.incPos(3*TILE, 4*LEVEL, -10*TILE); bqueen.incPos(2*TILE, 4*LEVEL,
	 * -10*TILE);
	 * 
	 * 
	 * 
	 * ///////// // Prepare camera.move(); renderer.prepare(); shader.start();
	 * shader.loadLight(light); shader.loadViewMatrix(camera);
	 * 
	 * // Implement
	 * 
	 * // Render
	 * 
	 * renderer.render(board, shader);
	 * 
	 * //White for (int i = 0; i < 8; i++) { renderer.render(wpawn[i], shader); if(i
	 * < 2) { renderer.render(wrook[i], shader); renderer.render(wbishop[i],
	 * shader); renderer.render(wknight[i], shader); } } renderer.render(wking,
	 * shader); renderer.render(wqueen, shader);
	 * 
	 * //Black for (int i = 0; i < 8; i++) { renderer.render(bpawn[i], shader); if(i
	 * < 2) { renderer.render(brook[i], shader); renderer.render(bbishop[i],
	 * shader); renderer.render(bknight[i], shader); } } renderer.render(bking,
	 * shader); renderer.render(bqueen, shader);
	 * 
	 * // Unprepare shader.stop();
	 * 
	 * 
	 * 
	 * shader.cleanUp(); objLoader.cleanUp();
	 */

}