package com.workoutmanager.HttpClient;

import com.workoutmanager.Models.AddRoutineModel;
import com.workoutmanager.Models.GoogleToken;
import com.workoutmanager.Models.IdModel;
import com.workoutmanager.Models.Routine;
import com.workoutmanager.Models.Workout;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;


public interface RestInterface {

    @GET("/")
    Call<List<Workout>> workoutList(
    );

    @GET("/routine/")
    Call<Routine> routineDetails(@Query("id") Integer id, @Query("user_id") String idModel);

    @POST("/routine/")
    @Headers("Content-Type: application/json")
    Call<String> sendRoutine(@Body AddRoutineModel routine);

    @POST("/token/")
    @Headers("Content-Type: application/json")
    Call<String> sendToken(@Body GoogleToken tokenId);

    @PUT("/routine/")
    Call<String> addWorkout(@Body AddRoutineModel routine);
}
