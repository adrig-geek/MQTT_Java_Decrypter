package MQTT;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTClient implements MqttCallback {

    private static final String topic = "enc/";
    private static final int qos = 2;
    private String broker;
    private String clientId;
    private MemoryPersistence persistence = new MemoryPersistence();
    private int clientIdAcum;
    private MqttClient mqttClient;
    private MQTTDecrypter mqttDecrypter;

    public MQTTClient(String broker){
        this.broker = broker;
        clientId = "AESCipherClient"+clientIdAcum++;
        connect();
        mqttDecrypter = new MQTTDecrypter();


    }

    private void connect(){
        try {
            this.mqttClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            mqttClient.connect(connOpts);
            System.out.println("Connected");
            mqttClient.setCallback(this);
            mqttClient.subscribe(MQTTClient.topic+"#");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    private void publish(String content, String topicIn){
        try {

            System.out.println("PUBLISH message[ " + topicIn + " ]: " +content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            mqttClient.publish(topicIn, message);
            System.out.println("Message published\n");
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }

    public void disconnect(){
        try {
            mqttClient.disconnect();
            System.out.println("Disconnected");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message){
        System.out.println("MSG ARR. [ " + topic + " ]");
        System.out.println(new String(message.getPayload()));
        String decrypted = mqttDecrypter.decryptMQTTMessage(new String(message.getPayload()));
        String[] splitted = topic.split("/");
        publish(decrypted, splitted[1]);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    @Override
    public void connectionLost(Throwable cause) {
    }

}
