package com.mwinzi.mrbot;

import android.content.Intent;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
TextToSpeech text;
TextView  message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         message = findViewById(R.id.from);

        String welcome = "Hey there,I am AlgoBot from Algoverse. Your talking buddy  ";

        text  = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                text.setLanguage(Locale.ENGLISH);
                text.speak(welcome,TextToSpeech.QUEUE_ADD,null);
                text.setSpeechRate(0.8f);
                text.setPitch(0.7f);

            }
        });

        message.setTranslationY(800);
        message.setAlpha(0);

        message.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(200).start();

        Handler handler  = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent  = new Intent(MainActivity.this,Chat.class);
                startActivity(intent);
            }
        },9000);


    }

}