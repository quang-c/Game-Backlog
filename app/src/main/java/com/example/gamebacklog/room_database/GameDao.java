package com.example.gamebacklog.room_database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.gamebacklog.model.Game;

import java.util.List;

// data access object, specifying SQL queries that will be associated with method calls in another class
@Dao
public interface GameDao {

    // SQL queries for inserting, deleting, updating and get the games from the games list
    @Insert
    void insert(Game game);

    @Insert
    void insert(List<Game> games);

    @Delete
    void delete(Game game);

    // delete all games in the list
    @Delete
    void delete(List<Game> games);

    @Update
    void update(Game game);

    // retrieve all games in the list
    @Query("SELECT * FROM game_table")
    LiveData<List<Game>> getAllGames();
}

