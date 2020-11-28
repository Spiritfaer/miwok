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
public class FamilyFragment extends Fragment {
    private MediaPlayer mPlayer;
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener afChangeListener;

    private final ArrayList<Word> words;
    {
        words = new ArrayList<>();
        words.add(new Word("father", "әpә", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("mother", "әṭa", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("son", "angsi", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("older brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("younger brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("older sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("younger sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word("grandmother", "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word("grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather));
        Log.v("NumberActivity", "Static init words in ArrayList");
    }

    public FamilyFragment() {
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

    public void initAdapter(View rootVew) {
        ListView listView = (ListView) rootVew.findViewById(R.id.list);
        WordsAdapter itemsAdapter = new WordsAdapter(getActivity(), R.layout.list_item, words, R.color.category_family);
        listView.setAdapter(itemsAdapter);
    }

    public void initItemListenerForPlayer(View rootView) {
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