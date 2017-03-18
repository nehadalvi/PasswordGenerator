package com.mad.passwordgenerator;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class GeneratedPasswords extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_passwords);

        ArrayList<String> threadPasswords;
        ArrayList<String> asyncPasswords;

        if (getIntent().getExtras() == null)
        {
            Toast.makeText(this,"Error Generating Passwords.",Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            threadPasswords = (ArrayList<String>) getIntent().getExtras().getSerializable("threadsPasswords");
            asyncPasswords = (ArrayList<String>) getIntent().getExtras().getSerializable("asyncPasswords");

            LinearLayout threadPasswordsView = (LinearLayout) findViewById(R.id.threadsList);
            LinearLayout asyncPasswordsView = (LinearLayout) findViewById(R.id.asyncList);
            //ScrollView asyncPasswordsView = (ScrollView)findViewById(R.id.asyncPasswords);

            threadPasswordsView.removeAllViews();
            asyncPasswordsView.removeAllViews();
            
            for (int i = 0; i < threadPasswords.size(); i++)
            {
                TextView text = new TextView(this);
                text.setText(threadPasswords.get(i));
                threadPasswordsView.addView(text);
            }

            for (int i = 0; i < asyncPasswords.size(); i++)
            {
                TextView text = new TextView(this);
                text.setText(asyncPasswords.get(i));
                asyncPasswordsView.addView(text);
            }

        }

        findViewById(R.id.finishButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }



}
