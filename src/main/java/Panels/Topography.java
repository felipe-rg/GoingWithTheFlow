package Panels;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Topography extends JPanel{
    BedButton bed1;
    BedButton bed2;
    BedButton bed3;
    BedButton bed4;
    BedButton bed5;
    BedButton bed6;
    BedButton bed7;
    BedButton bed8;
    ArrayList<BedButton> beds = new ArrayList<>();
    Integer FCount = 0;
    Integer CCount = 0;
    Integer ECount = 0;

    public Integer getECount(){ return this.ECount; }
    public Integer getFCount(){ return this.FCount; }
    public Integer getCCount(){ return this.CCount; }

    public Topography() {
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(100, 70));
        this.setLayout(null);

        //will make this automatic when i know exactly how the information is going to come
        bed1 = new BedButton("1",'F',false,200, 150, 28, 'f');
        bed1.setDia("asthma");
        this.add(bed1);
        beds.add(bed1);
        bed2 = new BedButton("2",'E',false,400, 150,0,'x');
        this.add(bed2);
        beds.add(bed2);
        bed3 = new BedButton("3",'E',false,600, 150,0,'x');
        this.add(bed3);
        beds.add(bed3);
        bed4 = new BedButton("4",'C',false,800, 150,0,'x');
        this.add(bed4);
        beds.add(bed4);
        bed5 = new BedButton("5",'E',false,200, 450,0,'x');
        this.add(bed5);
        beds.add(bed5);
        bed6 = new BedButton("6",'F',true,400, 450,65,'m');
        bed6.setDia("COVID-19");
        this.add(bed6);
        beds.add(bed6);
        bed7 = new BedButton("7",'F',false,600, 450,19,'m');
        bed7.setDia("Broken Leg");
        this.add(bed7);
        beds.add(bed7);
        bed8 = new BedButton("8",'E',false,800, 450,0,'x');
        this.add(bed8);
        beds.add(bed8);

        for (BedButton b : beds){
            b.addActionListener(evt -> {
                if(b.getStatus() == 'F'){ b.printInfoFull(); }
                if(b.getStatus() == 'E' || b.getStatus() == 'C'){ b.printInfoEmpty(); }
                b.repaint();
            });
        }
        CountBeds();
    }

    public void CountBeds(){
        for (BedButton b : beds) {
            if(b.getStatus() == 'F'){ FCount = FCount + 1; }
            if(b.getStatus() == 'E'){ ECount = ECount + 1; }
            if(b.getStatus() == 'C'){ CCount = CCount + 1; }
        }
    }

}
