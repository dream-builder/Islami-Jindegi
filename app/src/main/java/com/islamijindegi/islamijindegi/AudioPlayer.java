package com.islamijindegi.islamijindegi;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AudioPlayer extends AppCompatActivity {

    ImageButton playImageButton,stopImageButton,backImageButton;
    TextView authorTextView,titleTextView,categoryTextView,playInfoTextView;
    SeekBar playSeekbar,volSeekBar;
    ImageView volImageView;

    Boolean isPlaying=false;
    Boolean isPause =false;
    MediaPlayer mPlayer;

    FileHandler fileHandler;

    static float MaxVol=1f,CurrVol=.5f;

    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio_player);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        getSupportActionBar().hide();
        init();
        setValue();
        SeekBarControl();

    }

    private void init() {
       titleTextView = (TextView)findViewById(R.id.titleTextView);
       authorTextView = (TextView)findViewById(R.id.authorTextView);
       categoryTextView = (TextView)findViewById(R.id.catTextView);
       playInfoTextView = (TextView)findViewById(R.id.playInfoTextView);

       playSeekbar=(SeekBar) findViewById(R.id.playSeekBar);
       playSeekbar.setEnabled(false);

       playImageButton=(ImageButton)findViewById(R.id.playImageButton);
       stopImageButton=(ImageButton)findViewById(R.id.stopImageButton);
       backImageButton=(ImageButton)findViewById(R.id.backImageButton);

        fileHandler = new FileHandler(getApplicationContext());

    }

    void SeekBarControl(){

        playSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mPlayer != null && fromUser){
                    mPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                if(mPlayer!=null){

                    //mPlayer.pause();
                }

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mPlayer!=null){

                    //mPlayer.start();
                }
            }
        });




    }


    private void setValue(){

        Bundle bundle = getIntent().getExtras();

        titleTextView.setText(bundle.getString("title"));
        authorTextView.setText(bundle.getString("author"));
        categoryTextView.setText("");


        final String path = bundle.getString("url");


        playImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_file(path);
            }
        });
        stopImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying();
            }
        });
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    public void play_file(String path){

        File Root = Environment.getExternalStorageDirectory();
        File dir = new File(Root.getAbsolutePath() + "/IJFiles");
        String p = Root.getAbsolutePath() + path;

        File file = new File(p);

       //if(file.exists()) {


           if (isPlaying == false) {


               mPlayer = new MediaPlayer();

               try {
                   mPlayer.setDataSource(p);
                   mPlayer.prepare();
               } catch (IOException e) {
                   e.printStackTrace();
               }

               mPlayer.start();
               mPlayer.setVolume(1, 1);
               isPlaying = true;

               finalTime = mPlayer.getDuration();
               startTime = mPlayer.getCurrentPosition();
               playSeekbar.setMax((int) finalTime);


               playSeekbar.setProgress((int) startTime);
               playSeekbar.setEnabled(true);
               myHandler.postDelayed(UpdatePlayTime, 100);

               playImageButton.setImageResource(R.drawable.pause);


           } else {

               if (isPause == false) {
                   pausePlaying();
                   isPause = true;
                   playImageButton.setImageResource(R.drawable.play_button);
               } else {
                   mPlayer.start();
                   isPause = false;
                   playImageButton.setImageResource(R.drawable.pause);
               }

           }
       //}
        //else{

           //Toast.makeText(getApplicationContext(),"Sorry! File is not found in your device.",Toast.LENGTH_LONG).show();
       //}
    }

    private void stopPlaying() {
        if (mPlayer != null) {

            myHandler.removeCallbacks(UpdatePlayTime);
            playSeekbar.setProgress(0);
            playSeekbar.setEnabled(false);
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            isPlaying=false;
            playInfoTextView.setText("0/0");

            playImageButton.setImageResource(R.drawable.play_button);
        }
    }


    private void pausePlaying(){

        if (mPlayer != null) {
            mPlayer.pause();
            isPause=Boolean.TRUE;
            playImageButton.setImageResource(R.drawable.play_button);
        }
    }

    @Override
    protected void onDestroy() {

        if(mPlayer!=null && mPlayer.isPlaying()) {
            myHandler.removeCallbacks(UpdatePlayTime);
            mPlayer.pause();
            mPlayer.release();
            mPlayer = null;
            isPlaying=false;
        }
        super.onDestroy();
    }

    private Runnable UpdatePlayTime = new Runnable() {
        public void run() {
            startTime = mPlayer.getCurrentPosition();
            playInfoTextView.setText(String.format("%d Min, %d Sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))+"/"+TimeUnit.MILLISECONDS.toMinutes((long)finalTime) +"Min"
            );
            playSeekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };
}
