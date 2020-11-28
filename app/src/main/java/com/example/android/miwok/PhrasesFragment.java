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
public class PhrasesFragment extends Fragment {

    private MediaPlayer mPlayer;
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener afChangeListener;
    private final ArrayList<Word> words;
    {
        words = new ArrayList<>();
        words.add(new Word("Where are you going?", "minto wuksus", Word.EMPTY, R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?", "tinnә oyaase'nә", Word.EMPTY, R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...", "oyaaset...", Word.EMPTY, R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?", "michәksәs?", Word.EMPTY, R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.", "kuchi achit", Word.EMPTY, R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?", "әәnәs'aa?", Word.EMPTY, R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming.", "hәә’ әәnәm", Word.EMPTY, R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.", "әәnәm", Word.EMPTY, R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.", "yoowutis", Word.EMPTY, R.raw.phrase_lets_go));
        words.add(new Word("Come here.", "әnni'nem", Word.EMPTY, R.raw.phrase_come_here));
        Log.v("NumberActivity", "Static init words in ArrayList");
    }

    public PhrasesFragment() {
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
        releaseMediaPlayer();
    }

    public void initAdapter(View rootVew) {
        ListView listView = (ListView) rootVew.findViewById(R.id.list);
        WordsAdapter adapter = new WordsAdapter(getActivity(), R.layout.word_list, words, R.color.category_phrases);
        listView.setAdapter(adapter);
    }

    private void initItemListenerForPlayer(View rootVew) {
        ListView listView = (ListView) rootVew.findViewById(R.id.list);
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