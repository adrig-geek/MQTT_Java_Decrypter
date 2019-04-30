package MQTT;

import AES.AesCipher;

public class MQTTDecrypter {

    private static final String key = "0123456789012345";
    private static final String iv = "0000000000000000";
    private AesCipher aesCipher;

    public MQTTDecrypter(){
        configureAES();
    }

    private void configureAES(){
        // create crypter
        aesCipher = new AesCipher();

        // set keys
        aesCipher.setCryptKey(key, iv);
    }

    public String decryptMQTTMessage(String message){
        String result = aesCipher.decrypt(message);
        System.out.println("Decrypted: "+result);
        return result;
    }


}
