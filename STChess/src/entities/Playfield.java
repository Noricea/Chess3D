package entities;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Playfield {

	private static final float WIDTH = 0.04f;
	private static final float HEIGHT = 0.05f;

	private Vector3f TOP = new Vector3f(-0.08f, 0.1f, -0.2f);
	private Vector3f MID = new Vector3f(-0.08f, 0f, -0.08f);
	private Vector3f BOTTOM = new Vector3f(-0.08f, -0.1f, 0.04f);
	private Vector3f TLEFT = new Vector3f(-0.12f, 0.15f, -0.24f);
	private Vector3f TRIGHT = new Vector3f(0.04f, 0.15f, -0.24f);
	private Vector3f BLEFT = new Vector3f(-0.12f, -0.05f, 0.16f);
	private Vector3f BRIGHT = new Vector3f(0.04f, -0.05f, 0.16f);
	
	public boolean onField(Vector3f pos) {
		if (pos.y > TLEFT.y - 0.025f && pos.y <= TLEFT.y + 0.025f) {			
			return ((pos.x >= TLEFT.x 
					&& pos.x <= TLEFT.x + 0.08f
					&& pos.z >= TLEFT.z
					&& pos.z <= TLEFT.z + 0.08f)
					|| pos.x >= TRIGHT.x 
					&& (pos.x <= TRIGHT.x + 0.08f
					&& pos.z >= TRIGHT.z
					&& pos.z <= TRIGHT.z + 0.08f));
		} else if (pos.y > TOP.y - 0.025f && pos.y <= TOP.y + 0.025f) {			
			return (pos.x >= TOP.x 
					&& pos.x <= TOP.x + 0.16f 
					&& pos.z >= TOP.z
					&& pos.z <= TOP.z + 0.16f);
		} else if (pos.y > MID.y - 0.025f && pos.y <= MID.y + 0.025f) {			
			return (pos.x >= MID.x 
					&& pos.x <= MID.x + 0.16f
					&& pos.z >= MID.z
					&& pos.z <= MID.z + 0.16f);			
		} else if (pos.y > BLEFT.y - 0.025f && pos.y <= BLEFT.y + 0.025f) {			
			return ((pos.x >= BLEFT.x 
					&& pos.x <= BLEFT.x + 0.08f
					&& pos.z >= BLEFT.z
					&& pos.z <= BLEFT.z + 0.08f)
					|| (pos.x >= BRIGHT.x 
					&& pos.x <= BRIGHT.x + 0.08f 
					&& pos.z >= BRIGHT.z
					&& pos.z <= BRIGHT.z + 0.08f));
		} else if (pos.y > BOTTOM.y - 0.025f && pos.y <= BOTTOM.y + 0.025f) {			
			return (pos.x >= BOTTOM.x 
					&& pos.x <= BOTTOM.x + 0.16f
					&& pos.z >= BOTTOM.z
					&& pos.z <= BOTTOM.z + 0.16f);			
		} else {
			return false;
		}
	}
}
