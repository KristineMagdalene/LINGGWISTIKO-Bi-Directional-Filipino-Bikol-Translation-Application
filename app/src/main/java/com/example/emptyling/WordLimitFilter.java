package com.example.emptyling;

import android.text.InputFilter;
import android.text.Spanned;

public class WordLimitFilter implements InputFilter {
    private int maxWords;

    public WordLimitFilter(int maxWords) {
        this.maxWords = maxWords;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String[] words = dest.toString().split("\\s+");
        int wordCount = words.length;

        if (wordCount >= maxWords) {
            return "";
        }

        String[] newWords = source.toString().split("\\s+");
        if (wordCount + newWords.length > maxWords) {
            int keep = maxWords - wordCount;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < keep; i++) {
                builder.append(newWords[i]);
                if (i != keep - 1) {
                    builder.append(" ");
                }
            }
            return builder.toString();
        }

        return null;
    }
}
