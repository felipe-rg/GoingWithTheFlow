package AMCWardPanels;

import Client.Bed;
import Methods.GeneralWard;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Topography extends JPanel{
    ArrayList<BedButton> beds = new ArrayList<>();
    Integer RCount;
    Integer OCount;
    Integer GCount;
    ArrayList<Bed> dbBeds;
    BedStatus bedstatus;

    public Topography(BedStatus bedstatus, GeneralWard methods) {

        this.bedstatus = bedstatus;

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
            BedButton newBed = new BedButton(this, methods, b, x, y);
            beds.add(newBed);
            this.add(newBed);
        }

        CountBeds();
        updateBedStatus(bedstatus);

        for (BedButton b : beds){
            b.addActionListener(evt -> {
                if(b.getBedButtonStatus().equals("O")){ b.printInfoFull(); }
                if(b.getBedButtonStatus().equals("F") || b.getBedButtonStatus().equals("C")){ b.printInfoEmpty(); }

                // every time something is done to the beds, check whether the bedstatus must change and update it
                CountBeds();
                updateBedStatus(bedstatus);
            });
        }
    }

    // counts how many red-green-orange beds there are
    public void CountBeds() {
        OCount = GCount = RCount = 0;
        for (BedButton b : beds) {
            if (b.getBedButtonStatus().equals("C")) { RCount = RCount + 1; }
            if (b.getBedButtonStatus().equals("F")) { GCount = GCount + 1; }
            if (b.getBedButtonStatus().equals("O")) {
                LocalDateTime leaving = b.getBedButtonETD();
                LocalDateTime now = LocalDateTime.now();
                if(leaving.isBefore(now.plusHours(4))){
                    RCount = RCount + 1;
                }
                else{ OCount = OCount + 1; }
            }
        }
    }

    //updates bed status numbers
    public void updateBedStatus(BedStatus bedstatus){
        bedstatus.setAmbarBedsNum(OCount);
        bedstatus.setGreenBedsNum(GCount);
        bedstatus.setRedBedsNum(RCount);
    }

    public void refresh(){
        CountBeds();
        updateBedStatus(bedstatus);
    }

}
