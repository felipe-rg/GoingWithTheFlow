package AMCWardPanels;

import Methods.AMCWard;
import Methods.GeneralWard;
import Methods.LongstayWard;
import AMCWardPanels.TableFrames.DisTable;
import AMCWardPanels.TableFrames.InTable;
import AMCWardPanels.TableFrames.OthTable;
import AMCWardPanels.TableFrames.TotTable;
import AMCWardPanels.TableFrames.TransTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    Topography top;
    WardInfo here;
    GeneralWard methods;

    public WardInfo(Topography top, LongstayWard methods){
        this.methods = methods;
        this.top = top;
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

    public WardInfo(Topography top, AMCWard methods){
        this.methods = methods;
        this.top = top;
        this.setLayout(new GridLayout(10,1));
        setup();
        transLabel = new JLabel("Transferring Patients");
        transbut = new JButton(String.valueOf(methods.getTransferNumber()));
        editLabel(transLabel);
        editButton(transbut);
        transbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TransTable transTable = new TransTable(top, here, methods);
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
        here = this;
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
        inbut = new JButton(String.valueOf(methods.getIncomingNumber()));
        disbut = new JButton(String.valueOf(methods.getDischargeNumber()));
        othbut = new JButton(String.valueOf(methods.getOtherNumber()));
        totbut = new JButton(String.valueOf(methods.getPatientsInWard()));
        //Editing Buttons
        editButton(inbut, disbut, othbut, totbut);


        //Actionlisteners for buttons opening new window displaying table
        inbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InTable inTable = new InTable(top, here, methods);
            }
        });

        disbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DisTable disTable = new DisTable(top, here, methods);
            }
        });

        othbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OthTable othTable = new OthTable(top, here, methods);
            }
        });

        totbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TotTable totTable = new TotTable(top, here, methods);
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

    public void refresh(){
        inbut = new JButton(String.valueOf(methods.getIncomingNumber()));
        disbut = new JButton(String.valueOf(methods.getDischargeNumber()));
        othbut = new JButton(String.valueOf(methods.getOtherNumber()));
        totbut = new JButton(String.valueOf(methods.getPatientsInWard()));
    }


}
