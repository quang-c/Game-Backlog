package com.example.gamebacklog.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.gamebacklog.R;
import com.example.gamebacklog.model.Game;

// The user can create or edit a game card on this "create/edit a game screen" and saving it with the FAB

public class AddGameActivity extends AppCompatActivity {

    private Game mGameState;
    private Spinner statusSelection;
    private EditText mTitle;
    private EditText mPlatform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // default init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // init variables
        mTitle = findViewById(R.id.editTitle);
        mPlatform = findViewById(R.id.editPlatform);
        statusSelection = findViewById(R.id.statusDropMenu);

        // if mGameState is null, the screen will be a create game screen
        mGameState = getIntent().getParcelableExtra(MainActivity.UPDATE_GAME);

        //init the floating button
        initFAB();

        // if the game card has already been created before,
        // the user is going to EDIT the game card
        // else, a new game card is going to be CREATED
        if (mGameState != null) {
            //retrieving previous saved data
            mTitle.setText(mGameState.getTitle());
            mPlatform.setText(mGameState.getPlatform());
            statusSelection.setSelection(((ArrayAdapter) statusSelection.getAdapter()).getPosition(mGameState.getStatus()));
            // set header title
            this.setTitle("Edit Game");
        } else
            this.setTitle("Create Game");
    }


    private void initFAB() {
        FloatingActionButton fab = findViewById(R.id.fabSaveGame);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // the space is to prevent the italic font style to cut off at the end
                String titleText = mTitle.getText().toString() + " ";
                String platformText = mPlatform.getText().toString();
                String statusText = statusSelection.getSelectedItem().toString();

                // if the user is editing a game card,
                // else if the user is added a new game card
                if (mGameState != null) {
                    // if the fields are NOT empty, overwrite and save the new data and send it back to main activity
                    if (!TextUtils.isEmpty(titleText) && !TextUtils.isEmpty(platformText)) {
                        mGameState.setTitle(titleText);
                        mGameState.setPlatform(platformText);
                        mGameState.setStatus(statusText);

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(MainActivity.UPDATE_GAME, mGameState);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    } else
                        Snackbar.make(view, "Please fill in the fields", Snackbar.LENGTH_LONG).show();
                } else {
                    // if the fields are NOT empty, create and save the new data and send it to main activity
                    if (!TextUtils.isEmpty(titleText) && !TextUtils.isEmpty(platformText)) {
                        mGameState = new Game(titleText, platformText, statusText);

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(MainActivity.NEW_GAME, mGameState);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    } else
                        Snackbar.make(view, "Please fill in the fields", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

}
