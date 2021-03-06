package AMCWardPanels.TableFrames.Transfer;

import AMCWardPanels.TableFrames.MainPanel;
import AMCWardPanels.Title;
import Methods.AMCWard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransTable {

    public TransTable(AMCWard methods){

        //Frame containing everything
        JFrame frame = new JFrame("Transferring Patients");
        JPanel mainPanel = new JPanel(new BorderLayout());

        //Panel containing the table
        TransTablePanel transtablePanel = new TransTablePanel(methods);


        //Buttons that will be displayed in the title panel at the top
        JButton backButton = new JButton("Back");
        JButton refreshButton = new JButton("Refresh Page");

        //Refresh button action (reopen frame)
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new TransTable(methods);
            }
        });

        //Back button action (close frame)
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        //Title Panel
        Title titlePanel = new Title("Transfer Patients", backButton, refreshButton, 350, 350);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));


        //MainPanel that contains the tablePanel and the titlePanel
        MainPanel mainTablePanel = new MainPanel(false, 2, "AMC");


        //Adding the title and the transtablePanel to the mainPanel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainTablePanel.add(transtablePanel, BorderLayout.CENTER);
        mainPanel.add(mainTablePanel, BorderLayout.CENTER);

        //Adding mainPanel to frame
        frame.add(mainPanel);


        //Standard frame editing
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);                                             // makes JFrame visible
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

    }
}