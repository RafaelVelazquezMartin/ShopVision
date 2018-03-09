package com.example.shopvision;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by hopedoolan on 04/03/2018.
 */

public class AskForHelp extends AppCompatActivity{

    private static final String CLOUD_VISION_API_KEY = " AIzaSyAQ9EZW41C772lhkZMpbqf4wFe-KWiRmAQ ";
    private final String GOOGLE_TRANSLATE_URL = "https://www.googleapis.com/language/translate/v2";

    TextToSpeech toSpeech;
    int result;
    EditText editText;
    String text;

    private Spinner sLangSelect;
    private ArrayAdapter<String> aaLangSelect;

    String[] langOptions;
    private String languageSelected;
    private String detectedLanguage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_activity);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AndroidNetworking.initialize(getApplicationContext());

        sLangSelect = (Spinner) findViewById(R.id.askLangSpinner);

        langOptions = new String[GoogleTranslate.langOptionsList.size()];
        langOptions = GoogleTranslate.langOptionsList.toArray(langOptions);
        aaLangSelect = new ArrayAdapter(this, android.R.layout.simple_list_item_1, langOptions);
        aaLangSelect.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sLangSelect.setAdapter(aaLangSelect);
        sLangSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (adapterView.getId()) {
                    case R.id.askLangSpinner:
                        languageSelected = sLangSelect.getSelectedItem().toString().split(" - ")[1];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void TTS(View view){
        switch (view.getId()){
            case R.id.speakbutton:
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                    Toast.makeText(getApplicationContext(),"Not supported",Toast.LENGTH_SHORT).show();
                }else{
                    text = "Can you help me, I am looking for "+ editText.getText().toString();
                    Log.d("TEXT", text);
                    AndroidNetworking.get(GOOGLE_TRANSLATE_URL + "?key={api_key}&source=en&target={to}&q={text}")
                            .addPathParameter("api_key", CLOUD_VISION_API_KEY)
                            .addPathParameter("to", languageSelected)
                            .addPathParameter("text", text)
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONObject data = response.getJSONObject("data");
                                        JSONArray translations = data.getJSONArray("translations");
                                        JSONObject finalObject = translations.getJSONObject(0);
                                        String translatedText = finalObject.getString("translatedText");

                                        text = translatedText;
                                        toSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Log.e("TRANSLATE_BODY", anError.getErrorBody());
                                    Log.e("TRANSLATE_DETAIL", anError.getErrorDetail());
                                    Toast.makeText(getApplicationContext(),"Translation failed!",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
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

