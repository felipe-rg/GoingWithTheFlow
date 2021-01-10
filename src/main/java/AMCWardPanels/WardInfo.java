package AMCWardPanels;

import Methods.AMCWard;
import Methods.GeneralWard;
import Methods.LongstayWard;
import AMCWardPanels.TableFrames.Discharge.DisTable;
import AMCWardPanels.TableFrames.Incoming.InTable;
import AMCWardPanels.TableFrames.Others.OthTable;
import AMCWardPanels.TableFrames.Total.TotTable;
import AMCWardPanels.TableFrames.Transfer.TransTable;
import Methods.tableInfo.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class WardInfo extends JPanel{
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
    GeneralWard methods;


    public WardInfo(LongstayWard methods){
        this.methods = methods;
        this.setLayout(new GridLayout(8,1));
        setup();
        //Adding labels and buttons into Panel
        add(inLabel);
        add(inbut);
        add(disLabel);
        add(disbut);
        add(othLabel);
        add(othbut);
        add(totLabel);
        add(totbut);
    }

    public WardInfo(AMCWard methods){
        this.methods = methods;
        this.setLayout(new GridLayout(10,1));
        setup();

        transLabel = new JLabel("Transferring Patients");
        transbut = new JButton(String.valueOf(methods.getTransNumber()));
        editLabel(transLabel);
        editButton(transbut);
        transbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TransTable transTable = new TransTable(methods);
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

    private void setup(){
        this.setPreferredSize(new Dimension(300,600));
        this.setBackground(Color.white);

        //Labels
        inLabel = new JLabel("Incoming Patients");
        disLabel = new JLabel("Discharge Patients");
        othLabel = new JLabel("Others");
        totLabel = new JLabel("Total Patients in Ward");
        //Editing labels
        editLabel(inLabel, disLabel, othLabel, totLabel);

        //Buttons
        inbut = new JButton(String.valueOf(methods.getInNumber()));
        disbut = new JButton(String.valueOf(methods.getDischargeNumber()));
        othbut = new JButton(String.valueOf(methods.getOthNumber()));
        totbut = new JButton(String.valueOf(methods.getPatientsInWard()));

        //Editing Buttons
        editButton(inbut, disbut, othbut, totbut);


        //Actionlisteners for buttons opening new window displaying table
        inbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InTable inTable = new InTable(methods);
            }
        });

        disbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DisTable disTable = new DisTable(methods);
            }
        });

        othbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OthTable othTable = new OthTable(methods);
            }
        });

        totbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TotTable totTable = new TotTable(methods);
            }
        });
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

    public void setInText(String number){
        inbut.setText(number);
    }
    public void setOthText(String number){
        othbut.setText(number);
    }
    public void setTransText(String number){
        transbut.setText(number);
    }
    public void setDisText(String number){
        disbut.setText(number);
    }
    public void setTotText(String number){
        totbut.setText(number);
    }

}
