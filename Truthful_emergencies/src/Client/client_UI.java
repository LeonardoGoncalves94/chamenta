package Client;

import Interface.Iemergency;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class client_UI implements Interface.IClient{
    private JComboBox typeComboBox;
    private JTextField NumberTextField;
    private JTextField locationsTextField;
    private JButton requestAmbulanceButton;
    private JLabel typeOfInjuryLabel;
    private JLabel numberOfInjuriesLabel;
    private JLabel locationLabel;
    private JPanel panelMain;
    private JLabel StatusLabel;
    private JLabel Status;
    private String location;
    private int numberInjuries;
    private String typeInjury;
    private Iemergency stub;
    private Interface.IClient client;


    public client_UI() {
        try {
            Registry registry = LocateRegistry.getRegistry(1000);
            UnicastRemoteObject.exportObject((Interface.IClient)this, 0);
            stub = (Iemergency) registry.lookup("Iemergency");

            client = (Interface.IClient)this;

        }
        catch(Exception e){
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        requestAmbulanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            if(numberInjuries != 0){


                try {
                    stub.receiveMessage(numberInjuries,typeInjury,location, client);
                } catch (Exception t) {
                    System.err.println("Client exception: " + t.toString());
                    t.printStackTrace();
                }

            }

            }
        });
        locationsTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                location = locationsTextField.getText();
            }
        });
        NumberTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    numberInjuries = Integer.parseInt(NumberTextField.getText());
                }
                catch(NumberFormatException t) {
                    System.out.println("This value should be a integer");
                }

            }
        });
        typeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object obj = typeComboBox.getSelectedItem();
                if(obj != null) {
                    typeInjury = obj.toString();
                }
            }
        });
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("cient_UI");
        frame.setContentPane(new client_UI().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

       // String host = (args.length < 1) ? null : args[0];
    }

    public void sendFeedback(boolean bool){
        if(bool){
            Status.setText("Help on the way!");
            offenseGenerator();
        }
        else{
            Status.setText("Communication Error! Try again!");
        }
    }

    public void offenseGenerator(){
        Random random = new Random();
        int i = random.nextInt(99);

        if(i < 100) {
            try {
                stub.checkOffense(true);
            }
            catch (Exception e){

            }
        }
        else{
            try {
                stub.checkOffense(false);
            }
            catch (Exception e){

            }
        }
    }
}

