package org.eclipse.paho;


import AES.AesCipher;
import MQTT.MQTTClient;

import java.util.Scanner;

public class App {


    public static void main( String[] args ){
        System.out.println("****************************");
        System.out.println("Testbed Java Decrypter");
        System.out.println("****************************");
        new MQTTClient("tcp://"+enterIP()+":1883");
        testAES();
    }

    private static String enterIP(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the IP of the Broker:");
        return scanner.nextLine();
    }

    private static void testAES() {

        System.out.println("*******************************");
        System.out.println("TEST AES");
        System.out.println("*******************************");

        // source text
        final String srcText = "I'm a GRITS Researcher.";

        String key = "0123456789012345";
        String iv = "0000000000000000";

        // create crypter
        final AesCipher crypter = new AesCipher();

        // set keys
        crypter.setCryptKey(key, iv);

        // do encrypt
        String encryptedText = crypter.encrypt(srcText);

        // show result
        System.out.println("Original = " + srcText + " -> Encrypted = " + encryptedText);

        System.out.println("Encrypted = " + encryptedText + " -> Decrpyted = " + crypter.decrypt(encryptedText));

        System.out.println("*******************************");
    }
}
