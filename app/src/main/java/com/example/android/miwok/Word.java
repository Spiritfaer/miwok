package com.example.android.miwok;

public class Word {
    private final String mMiwokTranslation;
    private final String mDefaultTranslation;
    private final int mImage;
    private final int mAudio;
    private boolean mHasImage;
    private boolean mHasAudio;
    static final int EMPTY = -1;

    /**
     * Create a new Word object without image and audio, these variables shot be Word.EMPTY
     * @param defultTranslation in the word in a language that the user is already familiar with (such as English)
     * @param miwokTranslation in the word in a Miwok language
     */

    public Word(String defultTranslation, String miwokTranslation) {
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defultTranslation;
        mImage = Word.EMPTY;
        mHasImage = false;
        mAudio = Word.EMPTY;
        mHasAudio = false;
    }

    /**
     * Create a new Word object with image, but without audio
     * @param defultTranslation in the word in a language that the user is already familiar with (such as English)
     * @param miwokTranslation in the word in a Miwok language
     * @param image is the resource ID for the image file associated with this word
     */

    public Word(String defultTranslation, String miwokTranslation, int image) {
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defultTranslation;
        mImage = image;
        mHasImage = true;
        mAudio = 0;
        mHasAudio = false;
    }

    /**
     * Create a new Word object with image, but without audio
     * @param defultTranslation in the word in a language that the user is already familiar with (such as English)
     * @param miwokTranslation in the word in a Miwok language
     * @param image is the resource ID for the image file associated with this word, it can be Word.EMPTY
     * @param audio is the resource ID for the audio file associated with this word
     */

    public Word(String defultTranslation, String miwokTranslation, int image, int audio) {
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defultTranslation;
        if (image != Word.EMPTY) {
            mImage = image;
            mHasImage = true;
        } else {
            mImage = Word.EMPTY;
            mHasImage = false;
        }
        mAudio = audio;
        mHasAudio = true;
    }

    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    public int getImage() {
        return mImage;
    }

    public int getAudio() {
        return mAudio;
    }

    public boolean isHasImage() {
        return mHasImage;
    }

    public boolean isHasAudio() {
        return mHasAudio;
    }

    @Override
    public String toString() {
        return "Word{" +
                "mMiwokTranslation='" + mMiwokTranslation + '\'' +
                ", mDefaultTranslation='" + mDefaultTranslation + '\'' +
                ", mHasImage=" + mHasImage +
                ", mImage=" + mImage +
                ", mHasAudio=" + mHasAudio +
                ", mAudio=" + mAudio +
                '}';
    }
}
