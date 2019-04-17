package com.example.gamebacklog.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.gamebacklog.R;
import com.example.gamebacklog.model.Game;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// main activity with recyclerView containing cardViews

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener{

    public static final String NEW_GAME = "NewGame";
    public static final int NEW_GAME_REQUEST_CODE = 1234;

    public static final String UPDATE_GAME = "UpdateGame";
    public static final int UPDATE_GAME_REQUEST_CODE = 4321;

    private List<Game> mGames;
    private GameAdapter mGameAdapter;
    private RecyclerView mRecyclerView;
    private GestureDetector mGestureDetector;
    private MainViewModel mMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // default init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGames = new ArrayList<>();

        //init the floating action button which navigates to the "add/edit a game screen"
        FloatingActionButton fab = findViewById(R.id.fabAddGame);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // usage of Intent to navigate to the screen with the AddGameActivity class
                Intent intent = new Intent(MainActivity.this, AddGameActivity.class);
                startActivityForResult(intent, NEW_GAME_REQUEST_CODE);
            }
        });

        // init methods
        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView() {
        // default init of the recyclerView
        mGameAdapter = new GameAdapter(mGames);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mGameAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        // registers gestures
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        // swiping method
        setupItemTouchHelper();
        mRecyclerView.addOnItemTouchListener(this);
    }

    // method for swiping left or right on the game cards
    // showing a Snackbar with an undo button on it
    private void setupItemTouchHelper() {
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = (viewHolder.getAdapterPosition());
                final Game gameData = mGames.get(position);

                mMainViewModel.delete(mGames.get(position));
                mGames.remove(position);
                mGameAdapter.notifyItemRemoved(position);

                Snackbar undoBar = Snackbar.make(viewHolder.itemView, "DELETED: " + gameData.getTitle(), Snackbar.LENGTH_LONG);
                undoBar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMainViewModel.insert(gameData);
                    }
                });
                undoBar.show();
            }
        });
        touchHelper.attachToRecyclerView(mRecyclerView);
    }

    private String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(Calendar.getInstance().getTime());
    }

    // init of the viewModel
    private void initViewModel() {
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.getGamesList().observe(this, new Observer<List<Game>>() {
            @Override
            public void onChanged(@Nullable List<Game> games) {
                mGames = games;
                updateUI();
            }
        });
    }

    private void updateUI() {
        if (mGameAdapter == null) {
            mGameAdapter = new GameAdapter(mGames);
            mRecyclerView.setAdapter(mGameAdapter);
        } else
            mGameAdapter.updateGamesList(mGames);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // finding the garbage bin icon in the header
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // if the user pressed the icon, the method OnDeleteAllClick will be called
        if (id == R.id.deleteList) {
            onDeleteAllClick(mGames);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // method of deleting the whole list
    // Snackbar included to undo the change
    private void onDeleteAllClick(List<Game> gamesList) {
        // deleting the whole list is only possible if there is at least one game card
        if (gamesList.size() > 0) {
            final List<Game> tempGamesList = mGames;

            mMainViewModel.deleteAll(gamesList);

            Snackbar undoBar = Snackbar.make(mRecyclerView, "DELETED ALL GAMES", Snackbar.LENGTH_LONG);
            undoBar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // insert the data back into list
                    mMainViewModel.insertAll(tempGamesList);
                }
            });
            undoBar.show();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (child != null) {
            int adapterPosition = recyclerView.getChildAdapterPosition(child);
            if (mGestureDetector.onTouchEvent(motionEvent)) {
                Intent intent = new Intent(MainActivity.this, AddGameActivity.class);
                intent.putExtra(UPDATE_GAME, mGames.get(adapterPosition));
                startActivityForResult(intent, UPDATE_GAME_REQUEST_CODE);
            }
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_GAME_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Game newGame = data.getParcelableExtra(MainActivity.NEW_GAME);
                newGame.setDate(getCurrentDate());
                mMainViewModel.insert(newGame);
            }
        } else if (requestCode == UPDATE_GAME_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Game updateGame = data.getParcelableExtra(MainActivity.UPDATE_GAME);
                updateGame.setDate(getCurrentDate());
                mMainViewModel.update(updateGame);
            }
        }
    }
}
