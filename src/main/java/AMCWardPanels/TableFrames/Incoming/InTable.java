package AMCWardPanels.TableFrames.Incoming;

import AMCWardPanels.TableFrames.MainPanel;
import AMCWardPanels.Title;
import Methods.GeneralWard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InTable {

    public InTable(GeneralWard methods){
        //We create a new frame
        JFrame frame = new JFrame("Incoming Patients");
        JPanel mainPanel = new JPanel(new BorderLayout());

        //Creating panel containing the table
        InTablePanel inTablePanel = new InTablePanel(methods);

        //Buttons that will be displayed in the title panel at the top
        JButton backButton = new JButton("Back");
        JButton refreshButton = new JButton("Refresh Page");

        //Refresh button action (reopen frame)
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new InTable(methods);
            }
        });

        //Backbution action (close frame)
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        //Title Panel
        Title titlePanel = new Title("Incoming Patients", backButton, refreshButton, 350, 350);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        //We create the mainPanel where everything will be
        MainPanel mainTablePanel = new MainPanel(true,  1, inTablePanel.getAMCorLS());

        //We add the titlePanel and mainTablePanel to the mainPanel
        mainTablePanel.add(inTablePanel, BorderLayout.CENTER);
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(mainTablePanel, BorderLayout.CENTER);

        //Adding mainPanel to frame
        frame.add(mainPanel);


        //Standard frame editing
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

    }
}
