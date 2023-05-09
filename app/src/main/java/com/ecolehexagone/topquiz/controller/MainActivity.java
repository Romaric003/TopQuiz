package com.ecolehexagone.topquiz.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ecolehexagone.topquiz.R;
import com.ecolehexagone.topquiz.model.User;

public class MainActivity extends AppCompatActivity {

    // Declaration des variables utilisees dans MainActivity
    private EditText mNameEditText;
    private Button mPlayButton;

    private User mUser;

    private final int GAME_ACTIVITY_REQUEST_CODE = 42;
    private static final String SHARED_PREF_USER_INFO_NAME = "SHARED_PREF_USER_INFO_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Référencement des vues et stockage dans les variables locales
        TextView greetingTextView = findViewById(R.id.main_textview_greeting);
        mNameEditText= findViewById(R.id.main_edittext_name);
        mPlayButton = findViewById(R.id.main_button_play);

        mUser = new User();

        // Récupération du prénom stocké dans les SharedPreferences
        String firstName = getSharedPreferences(SHARED_PREF_USER_INFO_NAME, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_NAME,null);

        // Activation / désactivation du bouton "Play" en fonction de la saisie du nom
        mPlayButton.setEnabled(!mNameEditText.getText().toString().isEmpty());
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                mPlayButton.setEnabled(!editable.toString().isEmpty());
            }
        });

        // Démarrage de la GameActivity lors du clic sur le bouton "Play"
        mPlayButton.setOnClickListener(view -> {
            Intent gameActivityIntent = new Intent(MainActivity.this, GameActivity.class);
            startActivityForResult(gameActivityIntent, GAME_ACTIVITY_REQUEST_CODE);
            mUser.setFirstName(mNameEditText.getText().toString());

            // Stockage du prénom dans les SharedPreferences
            getSharedPreferences(SHARED_PREF_USER_INFO_NAME, MODE_PRIVATE)
                .edit()
                .putString(SHARED_PREF_USER_INFO_NAME, mNameEditText.getText().toString())
                .apply();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Récupération du score depuis l'Intent
            int score = data != null ? data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0) : 0;
        }
    }
}
