package pieces;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import entities.Object;
import render.ObjectLoader;
import render.RenderHandler;
import util.MousePicker;
import util.Vector3i;

public class Team {

	private String colour;
	private List<Piece> units = new ArrayList<Piece>();
	private boolean anyPicked = false;
	private Vector3i fallbackPosition;

	public Team(String colour) {
		this.colour = colour;
	}

	public void init(ObjectLoader objLoader, int[] startPosition) {
		units.add(new Piece(objLoader, "king", colour));
		units.add(new Piece(objLoader, "queen", colour));
		units.add(new Piece(objLoader, "bishop", colour));
		units.add(new Piece(objLoader, "bishop", colour));
		units.add(new Piece(objLoader, "knight", colour));
		units.add(new Piece(objLoader, "knight", colour));
		units.add(new Piece(objLoader, "rook", colour));
		units.add(new Piece(objLoader, "rook", colour));

		for (int i = 0; i < 8; i++) {
			units.add(new Piece(objLoader, "pawn", colour));
		}

		for (int i = 0; i < 16; i++) {
			units.get(i).setPosition(startPosition[i * 3], startPosition[i * 3 + 1], startPosition[i * 3 + 2]);
		}

		if (colour == "white") {
			units.get(4).getFigure().setRotation(0, 180, 0);
			units.get(5).getFigure().setRotation(0, 180, 0);
		}
	}

	public void render(RenderHandler renderer) {
		for (Piece unit : units) {
			renderer.processObject(unit.getFigure());
		}
	}

	public void verifyMove(Team enemies) {
		for (Piece unit : units) {
			if (unit.getFigure().getInHand()) {
				boolean freeSpot = true;
				for (Piece ally : units) {
					if (ally.getPosition().x == unit.getPosition().x && ally.getPosition().y == unit.getPosition().y
							&& ally.getPosition().z == unit.getPosition().z && ally != unit) {
						freeSpot = false;
					}
				}
				for (Piece enemy : enemies.getUnits()) {
					if (enemy.getPosition().x == unit.getPosition().x && enemy.getPosition().y == unit.getPosition().y
							&& enemy.getPosition().z == unit.getPosition().z) {
						enemy.setPosition(1000, 1000, 1000);
					}
				}

				if (freeSpot) {
					unit.getFigure().setInHand(false);
					anyPicked = false;
				}
			}
		}
	}

	public void isInFocus(MousePicker picker) {
		for (Piece unit : units) {
			if (!unit.getFigure().getInHand() && picker.pointsAt(unit.getFigure().getPos())) {
				fallbackPosition = unit.getPosition();
				unit.getFigure().setInHand(true);
				anyPicked = true;
				break;
			}
		}
	}

	public void dragAround(MousePicker picker) {
		if (anyPicked) {
			for (Piece unit : units) {
				if (unit.getFigure().getInHand()) {
					Vector3f pos = picker.getField();
					unit.setPosition(pos);
				}
			}
		}
	}

	public List<Piece> getUnits() {
		return units;
	}

	public boolean getPicked() {
		return anyPicked;
	}
}
