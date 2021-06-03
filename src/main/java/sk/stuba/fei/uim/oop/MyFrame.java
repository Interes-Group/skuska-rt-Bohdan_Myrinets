package sk.stuba.fei.uim.oop;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;

public class MyFrame extends JFrame{
    JFrame frame;
    int figtomove = -1;
    ArrayList<Figure> figArray = new ArrayList<Figure>();
    boolean dragging = false;
    int ccolorInd = -1; // 0 - red, 1 - green, blue
    int mode = 0; // 0 - idle, 1 - figure drawing, 2 - change size
    String currentColor = "Black"; // 1-R,2-G,3-B
    JLabel color = new JLabel("Current Color: Choose");
    JButton nextColor = new JButton("Next color");
    JButton btree = new JButton("Tree");
    JButton bmove = new JButton("Move");
    int currentFindex = 0;
    int prevFindex = 0;
    public MyFrame(){
        this.frame = new JFrame("App");
        call();
    }

    String colorDeindex(int ccolorInd){
        switch (ccolorInd){
            case 0:
                return "Red";
            case 1:
                return "Green";
            case 2:
                return "Blue";
            default:
                return "Black";
        }
    }

    Color colorSetter(String fgcolor){
        switch (fgcolor){
            case "Red":
                return Color.RED;
            case "Green":
                return Color.GREEN;
            case "Blue":
                return Color.BLUE;
        }
        return Color.BLACK;
    }


