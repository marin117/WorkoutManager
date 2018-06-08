package com.workoutmanager.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.workoutmanager.Adapters.ExerciseAdapter;
import com.workoutmanager.Adapters.WorkoutAdapter;
import com.workoutmanager.HttpClient.RetrofitClient;
import com.workoutmanager.Models.User;
import com.workoutmanager.Models.UserDetails;
import com.workoutmanager.Models.Workout;
import com.workoutmanager.R;
import com.workoutmanager.Utils.DataHandler;
import com.workoutmanager.ViewModel.MainViewModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.workoutmanager.Utils.PictureUtils.loadPicture;

public class UserFragment extends Fragment implements DataHandler {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView username;
    private TextView types;
    private TextView exercises;
    private MainViewModel mainViewModel;
    private RetrofitClient retrofit;
    private CircleImageView userPhoto;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mainViewModel = ViewModelProviders.of(getActivity()).
                get(MainViewModel.class);
        getData();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.user_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.user_workout);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        username = view.findViewById(R.id.username);
        types = view.findViewById(R.id.fav_types);
        exercises = view.findViewById(R.id.fav_exercises);
        userPhoto = view.findViewById(R.id.user_photo);

        retrofit = new RetrofitClient();

        return view;
    }

    @Override
    public void getData() {
        mainViewModel.getUserId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String userId) {
                Call<UserDetails> userInfo = retrofit.createClient().getUserInfo(userId);
                userInfo.enqueue(new Callback<UserDetails>() {
                    @Override
                    public void onResponse(@NonNull Call<UserDetails> call,@NonNull Response<UserDetails> response) {
                        username.setText(response.body().getUser().getUsername());
                        loadPicture(response.body().getUser().getPicture(), 300, 300, userPhoto);
                        mAdapter = new WorkoutAdapter(response.body().getWorkouts(), mainViewModel,getContext());
                        mRecyclerView.setAdapter(mAdapter);
                        types.setText(TextUtils.join(", ", response.body().getType()));
                        exercises.setText(TextUtils.join(", ", response.body().getExercise()));
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserDetails> call,@NonNull Throwable t) {

                    }
                });

            }
        });

    }
}
