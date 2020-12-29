package Panels;

import Client.Bed;
import Methods.GeneralWard;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
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
    Integer RCount = 0;
    Integer OCount = 0;
    Integer GCount = 0;
    ArrayList<Bed> dbBeds;

    public Topography(BedStatus bedstatus, GeneralWard methods) {
        try {
            dbBeds = methods.getBeds();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(100, 70));
        this.setLayout(null);
        int count = 0;
        int x = 200;
        int y = 150;
        for(Bed b:dbBeds){
            count++;
            if(count == 1 || count == 5){
                x=200;
            }
            if(count == 2 || count == 6){
                x=400;
            }
            if(count == 3 || count == 7){
                x=600;
            }
            if(count == 4 || count == 8){
                x=800;
            }
            if(count < 5){
                y=150;
            } else {
                y = 450;
            }
            BedButton newBed = new BedButton(methods, b.getId(), b.getStatus(), b.getForSex(), b.getHasSideRoom(), x, y);
            beds.add(newBed);
        }

        CountBeds();
        updateBedStatus(bedstatus);

        for (BedButton b : beds){
            b.addActionListener(evt -> {
                if(b.getStatus() == "F"){ b.printInfoFull(); }
                if(b.getStatus() == "E" || b.getStatus() == "C"){ b.printInfoEmpty(); }

                // every time something is done to the beds, check whether the bedstatus must change and update it
                CountBeds();
                updateBedStatus(bedstatus);
            });
        }
    }

    public void CountBeds() {
        OCount = GCount = RCount = 0;
        for (BedButton b : beds) {
            if (b.getStatus() == "C") { RCount = RCount + 1; }
            if (b.getStatus() == "E") { GCount = GCount + 1; }
            if (b.getStatus() == "F") {
                //this is never true for some reason
                if((b.getETD().getHour() == 00) && (b.getETD().getMinute() == 00)){ RCount = RCount + 1; }
                else{ OCount = OCount + 1; }
            }

        }

    }

    public void updateBedStatus(BedStatus bedstatus){
        bedstatus.setAmbarBedsNum(OCount);
        bedstatus.setGreenBedsNum(GCount);
        bedstatus.setRedBedsNum(RCount);
    }

}
