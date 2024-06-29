package com.example.emptyling;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private boolean isCardViewAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find reference to the translate button
        ImageButton translateButton = findViewById(R.id.button_translate);
        LinearLayout parentLayout = findViewById(R.id.parentLayout);

        // Set click listener to the translate button
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCardViewAdded) {
                    // Create a new CardView
                    CardView cardView = new CardView(MainActivity.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.setMargins(26, 16, 26, 16);
                    cardView.setLayoutParams(layoutParams);
                    cardView.setCardElevation(4f);

                    // Add content to the CardView
                    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                    View cardContentView = inflater.inflate(R.layout.result_card_view, null);
                    cardView.addView(cardContentView);

                    // Find reference to the button_cancel ImageButton from the inflated view
                    ImageButton cancelButton = cardContentView.findViewById(R.id.button_cancel);

                    // Set click listener to the button_cancel
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Remove the CardView from its parent layout
                            parentLayout.removeView(cardView);

                            // Update flag
                            isCardViewAdded = false;
                        }
                    });

                    // Add the CardView to the parent layout
                    parentLayout.addView(cardView);

                    // Update flag
                    isCardViewAdded = true;
                }
            }
        });

        // Find references to the left and right buttons
        Button leftButton = findViewById(R.id.leftButton);
        Button rightButton = findViewById(R.id.rightButton);

        // Set click listeners to navigate to DownloadLangActivity
        View.OnClickListener navigateToDownloadLang = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownloadLangActivity.class);
                startActivity(intent);
            }
        };

        leftButton.setOnClickListener(navigateToDownloadLang);
        rightButton.setOnClickListener(navigateToDownloadLang);
    }
}
