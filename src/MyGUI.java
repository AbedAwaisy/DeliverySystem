import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyGUI extends JFrame {
    private JLabel processingTimeLabel;
    private JTextField processingTimeField;
    private JLabel numOfDriversLabel;
    private JTextField numOfDriversField;
    private JButton startButton;
    private JButton cancelButton;

    public MyGUI() {
        // Set up the JFrame
        setTitle("My GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLayout(new GridLayout(4, 2));

        // Create and add components to the JFrame
        processingTimeLabel = new JLabel(" Car Officer processing time:");
        add(processingTimeLabel);

        processingTimeField = new JTextField("1.0");
        add(processingTimeField);

        numOfDriversLabel = new JLabel(" Number of drivers:");
        add(numOfDriversLabel);

        numOfDriversField = new JTextField();
        add(numOfDriversField);

        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve the values from the text fields
                double processingTime = Double.parseDouble(processingTimeField.getText());
                int numOfDrivers = Integer.parseInt(numOfDriversField.getText());

                // Perform necessary actions based on the retrieved values
                // ...
                Main.start(numOfDrivers, processingTime);
            }
        });
        add(startButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display a relevant message
                JOptionPane.showMessageDialog(MyGUI.this, "Program interrupted");

                // Close the GUI and exit the program
                dispose();
                System.exit(0);
            }
        });
        add(cancelButton);
    }

    // Optional: Methods to retrieve data from the GUI fields, if needed
    public double getProcessingTime() {
        return Double.parseDouble(processingTimeField.getText());
    }

    public int getNumOfDrivers() {
        return Integer.parseInt(numOfDriversField.getText());
    }

    // Optional: Other methods to perform additional GUI-related actions
}