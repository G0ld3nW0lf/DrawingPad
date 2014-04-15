/*
 * Shashank Guduru
 */
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
public class DrawingPad implements ActionListener{
    private int height;
    private int width;
    private int bheight;
    private int bwidth;
    private JMenuItem saveFile, openFile;
    private JPanel p, p2, p3;
    private JButton b1, b2, b3, b4;
    private Color[][] board = new Color[20][20];
    private MySketchPad panel = null;
    private myMouse mouse = new myMouse();
    private Color mainc = Color.BLACK;
    private File input;
    private File output;
    private String sfile;
    public static void main(String[] args) {
        DrawingPad bob = new DrawingPad();
        bob.createBoard();
        bob.createWindow();
    }

    private void createBoard(){
        for(int i = 0; i < 20; i++){

            for(int j = 0; j < 20; j++){
                board[i][j] = (Color.WHITE);
            }
        }
    }

    public void createWindow(){
        JFrame window = new JFrame("sampleDraw");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        width = 425;
        height = 427;
        window.setBounds(100, 100, width, 580);
        bheight = 400/20;
        bwidth = 400/20;

        window.setResizable(false);

        //jmenu
        JMenuBar menu = new JMenuBar();
        JMenu fileMenu = new JMenu("file");
        saveFile = new JMenuItem("save", 's');
        openFile = new JMenuItem("open", 'o');
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        menu.add(fileMenu);
        window.setJMenuBar(menu);

        saveFile.addActionListener(this);
        openFile.addActionListener(this);

        //BoxLayout awesome = new boxLayout(3,1);
        //window.setLayout(awesome);
        //window.setLayout(new BoxLayout(window, BoxLayout.Y_AXIS));
        Dimension pD = new Dimension(width,60); 
        Dimension panelD = new Dimension(width,height); 
        panel = new MySketchPad();
        panel.setPreferredSize(panelD);
        panel.setMaximumSize(panelD); 
        panel.setMinimumSize(panelD);
        panel.addMouseListener(mouse);
        panel.addMouseMotionListener(mouse);

        b1 = new JButton("Choose Color");
        b1.setBackground(mainc);
        b1.addActionListener(this);
        b4 = new JButton("Clear");
        b4.addActionListener(this);
        b4.setActionCommand("Clear");
        p = new JPanel();
        p2 = new JPanel();
        p2.setPreferredSize(pD);
        p2.setMaximumSize(pD); 
        p2.setMinimumSize(pD);

        p3 = new JPanel();
        p3.setPreferredSize(pD);
        p3.setMaximumSize(pD); 
        p3.setMinimumSize(pD);

        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        TitledBorder tborder = new TitledBorder("Drawing Color");
        p2.setBorder(tborder);
        p2.setLayout(new FlowLayout());
        p2.add(b1);
        p.add(panel);
        p.add(p2);
        p3.add(b4);
        p.add(p3);
        window.add(p);

        window.setVisible(true);
    }

