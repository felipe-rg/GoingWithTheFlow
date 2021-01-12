package AMCWardPanels;

/*
This class is the "map" that can be found in the AMCs and Long Stay Wards. It contains an ArrayList of BedButtons, which
run BedButton methods when clicked. It also communicates with and updates BedStatus so it count the right number of
each of the different kinds of beds
 */

import AMCWardPanels.TableFrames.BedColorCodePanel;
import Client.Bed;
import Methods.GeneralWard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class Topography extends JPanel{
    ArrayList<BedButton> beds = new ArrayList<>();
//    Integer RCount;
//    Integer OCount;
//    Integer GCount;
    ArrayList<Bed> dbBeds;
    BedStatus bedstatus;

    // constructor initializes all the beds in the ward with their corresponding patient information
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
            else if(count == 2 || count == 6){
                x=400;
            }
            else if(count == 3 || count == 7){
                x=600;
            }
            else if(count == 4 || count == 8){
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


        for (BedButton b : beds){
            b.addActionListener(evt -> {
                if (b.getBedButtonStatus().equals("O")) {
                    b.printInfoFull();
                }
                if (b.getBedButtonStatus().equals("F")) {
                    b.printInfoEmpty();
                }
                if (b.getBedButtonStatus().equals("C")) {
                    b.printInfoClosed();
                }
                // every time something is done to the beds, check whether the bedstatus must change and update it
            });
        }

        //Adding bottom panel indicating colorcode for beds
        BedColorCodePanel bedColorCodePanel = new BedColorCodePanel();
        bedColorCodePanel.setBounds(2, 2, 1200, 50);
        this.add(bedColorCodePanel);
    }

    // used to make a BedButton red (occupied without ETD)
    public void makeBedButtonRed(int bedId){
        BedButton bedButton = null;
        for(BedButton bed:beds){
            if(bed.getBedButtonBedId()==bedId){
                bedButton = bed;
            }
        }
        for( ActionListener al : bedButton.getActionListeners() ) {
            bedButton.removeActionListener( al );
        }
        BedButton finalBedButton = bedButton;
        finalBedButton.setBorder(BorderFactory.createLineBorder(Color.decode("#E74C3C"),4));
        bedButton.addActionListener(evt ->{
            finalBedButton.printInfoFull();
        });
    }

    // used to make BedButton green (free)
    public void makeBedButtonGreen(int bedId){
        BedButton bedButton = null;
        for(BedButton bed:beds){
            if(bed.getBedButtonBedId()==bedId){
                bedButton = bed;
            }
        }
        for( ActionListener al : bedButton.getActionListeners() ) {
            bedButton.removeActionListener( al );
        }
        BedButton finalBedButton = bedButton;
        finalBedButton.setBorder(BorderFactory.createLineBorder(Color.decode("#2ECC71"),4));
        bedButton.addActionListener(evt ->{
            finalBedButton.printInfoEmpty();
        });
    }




}
