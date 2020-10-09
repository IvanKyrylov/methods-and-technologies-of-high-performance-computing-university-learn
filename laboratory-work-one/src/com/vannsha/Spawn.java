package com.vannsha;

import java.util.Random;

public class Spawn {
    private Handler handler;
    private Random r;
    private long timer = System.currentTimeMillis();
    private double count = 0;

    public Spawn(Handler handler) {
        this.handler = handler;
        r = new Random();
    }

    public void tick() {
        if (Game.gameState == STATE.Game) {
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                count += 0.1;
                handler.objects.add(new BasicEnemy(r.nextInt(Game.WIDTH/2), r.nextInt(Game.HEIGHT/3), ID.BasicEnemy, handler,
                        (int) count, (int) count));
                for (int i = 0; i < count; i++) {
                    handler.objects.add(new BasicEnemy(r.nextInt(Game.WIDTH/2), r.nextInt(Game.HEIGHT/3), ID.BasicEnemy, handler,
                            (int) count, (int) count));
                }
            }
        }

    }
}
