package com.vannsha;

import java.awt.*;

public class Projectile extends GameObject{
    private Handler handler;
    public Projectile(int x, int y, ID id, Handler handler) {
        super(x, y, id);
        velY = 2;
        this.handler = handler;
    }

    @Override
    public void trick() {
        y -= velY;
        collision();
    }

    private void collision() {
        if (y <= 0 || y >= Game.HEIGHT || x <= 0 || x >= Game.WIDTH) {
            handler.removeObject(this);
        }
    }

    @Override
    public void render(Graphics g) {
//        Graphics2D g2d = (Graphics2D) g;
//        g.setColor(Color.green);
//        g2d.draw(getBounds());

        g.setColor(Color.white);
        g.drawString("|", x, y);

    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x-2,y-11,6,16);
    }

    @Override
    public void run() {
        trick();
    }
}
