package com.example.emptyling;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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

        // Find references to the left and right spinners
        Spinner leftSpinner = findViewById(R.id.leftSpinner);
        Spinner rightSpinner = findViewById(R.id.rightSpinner);

        // Define the values for the spinners
        String[] languages = {"Filipino", "Bikol Cam. Norte","Bikol Central","Camalig", "Manito","Daraga", "Oas", "Pioduran","Libon" };

        // Create an ArrayAdapter using the custom layout for spinner items
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter to the left and right spinners
        leftSpinner.setAdapter(adapter);
        rightSpinner.setAdapter(adapter);

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
                            // Define the action to perform when button_cancel is clicked
                            // For example, displaying a toast message
                            //Toast.makeText(MainActivity.this, "Cancel button clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Add the CardView to the parent layout
                    parentLayout.addView(cardView);

                    // Update flag
                    isCardViewAdded = true;
                }
            }
        });
    }
}