    void init_buttons(JButton btree,JButton bmove,JButton bnextColor){
        btree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                figArray.add(new Figure(colorDeindex(ccolorInd)));
                currentFindex++;
                System.out.println("HELLO");
            }
        });
        bmove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 2;

            }
        });
        bnextColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ccolorInd = (ccolorInd + 1)%3;

            }

        });
    }

    void labelState(int mode){
        switch (mode){
            case 1:
                color.setText("DRAWING");
            case 0:
                color.setText("Color: "+ colorDeindex(ccolorInd));
        }
        frame.repaint();
    }


    int inShape(int x,int y,ArrayList<Figure> figArray){
        if(figArray.toArray().length>0) {
            for (int i = figArray.toArray().length - 1; i >= 0; i--) {
                if(figArray.get(i).top.contains(x,y)){
                    return i;
                }
                if(figArray.get(i).stem.contains(x,y)){
                    return i;
                }

            }

        }
        return -1;
    }

    void call(){

        JPanel dCanvas = new JPanel() {
            Graphics2D g2;
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(3));
                labelState(mode);
                for(Figure fgx : figArray){
                    if(fgx.isVisibility()&&(currentFindex>0)){
                        g2.setColor(colorSetter(fgx.getColor()));
                        fgx.top = new Ellipse2D.Double(fgx.start_coord_x,fgx.start_coord_y,fgx.top_size_x,fgx.top_size_y);
                        fgx.stem = new Rectangle2D.Double((fgx.start_coord_x+(double)(int)(double)fgx.top_size_x/3),fgx.start_coord_y+fgx.top_size_y,(double)(int)(double)fgx.top_size_x/3,(double)(int)(double)fgx.top_size_y/2);
                        g2.draw(fgx.top);
                        g2.draw(fgx.stem);
                    }

                }
            }
        };


        dCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(mode == 0&&(currentFindex>0) && currentFindex!=prevFindex ){

                    figArray.get(currentFindex-1).setStart_coord_x(e.getX());
                    figArray.get(currentFindex-1).setStart_coord_y(e.getY());
                    figArray.get(currentFindex-1).setEnd_coord_x(figArray.get(currentFindex-1).getStart_coord_x());
                    figArray.get(currentFindex-1).setEnd_coord_y(figArray.get(currentFindex-1).getStart_coord_y());
                    figArray.get(currentFindex-1).setVisibility(true);
                    mode = 1;
                    labelState(mode);
                    frame.repaint();
                }
                if(mode == 2){
                    System.out.println("INSHAPE DETECTION");
                    figtomove = inShape(e.getX(),e.getY(),figArray);
                    System.out.printf("FIGTOMOVE%d",figtomove);

                }

            }
        });
        dCanvas.addMouseMotionListener(new MouseAdapter() {     //canvas drag
            @Override
            public void mouseDragged(MouseEvent e) {
                
                if(mode == 1&&currentFindex>0){
                    System.out.printf("%d,%d\n",figArray.get(currentFindex-1).getTop_size_x(),figArray.get(currentFindex-1).getTop_size_y());
                    figArray.get(currentFindex-1).setStart_coord_x(figArray.get(currentFindex-1).getEnd_coord_x()-(e.getX() - figArray.get(currentFindex-1).getEnd_coord_x()));
                    figArray.get(currentFindex-1).setStart_coord_y(figArray.get(currentFindex-1).getEnd_coord_y()-(e.getY() - figArray.get(currentFindex-1).getEnd_coord_y()));

                    if(e.getX() - figArray.get(currentFindex-1).getEnd_coord_x() > 0 && figArray.get(currentFindex-1).getTop_size_x() >= 2){
                        figArray.get(currentFindex-1).setTop_size_x(figArray.get(currentFindex-1).getTop_size_x()-1);
                        figArray.get(currentFindex-1).setTop_size_y(figArray.get(currentFindex-1).getTop_size_x()*2/3);
                    }
                    else if(e.getX() - figArray.get(currentFindex-1).getEnd_coord_x() <= 0){
                        figArray.get(currentFindex-1).setTop_size_x(figArray.get(currentFindex-1).getTop_size_x()+1);
                        figArray.get(currentFindex-1).setTop_size_y(figArray.get(currentFindex-1).getTop_size_x()*2/3);
                    }
                    if(e.getY() - figArray.get(currentFindex-1).getEnd_coord_y() > 0 && figArray.get(currentFindex-1).getTop_size_y() >= 2){
                        figArray.get(currentFindex-1).setTop_size_y(figArray.get(currentFindex-1).getTop_size_y()-1);
                        figArray.get(currentFindex-1).setTop_size_x(figArray.get(currentFindex-1).getTop_size_y()*3/2);
                    }
                    else if(e.getY() - figArray.get(currentFindex-1).getEnd_coord_y() <= 0){
                        figArray.get(currentFindex-1).setTop_size_y(figArray.get(currentFindex-1).getTop_size_y()+1);
                        figArray.get(currentFindex-1).setTop_size_x(figArray.get(currentFindex-1).getTop_size_y()*3/2);
                    }

                    dragging = true;

                }
                else if(mode == 2){
                    if(currentFindex>0 && figtomove != -1){
                        figArray.get(figtomove).setStart_coord_x(figArray.get(figtomove).getEnd_coord_x()-(e.getX() - figArray.get(figtomove).getEnd_coord_x()));
                        figArray.get(figtomove).setStart_coord_y(figArray.get(figtomove).getEnd_coord_y()-(e.getY() - figArray.get(figtomove).getEnd_coord_y()));

                        dragging = true;
                    }
                    if(figtomove == -1){
                        mode = 0;
                    }
                }

            }
        });



        dCanvas.addMouseListener(new MouseAdapter() {     //canvas drop
            @Override
            public void mouseReleased(MouseEvent e) {
                if(dragging && mode == 1){
                    System.out.println("MOUSE RELEASED!");
                    dragging = false;
                    mode = 0;
                    prevFindex = currentFindex;
                }
                if(dragging && mode == 2){
                    dragging = false;
                    mode = 0;
                }

            }
        });

        init_buttons(btree,bmove,nextColor);
        frame.setBounds(0, 0, 540, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel bpanel = new JPanel();
        bpanel.setLayout(new GridLayout(1,4));
        bpanel.add(btree);
        bpanel.add(bmove);
        bpanel.add(nextColor);
        bpanel.add(color);
        frame.add(bpanel,BorderLayout.SOUTH);
        frame.add(dCanvas,BorderLayout.CENTER);
        dCanvas.setVisible(true);
        dCanvas.setFocusable(true);
        frame.setFocusable(true);
        frame.setVisible(true);
        while(true){
            frame.repaint();
        }
    }
}
