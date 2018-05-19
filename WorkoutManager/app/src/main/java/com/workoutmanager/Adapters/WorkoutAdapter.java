package com.workoutmanager.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.workoutmanager.Fragments.RoutineDetailFragment;
import com.workoutmanager.R;
import com.workoutmanager.Models.Workout;
import com.workoutmanager.ViewModel.MainViewModel;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutHolder> {

    private List<Workout> workoutList;
    private MainViewModel mainViewModel;

    public class WorkoutHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView name, user, date, location, stars, owner;
        WorkoutHolder(View view) {
            super(view);
            name = view.findViewById(R.id.workout_name);
            user = view.findViewById(R.id.user);
            date = view.findViewById(R.id.date);
            location = view.findViewById(R.id.location);
            stars = view.findViewById(R.id.stars);
            owner = view.findViewById(R.id.owner);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Log.d("ID", String.valueOf(workoutList.get(getAdapterPosition()).getRoutineId()));
            Context context = v.getContext();
            Toast.makeText(context,
                    "ID: "+ String.valueOf(workoutList.get(getAdapterPosition()).getName()),
                    Toast.LENGTH_LONG).show();

            Workout workout = workoutList.get(getAdapterPosition());
            mainViewModel.setRoutineID(workout);


            FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment ,new RoutineDetailFragment()).addToBackStack(null);
            fragmentTransaction.commit();

        }
    }

    public WorkoutAdapter(List<Workout> workoutList, MainViewModel mainViewModel){
        this.workoutList = workoutList;
        this.mainViewModel = mainViewModel;
    }

    @Override
    public WorkoutAdapter.WorkoutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workout_list_item, parent, false);


        return new WorkoutHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WorkoutAdapter.WorkoutHolder holder, int position) {
        Workout workout = workoutList.get(position);
        holder.name.setText(workout.getName());
        holder.user.setText(workout.getUsername());
        holder.date.setText(workout.getDate());
        holder.location.setText(workout.getLocation());
        holder.stars.setText(workout.getAppraisal().toString());
        holder.owner.setText(workout.getOwner());
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }
}
