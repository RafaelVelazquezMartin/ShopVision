package com.example.shopvision;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by hopedoolan on 04/03/2018.
 */

public class AskForHelp extends AppCompatActivity{

    TextToSpeech toSpeech;
    int result;
    EditText editText;
    String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_activity);

        editText = findViewById(R.id.editText);
        toSpeech = new TextToSpeech(AskForHelp.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status==TextToSpeech.SUCCESS){
                    result = toSpeech.setLanguage(Locale.ENGLISH);
                }else{
                    Toast.makeText(getApplicationContext(),"Not supported",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void TTS(View view){
        switch (view.getId()){
            case R.id.speakbutton:
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                    Toast.makeText(getApplicationContext(),"Not supported",Toast.LENGTH_SHORT).show();
                }else{
                    text = "Can you help me, I am looking for "+ editText.getText().toString();
                    toSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toSpeech!= null){
            toSpeech.stop();
            toSpeech.shutdown();
        }
    }
}

