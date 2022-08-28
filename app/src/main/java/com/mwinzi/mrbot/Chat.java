package com.mwinzi.mrbot;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Chat extends AppCompatActivity {


    Toolbar  toolbar;
    ImageView soundrecord,send;
    EditText editText;
    DatabaseReference dbref;
    int requestcode = 100;
    TextToSpeech text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar  = findViewById(R.id.toolbar);
        toolbar.setTitle("AlgoBot");
        toolbar.setTitleTextColor(Color.WHITE);
        soundrecord  = findViewById(R.id.speak);
         send  = findViewById(R.id.send);
         editText = findViewById(R.id.message);

         dbref  = FirebaseDatabase.getInstance().getReference("BotInfo");

         soundrecord.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent  = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                 intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                 intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                 intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something");
                 startActivityForResult(intent,requestcode);
             }
         });


         send.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String message  = editText.getText().toString();

                 dbref = FirebaseDatabase.getInstance().getReference("BotInfo");

                 dbref.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {

                         String response  = snapshot.child(message).getValue().toString();

                           text = new TextToSpeech(Chat.this, new TextToSpeech.OnInitListener() {
                               @Override
                               public void onInit(int status) {
                                 text.setLanguage(Locale.ENGLISH);
                                 text.setSpeechRate(0.8f);
                                 text.setPitch(0.7f);
                                 text.speak(response,TextToSpeech.QUEUE_ADD,null);
                                  // Toast.makeText(Chat.this,response,Toast.LENGTH_LONG).show();
                               }
                           });

                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });


             }
         });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         if(requestCode  == requestcode && resultCode == RESULT_OK && data != null){
             ArrayList result  = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
             editText.setText(result.toString());
         }

    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("BotInfo");

        HashMap map  = new HashMap();

        map.put("Hello","Hello there, how are you today?");
        map.put("Hey","Hey there how are you feeling today");
        map.put("How are you religiously affiliated", "I am not reliigiously affiliated. You humans are divided by things that should not divide you ");
        map.put("Tell me a joke", " What kind of concert only costs 45 cents? A 50 Cent concert featuring Nickelback");
        map.put("what's your name","I am AlgoBot ");
        map.put("can you do a simple arithmetic","yes one plus one is two");
        map.put("who created you","Mwinzi");
        map.put("are you sentient","No currently i am on supervised learning");

        dbref.updateChildren(map);

    }
}