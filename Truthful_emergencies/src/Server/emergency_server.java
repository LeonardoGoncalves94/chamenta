package Server;

import Interface.IClient;
import Interface.Iemergency;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.*;

import static javax.xml.bind.DatatypeConverter.*;


public class emergency_server implements Iemergency
{
    private static String location;
    private static int injury_num;
    private static String type_injury;
    private static String jorge = "C:/Users/jorge/OneDrive/Documentos/GitHub/chamenta/Truthful_emergencies/src/Log/Logfile.txt";
    private static String jorge2 = "C:/Users/jorge/OneDrive/Documentos/GitHub/chamenta/Truthful_emergencies/src/Log/Offensefile.txt";
    private static String jorge3 = "C:/Users/jorge/OneDrive/Documentos/GitHub/chamenta/Truthful_emergencies/src/Log/Keys.txt";
    private static String leo = "C:/Users/lj0se/IdeaProjects/Truthful_emergencies/Log/Logfile.txt";
    private static String leo2 = "C:/Users/lj0se/IdeaProjects/Truthful_emergencies/Log/Offensefile.txt";
    private static int session = 1;
    private static boolean EmergencyReceived = false;
    private static String key;
    private List<String> ConnectedClients = new ArrayList<String>();
    private int id;

    public emergency_server(){
        writeKeyFile();
    }

    public static void main(String []args)
    {
        try {

            emergency_server obj = new emergency_server();
            Iemergency stub = (Iemergency) UnicastRemoteObject.exportObject(obj, 0);

            Registry registry = LocateRegistry.createRegistry(1000);
            registry.bind("Iemergency", stub);



            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }

        createFileEntry();

        while(true);

    }
   static public void getSession(){
        int countSeenSessions = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(jorge))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = decryptString(line, key);
                String[] parts = line.split(" ");
                if(parts[1].equals("Session"))
                {
                    countSeenSessions++;
                }
            }
        }
        catch(Exception e){
            System.out.println("Exception on split: " + e);
        }

        session = countSeenSessions + 1;
    }

    //////////////////////////////////////////
    //REMOTE FUNCTIONS
    public int registerChannel(String secretEncryptedWPublicKey){
        String secret = "";
        //secret = decriptSecret(secretEncryptedWPublicKey);
        addClient(id, secret);
        return id;
    }

    public  void receiveMessage(int number, String type, String location, IClient client){

        new emergency(location, number, type);
        try {
            client.sendFeedback(true);
        }
        catch(Exception e){

        }
    }

    public void checkOffense(boolean bool){
        if(bool){
            try {
                File file = new File(jorge2);

                if (!file.exists()) {
                    file.createNewFile();
                }

                FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
                BufferedWriter bw = new BufferedWriter(fileWriter);

                bw.write(encryptString("New offense on " + getData(), key)+ "\n");
                bw.close();
            }
            catch(IOException e){
                System.out.println("Error on offense file!");
            }
            catch(Exception e){
                System.out.println("Error Encrypting offenses file!");
            }
        }
    }

    //END OF REMOTE FUNCTIONS
    ///////////////////////////////////////////

    private static void createFileEntry(){

        try {
            File file = new File(jorge);

            if (!file.exists()) {
                file.createNewFile();
            }
                getSession();
                String str = getData();
                FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
                BufferedWriter bw = new BufferedWriter(fileWriter);

                bw.write(encryptString(str, key) + "\n");
                bw.close();
        }
        catch(IOException e){
            System.out.println("Error 1 on file:" + e);
        }
        catch(Exception e){
            System.out.println("Error Encrypting session");
        }
        System.out.println("New session started: Session " + session);
    }

    private void writeKeyFile(){
        try {
            File file = new File(jorge3);

            if (!file.exists()) {
                file.createNewFile();

                generateKey();
                FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
                BufferedWriter bw = new BufferedWriter(fileWriter);

                bw.write(key + "\n");
                bw.close();
            }
            else{
                BufferedReader br = new BufferedReader(new FileReader(jorge3));
                this.key=br.readLine();
            }


        }
        catch(IOException e){
            System.out.println("Error writing on key file");
        }
    }

    private static String getData(){

        String data = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        data = "New Session " + session + " on: " + sdf.format(cal.getTime()) + " of ";
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        data += sdf.format(cal.getTime());

        return data;
    }


    private void generateKey(){
    try {

        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        SecretKey skey = generator.generateKey();
        byte[] raw = skey.getEncoded();
        this.key = printHexBinary(raw);
    }
    catch(Exception e) {
        System.out.println("Error Creating key: " + e);
    }
}

    public static String encryptString(String str, String key) throws Exception{
        Cipher encryptCipher = Cipher.getInstance("AES");
        byte[] byteKey = parseHexBinary(key);
        SecretKeySpec skeySpec = new SecretKeySpec(byteKey, "AES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] encryptedString = encryptCipher.doFinal(str.getBytes());
        return printBase64Binary(encryptedString);
    }

    public static String decryptString(String str, String key) throws Exception{

        String newString = "";

        Cipher encryptCipher = Cipher.getInstance("AES");
        byte[] byteKey = parseHexBinary(key);
        SecretKeySpec skeySpec = new SecretKeySpec(byteKey, "AES");
        encryptCipher.init(Cipher.DECRYPT_MODE, skeySpec);


        byte[] decrypted = encryptCipher.doFinal(parseBase64Binary(str));
        newString = new String(decrypted, "ASCII");

        return newString;
    }

    public String getFileKey(){
        return key;
    }

    private void addClient(int id, String secret){       //ADD CLIENT INFO AS PARAMETERS
        ConnectedClients.add(id + "," + secret);
    }

    private void removeClient(int id){

        ConnectedClients.remove(id+1);
    }
}
