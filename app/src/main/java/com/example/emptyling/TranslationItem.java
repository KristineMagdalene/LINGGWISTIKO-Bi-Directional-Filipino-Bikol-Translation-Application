package com.example.emptyling;

import android.os.Parcel;
import android.os.Parcelable;

public class TranslationItem implements Parcelable {
    private String translation;
    private String leftLanguage;
    private String rightLanguage;

    public TranslationItem(String translation, String leftLanguage, String rightLanguage) {
        this.translation = translation;
        this.leftLanguage = leftLanguage;
        this.rightLanguage = rightLanguage;
    }

    protected TranslationItem(Parcel in) {
        translation = in.readString();
        leftLanguage = in.readString();
        rightLanguage = in.readString();
    }

    public static final Creator<TranslationItem> CREATOR = new Creator<TranslationItem>() {
        @Override
        public TranslationItem createFromParcel(Parcel in) {
            return new TranslationItem(in);
        }

        @Override
        public TranslationItem[] newArray(int size) {
            return new TranslationItem[size];
        }
    };

    public String getTranslation() {
        return translation;
    }

    public String getLeftLanguage() {
        return leftLanguage;
    }

    public String getRightLanguage() {
        return rightLanguage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(translation);
        dest.writeString(leftLanguage);
        dest.writeString(rightLanguage);
    }
}
