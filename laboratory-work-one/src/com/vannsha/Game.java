package com.vannsha;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class Game extends Canvas implements Runnable {

    static int WIDTH, HEIGHT;

    private Thread thread;
    private boolean running = false;

    private Random r;
    
    private Handler handler;
    private HUD hud;
    private Spawn spawner;
    private long timer = System.currentTimeMillis();
    private Window window;

    public static STATE gameState = STATE.Menu;

    public Game() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        WIDTH = (int) screenSize.getWidth();
        HEIGHT = (int) screenSize.getHeight();
        handler = new Handler();
        hud = new HUD();
        this.addKeyListener(new KeyInput(handler));
        window = new Window(WIDTH, HEIGHT, "HEALTH: " + HUD.HEALTH + "\t POINTS: " + HUD.POINT, this);
        spawner = new Spawn(handler);
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTricks = 60.0;
        double ns = 1_000_000_000 / amountOfTricks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            if (running) {
                try {
                    render();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }

    private void tick() {

        if(gameState == STATE.Game) {
            handler.trick();
            hud.trick();
            spawner.tick();
        }
        window.setTitle("HEALTH: " + HUD.HEALTH + "\t POINTS: " + HUD.POINT);

    }

    private void render() throws InterruptedException {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.black);
        g.fillRect(0,0,WIDTH,HEIGHT);

        if(gameState == STATE.Game) {
            handler.render(g);
            hud.render(g);
        }
        if (gameState == STATE.Menu) {
            g.setColor(Color.white);
            g.drawString("ThreadWar", WIDTH/2-35, HEIGHT/2-48);
            g.drawString("Press <- or ->", WIDTH/2-32, HEIGHT/2-32);
        }
        if (gameState == STATE.Menu && System.currentTimeMillis() - timer >= 15000.0) {
            gameState = STATE.Game;
            handler.addObject(new Player(WIDTH/2, HEIGHT-50, ID.Player));
            handler.render(g);
            hud.render(g);
        }
        if (gameState == STATE.End) {
            g.setColor(Color.white);
            g.drawString("You Died!", WIDTH/2-32, HEIGHT/2-32);
            g.drawString("Point: " + HUD.POINT, WIDTH/2-28, HEIGHT/2-12);

        }

        g.dispose();
        bs.show();
    }



}
