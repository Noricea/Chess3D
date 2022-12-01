package render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Object;
import entities.Light;
import shader.StaticShader;

public class RenderHandler {

	private StaticShader shader = new StaticShader();
	private Renderer renderer = new Renderer(shader);
	
	private Map<Model, List<Object>> objects = new HashMap<Model, List<Object>>();
	
	
	public void render(Light light, Camera camera) {
		renderer.prepare();
		shader.start();
		shader.loadLight(light);
		shader.loadViewMatrix(camera);
		renderer.render(objects);
		shader.stop();
		objects.clear();
	}
	
	public void processObject(Object object) {
		Model model = object.getModel();
		List<Object> batch = objects.get(model);
		if(batch!=null) {
			batch.add(object);
		}
		else {
			List<Object> newBatch = new ArrayList<Object>();
			newBatch.add(object);
			objects.put(model, newBatch);
		}
	}
	
	public Matrix4f getProjectionMatrix() {
		return renderer.getProjectionMatrix();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
}
