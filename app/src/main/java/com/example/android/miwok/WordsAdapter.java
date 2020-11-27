package com.example.android.miwok;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class WordsAdapter extends ArrayAdapter<Word> {
    private int mCategoryColor;
    private MediaPlayer mMediaPlayer;

    public WordsAdapter(Context context, int resource, List<Word> list, int categoryNumbers) {
        super(context, resource, list);
        mCategoryColor = categoryNumbers;
    }

    public WordsAdapter(Context context, int resource, List<Word> list, Color color) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Word word = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        LinearLayout rootView = (LinearLayout) convertView.findViewById(R.id.text_holder);
        rootView.setBackgroundResource(mCategoryColor);

        TextView textViewMiwok = (TextView) convertView.findViewById(R.id.text_miwok);
        textViewMiwok.setText(word.getMiwokTranslation());

        TextView textViewDefault = (TextView) convertView.findViewById(R.id.text_default);
        textViewDefault.setText(word.getDefaultTranslation());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_placeholder);
        if (word.isHasImage()) {
            imageView.setImageResource(word.getImage());
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }

        return convertView;
    }
}
