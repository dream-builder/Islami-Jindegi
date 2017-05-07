package com.islamijindegi.islamijindegi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SyncActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    Button syncButton,clearButton,backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);


        init();
        buttonAction();
    }

    private void init() {

        syncButton = (Button)findViewById(R.id.syncButton);
        clearButton = (Button)findViewById(R.id.clearButton);
        backButton = (Button)findViewById(R.id.backButton);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.openDB();
        //databaseHelper.createCategoryTable();

    }

    private void buttonAction(){

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



}
