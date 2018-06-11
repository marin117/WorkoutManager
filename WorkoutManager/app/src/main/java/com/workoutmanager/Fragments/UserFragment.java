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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;
import com.workoutmanager.Adapters.ExerciseAdapter;
import com.workoutmanager.Adapters.WorkoutAdapter;
import com.workoutmanager.HttpClient.RetrofitClient;
import com.workoutmanager.Models.User;
import com.workoutmanager.Models.UserDetails;
import com.workoutmanager.Models.Workout;
import com.workoutmanager.R;
import com.workoutmanager.Utils.DataHandler;
import com.workoutmanager.Utils.SharedPreferencesUtil;
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
    private TextView stars;
    private MainViewModel mainViewModel;
    private RetrofitClient retrofit;
    private User currentUser;
    private CircleImageView userPhoto;
    private LikeButton starButton;
    private String id;
    private RelativeLayout actionContainer, infoContainer;

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
        stars = view.findViewById(R.id.star_number);
        starButton = view.findViewById(R.id.user_star);
        actionContainer = view.findViewById(R.id.user_action);
        infoContainer = view.findViewById(R.id.user_info);

        starButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                starUser();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                unStarUser();
            }
        });

        id = new SharedPreferencesUtil(getActivity()).readData(getString(R.string.id));
        retrofit = new RetrofitClient();

        stars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.setUserId(currentUser.getId());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().
                        addToBackStack(null).
                        replace(R.id.main_fragment,new UserStarsFragment()).commit();
            }
        });

        return view;
    }

    @Override
    public void getData() {
        mainViewModel.getUserId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String userId) {
                Call<UserDetails> userInfo = retrofit.createClient().getUserInfo(id, userId);
                userInfo.enqueue(new Callback<UserDetails>() {
                    @Override
                    public void onResponse(@NonNull Call<UserDetails> call,@NonNull Response<UserDetails> response) {

                        if (response.code() == 200) {
                            actionContainer.setVisibility(View.VISIBLE);
                            infoContainer.setVisibility(View.VISIBLE);
                            currentUser = response.body().getUser();
                            username.setText(response.body().getUser().getUsername());
                            loadPicture(response.body().getUser().getPicture(), 300, 300, userPhoto);
                            if (response.body().getWorkouts().size() == 1 &&
                                    response.body().getWorkouts().get(0).getRoutineId() == -1){
                                mRecyclerView.setAdapter(null);

                            }
                            else {
                                mAdapter = new WorkoutAdapter(response.body().getWorkouts(), mainViewModel, getContext());
                                mRecyclerView.setAdapter(mAdapter);
                            }
                            types.setText(TextUtils.join(", ", response.body().getType()));
                            exercises.setText(TextUtils.join(", ", response.body().getExercise()));
                            stars.setText(response.body().getUser().getStars().toString());

                            if (response.body().getUser().getId().equals(id)) {
                                starButton.setVisibility(View.INVISIBLE);
                            }
                            else {
                                if (response.body().getUser().getIsstar())
                                    starButton.setLiked(true);
                            }
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<UserDetails> call,@NonNull Throwable t) {
                    }
                });

            }
        });

    }

    private void starUser(){
        Call<String> star = retrofit.createClient().starUser(id, currentUser);
        star.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call,@NonNull Response<String> response) {
                currentUser.setStars(currentUser.getStars() + 1);
                stars.setText(currentUser.getStars().toString());
            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {

            }
        });

    }

    private void unStarUser(){
        Call<String> unStar = retrofit.createClient().unStarUser(id, currentUser.getId());
        unStar.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                currentUser.setStars(currentUser.getStars() - 1);
                stars.setText(currentUser.getStars().toString());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }
}
