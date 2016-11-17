package Server;


import java.io.*;
import java.util.Calendar;
import java.util.Scanner;
import java.text.SimpleDateFormat;


public class emergency extends emergency_server {

    private String location;
    private int injuryNum;
    private String typeInjury;
    private String finalText;
    private static String jorge = "C:/Users/jorge/OneDrive/Documentos/GitHub/chamenta/Truthful_emergencies/src/Log/Logfile.txt";
    private static String leo = "C:/Users/lj0se/IdeaProjects/Truthful_emergencies/Log/Logfile.txt";

    public emergency(){

    }
    public emergency(String location, int injuryNum, String typeInjury){
        this.location = location;
        this.injuryNum = injuryNum;
        this.typeInjury = typeInjury;

        printInfo();
        writeInLog();
    }

    public static void main(String []args){

    }

    private void printInfo(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        finalText = "\t";
        finalText += sdf.format(cal.getTime()) + ": " + injuryNum + " ambulances sent to " + location;
        System.out.println(finalText);
    }

    private void writeInLog(){
        try {
            File file = new File(jorge);
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fileWriter);

            bw.write(encryptString(finalText, getFileKey()) + "\n");
            bw.close();
        }
        catch(IOException e){
            System.out.println("Error 2 on the file");
        }
        catch(Exception e){
            System.out.println("Error Encrypting logs!");
        }

    }


}


