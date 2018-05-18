package com.workoutmanager.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.workoutmanager.Adapters.AddExerciseAdapter;
import com.workoutmanager.HttpClient.RetrofitClient;
import com.workoutmanager.Models.AddRoutineModel;
import com.workoutmanager.Models.Exercise;
import com.workoutmanager.Models.Routine;
import com.workoutmanager.Models.Workout;
import com.workoutmanager.R;
import com.workoutmanager.ViewModel.AddExerciseViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddExerciseFragment extends Fragment {

    private AddExerciseViewModel mViewModel;
    private ArrayList<String> types;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Exercise> items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_exercise_fragment, container, false);
        types = new ArrayList<String>();
        items = new ArrayList<Exercise>();

        mRecyclerView = view.findViewById(R.id.add_exercise_recycler_view);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        initExercise();
        setHasOptionsMenu(true);

        mViewModel = ViewModelProviders.of(getActivity())
                .get(AddExerciseViewModel.class);

        mViewModel.getTypesFrag().observe(getActivity(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(@Nullable ArrayList<String> typesFrag) {
                types = typesFrag;
                mViewModel.getExercises(types.toArray(new String[0])).observe(getActivity(),
                        new Observer<List<String>>(){
                    @Override
                    public void onChanged(@Nullable List<String> exercises) {
                        mAdapter = new AddExerciseAdapter(getContext(), exercises, items);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem confirmButton = menu.findItem(R.id.confirm);
        confirmButton.setIcon(R.drawable.confirm_button);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_item:
                if (items.size() <= 5) {
                    items.add(new Exercise());
                    mAdapter.notifyItemInserted(items.size() - 1);
                }
                else {
                    Toast.makeText(getContext(), "You can't add more exercises.", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.confirm:
                mViewModel.getRoutine().observe(getActivity(), new Observer<Routine>() {
                    @Override
                    public void onChanged(@Nullable Routine routine) {
                        routine.setExercise(items);
                        AddRoutineModel routineModel = new AddRoutineModel(routine);
                        RetrofitClient retrofit = new RetrofitClient();
                        Call<String> call = retrofit.createClient().sendRoutine(routineModel);

                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                Toast.makeText(getContext(), "dobio sam odg", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                Toast.makeText(getContext(), "nisam odg", Toast.LENGTH_LONG).show();

                            }
                        });

                    }
                });
                return true;

            default:
                break;
        }
        return false;
    }

    private void initExercise(){
        int INIT_NUMBER = 3;
        for (int i =0; i < INIT_NUMBER; i++) {
            items.add(new Exercise());
        }
    }

}
