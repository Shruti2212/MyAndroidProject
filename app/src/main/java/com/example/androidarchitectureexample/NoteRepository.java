package com.example.androidarchitectureexample;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import org.w3c.dom.Node;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDao = noteDatabase.notedao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note) {
        new InsertNoteAssynctask(noteDao).execute(note);
    }

    public void update(Note note) {
        new UpdateNoteAssynctask(noteDao).execute(note);

    }

    public void delete(Note note) {
        new DeleteNoteAssynctask(noteDao).execute(note);
    }

    public void deleteAllNotes() {
        new DeleteAllNoteAssynctask(noteDao).execute();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }


    public static class InsertNoteAssynctask extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        private InsertNoteAssynctask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    public static class UpdateNoteAssynctask extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        private UpdateNoteAssynctask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    public static class DeleteNoteAssynctask extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        private DeleteNoteAssynctask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    public static class DeleteAllNoteAssynctask extends AsyncTask<Void, Void, Void> {

        private NoteDao noteDao;

        private DeleteAllNoteAssynctask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }


    }

}
