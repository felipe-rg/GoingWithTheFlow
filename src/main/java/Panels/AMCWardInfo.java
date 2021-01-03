package Panels;

import Methods.AMCWard;
import Methods.GeneralWard;
import Panels.TableFrames.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AMCWardInfo extends JPanel{
    //Labels
    JLabel inLabel;
    JLabel transLabel;
    JLabel disLabel;
    JLabel othLabel;
    JLabel totLabel;
    //Buttons
    JButton inbut;
    JButton transbut;
    JButton disbut;
    JButton othbut;
    JButton totbut;


    public AMCWardInfo(AMCWard methods){
        this.setPreferredSize(new Dimension(300,600));
        this.setBackground(Color.white);

        this.setLayout(new GridLayout(10,1));

        //Labels
        inLabel = new JLabel("Incoming Patients");
        transLabel = new JLabel("Transferring Patients");
        disLabel = new JLabel("Discharge Patients");
        othLabel = new JLabel("Others");
        totLabel = new JLabel("Total Patients in Ward");
        //Editing labels
        editLabel(inLabel, transLabel, disLabel, othLabel, totLabel);

        //Buttons
        inbut = new JButton(String.valueOf(methods.incomingNumber));
        transbut = new JButton(String.valueOf(methods.transferNumber));
        disbut = new JButton(String.valueOf(methods.dischargeNumber));
        othbut = new JButton(String.valueOf(methods.otherNumber));
        totbut = new JButton(String.valueOf(methods.transferNumber+methods.dischargeNumber+methods.otherNumber+methods.incomingNumber));
        //Editing Buttons
        editButton(inbut, transbut, disbut, othbut, totbut);


        //Actionlisteners for buttons opening new window displaying table
        inbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InTable inTable = new InTable();
            }
        });

        transbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TransTable transTable = new TransTable();
            }
        });

        disbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DisTable disTable = new DisTable();
            }
        });

        othbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OthTable othTable = new OthTable();
            }
        });

        totbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TotTable totTable = new TotTable();
            }
        });



        //Adding labels and buttons into Panel
        add(inLabel);
        add(inbut);
        add(transLabel);
        add(transbut);
        add(disLabel);
        add(disbut);
        add(othLabel);
        add(othbut);
        add(totLabel);
        add(totbut);

    }

    public void editLabel(JLabel... a){
        for (JLabel i:a){
            //i.setOpaque(true);
            i.setHorizontalAlignment(JLabel.CENTER);
            i.setVerticalAlignment(JLabel.CENTER);
            i.setFont(new Font("Verdana", Font.PLAIN, 20));
            //i.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
        }
    }

    public void editButton(JButton... a){
        for (JButton i:a){
            i.setFont(new Font ("Verdana", Font.PLAIN, 20));
        }
    }

    public void addLabel(JLabel ... a){
        for(JLabel i:a){
            add(i);
        }
    }
    public void addButton(JButton ... a){
        for(JButton i:a){
            add(i);
        }
    }


}