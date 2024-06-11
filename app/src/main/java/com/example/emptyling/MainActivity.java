package com.example.emptyling;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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
        String[] languages = {"Filipino", "Bikol Albay"};

        // Create an ArrayAdapter using the custom layout for spinner items
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter to the left and right spinners
        leftSpinner.setAdapter(adapter);
        rightSpinner.setAdapter(adapter);


        //Reference of Imagebutton
        ImageButton buttonTranslate = findViewById(R.id.button_translate);

        //OnclickListener
        buttonTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardView cardView = new CardView(MainActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(16,16,16,16);
                cardView.setLayoutParams(layoutParams);
                cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white));
                cardView.setRadius(16);

                //inflate the card content layout form XML
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View cardContent = inflater.inflate(R.layout.result_card_view, cardView, false);

                //Add the inflated layout to th Cardview
                cardView.addView(cardContent);

                // Add the CardView to the parent layout
                LinearLayout parentLayout = findViewById(R.id.parentLayout);
                parentLayout.addView(cardView);
            }
        });
    }

}
