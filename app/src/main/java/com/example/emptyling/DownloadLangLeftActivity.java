// DownloadLangLeftActivity.java
package com.example.emptyling;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.ArrayList;
import java.util.List;

public class DownloadLangLeftActivity extends AppCompatActivity {

    private static final String TAG = "DownloadLangLeftActivity";
    private List<View> languageItems;
    private List<ImageView> checkIcons;
    private List<View> layouts;
    private TextView selectedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_lang_left);

        languageItems = new ArrayList<>();
        checkIcons = new ArrayList<>();
        layouts = new ArrayList<>();

        addLanguageItem(findViewById(R.id.recent_language_item), findViewById(R.id.recent_language_text), findViewById(R.id.check_icon_recent), findViewById(R.id.download_icon));
        addLanguageItem(findViewById(R.id.all_language_item1), findViewById(R.id.all_language_text1), findViewById(R.id.check_icon1), findViewById(R.id.download_icon1));
        addLanguageItem(findViewById(R.id.all_language_item2), findViewById(R.id.all_language_text2), findViewById(R.id.check_icon2), findViewById(R.id.download_icon2));
        addLanguageItem(findViewById(R.id.all_language_item3), findViewById(R.id.all_language_text3), findViewById(R.id.check_icon3), findViewById(R.id.download_icon3));
        addLanguageItem(findViewById(R.id.all_language_item4), findViewById(R.id.all_language_text4), findViewById(R.id.check_icon4), findViewById(R.id.download_icon4));
        addLanguageItem(findViewById(R.id.all_language_item5), findViewById(R.id.all_language_text5), findViewById(R.id.check_icon5), findViewById(R.id.download_icon5));
        addLanguageItem(findViewById(R.id.all_language_item6), findViewById(R.id.all_language_text6), findViewById(R.id.check_icon6), findViewById(R.id.download_icon6));
        addLanguageItem(findViewById(R.id.all_language_item7), findViewById(R.id.all_language_text7), findViewById(R.id.check_icon7), findViewById(R.id.download_icon7));
    }

    private void addLanguageItem(View layout, TextView textView, ImageView checkIcon, ImageButton downloadButton) {
        languageItems.add(textView);
        checkIcons.add(checkIcon);
        layouts.add(layout);
        setupLanguageItem(layout, textView, checkIcon, downloadButton);
    }

    private void setupLanguageItem(View layout, TextView textView, ImageView checkIcon, ImageButton downloadButton) {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllItems();
                checkIcon.setVisibility(View.VISIBLE);
                layout.setBackgroundColor(getResources().getColor(R.color.selected_blue));
                Log.d(TAG, "Item clicked: " + textView.getText());
                selectedTextView = textView;

                // Automatically go back to MainActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedLanguage", textView.getText().toString());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the image resource to ic_check
                downloadButton.setImageResource(R.drawable.ic_check);
                Log.d(TAG, "Download button clicked for: " + textView.getText());
            }
        });
    }

    private void resetAllItems() {
        for (int i = 0; i < languageItems.size(); i++) {
            checkIcons.get(i).setVisibility(View.GONE);
            layouts.get(i).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }
}
