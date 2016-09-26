package test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import com.school.game.Area;
import com.school.game.Bundle;
import com.school.game.FloorTile;
import com.school.game.SpookySchool;
import com.school.game.WallTile;

public class ChangeTester {

	public static void main(String[] args) {
		SpookySchool game = new SpookySchool();

		game.addPlayer("player");

		Bundle bundle = convertFromObject(game.getBundle("player"));

		System.out.println(bundle.getPlayerID());

		Area area = bundle.getNewArea();

		for (int row = 0; row < area.height; row++) {
			for (int col = 0; col < area.width; col++) {

				if (area.getArea()[row][col] == null) {
					System.out.print("N");
				} else if (area.getArea()[row][col] instanceof FloorTile) {
					System.out.print("F");
				} else if (area.getArea()[row][col] instanceof WallTile) {
					System.out.print("W");
				}
			}
			System.out.println();
		}

		System.out.println(bundle.getChanges());

	}


	public static Bundle convertFromObject(byte[] yourBytes) {
		ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			Object o = in.readObject();
			return (Bundle) o;

		} catch (IOException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				// ignore close exception
			}
		}

		return null;
	}

}
