package com.yourname.pacman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Animation<TextureRegion> chayLen, chayXuong, chayTrai, chayPhai;
    Animation<TextureRegion> dungImLen, dungImXuong, dungImTrai, dungImPhai;
    Animation<TextureRegion> attackUp, attackDown, attackLeft, attackRight;
    Animation<TextureRegion> hoatAnhHienTai;
    Texture anhChayLen, anhChayXuong, anhChayTrai, anhChayPhai;
    Texture anhDungImLen, anhDungImXuong, anhDungImTrai, anhDungImPhai;
    Texture anhAttackUp, anhAttackDown, anhAttackLeft, anhAttackRight;
    float viTriX, viTriY, thoiGianTrangThai = 0f;
    float diChuyenX = 0f, diChuyenY = 0f;
    float tocDo = 250f;
    boolean isAttacking = false;

    // Định nghĩa các hướng di chuyển
    enum Direction {
        UP, DOWN, LEFT, RIGHT, IDLE
    }

    // Biến lưu hướng di chuyển hiện tại
    Direction currentDirection = Direction.IDLE;

    @Override
    public void create() {
        Gdx.input.setInputProcessor(new KeyboardController(this));// chạy input bàn phím từ file bàn phím contro
        batch = new SpriteBatch();
        initializeAnimations();
        hoatAnhHienTai = dungImXuong; // Mặc định bắt đầu với hướng đứng yên xuống
        viTriX = Gdx.graphics.getWidth() / 2 - 20;
        viTriY = Gdx.graphics.getHeight() / 2 - 20;

        Controllers.addListener(new ControllerAdapter() {
            @Override
            public boolean buttonDown(Controller controller, int buttonCode) {
                if (buttonCode == 1) { // Giả sử mã cho nút X là 1
                    isAttacking = true;
                    return true;
                }
                return false;
            }

            @Override
            public boolean buttonUp(Controller controller, int buttonCode) {
                if (buttonCode == 1) {
                    isAttacking = false;
                    return true;
                }
                return false;
            }

            @Override
            public boolean axisMoved(Controller controller, int axisIndex, float value) {
                if (Math.abs(value) > 0.2) { // Độ nhạy để tránh nhận diện nhầm những chuyển động nhỏ
                    if (axisIndex == 0) { // Trục X
                        diChuyenX = value * tocDo;
                    } else if (axisIndex == 1) { // Trục Y
                        diChuyenY = -value * tocDo; // Đảo ngược giá trị vì trục Y trên joystick ngược với trục Y trên màn hình
                    }
                    return true;
                } else {
                    if (axisIndex == 0) {
                        diChuyenX = 0;
                    } else if (axisIndex == 1) {
                        diChuyenY = 0;
                    }
                }
                return false;
            }
        });
    }

    private void initializeAnimations() {
        // Khởi tạo và cấu hình animations ở đây
        anhChayLen = new Texture(Gdx.files.internal("runup.png"));
        anhChayXuong = new Texture(Gdx.files.internal("rundown.png"));
        anhChayTrai = new Texture(Gdx.files.internal("runleft.png"));
        anhChayPhai = new Texture(Gdx.files.internal("runright.png"));

        anhDungImLen = new Texture(Gdx.files.internal("idleup.png"));
        anhDungImXuong = new Texture(Gdx.files.internal("idledown.png"));
        anhDungImTrai = new Texture(Gdx.files.internal("idleleft.png"));
        anhDungImPhai = new Texture(Gdx.files.internal("idleright.png"));

        anhAttackUp = new Texture(Gdx.files.internal("attackup.png"));
        anhAttackDown = new Texture(Gdx.files.internal("attackdown.png"));
        anhAttackLeft = new Texture(Gdx.files.internal("attackleft.png"));
        anhAttackRight = new Texture(Gdx.files.internal("attackright.png"));

        chayLen = createAnimation(anhChayLen, 6);
        chayXuong = createAnimation(anhChayXuong, 6);
        chayTrai = createAnimation(anhChayTrai, 6);
        chayPhai = createAnimation(anhChayPhai, 6);

        dungImLen = createAnimation(anhDungImLen, 4);
        dungImXuong = createAnimation(anhDungImXuong, 4);
        dungImTrai = createAnimation(anhDungImTrai, 4);
        dungImPhai = createAnimation(anhDungImPhai, 4);

        attackUp = createAnimation(anhAttackUp, 7);
        attackDown = createAnimation(anhAttackDown, 7);
        attackLeft = createAnimation(anhAttackLeft, 7);
        attackRight = createAnimation(anhAttackRight, 7);
    }

    private Animation<TextureRegion> createAnimation(Texture texture, int frameCount) {
        TextureRegion[][] tmpFrames = TextureRegion.split(texture, texture.getWidth() / frameCount, texture.getHeight());
        TextureRegion[] animationFrames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            animationFrames[i] = tmpFrames[0][i];
        }
        return new Animation<>(0.1f, animationFrames);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        thoiGianTrangThai += Gdx.graphics.getDeltaTime();
        updateGameState();
        updateAnimationState();

        batch.begin();
        TextureRegion khungHienTai = hoatAnhHienTai.getKeyFrame(thoiGianTrangThai, true);
        batch.draw(khungHienTai, viTriX, viTriY, khungHienTai.getRegionWidth() * 2, khungHienTai.getRegionHeight() * 2);
        batch.end();
    }

    private void updateGameState() {
        // Cập nhật trạng thái game, bao gồm vị trí nhân vật
        float deltaTime = Gdx.graphics.getDeltaTime();
        viTriX += diChuyenX * deltaTime;
        viTriY += diChuyenY * deltaTime;

        // Cập nhật hướng di chuyển dựa trên di chuyển X và Y
        if (diChuyenX < 0) {
            currentDirection = Direction.LEFT;
        } else if (diChuyenX > 0) {
            currentDirection = Direction.RIGHT;
        } else if (diChuyenY > 0) {
            currentDirection = Direction.UP;
        } else if (diChuyenY < 0) {
            currentDirection = Direction.DOWN;
        } else if (diChuyenX == 0 && diChuyenY == 0 && !isAttacking) {
            currentDirection = Direction.IDLE;
        }

        viTriX = Math.max(0, Math.min(viTriX, Gdx.graphics.getWidth() - 40));
        viTriY = Math.max(0, Math.min(viTriY, Gdx.graphics.getHeight() - 40));
    }

    private void updateAnimationState() {
        if (isAttacking) {
            switch (currentDirection) {
                case UP:
                    hoatAnhHienTai = attackUp;
                    break;
                case DOWN:
                    hoatAnhHienTai = attackDown;
                    break;
                case LEFT:
                    hoatAnhHienTai = attackLeft;
                    break;
                case RIGHT:
                    hoatAnhHienTai = attackRight;
                    break;
                case IDLE:
                default:
                    // Nếu nhân vật đứng yên khi bắt đầu tấn công, có thể giữ nguyên hướng tấn công cuối cùng
                    // hoặc đặt một hướng mặc định
                    hoatAnhHienTai = attackDown;
                    break;
            }
        } else {
            // Xử lý hướng di chuyển và chọn hoạt ảnh phù hợp
            if (diChuyenX < 0) {
                hoatAnhHienTai = chayTrai;
            } else if (diChuyenX > 0) {
                hoatAnhHienTai = chayPhai;
            } else if (diChuyenY > 0) {
                hoatAnhHienTai = chayLen;
            } else if (diChuyenY < 0) {
                hoatAnhHienTai = chayXuong;
            } else {
                switch (currentDirection) {
                    case UP:
                        hoatAnhHienTai = dungImLen;
                        break;
                    case DOWN:
                        hoatAnhHienTai = dungImXuong;
                        break;
                    case LEFT:
                        hoatAnhHienTai = dungImTrai;
                        break;
                    case RIGHT:
                        hoatAnhHienTai = dungImPhai;
                        break;
                    case IDLE:
                    default:
                        hoatAnhHienTai = dungImXuong; // Hoặc giữ nguyên hướng đứng yên cuối cùng
                        break;
                }
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        anhChayLen.dispose();
        anhChayXuong.dispose();
        anhChayTrai.dispose();
        anhChayPhai.dispose();
        anhDungImLen.dispose();
        anhDungImXuong.dispose();
        anhDungImTrai.dispose();
        anhDungImPhai.dispose();
        anhAttackUp.dispose();
        anhAttackDown.dispose();
        anhAttackLeft.dispose();
        anhAttackRight.dispose();
    }
}
