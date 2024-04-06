package com.yourname.pacman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class KeyboardController extends InputAdapter {
    private MyGdxGame game;

    public KeyboardController(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                game.diChuyenY = game.tocDo;
                game.currentDirection = MyGdxGame.Direction.UP;
                break;
            case Input.Keys.DOWN:
                game.diChuyenY = -game.tocDo;
                game.currentDirection = MyGdxGame.Direction.DOWN;
                break;
            case Input.Keys.LEFT:
                game.diChuyenX = -game.tocDo;
                game.currentDirection = MyGdxGame.Direction.LEFT;
                break;
            case Input.Keys.RIGHT:
                game.diChuyenX = game.tocDo;
                game.currentDirection = MyGdxGame.Direction.RIGHT;
                break;
            case Input.Keys.Q:
                game.isAttacking = true;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
            case Input.Keys.DOWN:
                game.diChuyenY = 0;
                // Đặt trạng thái IDLE nếu không còn di chuyển
                if (game.diChuyenX == 0) {
                    game.currentDirection = MyGdxGame.Direction.IDLE;
                }
                break;
            case Input.Keys.LEFT:
            case Input.Keys.RIGHT:
                game.diChuyenX = 0;
                // Đặt trạng thái IDLE nếu không còn di chuyển
                if (game.diChuyenY == 0) {
                    game.currentDirection = MyGdxGame.Direction.IDLE;
                }
                break;
            case Input.Keys.Q:
                game.isAttacking = false;
                break;
        }
        return true;
    }
}