    class MySketchPad extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D)g;

            g2.setColor(Color.WHITE);

            int xcount = 0;
            int ycount = 0;

            int xpos = 0;
            int ypos = 0;
            for(int i = 0; i < 20; i++){
                for(int j = 0; j < 20; j++){
                    if(board[i][j] != Color.WHITE){

                        g2.setColor(board[i][j]);
                    }
                    g2.fillRect(xpos, ypos, bwidth, bheight);

                    ypos += (bheight+1);
                    if(j == 19){
                        ypos = 0;
                    }
                    g2.setColor(Color.WHITE);
                }
                xpos += (bwidth+1);

            }
        }
    }

    class myMouse extends MouseAdapter{
        public void mouseClicked(MouseEvent e){
            int x = e.getX();
            int y = e.getY();

            position bob = new position();
            bob = findCorner(x, y);
            x = bob.getX()-1;
            y = bob.getY()-1;
            if(e.getButton() == MouseEvent.BUTTON1){
                if(x <= 20 && x >= 0 && y <= 20 && y >= 0){
                    board[x] [y] = mainc;
                    panel.repaint();
                }
            }else if(e.getButton() == MouseEvent.BUTTON3){
                if(x <= 20 && x >= 0 && y <= 20 && y >= 0){
                    board[x] [y] = Color.WHITE;
                    panel.repaint();
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e){
            //System.out.println("Clicked");
            int x = e.getX();
            int y = e.getY();
            //System.out.println(x + " " +  y);

            position bob = new position();
            bob = findCorner(x, y);
            x = bob.getX()-1;
            y = bob.getY()-1;
            //System.out.println(e.getButton());
            if((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK){
                if(x <= 20 && x >= 0 && y <= 20 && y >= 0){
                    board[x] [y] = mainc;
                    panel.repaint();
                }
            }else if((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK){
                if(x <= 20 && x >= 0 && y <= 20 && y >= 0){
                    board[x] [y] = Color.WHITE;
                    panel.repaint();
                }
            }

        }
    }

    private position findCorner(int x, int y){
        int xpos = 0;
        int tempx = 0;
        for(int i = 0; i < 20; i++){
            if(xpos <= x){
                tempx++;
            }else{
                break;
            }
            xpos += (bwidth+1);
        }

        int ypos = 0;
        int tempy = 0;
        for(int i = 0; i < 20; i++){
            if(ypos <= y){
                tempy++;
            }else{
                break;
            }
            ypos += (bheight+1);
        }
        //System.out.println("X " + tempx + " Y " + tempy);
        position pos = new position(tempx, tempy);
        return pos;
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == b1){
            JColorChooser cc = new JColorChooser(Color.blue);
            mainc = cc.showDialog(null, "cc", Color.blue);
            b1.setBackground(mainc);
        }else if(e.getSource() == b4){
            clearBoard();
            panel.repaint();
        }
        
        
        if(e.getSource() == openFile){
            JFileChooser bill = new JFileChooser();
            
            try{
                bill.showOpenDialog(null);
                input = bill.getSelectedFile();
                readPPM();
                panel.repaint();
            }catch(NullPointerException ee){
                //ee.printStackTrace();
            }
        }

        if(e.getSource() == saveFile){
            JFileChooser bill = new JFileChooser();
            
            try{
                //input = bill.getSelectedFile();
                bill.showSaveDialog(null);
                String dir = bill.getCurrentDirectory().getName();
                //sfile = bill.getSelectedFile().getName();
                //System.out.println(sfile);
                output = bill.getSelectedFile();
                savePPM();

                //panel.repaint();
            }catch(NullPointerException ee){
                //ee.printStackTrace();
            }
        }
    }

    public void clearBoard(){
        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 20; j++){
                board[i][j] = Color.WHITE;
            }
        }
    }

    public void readPPM(){
        try{
            Scanner ppm = new Scanner(input);
            ppm.nextLine();
            ppm.nextLine();
            ppm.nextLine();
            for(int i = 0; i < 20; i++){
                for(int j = 0; j < 20; j++){
                    int red = 0;
                    int green = 0;
                    int blue = 0;
                    if(ppm.hasNext()){
                        red = ppm.nextInt();
                    }

                    if(ppm.hasNext()){
                        green = ppm.nextInt();
                    }

                    if(ppm.hasNext()){
                        blue = ppm.nextInt();
                    }
                    Color stuff = new Color(red, green, blue);
                    board[i][j] = stuff;
                }
            }

        }catch(FileNotFoundException e){
            e.getStackTrace();
        }
    }   

    public void savePPM(){
        //File soutput = new File(sfile);

        FileWriter writer = null;
        try{
            
            writer = new FileWriter(output);
            writer.write("P3\r\n20 20\r\n255\r\n");
            for(int i = 0; i < 20; i++){
                for(int j = 0; j < 20; j++){
                    int red = board[i][j].getRed();
                    int green = board[i][j].getGreen();
                    int blue = board[i][j].getBlue();
                    writer.write(red + " " + green + " " + blue + " ");
                }
                writer.write("\r\n");
            }
            //output.renameTo(new File(sfile));
            writer.close();
        }catch(IOException ex) {
            ex.printStackTrace();
        }

    }
}

class position{
    private int x;
    private int y;

    public position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public position(){
        this.x = 0;
        this.y = 0;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}