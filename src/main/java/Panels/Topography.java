package Panels;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Topography extends JPanel{
    //private ArrayList<Bed> bedList;
    Bed bed1;
    Bed bed2;
    Bed bed3;
    Bed bed4;
    Bed bed5;
    Bed bed6;
    Bed bed7;
    Bed bed8;


    public Topography(){
        this.setPreferredSize(new Dimension(900,700));
        this.setBackground(Color.white);
        this.setLayout(null);
        outline(this);

        setupBeds();


    }

    public void setupBeds(){
        this.bed1 = new Bed("1", 100, 100, 'f');
        this.bed2 = new Bed("2", 300, 100, 'f');
        this.bed3 = new Bed("3", 500, 100, 'f');
        this.bed4 = new Bed("4", 700, 100, 'f');
        this.bed5 = new Bed("5", 100, 400, 'f');
        this.bed6 = new Bed("6", 300, 400, 'f');
        this.bed7 = new Bed("7", 500, 400, 'f');
        this.bed8 = new Bed("8", 700, 400, 'f');

        this.add(bed1);
        this.add(bed2);
        this.add(bed3);
        this.add(bed4);
        this.add(bed5);
        this.add(bed6);
        this.add(bed7);
        this.add(bed8);
    }
    public void outline(JPanel panel){
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
}
