package com.example.league95.audioapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //Step 1)
    //We need to use something called media player
    MediaPlayer mediaPlayer;
    //And Our audio manager
    AudioManager audioManager;
    //Step2)
    //We need to establish current and max volume
    int maxVolume, currentVolume;
    //song duration
    int trackDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //We need to use something called media player
        mediaPlayer = MediaPlayer.create(this,R.raw.song);
        //Our audio manager
        //This is how you create audio managers
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //We use audio manager to get max volume
        //the getStreamMaxVolume takes in a stream from AudioManager
        //Stream 3 (STREAM_MUSIC) is used for this
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //Same thing with current volume
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //Step3)
        //Our seekbar (we're gonna integrate with volume)
        SeekBar skBar = (SeekBar) findViewById(R.id.seekBar);
        //Then we need to set the seek bar max to max volume
        skBar.setMax(maxVolume);
        //And same with our current volume
        skBar.setProgress(currentVolume);
        //Step4) handle all 3 events of Seek Bar
        skBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            /*We need to implement three methods in order to make the seek bar
            * work: onProgressChanged, which tells us the status of the audio, etc
            * onStartTrackingTouch for when user press on seek bar and
            * onStopTracking touch for when user lifts their finger off*/
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                //flag is just extra information, we dont want to.
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            //When user puts their finger on seek bar
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //Also when the user takes off their finger!
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        /*-----------------------------------------------------------------*/
        trackDuration = mediaPlayer.getDuration();
        //Now we make a length seek bar!!
        final SeekBar skBar2 = (SeekBar) findViewById(R.id.seekBar2);
        //Then set duartion with seekbar
        skBar2.setMax(trackDuration);
        //In order to match our seekbar with the location of
        //our audio file, we can use timer!
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //update our seekbar to show the location of the
                //audio file
                //basically, the pointer of the seekbar will go forward
                //every second.
                skBar2.setProgress(mediaPlayer.getCurrentPosition());
            }
            //1000 means we run what ever we do in the run() method
            //every second!
        }, 0, 1000);
        //For our length seekbar
        skBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //update the current position of our track
                //when ever the user drags to a specific position
                if (mediaPlayer != null && fromUser)
                {
                    mediaPlayer.seekTo(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();
            }
        });


    }
    //Play button
    public void play(View v)
    {

        //mediaPlayer.start();
        Button tbn = findViewById(R.id.button);
        if (mediaPlayer.isPlaying())
        {
            tbn.setText("Play");
            mediaPlayer.pause();
        } else
        {
            mediaPlayer.start();
            tbn.setText("Stop");
        }
    }
}
