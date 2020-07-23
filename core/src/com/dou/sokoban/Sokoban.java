package com.dou.sokoban;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Input;

public class Sokoban extends ApplicationAdapter {

	Stage stage;

	GameUnit[][] Units;
	public static int UnitSize = 60;

	int LevelIndex = 6;
	String[] Levels = new String[] {

			// 1
			"   ####\n" + "   #@ ##\n" + "   ##. #\n" + "  ## $ #\n" + "###  $ #\n" + "#    ###\n" + "#     .#\n"
					+ "########",
			// 2
			"  ####\n" + "  #. #\n" + " ##  #\n" + " #   #\n" + "##$*@#\n" + "#   ##\n" + "#   #\n" + "#####",
			// 3
			"     ###\n" + "     #.#\n" + "    ##.#\n" + "    #  #\n" + "#####  ##\n" + "# $ $   #\n" + "#   @$. #\n"
					+ "#########",
			// 4
			"    ####\n" + "    #. #\n" + "   ##  #\n" + "  ##  .#\n" + "###@$. #\n" + "#  $ $##\n" + "#     #\n"
					+ "#######",
			// 5
			"####\n" + "# .##\n" + "#$  ##\n" + "#+$  ##\n" + "## *  #\n" + " ##   #\n" + "  #####",
			// 6
			" ###\n" + "##.#\n" + "# .##\n" + "#   ##\n" + "#    ##\n" + "## $$ #\n" + " ##  @#\n" + "  #####",
			// 7
			"  ###\n" + "###@###\n" + "#.    #\n" + "###. $##\n" + "  # $  #\n" + "  ##   #\n" + "   #####",
			// 8
			"  ######\n" + "###.   ##\n" + "#   ##$@#\n" + "#     $ #\n" + "###  ####\n" + "  #  #\n" + "  #.##\n"
					+ "  ###",
			// 9
			"#########\n" + "#.  . $@#\n" + "### $####\n" + "  #  #\n" + "  #  #\n" + "  ####",
			// 10
			"  ######\n" + "###.$.@##\n" + "#   ##$ #\n" + "#       #\n" + "###  ####\n" + "  #  #\n" + "  ####", };

	@Override
	public void create() {

		stage = new Stage(new ScreenViewport());

		String[] levelRows = Levels[LevelIndex].split("\n");
		Vector2 levelSize = new Vector2(-1, levelRows.length);
		for (int i = 0; i < levelRows.length; i++) {
			if (levelRows[i].length() > levelSize.x)
				levelSize.x = levelRows[i].length();
		}
		// get level dimensions

		for (int y = 0; y < levelSize.y; y++) {
			for (int x = levelRows[y].length(); x < levelSize.x; x++) {
				levelRows[y] += " ";
			}
		}
		// fill all rows with spaces

		System.out.print(levelRows[4]);

		// make units
		Units = new GameUnit[(int) levelSize.y][];
		// the level is not square it's array of rows, so the y then the x
		for (int i = 0; i < levelSize.y; i++) {
			Units[i] = new GameUnit[(int) levelSize.x];
		}
		// init array with rect size

		for (int y = 0; y < levelSize.y; y++) {
			for (int x = 0; x < Units[y].length; x++) {
				switch (levelRows[y].charAt(x)) {

				case '#':// wall
					Units[y][x] = new GameUnit(UnitType.Wall, new Vector2(x, y));
					break;

				case ' ':// ground
					Units[y][x] = new GameUnit(UnitType.Ground, new Vector2(x, y));
					break;

				case '.':// print
					Units[y][x] = new GameUnit(UnitType.Print, new Vector2(x, y));
					break;

				case '@':// char
					Units[y][x] = new GameUnit(UnitType.Player, new Vector2(x, y));
					PlayerPoz = new Vector2(x, y);
					break;

				case '$':// box
					Units[y][x] = new GameUnit(UnitType.Box, new Vector2(x, y));
					BoxesCount++;
					break;

				case '*':// box on print
					Units[y][x] = new GameUnit(UnitType.BoxOnPrint, new Vector2(x, y));
					BoxesCount++;
					SolvedBoxes++;
					break;

				case '+':// char on print
					Units[y][x] = new GameUnit(UnitType.PlayerOnPrint, new Vector2(x, y));
					PlayerPoz = new Vector2(x, y);
					break;

				}
			}

		}

		for (int y = 0; y < levelSize.y; y++) {
			for (int x = 0; x < Units[y].length; x++) {
				if (Units[y][x] != null)
					stage.addActor(Units[y][x].Image);
			}
		}
	}

	Vector2 PlayerPoz;
	int SolvedBoxes, BoxesCount;

	void MovePlayer(Vector2 dir) {

		GameUnit playerUnit = GetUnit(PlayerPoz);

		Vector2 newPoz = new Vector2(PlayerPoz.x + dir.x, PlayerPoz.y + dir.y);
		GameUnit comingUnit = GetUnit(newPoz);

		if (comingUnit.Type == UnitType.Wall) {// wall
			return;
		} else if (comingUnit.Type == UnitType.Box || comingUnit.Type == UnitType.BoxOnPrint) {// box or box on print

			Vector2 newPozNext = new Vector2(comingUnit.Poz.x + dir.x, comingUnit.Poz.y + dir.y);
			GameUnit comingUnitNext = GetUnit(newPozNext);
			if (comingUnitNext.Type == UnitType.Box || comingUnitNext.Type == UnitType.BoxOnPrint
					|| comingUnitNext.Type == UnitType.Wall) {
				return;
			}

			if (comingUnitNext.Type == UnitType.Print) {
				comingUnitNext.SetType(UnitType.BoxOnPrint);
				SolvedBoxes++;
			} else {
				comingUnitNext.SetType(UnitType.Box);
			}

			if (comingUnit.Type == UnitType.BoxOnPrint) {
				comingUnit.SetType(UnitType.PlayerOnPrint);
				SolvedBoxes--;
			} else {
				comingUnit.SetType(UnitType.Player);
			}

		} else {// ground or print

			if (comingUnit.Type == UnitType.Print) {
				comingUnit.SetType(UnitType.PlayerOnPrint);
			} else {
				comingUnit.SetType(UnitType.Player);
			}

		}

		// if player will move it will reach this
		PlayerPoz = newPoz;
		if (playerUnit.Type == UnitType.PlayerOnPrint) {
			playerUnit.SetType(UnitType.Print);
		} else {
			playerUnit.SetType(UnitType.Ground);
		}

		if (SolvedBoxes == BoxesCount && LevelIndex != Levels.length - 1) {
			LevelIndex++;
			create();
		}
		System.out.print(String.valueOf(SolvedBoxes) + "\n");

	}

	GameUnit GetUnit(Vector2 poz) {
		// System.out.print(poz.toString() + '\n');
		// System.out.print(Units[(int) poz.y][(int) poz.x].Type.toString() + '\n');
		return Units[(int) poz.y][(int) poz.x];
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, .5f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			MovePlayer(new Vector2(-1, 0));
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			MovePlayer(new Vector2(1, 0));
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			MovePlayer(new Vector2(0, 1));
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			MovePlayer(new Vector2(0, -1));
		}

		stage.act();
		stage.draw();
	}

	// render
	// move while following laws

	@Override
	public void dispose() {
		stage.dispose();
	}
}
