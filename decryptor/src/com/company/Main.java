package com.company;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Key;
import java.util.Base64;
import java.util.Scanner;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.parseHexBinary;

public class Main {

    public static void main(String[] args) {
        String key = "";
        String text = "";
        int option = 0;
        String path="";


            Scanner scan = new Scanner(System.in);

            System.out.println("Which file do you want to decrypt?");
            System.out.println("1. Logfile");
            System.out.println("2. Offensefile");

        while(path.equals("")) {
            option = scan.nextInt();

            switch (option) {
                case 1:
                    path = "C:/Users/jorge/OneDrive/Documentos/GitHub/chamenta/Truthful_emergencies/src/Log/Logfile.txt";
                    break;
                case 2:
                    path = "C:/Users/jorge/OneDrive/Documentos/GitHub/chamenta/Truthful_emergencies/src/Log/Offensefile.txt";
                    break;
                default:
                    System.out.println("Invalid option, try again:");
                    break;
            }
        }

        try {

            BufferedReader br = new BufferedReader(new FileReader("C:/Users/jorge/OneDrive/Documentos/GitHub/chamenta/Truthful_emergencies/src/Log/Keys.txt"));
            key = br.readLine();


            String str = "";
            if(option == 1){
                str = "C:/Users/jorge/OneDrive/Documentos/GitHub/chamenta/Truthful_emergencies/src/Log/LogfileDecrypted.txt";
            }
            else if(option == 2){
                str = "C:/Users/jorge/OneDrive/Documentos/GitHub/chamenta/Truthful_emergencies/src/Log/OffensefileDecrypted.txt";
            }

            File file = new File(str);

            if(!file.exists()) {
                file.createNewFile();
            }
            else{
                file.delete();
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fileWriter);

            Cipher encryptCipher = Cipher.getInstance("AES");
            byte[] raw = parseHexBinary(key);
            SecretKey k = new SecretKeySpec(raw, "AES");
            encryptCipher.init(Cipher.DECRYPT_MODE, k);

            BufferedReader br2 = new BufferedReader(new FileReader(path));
            while((text = br2.readLine()) != null) {

                byte[] decrypted = encryptCipher.doFinal(parseBase64Binary(text));
                String newString = new String(decrypted, "ASCII");

                bw.write(newString + "\n");
            }
            bw.close();

            System.out.println("\n" + "File added to: " + str);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
