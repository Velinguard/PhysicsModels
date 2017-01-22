/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physicsmodels.Collisions.SHM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.stream.IntStream;
import javafx.scene.input.KeyCode;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import static physicsmodels.Collisions.SHM.SHM.HEIGHT;
import static physicsmodels.Collisions.SHM.SHM.SCALER;
import static physicsmodels.Collisions.SHM.SHM.WIDTH;

public class SHM extends JPanel{
    public String[] collumnNames = {"Index","Modulus1", "NaturalLength1", "Modulus2", "NaturalLength2", "Length", "StartPosition", "EndPosition", "Amplitude", "Equilibrium"};
    
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final double SCALER = 1;
    public static ArrayList<Integer> keysDown;
    public static Ball Player;
    public static int timer;
    public static boolean right;
    public static ArrayList<Rectangle> rect;
    public static double[] modulus;
    public static double[] natural;
    public static int length;
    public boolean moving;
    public boolean scaleChange;
    public int scaleScreen;
    public int scale = 100; //100 pixels = 1 meter;
    public int moverY, moverX;
    public static double delta;
    public static double milliSecondTimer;
    public static JSlider mod1, mod2, len1, len2;
    public double amplitude, equilibrium, minX, maxX;
    public ArrayList<Integer> MaxX;
    public ArrayList<Integer> MinX;    
    public ArrayList<Integer> Equilibrium;
    public int startPosition, endPosition;
    public boolean hasWritten;
    
