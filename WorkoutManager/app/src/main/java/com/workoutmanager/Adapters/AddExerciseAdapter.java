package com.workoutmanager.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.workoutmanager.Models.Exercise;
import com.workoutmanager.R;

import java.util.ArrayList;
import java.util.List;

public class AddExerciseAdapter extends RecyclerView.Adapter<AddExerciseAdapter.ExerciseHolder>{

    private List<String> exercises;
    private Context context;
    private ArrayList<Exercise> items;

    public class ExerciseHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public Spinner spinner;
        public Button delete;
        public EditText sets, reps;

        ExerciseHolder(View view) {
            super(view);
            spinner = view.findViewById(R.id.exercise_spinner);
            delete = view.findViewById(R.id.delete_exercise);
            sets = view.findViewById(R.id.edit_sets);
            reps = view.findViewById(R.id.edit_reps);


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    items.get(getAdapterPosition()).setExerciseName(parent.getItemAtPosition(position).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            sets.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().isEmpty()) {
                        sets.getText().clear();
                        reps.getText().clear();
                        items.get(getAdapterPosition()).setSets(0);
                    }

                    else items.get(getAdapterPosition()).setSets(Integer.valueOf(s.toString()));

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            if(items.size() == 3) delete.setVisibility(View.INVISIBLE);
            else delete.setVisibility(View.VISIBLE);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sets.getText().clear();
                    reps.getText().clear();
                    items.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                }
            });
        }
    }

    public AddExerciseAdapter(Context context, List<String> exercises,
                       ArrayList<Exercise> items ){
        this.exercises = exercises;
        this.context = context;
        this.items = items;
    }

    @Override
    public ExerciseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.add_exercise_item, parent, false);

        return new AddExerciseAdapter.ExerciseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ExerciseHolder holder, final int position) {
        ArrayAdapter<String> exerciseAdapter;
        exerciseAdapter = new ArrayAdapter<String>
                (context, android.R.layout.simple_spinner_dropdown_item, exercises);
        exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(exerciseAdapter);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
