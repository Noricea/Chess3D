package render;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import util.Utils;
import util.Vector3i;

public class ObjectLoader {

	private List<Integer> vaos = new ArrayList<>();
	private List<Integer> vbos = new ArrayList<>();
	private List<Integer> textures = new ArrayList<Integer>();

	public Model loadModel(float[] vertices, float[] texCoord, float[] normals, int[] indices) {
		int id = createVAO();
		storeIndicesBuffer(indices);
		storeDataInAttribList(0, 3, vertices);
		storeDataInAttribList(1, 2, texCoord);
		storeDataInAttribList(2, 3, normals);
		unbind();
		return new Model(id, indices.length);
	}

	public int loadTexture(String fileName) {
		Texture texture = null;
		try {
			if (System.getProperty("os.name").toLowerCase().contains("nix") || System.getProperty("os.name").toLowerCase().contains("nux")) {
				texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + fileName + ".png"));
			}
			else {
				texture = TextureLoader.getTexture("PNG", new FileInputStream("STChess/res/" + fileName + ".png"));
			}
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ".png , didn't work");
			System.exit(-1);
		}
		
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}

	public Model loadOBJModel(String fileName) {

		if (System.getProperty("os.name").toLowerCase().contains("nix")
				|| System.getProperty("os.name").toLowerCase().contains("nux")) {
			fileName = "/" + fileName + ".obj";
		} else {
			fileName = "/" + fileName + ".obj";
		}

		List<String> lines = readAllLines(fileName);
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3i> faces = new ArrayList<>();

		for (String line : lines) {
			String[] tokens = line.split("\\s+");
			switch (tokens[0]) {
			case "v":
				Vector3f verticesVec = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]),
						Float.parseFloat(tokens[3]));
				vertices.add(verticesVec);
				break;
			case "vt":
				Vector2f texturesVec = new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
				textures.add(texturesVec);
				break;
			case "vn":
				Vector3f normalsVec = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]),
						Float.parseFloat(tokens[3]));
				normals.add(normalsVec);
				break;
			case "f":
				processFace(tokens[1], faces);
				processFace(tokens[2], faces);
				processFace(tokens[3], faces);
				if (tokens.length > 4) {
					processFace(tokens[1], faces);
					processFace(tokens[3], faces);
					processFace(tokens[4], faces);
				}
				break;
			default:
				break;
			}
		}
		
		List<Integer> indices = new ArrayList<>();
		float[] verticesArr = new float[faces.size()];
		int i = 0;
		for (Vector3f pos : vertices) {
			verticesArr[i * 3] = pos.x;
			verticesArr[i * 3 + 1] = pos.y;
			verticesArr[i * 3 + 2] = pos.z;
			i++;
		}

		float[] texCoordArr = new float[vertices.size() * 2];
		float[] normalArr = new float[vertices.size() * 3];

		for (Vector3i face : faces) {
			processVertex(face.x, face.y, face.z, textures, normals, indices, texCoordArr, normalArr);
		}

		int[] indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();

		return loadModel(verticesArr, texCoordArr, normalArr, indicesArr);
	}

	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	private void storeIndicesBuffer(int[] indices) {
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		IntBuffer buffer = Utils.storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	private void storeDataInAttribList(int attribNo, int vertexCount, float[] data) {
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		FloatBuffer buffer = Utils.storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attribNo, vertexCount, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	private void unbind() {
		GL30.glBindVertexArray(0);
	}

	public void cleanUp() {
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for (int tex : textures) {
			GL11.glDeleteTextures(tex);
		}
	}

	private static void processVertex(int pos, int texCoord, int normal, List<Vector2f> texCoordList,
			List<Vector3f> normalList, List<Integer> indicesList, float[] texCoordArr, float[] normalArr) {
		indicesList.add(pos);
		if (texCoord >= 0) {
			Vector2f texCoordVec = texCoordList.get(texCoord);
			texCoordArr[pos * 2] = texCoordVec.x;
			texCoordArr[pos * 2 + 1] = 1f - texCoordVec.y;
		}
		if (normal >= 0) {
			Vector3f normalVec = normalList.get(normal);
			normalArr[pos * 3] = normalVec.x;
			normalArr[pos * 3 + 1] = normalVec.y;
			normalArr[pos * 3 + 2] = normalVec.z;
		}
	}

	private static void processFace(String token, List<Vector3i> faces) {
		String[] lineToken = token.split("/");
		int length = lineToken.length;
		int pos = -1, coords = -1, normal = -1;
		pos = Integer.parseInt(lineToken[0]) - 1;
		if (length > 1) {
			String textCoord = lineToken[1];
			coords = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : -1;
			if (length > 2) {
				normal = Integer.parseInt(lineToken[2]) - 1;
			}
		}
		Vector3i facesVec = new Vector3i(pos, coords, normal);
		faces.add(facesVec);
	}

	public static List<String> readAllLines(String fileName) {
		List<String> list = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(Class.forName(ObjectLoader.class.getName()).getResourceAsStream(fileName)))) {
			String line;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}
}
