// MainActivity.java
package com.example.emptyling;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_LEFT = 1;
    private static final int REQUEST_CODE_RIGHT = 2;

    private Button leftButton;
    private Button rightButton;
    private EditText editTextInput;
    private TextView textViewOutput;
    private ImageButton buttonMic;
    private LinearLayout iconsLayout;
    private ImageButton iconCopy, iconStar, iconAudio;

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

        // Initialize views
        editTextInput = findViewById(R.id.editTextInput);
        textViewOutput = findViewById(R.id.textViewOutput);
        buttonMic = findViewById(R.id.buttonMic);
        iconsLayout = findViewById(R.id.iconsLayout);
        iconCopy = findViewById(R.id.iconCopy);
        iconStar = findViewById(R.id.iconStar);
        iconAudio = findViewById(R.id.iconAudio);

        // Set up TextWatcher
        editTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Update textViewOutput with the text from editTextInput
                textViewOutput.setText(s.toString());
                if (s.length() > 0) {
                    // Show icons and change mic to cancel
                    iconsLayout.setVisibility(View.VISIBLE);
                    buttonMic.setImageResource(R.drawable.cancel);
                } else {
                    // Hide icons and revert cancel to mic
                    iconsLayout.setVisibility(View.GONE);
                    buttonMic.setImageResource(R.drawable.ic_mic);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed after text changed
            }
        });

        // Handle mic button click to clear text
        buttonMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextInput.setText("");
                buttonMic.setImageResource(R.drawable.ic_mic);
                iconsLayout.setVisibility(View.GONE);
            }
        });

        // Handle icon clicks
        iconCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Copy text to clipboard logic here
            }
        });

        iconStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add to favorites logic here
            }
        });

        iconAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Play audio logic here
            }
        });

        // Find references to the left and right buttons
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);

        // Set click listeners to navigate to respective DownloadLangActivity
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownloadLangLeftActivity.class);
                startActivityForResult(intent, REQUEST_CODE_LEFT);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownloadLangRightActivity.class);
                startActivityForResult(intent, REQUEST_CODE_RIGHT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String selectedLanguage = data.getStringExtra("selectedLanguage");
            if (selectedLanguage != null) {
                if (requestCode == REQUEST_CODE_LEFT) {
                    leftButton.setText(selectedLanguage);
                } else if (requestCode == REQUEST_CODE_RIGHT) {
                    rightButton.setText(selectedLanguage);
                }
            }
        }
    }
}
