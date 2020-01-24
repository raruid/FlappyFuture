/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cristorey;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class GameOverScreen implements Screen{
    
        final MyGdxGame game;
	OrthographicCamera camera;
        Texture backgroundImage;
        int score;
        int hight;

	public GameOverScreen(final MyGdxGame gam, int score, int hightScore) {
		game = gam;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
                backgroundImage = new Texture(Gdx.files.internal("gameover.jpg"));
                this.score = score;
                this.hight = hightScore;

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
                game.batch.draw(backgroundImage, 0, 0, 800, 480);
		game.font.draw(game.batch, "Game Over...!!! ", 100, 300);
                game.font.draw(game.batch, "Your score:  " + score, 100, 250);
                game.font.draw(game.batch, "Your best score is:  " + hight, 100, 200);
		game.font.draw(game.batch, "Tap anywhere to begin!", 100, 50);
		game.batch.end();

		if (Gdx.input.isTouched()) {
                    try {
                        game.setScreen(new GameScreen(game));
                    } catch (IOException ex) {
                        Logger.getLogger(GameOverScreen.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(GameOverScreen.class.getName()).log(Level.SEVERE, null, ex);
                    }
			dispose();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}    
    
}
