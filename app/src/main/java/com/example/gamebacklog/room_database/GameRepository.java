package com.example.gamebacklog.room_database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.gamebacklog.model.Game;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GameRepository {

    private GameRoomDatabase mGameRoomDatabase;
    private GameDao mGameDao;
    private LiveData<List<Game>> mGamesList;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public GameRepository (Context context){
        mGameRoomDatabase = GameRoomDatabase.getDatabase(context);
        mGameDao = mGameRoomDatabase.gameDao();
        mGamesList = mGameDao.getAllGames();
    }

    // associating the SQL queries in DAO with these methods
    public LiveData<List<Game>> getAllGames() {
        return mGamesList;
    }

    public void insert(final Game game){
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameDao.insert(game);
            }
        });
    }

    public void insertAll(final List<Game> games){
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameDao.insert(games);
            }
        });
    }

    public void delete(final Game game) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameDao.delete(game);
            }
        });
    }

    public void deleteAll(final List<Game> games) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameDao.delete(games);
            }
        });
    }

    public void update(final Game game) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameDao.update(game);
            }
        });
    }
}
