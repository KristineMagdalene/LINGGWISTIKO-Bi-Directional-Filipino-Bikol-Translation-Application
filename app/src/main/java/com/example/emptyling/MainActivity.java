package com.example.emptyling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;

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
    private ImageButton iconCopy, iconStar, iconAudio, starIcon;
    private LinearLayout parentLayout, recentInputsLayout;
    private boolean isStarred = false;
    private ArrayList<String> recentInputs = new ArrayList<>();
    private ArrayList<TranslationItem> savedTranslationItems = new ArrayList<>();
    private boolean inputCleared = false;
    private Handler handler = new Handler();
    private Runnable longPressRunnable;
    private String leftButtonText = "Filipino";
    private String rightButtonText = "Cam Norte";

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
        TextView goToSettings = findViewById(R.id.goToSettings);
        starIcon = findViewById(R.id.star);
        parentLayout = findViewById(R.id.parentLayout);
        recentInputsLayout = findViewById(R.id.recentInputsLayout);

        // Set up word limit for editTextInput
        editTextInput.setFilters(new InputFilter[]{new WordLimitFilter(12)});

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
                    inputCleared = false;  // Reset the flag
                } else {
                    if (!inputCleared) {
                        // Save the text before it was cleared
                        addRecentInputCard(s.toString());
                    }
                    // Hide icons and revert cancel to mic
                    iconsLayout.setVisibility(View.GONE);
                    buttonMic.setImageResource(R.drawable.ic_mic);
                    iconStar.setImageResource(R.drawable.ic_star); // Reset iconStar to default
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed after text changed
            }
        });

        editTextInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (longPressRunnable == null) {
                            longPressRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    editTextInput.setText("");
                                    buttonMic.setImageResource(R.drawable.ic_mic);
                                    iconsLayout.setVisibility(View.GONE);
                                    inputCleared = true;  // Set the flag to true when input is cleared
                                }
                            };
                        }
                        handler.postDelayed(longPressRunnable, 1000); // 1 second delay
                    } else if (event.getAction() == KeyEvent.ACTION_UP) {
                        handler.removeCallbacks(longPressRunnable);
                    }
                }
                return false;
            }
        });

        // Handle mic button click to clear text and add to recent inputs
        buttonMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = editTextInput.getText().toString();
                if (!inputText.isEmpty()) {
                    addRecentInputCard(inputText);
                }
                editTextInput.setText("");
                buttonMic.setImageResource(R.drawable.ic_mic);
                iconsLayout.setVisibility(View.GONE);
                inputCleared = true;  // Set the flag to true when input is cleared
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
                saveToFavorites();
                // Reset the star icon to default (unfilled)
                iconStar.setImageResource(R.drawable.ic_star);
            }
        });

        starIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SavedActivity without saving
                Intent intent = new Intent(MainActivity.this, SavedActivity.class);
                intent.putParcelableArrayListExtra("savedTranslationItems", savedTranslationItems);
                startActivity(intent);
            }
        });

        iconAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Play audio logic here
            }
        });

        // Handle goToSettings click
        goToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownloadOfflineLang.class);
                startActivity(intent);
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

    private void saveToFavorites() {
        String newItem = editTextInput.getText().toString();
        if (!newItem.isEmpty()) {
            // Create a new TranslationItem with current left and right button texts
            TranslationItem translationItem = new TranslationItem(newItem, leftButton.getText().toString(), rightButton.getText().toString());
            savedTranslationItems.add(translationItem);
        }

        Intent intent = new Intent(MainActivity.this, SavedActivity.class);
        intent.putParcelableArrayListExtra("savedTranslationItems", savedTranslationItems);
        startActivity(intent);
    }

    private void addRecentInputCard(String inputText) {
        // Avoid adding partial inputs
        if (!inputText.trim().isEmpty() && !recentInputs.contains(inputText)) {
            recentInputs.add(inputText);

            CardView cardView = new CardView(this);
            cardView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            cardView.setCardElevation(4);
            cardView.setRadius(8);
            cardView.setPadding(16, 16, 16, 16);
            cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white));

            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setText(inputText);
            textView.setTextSize(18);
            textView.setTextColor(getResources().getColor(android.R.color.black));

            cardView.addView(textView);
            recentInputsLayout.addView(cardView, 0); // Add new card at the top
        }
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
