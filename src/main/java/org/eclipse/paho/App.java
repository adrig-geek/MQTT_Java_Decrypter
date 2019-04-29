package org.eclipse.paho;


import AES.AesCipher;
import AES.AesTreatment;
import MQTT.MQTTClient;

public class App {


    public static void main( String[] args ){
        MQTTClient mqtt = new MQTTClient("tcp://172.16.2.153:1883");
        //mqtt.publish("Hola");
        //mqtt.disconnect();
        testAES();
    }

    private static void testAES() {

        System.out.println("*******************************");
        System.out.println("TEST MQTT");
        System.out.println("*******************************");
        // Eexample of TextAESCrypter

        // source text
        final String srcText = "I'm happy.";

        String key = "0123456789012345";
        String iv = "0000000000000000";

        // create crypter
        final AesCipher crypter = new AesCipher();

        // set keys
        crypter.setCryptKey(key, iv);

        // do encrypt
        String encryptedText = crypter.encrypt(srcText);

        // show result
        System.out.println("Original = " + srcText + " -> Encrypted = " + encryptedText + "\n");

        System.out.println("Encrypted = " + encryptedText + " -> Decrpyted = " + crypter.decrypt(encryptedText));

        System.out.println("*******************************");
    }
}
