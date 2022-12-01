package render;

import texture.TextureHandler;

public class Model {
	
	private int vaoID;
	private int vertexCount;
	private TextureHandler texture;
	
	public Model(int vaoID, int vertexCount, TextureHandler texture) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.texture = texture;
	}
	
	public Model(Model model, TextureHandler texture) {
		this.vaoID = model.getVaoID();
		this.vertexCount = model.getVertexCount();
		this.texture = texture;
	}
	
	public Model(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.texture = new TextureHandler(-1);
	}

	public TextureHandler getTexture() {
		return texture;
	}
	
	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
}
