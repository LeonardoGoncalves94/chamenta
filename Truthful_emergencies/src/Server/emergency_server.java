package Server;

import Interface.IClient;
import Interface.Iemergency;

import java.io.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Date;




public class emergency_server implements Iemergency
{
    private static String location;
    private static int injury_num;
    private static String type_injury;
    private static String jorge = "C:/Users/jorge/OneDrive/Documentos/Java SIRS/Truthful_emergencies/Log/Logfile.txt";
    private static String jorge2 = "C:/Users/jorge/OneDrive/Documentos/Java SIRS/Truthful_emergencies/Log/Offensefile.txt";
    private static String leo = "C:/Users/lj0se/IdeaProjects/Truthful_emergencies/Log/Logfile.txt";
    private static String leo2 = "C:/Users/lj0se/IdeaProjects/Truthful_emergencies/Log/Offensefile.txt";
    private static int session = 1;
    private static boolean EmergencyReceived = false;

    public emergency_server(){}

    public static void main(String []args)
    {
        getSession();
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
   static public void getSession() // tá a entrar no if(parts[1].equals("Session")) sempre
    {
        int countSeenSessions = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(leo))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if(parts[1].equals("Session"))
                {
                    countSeenSessions++;
                }
            }
        }
        catch(Exception e){}

        session = countSeenSessions + 1;
    }


    //REMOTE FUNCTIONS
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
                File file = new File(leo2);

                if (!file.exists()) {
                    file.createNewFile();
                }

                FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
                BufferedWriter bw = new BufferedWriter(fileWriter);

                bw.write("New offense on " + getData()+ "\n");
                bw.close();
            }
            catch(IOException e){
                System.out.println("Error on offense file!");
            }
        }
    }

    //END OF REMOTE FUNCTIONS

    private static void createFileEntry(){
        try {
            String str = getData();
            File file = new File(leo);

            if (!file.exists()) {
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
                BufferedWriter bw = new BufferedWriter(fileWriter);
                bw.write(str + "\n");
                bw.close();
            }
            else {


                FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
                BufferedWriter bw = new BufferedWriter(fileWriter);

                bw.write(str + "\n");
                bw.close();
            }
        }
        catch(IOException e){
            System.out.println("Error 1 on file");
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


}
