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
    Topography top;
    WardInfo here;
    GeneralWard methods;

    TransTableData transTableData;
    OtherTableData otherTableData;
    IncomingTableData incomingTableData;
    DischargeTableData dischargeTableData;
    TotalTableData totalTableData;

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

        transTableData = new TransTableData(methods.getClient(), methods.getWardId());
        setup();
        transLabel = new JLabel("Transferring Patients");
        transbut = new JButton(String.valueOf(transTableData.getNumber()));
        editLabel(transLabel);
        editButton(transbut);
        transbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TransTable transTable = new TransTable(top, here, methods, transTableData);
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
        otherTableData = new OtherTableData(methods.getClient(), methods.getWardId());
        incomingTableData = new IncomingTableData(methods.getClient(), methods.getWardId());
        dischargeTableData = new DischargeTableData(methods.getClient(), methods.getWardId());
        totalTableData = new TotalTableData(methods.getClient(), methods.getWardId());
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
        inbut = new JButton(incomingTableData.getNumber());
        disbut = new JButton(dischargeTableData.getNumber());
        othbut = new JButton(otherTableData.getNumber());
        totbut = new JButton(totalTableData.getNumber());

        //Editing Buttons
        editButton(inbut, disbut, othbut, totbut);


        //Actionlisteners for buttons opening new window displaying table
        inbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InTable inTable = new InTable(top, here, methods, incomingTableData);
            }
        });

        disbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DisTable disTable = new DisTable(top, here, methods, dischargeTableData);
            }
        });

        othbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OthTable othTable = new OthTable(top, here, methods, otherTableData);
            }
        });

        totbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TotTable totTable = new TotTable(top, here, methods, totalTableData);
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
        try {
            if(methods.getWardType(methods.getWardId()).equals("AMU")){
                TransTableData transTableData = new TransTableData(methods.getClient(), methods.getWardId());
                transbut.setText(transTableData.getNumber());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        otherTableData = new OtherTableData(methods.getClient(), methods.getWardId());
        incomingTableData = new IncomingTableData(methods.getClient(), methods.getWardId());
        dischargeTableData = new DischargeTableData(methods.getClient(), methods.getWardId());
        totalTableData = new TotalTableData(methods.getClient(), methods.getWardId());
        othbut.setText(otherTableData.getNumber());
        inbut.setText(incomingTableData.getNumber());
        disbut.setText(dischargeTableData.getNumber());
        totbut.setText(totalTableData.getNumber());

    }


}
