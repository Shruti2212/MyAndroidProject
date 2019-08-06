package com.example.androidarchitectureexample;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.w3c.dom.Node;

import java.util.List;

@Dao
public interface NoteDao  {
    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("Select * from note_table Order by priority ASC")
       LiveData<List<Note>> getAllNotes();
}
