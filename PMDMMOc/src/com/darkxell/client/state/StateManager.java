package com.darkxell.client.state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.mechanics.animation.AnimationTicker;
import com.darkxell.client.resources.images.FrameResources;

public class StateManager {

	private AbstractState currentState;

	public void onKeyPressed(KeyEvent e, short key) {
		if (this.currentState != null)
			this.currentState.onKeyPressed(key);
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			Launcher.chatbox.send();
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			Launcher.chatbox.textfield.pressLeft();
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			Launcher.chatbox.textfield.pressRight();
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
			Launcher.chatbox.textfield.pressDelete();
	}

	public void onKeyReleased(KeyEvent e, short key) {
		if (this.currentState != null)
			this.currentState.onKeyReleased(key);
	}

	public void onKeyTyped(KeyEvent e) {
		if (e.getKeyChar() != '\b')
			Launcher.chatbox.textfield.insertString(e.getKeyChar() + "");
	}

	public void onMouseClick(int x, int y) {
		if (this.currentState != null)
			this.currentState.onMouseClick(x, y);
	}

	public void onMouseMove(int x, int y) {
		if (this.currentState != null)
			this.currentState.onMouseMove(x, y);
	}

	public void onMouseRightClick(int x, int y) {
		if (this.currentState != null)
			this.currentState.onMouseRightClick(x, y);
	}

	private BufferedImage internalBuffer;
	private int displayWidth = (int) (256 * 1.6);
	private int displayHeight = (int) (192 * 1.6);

	/**
	 * Sets the resolution of the internal display. Bigger means a zoomed out
	 * game. Defaults at [256*192]*2 (Official DS resolution * 2).
	 */
	public void setInternalDisplaySize(int w, int h) {
		displayHeight = h;
		displayWidth = w;
		internalBuffer = null;
	}

	public void render(Graphics2D g, int width, int height) {
		if (width == 0)
			return;
		if (internalBuffer == null)
			internalBuffer = new BufferedImage(displayWidth, displayHeight, BufferedImage.TYPE_INT_ARGB);
		// Displays the game on the buffer
		Graphics2D g2 = internalBuffer.createGraphics();
		g2.clearRect(0, 0, displayWidth, displayHeight);
		if (this.currentState != null)
			this.currentState.render(g2, displayWidth, displayHeight);
		g2.dispose();
		// Calculates various values to draw the components to the window
		int gamewidth = (int) (0.6 * height * displayWidth / displayHeight <= 0.6 * width
				? 0.6 * height * displayWidth / displayHeight : 0.6 * width);
		int gameheight = gamewidth * displayHeight / displayWidth;
		int gamex = (int) (width * 0.35);
		int gamey = (int) (height * 0.35);
		int mapsize = (int) (gamewidth / 2 >= height * 0.25 ? height * 0.25 : gamewidth / 2);
		int mapx = (int) (width * 0.95) - mapsize < gamex + gamewidth - mapsize ? (int) (width * 0.95) - mapsize
				: gamex + gamewidth - mapsize;
		int mapy = (int) (height * 0.05);
		int chatwidth = (int) (width * 0.25);
		int chatheight = (int) (height * 0.9);
		int chatx = (int) (width * 0.05);
		int chaty = (int) (height * 0.05);
		// Draws the background
		drawBackground(g, width, height);
		// Draw the outlines
		drawBoxOutline(g, gamex, gamey, gamewidth, gameheight);
		drawBoxOutline(g, mapx, mapy, mapsize, mapsize);
		drawBoxOutline(g, chatx, chaty, chatwidth, chatheight);
		// draws the components insides
		g.drawImage(internalBuffer, gamex, gamey, gamewidth, gameheight, null);
		g.translate(chatx, chaty);
		Shape clp = g.getClip();
		g.setClip(new Rectangle(0, 0, chatwidth, chatheight));
		Launcher.chatbox.render(g, chatwidth, chatheight);
		g.setClip(clp);
		g.translate(-chatx, -chaty);
		g.setColor(Color.BLACK);
		g.fillRect(mapx, mapy, mapsize, mapsize);
	}

	private void drawBoxOutline(Graphics2D g, int x, int y, int width, int height) {
		g.drawImage(FrameResources.box_NW, x - 16, y - 10, 16, 10, null);
		g.drawImage(FrameResources.box_NE, x + width, y - 10, 16, 10, null);
		g.drawImage(FrameResources.box_SW, x - 16, y + height, 16, 10, null);
		g.drawImage(FrameResources.box_SE, x + width, y + height, 16, 10, null);
		g.drawImage(FrameResources.box_N, x, y - 10, width, 10, null);
		g.drawImage(FrameResources.box_S, x, y + height, width, 10, null);
		g.drawImage(FrameResources.box_W, x - 16, y, 16, height, null);
		g.drawImage(FrameResources.box_E, x + width, y, 16, height, null);
	}

	public byte backgroundID = 1;

	private void drawBackground(Graphics2D g, int fwidth, int fheight) {
		BufferedImage img;
		switch (backgroundID) {
		case 2:
			img = FrameResources.BG2;
			break;
		case 3:
			img = FrameResources.BG3;
			break;
		case 4:
			img = FrameResources.BG4;
			break;
		case 5:
			img = FrameResources.BG5;
			break;
		case 6:
			img = FrameResources.BG6;
			break;
		case 7:
			img = FrameResources.BG7;
			break;
		default:
			img = FrameResources.BG1;
			break;
		}
		if ((float) (fwidth) / (float) (fheight) < (float) (img.getWidth()) / (float) (img.getHeight())) {
			int drawwidth = fheight * img.getWidth() / img.getHeight();
			g.drawImage(img, 0, 0, drawwidth, fheight, null);
		} else {
			int drawheight = fwidth * img.getHeight() / img.getWidth();
			g.drawImage(img, 0, 0, fwidth, drawheight, null);
		}
	}

	public void randomizeBackground() {
		this.backgroundID = (byte) ((new Random().nextInt(7) + 1) % 7);
	}

	public AbstractState getCurrentState() {
		return this.currentState;
	}

	public void setState(AbstractState state) {
		if (this.currentState != null)
			this.currentState.onEnd();
		this.currentState = state;
		this.currentState.onStart();
	}

	public synchronized void update() {
		AnimationTicker.instance.update();
		if (this.currentState != null)
			this.currentState.update();
	}
}
