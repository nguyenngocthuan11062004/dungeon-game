package com.yourname.pacman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture pacmanTexture;
    float x, y;
    float speed = 200;
    // Thêm các biến để lưu trạng thái di chuyển
    float moveX = 0, moveY = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        pacmanTexture = new Texture("pacman.jpg");
        x = Gdx.graphics.getWidth() / 2 - pacmanTexture.getWidth() / 2;
        y = Gdx.graphics.getHeight() / 2 - pacmanTexture.getHeight() / 2;

        for (Controller controller : Controllers.getControllers()) {
            controller.addListener(new ControllerAdapter() {
                @Override
                public boolean axisMoved(Controller controller, int axisIndex, float value) {
                    if (Math.abs(value) > 0.2) { // Deadzone
                        if (axisIndex == 0) {
                            moveX = value * speed; 
                        } else if (axisIndex == 1) {
                            moveY = -value * speed; 
                        }
                    } else {
                        if (axisIndex == 0) moveX = 0;
                        if (axisIndex == 1) moveY = 0;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Cập nhật vị trí dựa trên trạng thái di chuyển
        x += moveX * Gdx.graphics.getDeltaTime();
        y += moveY * Gdx.graphics.getDeltaTime();

        batch.begin();
        batch.draw(pacmanTexture, x, y);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        pacmanTexture.dispose();
    }
}
