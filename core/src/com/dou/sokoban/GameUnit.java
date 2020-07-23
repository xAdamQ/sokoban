package com.dou.sokoban;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

enum UnitType {
	Ground, Wall, Box, Print, Player, BoxOnPrint, PlayerOnPrint,
}

public class GameUnit {

	public final Vector2 Poz;

	public Image Image;
	public UnitType Type;

	Texture wallTexture = new Texture("img/block_05.png");
	Texture playerTexture = new Texture("img/playerFace.png");
	Texture groundTexture = new Texture("img/ground_01.png");
	Texture boxTexture = new Texture("img/crate_42.png");
	Texture printTexture = new Texture("img/ground_04.png");
	Texture boxOnPrintTexture = new Texture("img/crate_45.png");
	Texture playerOnPrintTexture = new Texture("img/playerFace.png");

	Texture[] UnitTextures = new Texture[] { groundTexture, wallTexture, boxTexture, printTexture, playerTexture,
			boxOnPrintTexture, playerOnPrintTexture };

	public GameUnit(UnitType type, Vector2 poz) {
		Type = type;
		Image = new Image(UnitTextures[type.ordinal()]);

		Poz = poz;
		Image.setPosition(Poz.x * Sokoban.UnitSize, Poz.y * Sokoban.UnitSize);
	}

	public void SetType(UnitType type) {
		Type = type;
		Image.setDrawable(new SpriteDrawable(new Sprite(UnitTextures[type.ordinal()])));
	}

}
