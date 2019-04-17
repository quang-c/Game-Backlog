package com.example.gamebacklog.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.gamebacklog.model.Game;
import com.example.gamebacklog.room_database.GameRepository;

import java.util.List;

// Stores UI-related data

public class MainViewModel extends AndroidViewModel {

    private GameRepository mGameRepository;
    //
    private LiveData<List<Game>> mGamesList;

    public MainViewModel(@NonNull Application application) {
        super(application);

        // init the repository class, using the methods inside that class
        mGameRepository = new GameRepository(application.getApplicationContext());
        mGamesList = mGameRepository.getAllGames();
    }

    public LiveData<List<Game>> getGamesList() {
        return mGamesList;
    }

    public void insert(Game game) {
        mGameRepository.insert(game);
    }

    public void insertAll(List<Game> games) {
        mGameRepository.insertAll(games);
    }

    public void update(Game game) {
        mGameRepository.update(game);
    }

    public void delete(Game game) {
        mGameRepository.delete(game);
    }

    public void deleteAll(List<Game> games) {
        mGameRepository.deleteAll(games);
    }

}
