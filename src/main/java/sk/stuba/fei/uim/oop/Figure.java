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
import java.util.ArrayList;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Figure {
    String color;
    Shape top;
    Shape stem;
    int start_coord_x;
    int start_coord_y;
    int end_coord_x;
    int end_coord_y;
    int top_size_x;
    int top_size_y;
    boolean visibility;
    public Figure(String color){
        this.color = color;
        this.visibility = false;
        this.top_size_x = 10;
        this.top_size_y = top_size_x*2/3;
        this.top = new Ellipse2D.Double(start_coord_x,start_coord_y,top_size_x,top_size_y);
        this.stem = new Rectangle2D.Double((start_coord_x+(double)(int)(double)top_size_x/3),start_coord_y+top_size_y,(double)(int)(double)top_size_x/3,(double)(int)(double)top_size_x/3);
    }
}
