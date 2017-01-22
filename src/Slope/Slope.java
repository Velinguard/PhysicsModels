/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Slope;

import static Slope.Slope.HEIGHT;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import static Slope.Slope.SCALER;
import static Slope.Slope.WIDTH;
/**
 *
 * @author Sam
 */
public class Slope extends JPanel {
    public static int WIDTH = 1920;
    public static int HEIGHT = 1080;
    public static int SCALER = 1; //1 metre = SCALER * 100 pixels
    public static double milliSecondTimer;
    public static double delta;
    public static ArrayList<Integer> keysDown;
    public Slope(){
        //Init
        keysDown = new ArrayList<Integer>();
    }
    //Main class
    public static void main(String[] args) throws InterruptedException{
        JFrame frame = new JFrame("App Name");
        Slope app = new Slope();
        frame.setSize((int)(WIDTH * SCALER),(int)(HEIGHT * SCALER));
        frame.add(app);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.requestFocus();
        long lastLoopTime = System.nanoTime();
        int fps = 0, lastFpsTime = 0, lastMilliSecondTimer = 0, count = 1;
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        //Game loop
        while(true){
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            delta = updateLength / ((double)OPTIMAL_TIME);
            lastFpsTime += updateLength;
            lastMilliSecondTimer += updateLength;
            fps++;
            if (lastFpsTime > 100000000 * count){
               milliSecondTimer += 0.1;
               count++;
            }
            if (lastFpsTime >= 1000000000){
                System.out.println("(FPS: "+fps+")");
                //milliSecondTimer += 1;
                lastFpsTime = 0;
                fps = 0;
                count = 1;
            }
            app.repaint();
            Thread.sleep( (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );
        }
    }
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.setFocusable(true);
        this.requestFocusInWindow();

        int k;
        g2d.setColor(Color.CYAN);
        int w = 10 * SCALER;
        int rows = HEIGHT / w;
        int columns = WIDTH / w;
        for (k = 0; k < rows; k++) {
            g2d.drawLine(0, k * w, WIDTH, k * w);
        }
        for (k = 0; k < columns; k++) {
            g2d.drawLine(k * w, 0, k * w, HEIGHT);
        }
        
        g2d.setColor(Color.BLUE);
        w = 100 * SCALER;
        rows = HEIGHT / w;
        columns = WIDTH / w;
        for (k = 0; k < rows; k++) {
            g2d.drawLine(0, k * w, WIDTH, k * w);
        }
        for (k = 0; k < columns; k++) {
            g2d.drawLine(k * w, 0, k * w, HEIGHT);
        }
        //Game loop, but everything time related * delta to get seconds.
                                    
    }
    public class MyKeyListener implements KeyListener{

        public void action(){
            if (keysDown.contains(KeyEvent.VK_SHIFT)){
                //example
            }
        }
        @Override
        public void keyTyped(KeyEvent e) {
                       
        }

        @Override
        public void keyPressed(KeyEvent e) {
            action();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keysDown.remove(new Integer(e.getKeyCode()));
        }
    }
}
//Define other objects.
class Ball extends Ellipse2D.Float {

    Color colour;
    double ha; //horizontal acceleration, + = -->
    double va; //vertical acceleration, + = ^
    double vSpeed; //vertical speed, + = ^
    double hSpeed; //horizontal speed, + = -->
    int mass;

    public Ball(float x, float y, float r) {
        super(x, y, r, r);
        colour = Color.blue;
        this.va = (int) 9.81;
        this.ha = (int) 0;
        this.vSpeed = 0;
        this.hSpeed = 0;
        this.mass = 3;
    }

    public void move(int d, int s) {
        switch (d) {
            //if within the screen then move s up
            case 0:
                if (s < 0) {
                    if (HEIGHT * SCALER - super.height > super.getCenterY() + s) {
                        super.y -= s;
                    }
                } else {
                    if (super.height < super.getCenterY() - s) {
                        super.y -= s;
                    }
                }
                break;
            case 1:
                //if within the screen then move s to the right
                if (s < 0) {
                    if (super.width < super.getCenterX() - s) {
                        super.x -= s;
                    }
                } else {
                    if (WIDTH * SCALER - super.width > super.getCenterX() + s) {
                        super.x += s;
                    }
                }
                break;
        }
    }

    public void paint(Graphics2D g) {
        g.setColor(colour);
        g.fill(this);
    }
}
