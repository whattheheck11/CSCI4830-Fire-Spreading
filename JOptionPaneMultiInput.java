import javax.swing.*;

public class JOptionPaneMultiInput {
   public static void main(String[] args) {
      JTextField densityField = new JTextField(5);
      JTextField windField = new JTextField(5);
      JTextField humidityField = new JTextField(5);
      MapBuildTest m = new MapBuildTest();



      JPanel myPanel = new JPanel();
      myPanel.add(new JLabel("Tree Density (1: low, 2: mid, 3: high):"));
      myPanel.add(densityField);
      myPanel.add(Box.createHorizontalStrut(15)); // a spacer
      myPanel.add(new JLabel("Wind Direction (n, s, e, w):"));
      myPanel.add(windField);
      myPanel.add(Box.createHorizontalStrut(15)); // a spacer
      myPanel.add(new JLabel("Humidity (l: low, m: mid, h: high):"));
      myPanel.add(humidityField);

      int result = JOptionPane.showConfirmDialog(null, myPanel, 
               "Please Enter Map Info", JOptionPane.OK_CANCEL_OPTION);
      if (result == JOptionPane.OK_OPTION) {
    	  MapBuildTest.density = Integer.parseInt(densityField.getText());
    	  MapBuildTest.wind = (windField.getText()).charAt(0);
    	  MapBuildTest.humidity = (humidityField.getText()).charAt(0);
    	  
         //System.out.println("x value: " + xField.getText());
         //System.out.println("y value: " + yField.getText());
      }
   }
}


