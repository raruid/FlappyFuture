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
public class WinScreen implements Screen{
    
        final MyGdxGame game;
	OrthographicCamera camera;
        Texture backgroundImage;

	public WinScreen(final MyGdxGame gam) {
		game = gam;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
                backgroundImage = new Texture(Gdx.files.internal("win.jpg"));

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
                game.batch.draw(backgroundImage, 0, 0, 800, 480);
		game.font.draw(game.batch, "YOU HAVE WIN ", 100, 200);
                game.font.draw(game.batch, "YOU REACH THE MAX SCORE ", 40, 150);
		game.batch.end();

		if (Gdx.input.isTouched()) {
                    try {
                        game.setScreen(new GameScreen(game));
                    } catch (IOException ex) {
                        Logger.getLogger(WinScreen.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(WinScreen.class.getName()).log(Level.SEVERE, null, ex);
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
