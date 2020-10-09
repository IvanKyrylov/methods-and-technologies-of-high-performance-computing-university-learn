package com.vannsha;

import java.awt.*;
import java.util.Random;

public class Player extends GameObject {


    public Player(int x, int y, ID id) {
        super(x, y, id);
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    @Override
    public void trick() {
        x += velX;
        y += velY;

        x = clamp(x, 5 ,Game.WIDTH-27);
    }

    @Override
    public void render(Graphics g) {

        g.setColor(Color.white);
        g.drawString("A", x, y);
    }

    @Override
    public void run() {
        new Thread(this).start();
    }
}
