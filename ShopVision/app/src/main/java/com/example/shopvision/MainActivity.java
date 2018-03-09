package com.example.shopvision;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String CLOUD_VISION_API_KEY = " AIzaSyAQ9EZW41C772lhkZMpbqf4wFe-KWiRmAQ ";
    private final String GOOGLE_TRANSLATE_URL = "https://www.googleapis.com/language/translate/v2";

    private Button mNextButton;
    private Button mListButton;
    private Button mCloudVisionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mNextButton = findViewById(R.id.basic_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAskActivity();
            }
        });

        mListButton = findViewById(R.id.button_list);
        mListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openListActivity();
            }
        });

        mCloudVisionButton = findViewById(R.id.google_translate_button);
        mCloudVisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openGoogleTranslateActivity();
            }
        });

        new AsyncTask<Object, Void, String>(){
            @Override
            protected String doInBackground(Object... params) {
                AndroidNetworking.get(GOOGLE_TRANSLATE_URL + "/languages?key={api_key}&target={target}")
                        .addPathParameter("api_key", CLOUD_VISION_API_KEY)
                        .addPathParameter("target", "en")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject data = response.getJSONObject("data");
                                    JSONArray languages = data.getJSONArray("languages");
                                    for (int i = 0; i < languages.length(); i++) {
                                        JSONObject finalObject = languages.getJSONObject(i);
                                        String availableLanguage = finalObject.getString("language");
                                        String verboseLanguage = finalObject.getString("name");
                                        String completeLanguage = verboseLanguage + " - " + availableLanguage;
                                        Log.d("LANG", completeLanguage);
                                        GoogleTranslate.langOptionsList.add(completeLanguage);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {}
                        });
                return "Everything OK.";
            }
        }.execute();

    }

    public void openGoogleTranslateActivity(){
        Intent intent = new Intent(this, GoogleTranslate.class);
        startActivity(intent);
    }

    public void openAskActivity(){
        Intent intent = new Intent(this, AskForHelp.class);
        startActivity(intent);
    }

    public void openListActivity(){
        Intent intent = new Intent(this, ShoppingList.class);
        startActivity(intent);
    }



}