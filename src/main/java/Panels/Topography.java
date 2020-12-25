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

    public Topography() {
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(100, 70));
        this.setLayout(null);

        //will make this automatic when i know exactly how the information is going to come
        bed1 = new BedButton("1",'F',false,200, 150, 28, 'f');
        bed1.setDia("asthma");
        bed1.setETDinMins(15);
        this.add(bed1);
        beds.add(bed1);
        bed2 = new BedButton("2",'E',false,400, 150,0,'x');
        bed1.setETDinMins(0);
        this.add(bed2);
        beds.add(bed2);
        bed3 = new BedButton("3",'E',false,600, 150,0,'x');
        bed1.setETDinMins(0);
        this.add(bed3);
        beds.add(bed3);
        bed4 = new BedButton("4",'C',false,800, 150,0,'x');
        bed1.setETDinMins(0);
        this.add(bed4);
        beds.add(bed4);
        bed5 = new BedButton("5",'E',false,200, 450,0,'x');
        bed5.setETDinMins(0);
        this.add(bed5);
        beds.add(bed5);
        bed6 = new BedButton("6",'F',true,400, 450,65,'m');
        bed6.setDia("COVID-19");
        bed6.setETDinMins(200);
        this.add(bed6);
        beds.add(bed6);
        bed7 = new BedButton("7",'F',false,600, 450,19,'m');
        bed7.setDia("Broken Leg");
        bed7.setETDinMins(60);
        this.add(bed7);
        beds.add(bed7);
        bed8 = new BedButton("8",'E',false,800, 450,0,'x');
        bed8.setETDinMins(0);
        this.add(bed8);
        beds.add(bed8);

        //bed1.addActionListener(evt -> JOptionPane.showMessageDialog(bed1, "Patient: "+bed1.getID()+ "\nAge: " + bed1.getAge() + "\nGender: "+bed1.getGender()));
        for (BedButton b : beds){
            b.addActionListener(evt -> {
                b.printInfo();
                b.repaint();
            });
        }

    }


}
