package com.workoutmanager.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.workoutmanager.Models.Exercise;
import com.workoutmanager.R;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder> {

    private List<Exercise> exerciseList;


    public class ExerciseHolder extends RecyclerView.ViewHolder {
        public TextView name, sets, reps;
        ExerciseHolder(View view) {
            super(view);
            name = view.findViewById(R.id.exercise_name);
            sets = view.findViewById(R.id.sets);
            reps = view.findViewById(R.id.reps);
        }
    }

    public ExerciseAdapter(List<Exercise> exerciseList){
        this.exerciseList = exerciseList;
    }

    @Override
    public ExerciseAdapter.ExerciseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.exercise_list_item, parent, false);

        return new ExerciseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExerciseAdapter.ExerciseHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        holder.name.setText(String.format("Exercise: %s", exercise.getExerciseName()));
        holder.sets.setText(String.format("Sets: %d", exercise.getSets()));
        holder.reps.setText(String.format("Reps: %d", exercise.getReps()));
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }
}