    public SHM(){
        //initialise arraylists        
        keysDown = new ArrayList<Integer>();
        rect = new ArrayList<Rectangle>();
        MaxX = new  ArrayList<Integer>();
        MinX = new ArrayList<Integer>();
        Equilibrium = new ArrayList<Integer>();
        
        //initialise variables
        hasWritten = false;
        length = 500;
        amplitude = 0;
        equilibrium = length/2;
        minX = 0;
        maxX = 0;
        scaleScreen = 1;
        moverY = 0;
        moverX = 0;
        milliSecondTimer = 0;
        scaleChange = false;
        moving = true;
        modulus = new double[2];
        natural = new double[2];
        modulus[0] = 1;
        modulus[1] = 5;
        natural[0] = 1;
        natural[1] = 1;
        
        //initialise objects   
        rect.add(new Rectangle((int) 320, (int) ((HEIGHT * SCALER) / 2), 20, 20)); //Box on Left 0
        rect.add(new Rectangle((int) rect.get(0).getMaxX() + length, (int) ((HEIGHT * SCALER) / 2), 20, 20)); //Box on Right 1
        rect.add(new Rectangle((int) rect.get(0).getMaxX(),(int) (rect.get(0).getCenterY()), length, 1)); //String 2        
        rect.add(new Rectangle((int) rect.get(0).getMaxX(),(int) (rect.get(0).getCenterY() - 50), (int) (natural[0] * scale), 1)); //String Left NL 3
        rect.add(new Rectangle((int) rect.get(0).getMaxX(),(int) (rect.get(0).getCenterY() - 53), 1, 6)); //Left Border of first string 4
        rect.add(new Rectangle((int) (rect.get(0).getMaxX() +  natural[0] * scale),(int) (rect.get(0).getCenterY() - 53), 1, 6)); //Right Border of first string 5
        rect.add(new Rectangle((int) (rect.get(1).getMinX() - natural[1] * scale),(int) (rect.get(0).getCenterY() - 50), (int) (natural[1] * scale), 1)); //String Right NL 6
        rect.add(new Rectangle((int) (rect.get(1).getMinX() - natural[1] * scale),(int) (rect.get(0).getCenterY() - 53), 1, 6)); //Left border of second string 7
        rect.add(new Rectangle((int) rect.get(1).getMinX(),(int) (rect.get(0).getCenterY() - 53), 1, 6)); //Right border of second string. 8
        rect.add(new Rectangle(100,20,100,1)); 
        rect.add(new Rectangle(100,18,1,5));
        rect.add(new Rectangle(200,18,1,5));
        rect.add(new Rectangle(100,40,10,1));
        rect.add(new Rectangle(100,38,1,5));
        rect.add(new Rectangle(110,38,1,5));
        
        Player = new Ball((float)(rect.get(0).getMaxX() + (length / 2)),(float) ((HEIGHT * SCALER) / 2),(float) (20)); //Ball     
        startPosition = (int)(rect.get(0).getMaxX() + (length / 2));
        rect.get(2).string = true;
        
        //initalise other
        KeyListener listener = new MyKeyListener();
        addKeyListener(listener);
        setFocusable(true);
        setUpArea();
    }
    //Returns the average of an array of values.
    public double average(ArrayList<Integer> positions){
        int sum = 0;
        for (int i = 0; i < positions.size(); i++){
            sum += positions.get(i);
        }
        double average = sum / positions.size();
        return average;
    }
    public void setRectangles(){
        rect.get(3).setRect((int) rect.get(0).getMaxX(),(int) (rect.get(0).getCenterY() - 50), (int) (natural[0] * scale), 1);
        rect.get(4).setRect((int) rect.get(0).getMaxX(),(int) (rect.get(0).getCenterY() - 53), 1, 6);
        rect.get(5).setRect((int) (rect.get(0).getMaxX() +  natural[0] * scale),(int) (rect.get(0).getCenterY() - 53), 1, 6);
        rect.get(6).setRect((int) (rect.get(1).getMinX() - natural[1] * scale),(int) (rect.get(0).getCenterY() - 50), (int) (natural[1] * scale), 1);
        rect.get(7).setRect((int) (rect.get(1).getMinX() - natural[1] * scale),(int) (rect.get(0).getCenterY() - 53), 1, 6);
        rect.get(8).setRect((int) rect.get(1).getMinX(),(int) (rect.get(0).getCenterY() - 53), 1, 6);
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
    public double tension(){
        double top;
        double t[] = new double[2];
        top = modulus[0] * ((Player.getCenterX() - rect.get(0).getMaxX() - (natural[0] * scale)) / scale);
        t[0] = top / (natural[0]);
        top = modulus[1] * ((length - (Player.getCenterX() - rect.get(0).getMaxX()) - (natural[1] * scale)) / scale);
        t[1] = top / (natural[0]);
        return (int) (t[1] - t[0]) * scale ; //resultant tension -->
    }
    //Outputs data from the database
    public void databaseConnector(boolean read){
        try{
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/Outputs", "Sam", "sam");
            Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            String sql = "SELECT INDEX FROM SHM";
            ResultSet rs = st.executeQuery(sql);
            int len = 0;
            while(rs.next()) {
                len++;
            }
            
            if (read){
                databaseReader(st, len);
            } else {
                double data[] = {modulus[0] , natural[0], modulus[1], natural[1], length, startPosition, endPosition, amplitude, equilibrium};
                for (int i = 4; i < data.length; i++){
                    data[i] /= 100;
                }  
                PreparedStatement sqls = con.prepareStatement( "SELECT * FROM SHM WHERE " + collumnNames[1] + " = " + data[0] + " AND " + collumnNames[2] + " = " + data[1] + " AND "
                    + collumnNames[3] + " = " + data[2] + " AND " + collumnNames[4] + " = " + data[3] + " AND "
                    + collumnNames[5] + " = " + data[4] + " AND " + collumnNames[6] + " = " + data[5]);
                databaseWriter(st, len, sqls, data);
            }
            st.close();
            con.close();
        } catch (Exception e){
            e.printStackTrace();
        }        
    }
    
    public void databaseReader(Statement st, int len){
        try{
            Object[][] data;
                           
            //Get data from table and convert to 2D array
            data = new Object[len][collumnNames.length];
            String sql = "SELECT * FROM SHM";
            ResultSet rs = st.executeQuery(sql);
            int x = 0;
            while (rs.next()){
                for(int i = 0; i < collumnNames.length; i++){
                    data[x][i] = new Double(rs.getDouble(collumnNames[i]));
                }                
                x++;
            }
            rs.close();
            //Output data
            JFrame frame = new JFrame("Table");
            TableModel model = new DefaultTableModel(data, collumnNames);
            JTable table = new JTable(model);
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame.add(new JScrollPane(table));
            frame.setAlwaysOnTop(true);
            frame.pack();
            frame.setVisible(true);
            
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
    public void databaseWriter(Statement st, int len, PreparedStatement sql, double[] data){
        try{
            //checker
                       
            ResultSet rs = sql.executeQuery();
            
            if (!rs.next()){ //if not empty then
                rs = st.executeQuery("SELECT * FROM SHM");
                rs.moveToInsertRow();
                rs.updateInt(collumnNames[0],len + 1);
                for (int i = 0; i < data.length; i++){
                    rs.updateDouble(collumnNames[i+1],data[i]);
                }
                rs.insertRow();
            }
            rs.close();
            
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
    }
    public void calculationChecker(){
        int position = (int) (Player.getCenterX() - rect.get(0).getMaxX());
        if (Player.hSpeed <= 1 * delta && Player.hSpeed >= -1 * delta){
            //At a max or min
            if (Player.ha < 0){
                //Max
                MaxX.add(position);
                maxX = average(MaxX);
            } else {
                //Min
                MinX.add(position);
                minX = average(MinX);
            }
            amplitude = maxX - minX;
            amplitude /= 2;
        } else if (Player.ha == 0){
            //At equilibrium
            Equilibrium.add(position);
            equilibrium = average(Equilibrium);
        }
    }
    public void gravity(){
        if (moving){
             
            double force = tension();
            Player.ha = force / Player.mass;
            Player.hSpeed += Player.ha * delta / 60;
            //}
            Player.vSpeed += Player.va;
            
            Player.move(1, (int) (Player.hSpeed * delta));
            if (milliSecondTimer < 20){
                calculationChecker();
            }
            if (milliSecondTimer > 20 && !hasWritten){
                endPosition = (int) (maxX - rect.get(0).getMaxX());
                databaseConnector(false);
                hasWritten = true;
            }
        } else {
            milliSecondTimer = 0;
            hasWritten = false;
            amplitude = 0;
            equilibrium = 0;
            MaxX = new  ArrayList<Integer>();
            MinX = new ArrayList<Integer>();
            Equilibrium = new ArrayList<Integer>();
            Player.hSpeed = 0;
            Player.ha = 0;
        }
    }
    
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Simple Harmonic Motion");
        SHM app = new SHM();
        frame.setSize((int)(WIDTH * SCALER) + 350, (int) (HEIGHT * SCALER) + 20);
        frame.add(app, BorderLayout.CENTER);
        frame.add( SHM.leftPanel(),BorderLayout.WEST);
        frame.add( SHM.bottomPanel(),BorderLayout.SOUTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.requestFocus();
        long lastLoopTime = System.nanoTime();
        int fps = 0, lastFpsTime = 0, lastMilliSecondTimer = 0, count = 1;
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        
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
    public static JPanel leftPanel(){
        JLabel label1 = new JLabel("Modulus of elasticity for first spring:");
        mod1 = new JSlider(JSlider.HORIZONTAL,0, 25, 1);
        mod1.setMajorTickSpacing(5);
        mod1.setValue((int) modulus[0]);
        mod1.setPaintLabels(true);
        mod1.setPaintTicks(true);
        JLabel label2 = new JLabel("Natural Length of first spring:");
        len1 = new JSlider(JSlider.HORIZONTAL,0,50,1);
        len1.setMajorTickSpacing(10);
        len1.setValue((int) natural[0] * 10);
        len1.setMinorTickSpacing(2);
        len1.setPaintTicks(true);
        JLabel label3 = new JLabel("Modulus of elasticity for second spring:");
        mod2 = new JSlider(JSlider.HORIZONTAL,0, 25, 1);
        mod2.setMajorTickSpacing(5);
        mod2.setValue((int) modulus[1]);
        mod2.setPaintLabels(true);
        mod2.setPaintTicks(true);
        JLabel label4 = new JLabel("Natural Length of second spring:");
        len2 = new JSlider(JSlider.HORIZONTAL,0,50,1);
        len2.setMajorTickSpacing(10);
        len2.setMinorTickSpacing(2);
        len2.setValue((int) natural[1] * 10);
        len2.setPaintTicks(true);
        JLabel reset = new JLabel("Press space to pause, and space again to reset.");
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Box.createRigidArea(new Dimension(0,200)));
        panel.add(label1);
        panel.add(mod1);
        panel.add(label2);
        panel.add(len1);
        panel.add(label3);
        panel.add(mod2);
        panel.add(label4);
        panel.add(len2);
        panel.add(reset);
        
        return panel;
    }
    public static JPanel bottomPanel(){
        JLabel label = new JLabel ("Built and Designed By: Sam Brotherton");
        JPanel panel = new JPanel();
        panel.add(label);
        return panel;
    }
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.red);
        g2d.translate(moverX,moverY);       
        this.setFocusable(true);
        this.requestFocusInWindow();
           
        //GRID
        int k;
        g2d.setColor(Color.CYAN);
        int w = 10 * scaleScreen;
        int rows = HEIGHT / w;
        int columns = WIDTH / w;
        for (k = 0; k < rows; k++)
        g2d.drawLine(0, k * w , WIDTH, k * w );
        for (k = 0; k < columns; k++){
            g2d.drawLine(k*w , 0, k*w , HEIGHT);
        }
        g2d.setColor(Color.BLUE);
        w = 100 * scaleScreen;
        rows = HEIGHT / w;
        columns = WIDTH / w;
        for (k = 0; k < rows; k++)
            g2d.drawLine(0, k * w , WIDTH, k * w );
        for (k = 0; k < columns; k++){
            g2d.drawLine(k*w , 0, k*w , HEIGHT);
        }
            
        g2d.translate(WIDTH/2 , HEIGHT/2);
        g2d.scale(scaleScreen, scaleScreen);
        g2d.translate(-WIDTH/2, -HEIGHT/2);  

        gravity();
         
        for (int i = 0; i < rect.size();i++){
            rect.get(i).paint(g2d);
        }
        g.drawString("Natural Length = " + natural[0] + "m", (int) (rect.get(3).getMinX()), (int) (rect.get(3).getCenterY() - 10));
        g.drawString("Natural Length = " + natural[1] + "m", (int) (rect.get(6).getMinX()), (int) (rect.get(6).getCenterY() - 10));
        g.drawString("Scale = 1m", (int) (rect.get(9).getMinX()), (int) (rect.get(9).getCenterY() - 5));
        g.drawString("Scale = 10cm", (int) (rect.get(12).getMinX()), (int) (rect.get(12).getCenterY() - 5));
        DecimalFormat df = new DecimalFormat("#.00"); 
        g.drawString("Time: " + df.format(milliSecondTimer) + "s", (int) (rect.get(12).getMinX()), (int) (rect.get(12).getCenterY() + 15));
        g.drawString("Modulus = " + modulus[0] + "N", (int) (rect.get(2).getMinX()), (int) (rect.get(2).getCenterY() + 30));
        g.drawString("Modulus = " + modulus[1] + "N", (int) (rect.get(2).getMaxX() - 80), (int) (rect.get(2).getCenterY() + 30));
        g.drawString("Amplitude: " + df.format(amplitude / 100) + "m", (int) (rect.get(12).getMinX()), (int) (rect.get(12).getCenterY() + 35));
        g.drawString("Equilibrium: " + df.format(equilibrium / 100) + "m", (int) (rect.get(12).getMinX()), (int) (rect.get(12).getCenterY() + 55));
                      
        Player.paint(g2d);
            
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
                scaleChange = true;
                moverY += 10;
            }
            if (keysDown.contains(KeyEvent.VK_RIGHT) /*|| keysDown.contains(KeyEvent.VK_D)*/){
                if (!moving) {
                    Player.move(1,s);
                    right = true;
                } else {
                    moverX -= 10;
                }
            }
            if (keysDown.contains(KeyEvent.VK_DOWN) /*|| keysDown.contains(KeyEvent.VK_S)*/){
                scaleChange = true;
                moverY -= 10;
            }
            if (keysDown.contains(KeyEvent.VK_LEFT) /*|| keysDown.contains(KeyEvent.VK_A)*/){
                if (!moving) {
                    Player.move(3,s);
                    right = false;
                } else {
                    moverX += 10;
                }
                
            }
            if (keysDown.contains(KeyEvent.VK_E)){
                databaseConnector(true);
            }
            if (keysDown.contains(KeyEvent.VK_SPACE)){
                Player.ha = 0;
                Player.va = 0;
                Player.vSpeed = 0;
                Player.hSpeed = 0;
                if (!moving){
                    modulus[0] = mod1.getValue();
                    modulus[1] = mod2.getValue();
                    natural[0] = len1.getValue();
                    natural[0] /= 10;
                    natural[1] = len2.getValue();
                    natural[1] /= 10;
                    setRectangles();
                    startPosition = (int) (Player.getCenterX() - rect.get(0).getMaxX());
                }
                moving = !moving;
            }
            if (keysDown.contains(KeyEvent.VK_EQUALS)){
                scaleScreen += 1;
                scaleChange = true;
            }
            if (keysDown.contains(KeyEvent.VK_SLASH)){
                scaleScreen -= 1;
                if(scaleScreen == 0){
                    scaleScreen = 1;
                }                
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
    double vSpeed;
    double hSpeed;
    int mass;
    public Ball(float x, float y, float r){
        super(x,y,r,r);
        colour = Color.blue;
        this.va = (int) 9.81;
        this.ha = (int) 0;
        this.vSpeed = 0;
        this.hSpeed = 0;
        this.mass = 3;
    }
    public void move(int d, int s){
        switch (d){
            case 0:
                if (super.height < super.getCenterY() - s){
                    super.y -= s;
                }break;
            case 1:
                if (s < 0){
                    if (super.width < super.getCenterX() - s){
                        super.x -= s;
                    }
                } else {
                    if (WIDTH * SCALER - super.width > super.getCenterX() + s){
                        super.x += s;
                    }
                }
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
class Rectangle extends Rectangle2D.Float{
    boolean string;
    public Rectangle(int x, int y, int rx, int ry){
        super(x,y,rx,ry);
        this.string = false;
    }
    public void paint(Graphics2D g){
        if (string){
            g.setColor(Color.black);
        } else {
            g.setColor(Color.red);
        }
        g.fill(this);
    }
}
