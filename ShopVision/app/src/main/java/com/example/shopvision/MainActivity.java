package com.example.shopvision;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

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