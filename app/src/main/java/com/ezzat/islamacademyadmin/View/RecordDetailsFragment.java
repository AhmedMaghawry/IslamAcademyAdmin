package com.ezzat.islamacademyadmin.View;


import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ezzat.islamacademyadmin.Model.Post;
import com.ezzat.islamacademyadmin.R;

public class RecordDetailsFragment extends Fragment {

    TextView title;
    TextView desc;
    SeekBar seekBar;
    ImageButton imageButton;
    static final String AUDIO_PATH =
            "https://www.esm3.com//music/216/3amro..Zay_Elmalyka.mp3";
    private MediaPlayer mediaPlayer;
    private int playbackPosition=0;
    private int mediaFileLengthInMilliseconds;
    private final Handler handler = new Handler();
    Post post;

    public RecordDetailsFragment() {
        // Required empty public constructor
    }

    public static RecordDetailsFragment newInstance() {
        RecordDetailsFragment fragment = new RecordDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_details, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.des);
        post = ((DetailActivity)getActivity()).getPost();
        title.setText(post.getTitle());
        desc.setText(post.getDesc());
        imageButton = (ImageButton)view.findViewById(R.id.play);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.play){
                    /** ImageButton onClick event handler. Method which start/pause mediaplayer playing */
                    try {
                        mediaPlayer.setDataSource(post.getContent()); // setup song from https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
                        mediaPlayer.prepare(); // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL

                    if(!mediaPlayer.isPlaying()){
                        mediaPlayer.start();
                        imageButton.setImageResource(R.drawable.ic_pause);
                    }else {
                        mediaPlayer.pause();
                        imageButton.setImageResource(R.drawable.ic_play);
                    }

                    primarySeekBarProgressUpdater();
                }
            }
        });

        seekBar = (SeekBar)view.findViewById(R.id.seek);
        seekBar.setMax(99); // It means 100% .0-99
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v.getId() == R.id.seek){
                    /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
                    if(mediaPlayer.isPlaying()){
                        SeekBar sb = (SeekBar)v;
                        int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                        mediaPlayer.seekTo(playPositionInMillisecconds);
                    }
                }
                return false;
            }
        });

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                seekBar.setSecondaryProgress(percent);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageButton.setImageResource(R.drawable.ic_play);
            }
        });
    }

    private void primarySeekBarProgressUpdater() {
        seekBar.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaFileLengthInMilliseconds)*100)); // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification,1000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
        handler.removeCallbacksAndMessages(null);
    }

    /*private void playAudio(String url) throws Exception
    {
        killMediaPlayer();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    private void playLocalAudio() throws Exception
    {
        mediaPlayer = MediaPlayer.create(this, R.raw.music_file);
        mediaPlayer.start();
    }

    private void playLocalAudio_UsingDescriptor() throws Exception {

        AssetFileDescriptor fileDesc = getResources().openRawResourceFd(
                R.raw.music_file);
        if (fileDesc != null) {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fileDesc.getFileDescriptor(), fileDesc
                    .getStartOffset(), fileDesc.getLength());

            fileDesc.close();

            mediaPlayer.prepare();
            mediaPlayer.start();
        }
    }*/

    private void killMediaPlayer() {
        if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
