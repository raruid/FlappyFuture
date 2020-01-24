/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cristorey;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class GameScreen implements Screen{
  	final MyGdxGame game;

	Texture dropImage;
	Texture bucketImage;
        Texture bucketImage2;
        Texture bucketImage3;
        Texture backgroundImage;
        BitmapFont font3;
	Sound dropSound;
	Music rainMusic;
	OrthographicCamera camera;
	Rectangle bucket;
	Array<Rectangle> raindrops;
	long lastDropTime;
	int dropsGathered;
        int hightScore;
        boolean parar = false;
        
        
        final float GRAVITY = -18f;
        final int DISTANCIA = 650;
        final int SALTO = 300;
        float yVELOCITY = 0;

	public GameScreen(final MyGdxGame gam) throws IOException, FileNotFoundException, ClassNotFoundException {
            
		this.game = gam;
                
                leerScore();

		// load the images for the droplet and the bucket, 64x64 pixels each
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
                bucketImage2 = new Texture(Gdx.files.internal("bucket2.png"));
                bucketImage3 = new Texture(Gdx.files.internal("bucket3.png"));
                backgroundImage = new Texture(Gdx.files.internal("fondo.jpg"));

		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		rainMusic.setLooping(true);

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
               

		// create a Rectangle to logically represent the bucket
		bucket = new Rectangle();
		bucket.x = 30; // center the bucket horizontally
		bucket.y = 480 / 2 - 64 / 2; // bottom left corner of the bucket is 20 pixels above
						// the bottom screen edge
		bucket.width = 60;
		bucket.height = 50;

		// create the raindrops array and spawn the first raindrop
		raindrops = new Array<Rectangle>();
		spawnRaindrop();

	}
        
        public void escribirScore(int dropsGathered, int hightScore) throws FileNotFoundException, IOException{
            if(dropsGathered >= hightScore){
                File borrar = new File("FichHightSco.dat");
                borrar.delete();
                
                File fichero = new File("FichHightSco.dat");//declara el fichero
                FileOutputStream fileout = new FileOutputStream(fichero, true);  //crea el flujo de salida
                ObjectOutputStream dataOS = new ObjectOutputStream(fileout); 
 
                dataOS.writeInt(dropsGathered);
                
                dataOS.close();
            }else{
                hightScore = dropsGathered;
                escribirScore(dropsGathered, hightScore);
            }
        }
        
        public void leerScore() throws FileNotFoundException, IOException, ClassNotFoundException{
	File fichero = new File("FichHightSco.dat");
        if(fichero.length() > 0){
            ObjectInputStream dataIS = new ObjectInputStream(new FileInputStream(fichero));

            int scoriso = (int) dataIS.readInt();;
            this.hightScore = scoriso;
            System.out.println("jvgjhvh"+hightScore);

            dataIS.close(); // cerrar stream de entrada
        }            
        }

	private void spawnRaindrop() {
                
                int distancia_y;
                distancia_y = MathUtils.random(-450, -250);
            
		Rectangle raindrop1 = new Rectangle();
                Rectangle raindrop2 = new Rectangle();      

		raindrop1.x = 800;
		raindrop1.y = distancia_y;
                
                raindrop2.x = 800;
                raindrop2.y = distancia_y + DISTANCIA;
                
		raindrop1.width = 58;
		raindrop1.height = 500;
                
                raindrop2.width = 58;
                raindrop2.height = 500;
                
		raindrops.add(raindrop1);
                raindrops.add(raindrop2);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render(float delta) {
            
                yVELOCITY = yVELOCITY + GRAVITY;
                float y = bucket.getY();
                
                float yChange = yVELOCITY * delta;
                bucket.setPosition(bucket.x, y + yChange);
            
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);
            
		// begin a new batch and draw the bucket and
		// all drops
		game.batch.begin();
                game.batch.draw(backgroundImage, 0, 0, 800, 480);
		game.font.draw(game.batch, "Score: " + dropsGathered, 0, 480);
                //game.font.draw(game.batch, "Vidas: " + vidas, 5, 180);
		game.batch.draw(bucketImage, bucket.x, bucket.y);
                if(yVELOCITY > -125 && yVELOCITY < 125){
                    game.batch.draw(bucketImage, bucket.x, bucket.y);
                }else if(yVELOCITY > 0){
                    game.batch.draw(bucketImage2, bucket.x, bucket.y);
                }else if(yVELOCITY < 0){
                    game.batch.draw(bucketImage3, bucket.x, bucket.y);
                }
		for (Rectangle raindrop : raindrops) {
			game.batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		game.batch.end();

		// process user input
		if (Gdx.input.isTouched()) {
                    bucket.y += SALTO * Gdx.graphics.getDeltaTime();
                    yVELOCITY = 350;
		}
                
		if (Gdx.input.isKeyJustPressed(Keys.UP)){
                    bucket.y += SALTO * Gdx.graphics.getDeltaTime();
                    yVELOCITY = 350;
                }

		// make sure the bucket stays within the screen bounds

		if (bucket.y > 480 - 64)
			bucket.y = 480 - 64;

		// check if we need to create a new raindrop
		if (TimeUtils.nanoTime() - lastDropTime > 1150000000){
			spawnRaindrop();
                }

		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the later case we play back
		// a sound effect as well.
		Iterator<Rectangle> iter = raindrops.iterator();
                Boolean cuentoAhora = true;
		while (iter.hasNext()) {
			Rectangle raindrop = iter.next();
			
                        if(dropsGathered > 5 && dropsGathered <= 10){
                            raindrop.x -= 400 * Gdx.graphics.getDeltaTime();
                        }else if(dropsGathered > 10 && dropsGathered <= 50){
                            raindrop.x -= 500 * Gdx.graphics.getDeltaTime();
                        }else if(dropsGathered > 50 && dropsGathered <= 90){
                            raindrop.x -= 700 * Gdx.graphics.getDeltaTime();
                        }else if(dropsGathered > 90){
                            raindrop.x -= 850 * Gdx.graphics.getDeltaTime();
                        }else{
                            raindrop.x -= 300 * Gdx.graphics.getDeltaTime();
                        }
                        
			if (raindrop.x + 64 < 0){
				iter.remove();
                                if(cuentoAhora == true){

                                dropsGathered = dropsGathered + 1;
                                cuentoAhora = false;
                            } else {
                               cuentoAhora = true; 
                            }
                        }
                            
                        
                        if(bucket.y < 0){
                            
                            try {
                                escribirScore(dropsGathered, hightScore);
                                
                            } catch (IOException ex) {
                                Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            game.setScreen(new GameOverScreen(this.game, dropsGathered, hightScore));
                            this.dispose();
                        }
			if (raindrop.overlaps(bucket)) {
                            
                            try {
                                escribirScore(dropsGathered, hightScore);
                            } catch (IOException ex) {
                                Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
				game.setScreen(new GameOverScreen(this.game, dropsGathered, hightScore));
                                this.dispose();
                                
			}
                        if(dropsGathered == 100){
                             try {
                                escribirScore(dropsGathered, hightScore);
                            } catch (IOException ex) {
                                Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            game.setScreen(new WinScreen(this.game));
                            this.dispose();
                        }
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		rainMusic.play();
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
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
	}    
}
