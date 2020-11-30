package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ColorsFragment extends Fragment {
    private MediaPlayer mPlayer;
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener afChangeListener;

    private final ArrayList<Word> words;
    {
        words = new ArrayList<>();
        words.add(new Word("red", "weṭeṭṭi", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("green", "chokokki", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("brown", "ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("gray", "ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("black", "kululli", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("white", "kelelli", R.drawable.color_white, R.raw.color_white));
        words.add(new Word("dusty yellow", "ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Word("mustard yellow", "chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));
        Log.v("NumberActivity", "Static init words in ArrayList");
    }

    public ColorsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        initAdapter(rootView);
        initItemListenerForPlayer(rootView);
        initAudioManager();

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }

    private void initAdapter(View rootView) {
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        WordsAdapter itemsAdapter = new WordsAdapter(getActivity(), R.layout.word_item, words, R.color.category_colors);
        listView.setAdapter(itemsAdapter);
    }

    private void initItemListenerForPlayer(View rootView) {
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                int requestResult = audioManager.requestAudioFocus(afChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (requestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mPlayer = MediaPlayer.create(getActivity(), words.get(position).getAudio());
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            releaseMediaPlayer();
                        }
                    });
                    Log.v("FocusRequest", "focus request result :" + requestResult);
                    mPlayer.start();
                }
            }
        });
    }

    private void releaseMediaPlayer() {
        if (audioManager != null && afChangeListener != null) {
            audioManager.abandonAudioFocus(afChangeListener);
        }
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.release();
        }
        mPlayer = null;
    }

    private void initAudioManager() {
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ||
                        focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                    mPlayer.pause();
                    mPlayer.seekTo(0);
                } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                    releaseMediaPlayer();
                } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                    mPlayer.start();
                }
            }
        };
    }
}