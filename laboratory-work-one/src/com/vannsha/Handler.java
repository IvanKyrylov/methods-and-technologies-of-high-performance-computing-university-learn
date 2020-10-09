package com.vannsha;

import java.awt.*;
import java.util.LinkedList;

public class Handler implements Runnable {
    LinkedList<GameObject> objects = new LinkedList<>();
    private int coun = 0;

    public void trick() {
        for (int i = 0; i < objects.size(); i++) {
            GameObject tempObject = objects.get(i);
            tempObject.trick();
        }
    }

    public void render(Graphics g) {
        for (int i = 0; i < objects.size(); i++) {
            GameObject tempObject = objects.get(i);
            tempObject.render(g);
        }
    }

    public void addObject(GameObject object) {
        if (object.getId() == ID.Projectile) {
            if (coun < 3) {
                coun++;
                this.objects.add(object);
            }
        } else {
            this.objects.add(object);
        }

    }

    public void removeObject(GameObject object) {
        if (object.getId() == ID.Projectile) {
            coun--;
        }
        this.objects.remove(object);

    }

    @Override
    public void run() {

    }
}
