package com.vannsha;

import java.awt.*;
import java.util.Random;

public class BasicEnemy extends GameObject{

    private Handler handler;
    private Random random;

    public BasicEnemy(int x, int y, ID id, Handler handler) {
        super(x, y, id);
        random = new Random();
        velX = 1 + random.nextInt(2);
        velY = 1 + random.nextInt(2);

        this.handler = handler;
    }

    public BasicEnemy(int x, int y, ID id, Handler handler, int velX, int velY) {
        this(x, y, id, handler);
        this.velX += velX;
        this.velY += velY;

        this.handler = handler;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x-4,y-12,16,16);
    }

    @Override
    public void trick() {
        x += velX;
        y += velY;
        collision();

    }

    private void collision() {
        for (int i = 0; i < handler.objects.size(); i++) {

            GameObject tempObject = handler.objects.get(i);

            if (tempObject.getId() == ID.Projectile) {
                if (getBounds().intersects(tempObject.getBounds())){
                    HUD.POINT++;
                    handler.removeObject(this);
                    handler.removeObject(tempObject);
                }
            }
        }
        if (y <= 0 || y >= Game.HEIGHT || x <= 0 || x >= Game.WIDTH) {
            handler.objects.remove(this);
            HUD.HEALTH--;
        }
    }

    @Override
    public void render(Graphics g) {
//        Graphics2D g2d = (Graphics2D) g;
//        g.setColor(Color.green);
//        g2d.draw(getBounds());
        g.setColor(Color.red);
        g.drawString("O", x, y);
    }


    @Override
    public void run() {
        new Thread(this).start();
    }
}
