package com.mad.passwordgenerator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.Handler;


import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    SeekBar seekBar1,seekBar2,seekBar3,seekBar4;
    TextView seekbarOutput;
    int pwdCountThread, pwdLengthThread, pwdCountAsync, pwdLengthAsync;
    ProgressDialog progressDialog;
    Handler handler;
    String passThread;
    static int countThreadPwd=0, countAsynPwd=0;
    ArrayList<String> threadPasswords, asyncPasswords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        seekBar1 = (SeekBar) findViewById(R.id.threadCountBar);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                pwdCountThread = progress+1;
                Log.d("demo","pwd"+pwdCountThread);
                seekbarOutput = (TextView) findViewById(R.id.progressTextPwdCountThread);
                seekbarOutput.setText(String.valueOf(pwdCountThread));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar2 = (SeekBar) findViewById(R.id.threadLengthBar);
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("demo",""+progress);
                pwdLengthThread = progress+7;
                seekbarOutput = (TextView) findViewById(R.id.pwdLengthThread);
                seekbarOutput.setText(String.valueOf(pwdLengthThread));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar3 = (SeekBar) findViewById(R.id.asyncCountBar);
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("demo",""+progress);
                pwdCountAsync = progress+1;
                seekbarOutput = (TextView) findViewById(R.id.pwdCountAsync);
                seekbarOutput.setText(String.valueOf(pwdCountAsync));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar4 = (SeekBar) findViewById(R.id.asyncLengthBar);
        seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("demo",""+progress);
                pwdLengthAsync = progress+7;
                seekbarOutput = (TextView) findViewById(R.id.pwdLengthAsync);
                seekbarOutput.setText(String.valueOf(pwdLengthAsync));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                passThread = msg.getData().getString("ThreadPasswords");
                Log.d("Thread",passThread);
                threadPasswords.add(passThread);;
                countThreadPwd++;
                progressDialog.setProgress(progressDialog.getProgress()+1);
                Log.d("demo","progress = "+progressDialog.getProgress());
                if(progressDialog.getProgress()== progressDialog.getMax())
                {
                    Log.d("demo","max progress reached inside handler="+progressDialog.getProgress()+" "+progressDialog.getMax());
                    progressDialog.dismiss();
                    Intent intent = new Intent(MainActivity.this, GeneratedPasswords.class);
                    intent.putExtra("threadsPasswords", threadPasswords);
                    intent.putExtra("asyncPasswords",asyncPasswords);
                    startActivity(intent);
                }
                return false;
            }
        });

        findViewById(R.id.buttonGenerate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadPasswords = new ArrayList<String>();
                asyncPasswords = new ArrayList<String>();
                progressDialog=new ProgressDialog(MainActivity.this);
                progressDialog.setMax(pwdCountAsync+pwdCountThread);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.setProgress(0);
                progressDialog.setMessage("Generating Passwords");
                progressDialog.show();

                ExecutorService pool = Executors.newFixedThreadPool(1);
                GeneratePasswordsUsingThread thread = new GeneratePasswordsUsingThread(pwdCountThread,pwdLengthThread);
                pool.execute(thread);

                //String[]  = thread.returnPasswords();

                new GeneratePasswordsUsingAsync().execute(pwdCountAsync,pwdLengthAsync);

            }
        });

    }

    class GeneratePasswordsUsingThread implements Runnable{

        int pwdLength;
        int pwdCount;
        String passwordsThread;

        public GeneratePasswordsUsingThread(int pwdCount, int pwdLength) {
            this.pwdLength = pwdLength;
            this.pwdCount = pwdCount;

        }

        @Override
        public void run() {
            for(int i=0;i<pwdCount;i++) {
                Log.d("demo","inside for");
                passwordsThread = Util.getPassword(pwdLength);

                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("ThreadPasswords", passwordsThread);
                msg.setData(bundle);
                handler.sendMessage(msg);
                Log.d("demo","call after handler");
            }
            Log.d("demo","for loop exit");
        }


    }

    class GeneratePasswordsUsingAsync extends AsyncTask<Integer,Integer,String> {
        String password;
        @Override
        protected String doInBackground(Integer... params) {
            for(int i=0;i<params[0];i++){
                 password =Util.getPassword(params[1]);
                 asyncPasswords.add(password);
                 Log.d("Async",""+password);
                 publishProgress(i+1);
                //return password;
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String passwordsAsync) {
            if(progressDialog.getProgress()==progressDialog.getMax())
            {
                Log.d("demo","max progress reached inside postexecute="+progressDialog.getProgress()+" "+progressDialog.getMax());
                progressDialog.dismiss();
                Intent intent = new Intent(MainActivity.this, GeneratedPasswords.class);
                intent.putExtra("threadsPasswords", threadPasswords);
                intent.putExtra("asyncPasswords",asyncPasswords);
                startActivity(intent);
            }

            //progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(progressDialog.getProgress()+1);
            //progressDialog.setProgress(progressDialog.getProgress()+1);

        }


    }


}



