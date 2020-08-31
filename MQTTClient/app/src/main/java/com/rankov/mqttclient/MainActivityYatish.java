package com.rankov.mqttclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


import java.lang.reflect.Type;
import java.util.UUID;

public class MainActivityYatish extends AppCompatActivity implements MqttCallback {

    // Specify the connection parameters.
    private static final String TAG = "ActiveMQ";
    // public static final String serverURI = "tcp://localhost:1883";
    // public static final String serverURI = "tcp://3.6.147.80:1883";
    public static final String serverURI = "ssl://b-c7f1ea8f-f888-4c71-82a3-2d62988fdb64-1.mq.us-west-2.amazonaws.com:8883";
    public static final String publishTopic = "queue-store-one";
    public static final String subscribeTopic = "topic-store-one";

    String clientId = "abc-546-789";

   @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            connect();
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mqtt
    private void connect() throws MqttException, InterruptedException {
        String password = "admin-topic@2345";
        char[] pass = password.toCharArray();
        System.out.println(pass);
        final String text = "Hello from Amazon MQ!";

        // Create the MQTT client and specify the connection options.
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setAutomaticReconnect(true);
        connectOptions.setUserName("admin-topic");
        connectOptions.setPassword(pass);

        final MqttClient client = new MqttClient(serverURI, clientId, new MemoryPersistence());
        final MqttConnectOptions connectOpts = new MqttConnectOptions();

        connectOpts.setUserName("admin-topic");
        connectOpts.setPassword(pass);

        // Create a session and subscribe to a topic filter.
        client.connect(connectOpts);
        client.setCallback(this);
        client.subscribe("+");

        // Create a message.
        final MqttMessage message = new MqttMessage(text.getBytes());

        // Publish the message to a topic.
        client.publish(publishTopic, message);
        System.out.println("Published message.");

        // Wait for the message to be received.
        Thread.sleep(3000L);

        // Clean up the connection.
        client.disconnect();
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Lost connection.");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Received message from topic " + topic + ": " + message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("Delivered message.");
    }
}