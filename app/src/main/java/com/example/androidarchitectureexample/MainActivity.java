package com.example.androidarchitectureexample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteViewModel noteViewModel;
    private RecyclerView recyclerView;
    private ImageView saveIcon;
    public static final int ADD_NOTE_REQUEST=101;
    public static final int EDIT_NOTE_REQUEST=102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("TAG", "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveIcon=findViewById(R.id.add_icon);

        recyclerView= findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        saveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteEditNoteActivity.class);
                startActivityForResult(intent,ADD_NOTE_REQUEST);
            }
        });

        final Note_Adapter adapter = new Note_Adapter();
        recyclerView.setAdapter(adapter);


        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
                Toast.makeText(MainActivity.this,"onChange",Toast.LENGTH_LONG).show();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position =viewHolder.getAdapterPosition();
                Note note = adapter.getNote(position);
                noteViewModel.delete(note);
                Toast.makeText(MainActivity.this,"Note Deleted",Toast.LENGTH_LONG).show();


            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new Note_Adapter.OnNotesClickListener() {
            @Override
            public void onNoteClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddNoteEditNoteActivity.class);
                intent.putExtra(AddNoteEditNoteActivity.EXTRA_TITLE,note.getTitle());
                intent.putExtra(AddNoteEditNoteActivity.EXTRA_DESCRIPTION,note.getDescription());
                intent.putExtra(AddNoteEditNoteActivity.EXTRA_PRIORITY,note.getPriority());
                intent.putExtra(AddNoteEditNoteActivity.EXTRA_ID,note.getId());

                startActivityForResult(intent,EDIT_NOTE_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("TAG", "onActivityResult: Requset code"+ requestCode);
        Log.i("TAG", "onActivityResult: Result code"+ resultCode);
        if(requestCode==ADD_NOTE_REQUEST&&resultCode==RESULT_OK)
        {
            String title = data.getStringExtra(AddNoteEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNoteEditNoteActivity.EXTRA_DESCRIPTION);
            int priority= data.getIntExtra(AddNoteEditNoteActivity.EXTRA_PRIORITY,1);

            Note note = new Note(title,description,priority);
            noteViewModel.insert(note);

            Toast.makeText(this,"Note Saved",Toast.LENGTH_LONG).show();
        }
        else if(requestCode==EDIT_NOTE_REQUEST&&resultCode==RESULT_OK){
            Log.i("TAG", "onActivityResult: Inside Correct if");
            int id = data.getIntExtra(AddNoteEditNoteActivity.EXTRA_ID,-1);
            Log.i("TAG", "onActivityResult: Inside Correct if and ID value"+id);
            if(id == -1) {
                Toast.makeText(this, "Note cannot be updated", Toast.LENGTH_LONG).show();
                return;
            }

                Log.i("TAG", "onActivityResult: Inside Correct if within if");
                String title = data.getStringExtra(AddNoteEditNoteActivity.EXTRA_TITLE);
                String description = data.getStringExtra(AddNoteEditNoteActivity.EXTRA_DESCRIPTION);

                int priority= data.getIntExtra(AddNoteEditNoteActivity.EXTRA_PRIORITY,1);
           // Log.i("TAG", "onActivityResult: priority "+priority);
                Note note = new Note(title,description,priority);
                note.setId(id);
                noteViewModel.update(note);
                Toast.makeText(this,"Note Updated",Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this,"Note Not Saved",Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete_all_option:
                noteViewModel.deleteAll();
                Toast.makeText(this,"All Notes Deleted",Toast.LENGTH_LONG).show();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }

    }
}
