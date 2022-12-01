package entities;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Playfield {

	private static final float WIDTH = 0.04f;
	private static final float HEIGHT = 0.05f;

	private Vector4f TOP = new Vector4f(-0.08f, 0.1f, -0.2f, 4);
	private Vector4f MID = new Vector4f(-0.08f, 0f, -0.08f, 4);
	private Vector4f BOTTOM = new Vector4f(-0.08f, -0.1f, 0.04f, 4);
	private Vector4f TLEFT = new Vector4f(-0.12f, 0.15f, -0.24f, 2);
	private Vector4f TRIGHT = new Vector4f(0.04f, 0.15f, -0.24f, 2);
	private Vector4f BLEFT = new Vector4f(-0.12f, -0.05f, -0.2f, 2);
	private Vector4f BRIGHT = new Vector4f(0.04f, -0.05f, -0.2f, 2);

	//hi hier stimmt was nicht, schau nochmal die Limits an
	
	public boolean onField(Vector3f pos) {
		if (pos.y > TLEFT.y - 0.025f && pos.y <= TLEFT.y + 0.025f) {			
			return ((pos.x >= TLEFT.x 
					&& pos.x <= TLEFT.x + (WIDTH * TLEFT.w + 1) 
					&& pos.z >= TLEFT.z
					&& pos.z <= TLEFT.z + (WIDTH * TLEFT.w + 1))
					|| (pos.x >= TRIGHT.x 
					&& pos.x <= TRIGHT.x + (WIDTH * TRIGHT.w + 1) 
					&& pos.z >= TRIGHT.z
					&& pos.z <= TRIGHT.z + (WIDTH * TRIGHT.w + 1)));			
		} else if (pos.y > TOP.y - 0.025f && pos.y <= TOP.y + 0.025f) {			
			return (pos.x >= TOP.x 
					&& pos.x <= TOP.x + (WIDTH * TOP.w + 1) 
					&& pos.z >= TOP.z
					&& pos.z <= TOP.z + (WIDTH * TOP.w + 1));			
		} else if (pos.y > MID.y - 0.025f && pos.y <= MID.y + 0.025f) {			
			return (pos.x >= MID.x 
					&& pos.x <= MID.x + (WIDTH * (MID.w + 1)) 
					&& pos.z >= MID.z
					&& pos.z <= MID.z + (WIDTH * (MID.w + 1)));			
		} else if (pos.y > BLEFT.y - 0.025f && pos.y <= BLEFT.y + 0.025f) {			
			return ((pos.x >= BLEFT.x 
					&& pos.x <= BLEFT.x + (WIDTH * BLEFT.w + 1) 
					&& pos.z >= BLEFT.z
					&& pos.z <= BLEFT.z + (WIDTH * BLEFT.w + 1))
					|| (pos.x >= BRIGHT.x 
					&& pos.x <= BRIGHT.x + (WIDTH * BRIGHT.w + 1) 
					&& pos.z >= BRIGHT.z
					&& pos.z <= BRIGHT.z + (WIDTH * BRIGHT.w + 1)));
		} else if (pos.y > BOTTOM.y - 0.025f && pos.y <= BOTTOM.y + 0.025f) {			
			return (pos.x >= BOTTOM.x 
					&& pos.x <= BOTTOM.x + (WIDTH * BOTTOM.w + 1) 
					&& pos.z >= BOTTOM.z
					&& pos.z <= BOTTOM.z + (WIDTH * BOTTOM.w + 1));			
		}   else {
			return false;
		}
	}
}
