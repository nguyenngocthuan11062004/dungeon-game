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
    SpriteBatch loNen;
    Animation<TextureRegion> chayLen, chayXuong, chayTrai, chayPhai;
    Animation<TextureRegion> dungImLen, dungImXuong, dungImTrai, dungImPhai;
    Animation<TextureRegion> hoatAnhHienTai;
    Texture anhChayLen, anhChayXuong, anhChayTrai, anhChayPhai;
    Texture anhDungImLen, anhDungImXuong, anhDungImTrai, anhDungImPhai;
    float viTriX, viTriY, thoiGianTrangThai = 0;
    float diChuyenX = 0, diChuyenY = 0;
    float tocDo = 250;

    @Override
    public void create() {
        loNen = new SpriteBatch();
        // Tải và khởi tạo hình ảnh và hoạt ảnh cho mỗi trạng thái di chuyển và đứng yên
        anhChayLen = new Texture("runup.png");
        anhChayXuong = new Texture("rundown.png");
        anhChayTrai = new Texture("runleft.png");
        anhChayPhai = new Texture("runright.png");

        anhDungImLen = new Texture("idleup.png");
        anhDungImXuong = new Texture("idledown.png");
        anhDungImTrai = new Texture("idleleft.png");
        anhDungImPhai = new Texture("idleright.png");

        chayLen = taoHoatAnhTuAnh(anhChayLen, 6);
        chayXuong = taoHoatAnhTuAnh(anhChayXuong, 6);
        chayTrai = taoHoatAnhTuAnh(anhChayTrai, 6);
        chayPhai = taoHoatAnhTuAnh(anhChayPhai, 6);

        dungImLen = taoHoatAnhTuAnh(anhDungImLen, 1);
        dungImXuong = taoHoatAnhTuAnh(anhDungImXuong, 1);
        dungImTrai = taoHoatAnhTuAnh(anhDungImTrai, 1);
        dungImPhai = taoHoatAnhTuAnh(anhDungImPhai, 1);

        hoatAnhHienTai = dungImXuong; // Mặc định bắt đầu với hướng đứng yên xuống

        viTriX = Gdx.graphics.getWidth() / 2 - 20;
        viTriY = Gdx.graphics.getHeight() / 2 - 20;

        for (Controller dieuKhien : Controllers.getControllers()) {
            dieuKhien.addListener(new ControllerAdapter() {
                @Override
                public boolean axisMoved(Controller dieuKhien, int chiSoTruc, float giaTri) {
                    // Xử lý di chuyển dựa trên input từ controller
                    xuLyDiChuyen(chiSoTruc, giaTri);
                    return false;
                }
            });
        }
    }

    private void xuLyDiChuyen(int chiSoTruc, float giaTri) {
        boolean diChuyen = Math.abs(giaTri) > 0.2;
        if (chiSoTruc == 0) { // Trục X
            diChuyenX = diChuyen ? giaTri * tocDo : 0;
        } else if (chiSoTruc == 1) { // Trục Y
            diChuyenY = diChuyen ? -giaTri * tocDo : 0;
        }

        // Cập nhật hoạt ảnh dựa trên hướng di chuyển
        if (diChuyenX < 0) hoatAnhHienTai = chayTrai;
        else if (diChuyenX > 0) hoatAnhHienTai = chayPhai;
        else if (diChuyenY > 0) hoatAnhHienTai = chayLen;
        else if (diChuyenY < 0) hoatAnhHienTai = chayXuong;
        else { // Đứng yên
            if (hoatAnhHienTai == chayTrai || hoatAnhHienTai == dungImTrai) hoatAnhHienTai = dungImTrai;
            else if (hoatAnhHienTai == chayPhai || hoatAnhHienTai == dungImPhai) hoatAnhHienTai = dungImPhai;
            else if (hoatAnhHienTai == chayLen || hoatAnhHienTai == dungImLen) hoatAnhHienTai = dungImLen;
            else if (hoatAnhHienTai == chayXuong || hoatAnhHienTai == dungImXuong) hoatAnhHienTai = dungImXuong;
        }
    }

    private Animation<TextureRegion> taoHoatAnhTuAnh(Texture anh, int soKhungHinh) {
        TextureRegion[][] tmpFrames = TextureRegion.split(anh, 40, 40);
        TextureRegion[] hoatAnhFrames = new TextureRegion[soKhungHinh];
        for (int i = 0; i < soKhungHinh; i++) {
            hoatAnhFrames[i] = tmpFrames[0][i];
        }
        return new Animation<>(0.1f, hoatAnhFrames);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        thoiGianTrangThai += Gdx.graphics.getDeltaTime();
        TextureRegion khungHienTai = hoatAnhHienTai.getKeyFrame(thoiGianTrangThai, true);
        
        viTriX += diChuyenX * Gdx.graphics.getDeltaTime();
        viTriY += diChuyenY * Gdx.graphics.getDeltaTime();

        loNen.begin();
        loNen.draw(khungHienTai, viTriX, viTriY, 80, 80); // Vẽ nhân vật với kích thước gấp đôi
        loNen.end();
    }

    @Override
    public void dispose() {
        loNen.dispose();
        anhChayLen.dispose();
        anhChayXuong.dispose();
        anhChayTrai.dispose();
        anhChayPhai.dispose();
        anhDungImLen.dispose();
        anhDungImXuong.dispose();
        anhDungImTrai.dispose();
        anhDungImPhai.dispose();
    }
}
