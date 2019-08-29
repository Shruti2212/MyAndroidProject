package com.example.androidarchitectureexample;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Note_Adapter extends ListAdapter<Note, Note_Adapter.NoteHolder> {

    private OnNotesClickListener listener;

    public Note_Adapter() {
        super(Diff_Callback);

    }

    private static final DiffUtil.ItemCallback<Note> Diff_Callback = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return (oldItem.getId() == newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return ((oldItem.getTitle().equals(newItem.getTitle())) && (oldItem.getTitle().equals(newItem.getTitle())) && (oldItem.getPriority() == newItem.getPriority()));
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.note_item, parent, false);
        Log.i("TAG", "onCreateViewHolder: ");
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = getItem(position);
        holder.title.setText(currentNote.getTitle());
        holder.description.setText(currentNote.getDescription());
        Log.i("TAG", "onBindViewHolder: set description done ");
        holder.priority.setText(String.valueOf(currentNote.getPriority()));

    }

   /* @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> list) {
        this.notes = list;
        notifyDataSetChanged();
    }*/

    public Note getNote(int position) {
        return getItem(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private TextView priority;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_title);
            priority = itemView.findViewById(R.id.text_view_priority);
            description = itemView.findViewById(R.id.text_view_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onNoteClick(getItem(position));
                    }
                }
            });

        }
    }

    public interface OnNotesClickListener {
        void onNoteClick(Note note);
    }

    public void setOnItemClickListener(OnNotesClickListener listener) {
        this.listener = listener;

    }
}
