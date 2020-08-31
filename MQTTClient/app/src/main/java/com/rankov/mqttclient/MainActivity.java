package com.rankov.mqttclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ActiveMQ";
    public static final String clientId = "any_client_name";
    // public static final String serverURI = "tcp://localhost:1883";
    // public static final String serverURI = "tcp://3.6.147.80:1883";
    public static final String serverURI = "ssl://b-c7f1ea8f-f888-4c71-82a3-2d62988fdb64-1.mq.us-west-2.amazonaws.com:8883";
    public static final String publishTopic = "topic-store-one";
    public static final String subscribeTopic = "topic-store-one";


    MqttAndroidClient client;
    // MqttClient client;

    private Button publishBtn;
    private EditText messageEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        connect();
    }

    private void initViews() {
        publishBtn = (Button) findViewById(R.id.publish);
        messageEt = (EditText) findViewById(R.id.ntrMessage);
        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishMessage(messageEt.getText().toString());
            }
        });
        publishBtn.setEnabled(false);
    }

    private void connect() {
        String password = "admin-topic@2345";
        char[] pass = password.toCharArray();
        System.out.println(pass);
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setAutomaticReconnect(true);
        connectOptions.setUserName("admin-topic");
        connectOptions.setPassword(pass);

         client = new MqttAndroidClient(this, serverURI, MqttClient.generateClientId());
        try {
            client.connect(connectOptions, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    publishBtn.setEnabled(true);
                    subscribe();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable e) {
                    e.printStackTrace();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void subscribe() {
        try {
            client.subscribe(subscribeTopic, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(final String topic, final MqttMessage message) throws Exception {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Received: " + message.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void publishMessage(String message) {
        MqttMessage msg = new MqttMessage();
        msg.setPayload(message.getBytes());
        try {
            client.publish(publishTopic, msg);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
