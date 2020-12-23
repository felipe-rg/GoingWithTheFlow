package Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Topography extends JPanel{
    Bed bed1;
    Bed bed2;
    Bed bed3;
    Bed bed4;
    Bed bed5;
    Bed bed6;
    Bed bed7;
    Bed bed8;

    public Topography() {
        ArrayList<Bed> beds = new ArrayList<Bed>();

        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(100, 70));
        this.setLayout(null);

        //will make this automatic when i know exactly how the information is going to come
        bed1 = new Bed("1",'f',100, 200, 28, 'f');
        this.add(bed1);
        beds.add(bed1);
        bed2 = new Bed("2",'e',300, 200,0,'x');
        this.add(bed2);
        beds.add(bed2);
        bed3 = new Bed("3",'c',500, 200,0,'x');
        this.add(bed3);
        beds.add(bed3);
        bed4 = new Bed("4",'e',700, 200,0,'x');
        this.add(bed4);
        beds.add(bed4);
        bed5 = new Bed("5",'e',100, 400,0,'x');
        this.add(bed5);
        beds.add(bed5);
        bed6 = new Bed("6",'f',300, 400,65,'m');
        this.add(bed6);
        beds.add(bed6);
        bed7 = new Bed("7",'f',500, 400,19,'m');
        this.add(bed7);
        beds.add(bed7);
        bed8 = new Bed("8",'e',700, 400,0,'x');
        this.add(bed8);
        beds.add(bed8);

        //bed1.addActionListener(evt -> JOptionPane.showMessageDialog(bed1, "Patient: "+bed1.getID()+ "\nAge: " + bed1.getAge() + "\nGender: "+bed1.getGender()));
        for (Bed b : beds){
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    b.printInfo();
                }
            });
        }

    }
}
