package com.example.mediaplayer;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.*;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;
    private Button play_button;
    private Button pause_button;
    private Button forward_button;
    private Button backward_button;
    private SeekBar seekBar;
    private TextView time;
    private TextView songName;

    private final Handler handler = new Handler();
    private double startTime;
    private double finalTime;
    private final int forwardTime = 10000;
    private final int backwardTime = 10000;
    private int oneTime = 0;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        play_button = findViewById(R.id.play_button);
        pause_button = findViewById(R.id.pause_button);
        forward_button = findViewById(R.id.forward_button);
        backward_button = findViewById(R.id.backward_button);
        seekBar = findViewById(R.id.seekBar);
        time = findViewById(R.id.time);
        songName = findViewById(R.id.songName);

        mediaPlayer = MediaPlayer.create(this,R.raw.music);
        seekBar.setClickable(false);


        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();
            }
        });

        pause_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });

        forward_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;
                if(temp + forwardTime <= finalTime){
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }else{
                    Toast.makeText(MainActivity.this, "Can not Jump Sorry! ", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        backward_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;
                if(temp - backwardTime > 0){
                    startTime = startTime - forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }else{
                    Toast.makeText(MainActivity.this, "Can not Jump Sorry! ", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }

    @SuppressLint("DefaultLocale")
    private void playMusic(){
        mediaPlayer.start();

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if(oneTime == 0){
            seekBar.setMax((int) finalTime);
            oneTime = 1;
        }
        time.setText(format("%d min : %d sec", MILLISECONDS.toMinutes((long) finalTime),
                MILLISECONDS.toSeconds((long) finalTime) - MILLISECONDS.toMinutes((long) finalTime)));

        seekBar.setProgress((int) startTime);
        handler.postDelayed(updateSongTime, 100);
    }

    private Runnable updateSongTime = new Runnable() {
        @SuppressLint("DefaultLocale")
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            time.setText(String.format("%d min : %d sec", MILLISECONDS.toMinutes((long) startTime),
                    MILLISECONDS.toSeconds((long) startTime) - MILLISECONDS.toMinutes((long) startTime)));

            seekBar.setProgress((int)startTime);
            handler.postDelayed(this, 100);
        }
    };
}