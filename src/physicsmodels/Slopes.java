/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physicsmodels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javafx.scene.input.KeyCode;
import javax.swing.JFrame;
import javax.swing.JPanel;
import static physicsmodels.Slopes.HEIGHT;
import static physicsmodels.Slopes.SCALER;
import static physicsmodels.Slopes.WIDTH;

public class Slopes extends JPanel{
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final double SCALER = 1.5;
    public static ArrayList<Integer> keysDown;
    public static ArrayList<Shape> rectangles;
    public static Ball Player;
    public static Rectangle rect;
    public static int timer;
    public static boolean right;
    public static boolean bulletInPlay;
    
    public Slopes(){
        keysDown = new ArrayList<Integer>();
        rectangles = new ArrayList<Shape>();
        
        rect = new Rectangle(900,500,400,50);
        AffineTransform transform = new AffineTransform();
        transform.rotate(rect.angle);
        Shape rotatedRect = transform.createTransformedShape(rect);
        rectangles.add(rotatedRect);
        
        Player = new Ball((float)((WIDTH * SCALER) / 2),(float) ((HEIGHT * SCALER) / 2),(float) (20));
        bulletInPlay = false;
        KeyListener listener = new MyKeyListener();
        addKeyListener(listener);
        setFocusable(true);
        setUpArea();
    }
    public void setUpArea(){
        int x = (int) (WIDTH * SCALER);
        int y = (int) (HEIGHT * SCALER);
        Rectangle rectal = new Rectangle(x * 1, y * 7, x, y);
    }    
    public void offScreen(){
        if (Player.getMinY() < 0 || Player.getMaxX() > HEIGHT * SCALER|| Player.getMinX() < 0 || Player.getMaxX() > WIDTH*SCALER){
            //Game over
        } 
    }
    /*public void collision(){
        double m1 = Math.tan(Math.toRadians(rect.angle));
        double m2 = - 1 / m1;
        
        double boundsLeft = rect.getMinX();
        double boundsDown = rect.getMinY(); // think
        
        double cx = Player.getCenterX();
        double cy = Player.getCenterY(); //think
        
        double a = Math.pow(m2,2);
        double b = 2 * m2 * (cy - m2 * cx) - 2 * m2 * cy - 2 * cx;
        double c = Math.pow(cy, 2) + Math.pow(cx, 2) + Math.pow((cy - m2 * cx), 2) - 2 * cy * (cy - m2 * cy) - Math.pow(Player.getWidth() / 2, 2);
        double der = Math.pow(b, 2) - 4 * a * c;
        
        double[] x = new double[2];
        double[] y = new double[2];
        
        if ( der >= 0){
            x[0] = (- b + Math.sqrt(der)) / (2 * a);
            x[1] = (- b - Math.sqrt(der)) / (2 * a);
            y[0] = m2 * x[0] + cy - m2 * cx;
            y[1] = m2 * x[1] + cy - m2 * cx;
            if (y[0] == (m1 * x[0]) || y[1] == (m1 * x[1])){
                System.out.println("hit");
            }
        } else {
            System.err.println("Arithmetic logic error / 0");
        }
        
    }*/
    public void gravity(){
            boolean move = true;
            if (move){
                Player.speed += Player.va;
                java.awt.Rectangle bounds = Player.getBounds();
                //collision();
                //System.out.println(rectangles.get(0).getBounds());
                //Player.move(2, (int) (Player.speed / 40));
            }
    }
    
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Test");
        Slopes app = new Slopes();
        frame.setSize((int)(WIDTH * SCALER), (int) (HEIGHT * SCALER));
        frame.add(app);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.requestFocus();
        while(true){
            app.repaint();
            Thread.sleep(5);
        }
    }
    public void paint(Graphics g){
        super.paint(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.red);
            timer++;
            if (timer % 6 == 0){
                gravity();
                timer = 1;
            }
            
            
            Player.paint(g2d);
            rect.paint(g2d);
        }
    public class MyKeyListener implements KeyListener{

        public void action(){
            int s = 4;
            if (keysDown.contains(KeyEvent.VK_SHIFT)){
                s = 10;
            } else {
                s = 4;
            }
            if (keysDown.contains(KeyEvent.VK_UP) /*|| keysDown.contains(KeyEvent.VK_W)*/){
                Player.move(0,s);
            }
            if (keysDown.contains(KeyEvent.VK_RIGHT) /*|| keysDown.contains(KeyEvent.VK_D)*/){
                Player.move(1,s);
                right = true;
            }
            if (keysDown.contains(KeyEvent.VK_DOWN) /*|| keysDown.contains(KeyEvent.VK_S)*/){
                Player.move(2,s);
            }
            if (keysDown.contains(KeyEvent.VK_LEFT) /*|| keysDown.contains(KeyEvent.VK_A)*/){
                Player.move(3,s);
                right = false;
            }
            if (keysDown.contains(KeyEvent.VK_SPACE)){
                Player.move(0, 20);
            }
        }
        
        @Override
        public void keyTyped(KeyEvent e) {
                       
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!keysDown.contains(e.getKeyCode())){
               keysDown.add(e.getKeyCode()); 
            }
            action();
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keysDown.remove(new Integer(e.getKeyCode()));
        }
        
    }
    
}
class Ball extends Ellipse2D.Float{
    Color colour;
    double ha; //horizontal acceleration, + = -->
    double va; //vertical acceleration, + = ^
    double speed;
    public Ball(float x, float y, float r){
        super(x,y,r,r);
        colour = Color.blue;
        this.va = (int) 9.81;
        this.ha = (int) 0;
        this.speed = 0;
    }
    public void move(int d, int s){
        switch (d){
            case 0:
                if (super.height < super.getCenterY() - s){
                    super.y -= s;
                }break;
            case 1:
                if (WIDTH * SCALER - super.width > super.getCenterX() + s){
                    super.x += s;
                } break;
            case 2:
                if (HEIGHT * SCALER - super.height > super.getCenterY() + s){
                    super.y += s;
                }break;
            case 3: 
                if (super.width < super.getCenterX() - s){
                    super.x -= s;
                }break;
        }
    }
    public void paint(Graphics2D g){
        g.setColor(colour);
        g.fill(this);
    }
}
class Rectangle extends Rectangle2D.Double{
    double angle;
    public Rectangle(int x, int y, int rx, int ry){
        super(x,y,rx,ry);
        angle = 10;
    }
    public void paint(Graphics2D g){
        g.setColor(Color.red);
        g.rotate(Math.toRadians(angle));
        g.fill(this);
    }
}