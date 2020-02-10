package com.polyproject.mqttclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private MqttAndroidClient client;
    private String TAG = "MainActivity";
    private MqttClient mqttClient;

    private EditText textMessage, subscribeTopic, unSubscribeTopic;
    private Button publishMessage, subscribe, unSubscribe;
    public static TextView printMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mqttClient = new MqttClient();

        textMessage = (EditText) findViewById(R.id.textMessage);
        publishMessage = (Button) findViewById(R.id.publishMessage);

        subscribe = (Button) findViewById(R.id.subscribe);
        unSubscribe = (Button) findViewById(R.id.unSubscribe);

        subscribeTopic = (EditText) findViewById(R.id.subscribeTopic);
        unSubscribeTopic = (EditText) findViewById(R.id.unSubscribeTopic);
        client = mqttClient.getMqttClient(getApplicationContext(), Constants.MQTT_BROKER_URL, Constants.CLIENT_ID);

        printMessage = findViewById(R.id.printMessage);

        publishMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = textMessage.getText().toString().trim();
                if (!msg.isEmpty()) {
                    try {
                        mqttClient.publishMessage(client, msg, 1, Constants.PUBLISH_TOPIC);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = subscribeTopic.getText().toString().trim();
                if (!topic.isEmpty()) {
                    try {
                        mqttClient.subscribe(client, topic, 1);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        unSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = unSubscribeTopic.getText().toString().trim();
                if (!topic.isEmpty()) {
                    try {
                        mqttClient.unSubscribe(client, topic);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Intent intent = new Intent(MainActivity.this, MqttMessageService.class);
        startService(intent);
    }
}
