package com.vannsha;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.Semaphore;

import static com.vannsha.Game.HEIGHT;
import static com.vannsha.Game.WIDTH;

public class KeyInput extends KeyAdapter {
    private static final Semaphore SEMAPHORE = new Semaphore(3);
    private final Handler handler;
    private final boolean[] keyDown = new boolean[2];

    public KeyInput(Handler handler) {
        this.handler = handler;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_LEFT) && Game.gameState == STATE.Menu) {
            Game.gameState = STATE.Game;
            handler.addObject(new Player(WIDTH / 2, HEIGHT - 50, ID.Player));
        }

        for (int i = 0; i < handler.objects.size(); i++) {
            GameObject tempObject = handler.objects.get(i);
            int count = 0;
            if (tempObject.getId() == ID.Player) {
                if (key == KeyEvent.VK_RIGHT) {
                    tempObject.setVelX(3);
                    keyDown[0] = true;
                }
                if (key == KeyEvent.VK_LEFT) {
                    tempObject.setVelX(-3);
                    keyDown[1] = true;
                }
                if (key == KeyEvent.VK_SPACE && Game.gameState == STATE.Game) {
                    handler.addObject(new Projectile(tempObject.getX(), tempObject.getY(), ID.Projectile, handler));

                }
            }
        }
        if (key == KeyEvent.VK_ESCAPE) System.exit(1);
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        for (int i = 0; i < handler.objects.size(); i++) {
            GameObject tempObject = handler.objects.get(i);
            if (tempObject.getId() == ID.Player) {
                if (key == KeyEvent.VK_RIGHT) keyDown[0] = false;//tempObject.setVelX(0);
                if (key == KeyEvent.VK_LEFT) keyDown[1] = false;//tempObject.setVelX(0);
                if (!keyDown[0] && !keyDown[1]) tempObject.setVelX(0);
            }
        }

    }
}
