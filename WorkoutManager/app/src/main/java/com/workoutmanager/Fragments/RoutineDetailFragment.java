package com.workoutmanager.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.workoutmanager.Adapters.ExerciseAdapter;
import com.workoutmanager.HttpClient.RetrofitClient;
import com.workoutmanager.Models.AddRoutineModel;
import com.workoutmanager.Models.IdModel;
import com.workoutmanager.Models.Routine;
import com.workoutmanager.Models.Workout;
import com.workoutmanager.R;
import com.workoutmanager.Utils.DataHandler;
import com.workoutmanager.ViewModel.MainViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutineDetailFragment extends Fragment implements DataHandler{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView textOwner;
    private TextView textName;
    private TextView reuseNumber;
    private MainViewModel mainViewModel;
    private MenuItem addItem;
    private String userId;
    private RetrofitClient retrofit;
    private Routine currentRoutine;
    private LikeButton likeButton;
    private TextView likeNumber;
    private RelativeLayout actionContainer;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routine_detail, container, false);

        mRecyclerView = view.findViewById(R.id.detail_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        textOwner = view.findViewById(R.id.detail_user);
        textName = view.findViewById(R.id.detail_name);
        addItem = view.findViewById(R.id.new_routine);
        likeButton = view.findViewById(R.id.routine_like);
        likeNumber = view.findViewById(R.id.like_number);
        reuseNumber = view.findViewById(R.id.reuse_number);
        actionContainer = view.findViewById(R.id.action_container);


        retrofit = new RetrofitClient();
        userId = new IdModel(getActivity()).getId();

        textOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().
                        addToBackStack(null).
                        replace(R.id.main_fragment,new UserFragment()).commit();
            }
        });

        likeButton.setOnLikeListener(new OnLikeListener() {
            AddRoutineModel addRoutineModel = new AddRoutineModel(currentRoutine);
            @Override
            public void liked(LikeButton likeButton) {
                 likeRoutine();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                dislikeRoutine();
            }
        });

        likeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.setRoutineId(currentRoutine);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().
                        addToBackStack(null).
                        replace(R.id.main_fragment,new RoutineLikeFragment()).commit();
            }
        });

        reuseNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.setRoutineId(currentRoutine);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().
                        addToBackStack(null).
                        replace(R.id.main_fragment,new RoutineReuseFragment()).commit();
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mainViewModel = ViewModelProviders.of(getActivity()).
                get(MainViewModel.class);

        getData();

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void getData(){
        mainViewModel.getWorkout().observe(this, new Observer<Workout>() {
            @Override
            public void onChanged(@Nullable Workout workout) {
                final String owner = workout.getOwner();
                Call<Routine> call = retrofit.createClient().routineDetails(workout.getRoutineId(),
                        userId);
                call.enqueue(new Callback<Routine>() {
                    @Override
                    public void onResponse(@NonNull Call<Routine> call, @NonNull Response<Routine> response){
                        textName.setText(response.body().getName());
                        actionContainer.setVisibility(View.VISIBLE);
                        mAdapter = new ExerciseAdapter(response.body().getExercise());
                        mRecyclerView.setAdapter(mAdapter);
                        if (!response.body().getIsmy()){
                            addItem.setVisible(true);
                        }
                        currentRoutine = response.body();
                        textOwner.setText(owner);
                        reuseNumber.setText(response.body().getUsed().toString());
                        mainViewModel.setUserId(response.body().getUserId());
                        likeNumber.setText(response.body().getAppraisal().toString());
                        if (response.body().getIsliked())
                            likeButton.setLiked(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Routine> call, @NonNull Throwable t){
                        Toast.makeText(getActivity(),
                                "Failure",
                                Toast.LENGTH_SHORT).show();
                        Log.e("TAG", t.getMessage());

                    }
                });

            }
        });
    }

    private void sendRoutine(){
        AddRoutineModel addRoutineModel = new AddRoutineModel(currentRoutine);
        Call<String> call = retrofit.createClient().addWorkout(userId, addRoutineModel);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull  Call<String> call, @NonNull Response<String> response) {
                Toast.makeText(getContext(), "Workout added", Toast.LENGTH_SHORT).show();
                addItem.setVisible(false);
            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {

            }
        });

    }

    private void likeRoutine(){
        AddRoutineModel addRoutineModel = new AddRoutineModel(currentRoutine);
        Call<String> like = retrofit.createClient().likeRoutine(userId, addRoutineModel);
        like.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull  Call<String> call,@NonNull Response<String> response){
                currentRoutine.setAppraisal(currentRoutine.getAppraisal() + 1);
                likeNumber.setText(currentRoutine.getAppraisal().toString());


            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {

            }
        });

    }

    private void dislikeRoutine(){

        Call<String> like = retrofit.createClient().dislikeRoutine(userId, currentRoutine.getId());
        like.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull  Call<String> call,@NonNull Response<String> response){
                currentRoutine.setAppraisal(currentRoutine.getAppraisal() - 1);
                likeNumber.setText(currentRoutine.getAppraisal().toString());


            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {

            }
        });

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        addItem = menu.findItem(R.id.new_routine);
        //addItem.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_routine:
                sendRoutine();

        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_to_my_routine_menu, menu);
    }
}
