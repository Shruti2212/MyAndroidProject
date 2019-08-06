package com.example.androidarchitectureexample;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class},version = 2)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance;

    public abstract NoteDao notedao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new populateAssyncTask(instance).execute();
        }


    };
     private static class populateAssyncTask extends AsyncTask<Void,Void,Void>
     {
         private NoteDao noteDao;

         public populateAssyncTask(NoteDatabase db) {
             this.noteDao = db.notedao();
         }

         @Override
         protected Void doInBackground(Void... voids) {
             noteDao.insert(new Note("Title 1","Description 1",1));
             noteDao.insert(new Note("Title 2","Description 2",2));
             noteDao.insert(new Note("Title 3","Description 3",4));
             noteDao.insert(new Note("Title 3","Description 3",3));
             return null;
         }
     }

}
