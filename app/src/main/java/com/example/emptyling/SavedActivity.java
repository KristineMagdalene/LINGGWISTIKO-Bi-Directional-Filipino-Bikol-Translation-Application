// SavedActivity.java
package com.example.emptyling;

import android.os.Bundle;
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
        ArrayList<String> savedItems = getIntent().getStringArrayListExtra("savedItems");

        if (savedItems != null) {
            for (String item : savedItems) {
                // Add the new CardView to the layout
                addCardToLayout(item);
            }
        }
    }

    private void addCardToLayout(String translation) {

        LayoutInflater inflater = LayoutInflater.from(this);

        View cardView = inflater.inflate(R.layout.card_item, savedLayout, false);

        TextView locationText = cardView.findViewById(R.id.locationText);
        TextView translationText = cardView.findViewById(R.id.translationText);

        // Assuming location is not used, we can hide it or set it to a default value
        locationText.setVisibility(View.GONE); // or you can set locationText dynamically
        translationText.setText(translation);

        savedLayout.addView(cardView);
    }
}
