package com.ldjam.ld39.sgilhuly;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Note {
	public int track;
	public int bar;
	public float beat;
	public float position;
	public float time;
	public float showtime;
	public boolean done;
	
	public Sprite spr;
	
	public Note(int track, int bar, float beat, int beatcount) {
		this.track = track;
		this.bar = bar;
		this.beat = beat;
		position = (this.bar - 1) * beatcount + (this.beat - 1);
	}
	
	public void calculateTime(float bps, float offset) {
		time = (position / bps) + offset;
		showtime = ((position - Constants.NOTE_BEATS_SHOWN) / bps) + offset;
	}
	
	public void prepareToDraw(Texture tex) {
		spr = new Sprite(tex);
		done = false;
	}
	
	public boolean isShown(float currentTime) {
		return currentTime >= showtime;
	}
	
	public void draw(float currentTime, float speed, SpriteBatch batch) {
		float progress = Helper.untween(currentTime, showtime, time);
		float distance = (1 - progress) * Constants.TRACK_END_SCALE + 1;
		float posy = Helper.calcHeight(distance);
		float xbase = 0, xtop = 0;
		switch(track) {
		case RhythmScreen.LEFT:
			xbase = Constants.TRACK_LEFT_BASE_X;
			xtop = Constants.TRACK_LEFT_TOP_X;
			break;
		case RhythmScreen.MIDDLE:
			xbase = Constants.TRACK_BASE_X;
			xtop = Constants.TRACK_BASE_X;
			break;
		case RhythmScreen.RIGHT:
			xbase = Constants.TRACK_RIGHT_BASE_X;
			xtop = Constants.TRACK_RIGHT_TOP_X;
			break;
		}
		float posx = Helper.tween(Helper.untween(posy, Constants.TRACK_BASE_Y, Constants.TRACK_TOP_Y), xbase, xtop);
		// Calculate opacity, fade in, and fade out if wasn't hit
		float x = progress - 0.55f;
		float opacity = Helper.clamp(5 * (1 - x * x / 0.25f), 0, 1);
		spr.setScale(0.5f / distance);
		//spr.set
		spr.setPosition(posx - (spr.getWidth() / 2), posy - (spr.getHeight() / 2));
		spr.draw(batch, opacity);
		done = progress > 1 && opacity <= 0;
	}
}
