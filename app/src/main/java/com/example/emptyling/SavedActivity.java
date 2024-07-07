package com.example.emptyling;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class SavedActivity extends AppCompatActivity {

    private LinearLayout savedLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved);

        savedLayout = findViewById(R.id.savedLayout);

        // Set up the back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finish the activity and go back
            }
        });

        // Retrieve the intent data
        ArrayList<TranslationItem> savedTranslationItems = getIntent().getParcelableArrayListExtra("savedTranslationItems");

        if (savedTranslationItems != null) {
            for (TranslationItem item : savedTranslationItems) {
                // Add the new CardView to the layout
                addCardToLayout(item);
            }
        }
    }

    private void addCardToLayout(TranslationItem item) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View cardView = inflater.inflate(R.layout.card_item, savedLayout, false);

        TextView locationText = cardView.findViewById(R.id.locationText);
        TextView translationText = cardView.findViewById(R.id.translationText);
        ImageButton starButton = cardView.findViewById(R.id.starButton);
        TextView detailedTranslationText = cardView.findViewById(R.id.detailedTranslationText);

        // Set the text for location, translation, and detailed translation
        locationText.setText(item.getLeftLanguage() + " - " + item.getRightLanguage());
        translationText.setText(item.getTranslation());
        detailedTranslationText.setText(item.getTranslation());

        // Initially truncate text and hide the detailed translation text
        translationText.setMaxLines(2); // Adjust based on your requirement
        detailedTranslationText.setVisibility(View.GONE);

        // Handle the star button click
        starButton.setOnClickListener(new View.OnClickListener() {
            private boolean isFilled = true;

            @Override
            public void onClick(View v) {
                if (isFilled) {
                    starButton.setImageResource(R.drawable.star_icon);

                    // Delay the removal by 1 second (1000 milliseconds)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            savedLayout.removeView(cardView);
                        }
                    }, 1000);
                } else {
                    starButton.setImageResource(R.drawable.star_filled);
                }
                isFilled = !isFilled;
            }
        });

        // Handle the card click to expand or collapse the detailed view
        cardView.setOnClickListener(new View.OnClickListener() {
            private boolean isExpanded = false;

            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    translationText.setMaxLines(2);
                    detailedTranslationText.setVisibility(View.GONE);
                } else {
                    translationText.setMaxLines(Integer.MAX_VALUE);
                    detailedTranslationText.setVisibility(View.VISIBLE);
                }
                isExpanded = !isExpanded;
            }
        });

        savedLayout.addView(cardView);
    }
}
