/*
 * Chase Heck and Clint Olsen
 * ECEN 4313/CSCI 4830: Concurrent Programming
 * Final Project: The Simulation of Fire Spreading
 * Class Description: This class is the definition of the user input menu display at the start of the program to 
 * set the various parameters of the fire spread.
 */
import javax.swing.*;

public class JOptionPaneMultiInput {
   public static void main(String[] args) {
      JTextField densityField = new JTextField(5);
      JTextField windField = new JTextField(5);
      JTextField humidityField = new JTextField(5);

      //add input fields
      JPanel myPanel = new JPanel();
      myPanel.add(new JLabel("Tree Density (1: low, 2: mid, 3: high):"));
      myPanel.add(densityField);
      myPanel.add(Box.createHorizontalStrut(15)); // a spacer
      myPanel.add(new JLabel("Wind Direction (n, s, e, w):"));
      myPanel.add(windField);
      myPanel.add(Box.createHorizontalStrut(15)); // a spacer
      myPanel.add(new JLabel("Humidity (l: low, m: mid, h: high):"));
      myPanel.add(humidityField);

      //parse the input for desired values and convert if necessary
      int result = JOptionPane.showConfirmDialog(null, myPanel, 
               "Please Enter Map Info", JOptionPane.OK_CANCEL_OPTION);
      if (result == JOptionPane.OK_OPTION) {
    	  MapBuildTest.density = Integer.parseInt(densityField.getText());
    	  MapBuildTest.wind = (windField.getText()).charAt(0);
    	  MapBuildTest.humidity = (humidityField.getText()).charAt(0);
      }
   }
}


