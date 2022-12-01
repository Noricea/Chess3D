package texture;

public class TextureHandler {
	private int textureID;

	private float shineDamper = 10;
	private float reflectivity = 10;
	
	public TextureHandler(int texture){
		this.textureID = texture;
	}
	
	public int getID(){
		return textureID;
	}
	
	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
}
