package Client;

import Interface.Iemergency;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.util.Random;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

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
    private static String sessionKey;


    public client_UI() {
        try {
            Registry registry = LocateRegistry.getRegistry(1000);
            UnicastRemoteObject.exportObject((Interface.IClient)this, 0);
            stub = (Iemergency) registry.lookup("Iemergency");

            client = (Interface.IClient)this;

            generateSecret();
            stub.registerChannel(encryptRSA());
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
    private static void generateSecret(){
        try {

            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(128);
            SecretKey skey = generator.generateKey();
            byte[] raw = skey.getEncoded();
            sessionKey = printHexBinary(raw);
        }
        catch(Exception e) {
            System.out.println("Error Creating key: " + e);
        }
    }
    public String encryptRSA() throws Exception
    {


            Key serverKey = stub.getPublicKey();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, serverKey);
            byte[] cipherData = cipher.doFinal(sessionKey.getBytes());
            return printHexBinary(cipherData);
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

