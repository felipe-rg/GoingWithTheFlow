package Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bed extends JButton{
    String BedId;
    char status;    //Character saying if bed is free (f), if it is occupied (o) or closed (c)
    Integer age;
    char gender;

    public Bed(String BedId, char status, Integer x, Integer y, Integer age, char gender) {
        this.BedId = BedId;
        this.status = status; //f = full, e = empty, c = closed
        this.gender = gender;
        this.age = age;

        this.setText(BedId);
        this.setBounds(x, y, 70, 140);
        this.setOpaque(true);
        if(status == 'f'){setBackground(Color.RED); }
        if(status == 'e'){setBackground(Color.GREEN);}
        if(status == 'c'){setBackground(Color.YELLOW);}
    }

    public String getID(){
        return this.BedId;
    }

    public Integer getAge(){
        return this.age;
    }

    public char getGender(){
        return this.gender;
    }

    public void makeFull(){
        if(this.status == 'e'){
            this.status = 'f';
        }
    }

    public void makeEmpty(){
        if(this.status == 'f' || this.status == 'c'){
            this.status = 'e';
        }
    }



}
