package it.chalmers.tendu.screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class OnScreenText{
  private String text;
  private Vector2 position;
  private int width;
  private int height;

  public OnScreenText(String text,Vector2 position){
      this.text = text;
      this.position = position;
  }

  public void draw(SpriteBatch spriteBatch,BitmapFont font){
      width=(int)font.getBounds(text).width; //Get the width of the text we draw using the current font
      height=(int)font.getBounds(text).height; //Get the height of the text we draw using the current font
      font.draw(spriteBatch,text,position.x,
              position.y
              );
  }
  
public boolean collided(Vector2 touchPos) {
	if (touchPos.x > position.x && touchPos.x < position.x+width) {
		if (touchPos.y < position.y && touchPos.y > position.y-height) {
			return true;
		}
	}

	return false;
}
  public String getText() {
      return text;
  }

  public void setText(String text) {
      this.text = text;
  }

  public Vector2 getPosition() {
      return position;
  }

  public void setPosition(Vector2 position) {
      this.position = position;
  }

  public int getWidth() {
      return width;
  }

  public int getHeight() {
      return height;
  }

}