package AMCWardPanels.TableFrames;
import javax.swing.*;
import java.awt.*;

/*
    This class is the MainPanel where the table will be inputed, it contains the top, right and left side
    white panels to make things look good. It colorCode=true then it will also display the labels indicating
    the color code below the table

 */
public class MainPanel extends JPanel {
    public MainPanel(Boolean colorCode, int condition){

        //Creating panels
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        JPanel colorCodePanel = new ColorCodePanel();
        JPanel smallTopPanel = new JPanel();

        //Setting sizes
        leftPanel.setPreferredSize(new Dimension(100, 100));
        bottomPanel.setPreferredSize(new Dimension(100, 50));
        smallTopPanel.setPreferredSize(new Dimension(100,40));
        rightPanel.setPreferredSize(new Dimension(100,100));
        colorCodePanel.setPreferredSize(new Dimension(100,100));

        //SettingLayout and adding Panels
        this.setLayout(new BorderLayout());
        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.EAST);
        this.add(smallTopPanel, BorderLayout.NORTH);


            //Condition 3 is for distrans table in controlunit
            if (condition ==3){
                leftPanel.setPreferredSize(new Dimension(250,100));
                leftPanel.setLayout(new GridLayout(2,1));

                //Defining Labels
                JLabel transLabel = new JLabel("<html>Transfer<br>Patients</html>");
                JLabel disLabel = new JLabel("<html>Discharge<br>Patients</html>");
                //Editing labels
                setLabel(transLabel, disLabel);
                //Adding labels to left panel
                leftPanel.add(transLabel);
                leftPanel.add(disLabel);
            }

            //Condition 4 is for AMCInfo table
            if (condition == 4){
                leftPanel.setPreferredSize(new Dimension(150,100));
                leftPanel.setLayout(new GridLayout(3,1));
                //Defining Labels
                JLabel AMC1 = new JLabel("AMC1");
                JLabel AMC2 = new JLabel("AMC2");
                JLabel AAU = new JLabel("AAU");
                //Editing labels
                setLabel(AMC1, AMC2, AAU);
                //Adding labels to left panel
                leftPanel.add(AMC1);
                leftPanel.add(AMC2);
                leftPanel.add(AAU);

            }


        //Depending on the value of the boolean colorCode we will have the bottom panel displaying the colors or not
        if (colorCode == true){
            this.add(colorCodePanel, BorderLayout.SOUTH);
        }
        else{
            this.add(bottomPanel, BorderLayout.SOUTH);
        }

    }

    //Function in which you introduce labels and they are automatically edited
    public void setLabel(JLabel ... a){
        for (JLabel i:a){
            i.setOpaque(true);
            i.setHorizontalAlignment(JLabel.CENTER);
            i.setVerticalAlignment(JLabel.CENTER);
            i.setFont(new Font("Verdana", Font.PLAIN, 30));
        }
    }

}