package com.vannsha;

import java.awt.*;

public class HUD {

    public static int HEALTH = 30;
    public static int POINT = 0;


    public void trick() {
        if (HEALTH == 0) Game.gameState = STATE.End;
    }

    public void render(Graphics g) {
        g.setColor(Color.white);
        g.drawString("HP: " + HEALTH, 25,32);
        g.drawString("Point: " + POINT, 75,32);
    }

}
