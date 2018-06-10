package com.workoutmanager.HttpClient;

import com.workoutmanager.Models.AddRoutineModel;
import com.workoutmanager.Models.Token;
import com.workoutmanager.Models.Routine;
import com.workoutmanager.Models.User;
import com.workoutmanager.Models.UserDetails;
import com.workoutmanager.Models.Workout;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RestInterface {

    @GET("/")
    Call<List<Workout>> workoutList(@Query("filter") String filter);

    @GET("/routine/")
    Call<Routine> routineDetails(@Query("id") Integer id, @Query("user_id") String idModel);

    @POST("/routine/")
    @Headers("Content-Type: application/json")
    Call<String> sendRoutine(@Body AddRoutineModel routine);

    @POST("/token/")
    @Headers("Content-Type: application/json")
    Call<User> sendToken(@Body Token tokenId);

    @PUT("/routine/")
    Call<String> addWorkout(@Query("id") String userId, @Body AddRoutineModel routine);

    @GET("/{userId}/workout/")
    Call<List<Workout>> myWorkoutList(@Path("userId") String userId);

    @GET("/user/")
    Call<UserDetails> getUserInfo(@Query("id") String id, @Query("userId") String userId);

    @PATCH("/routine/")
    @Headers("Content-Type: application/json")
    Call<String> likeRoutine(@Query("id") String user_id, @Body AddRoutineModel routine);

    @DELETE("/routine/")
    Call<String> dislikeRoutine(@Query("userId") String userId, @Query("routineId") int routineId);

    @GET("/persons/")
    Call<List<User>> getPersons(@Query("filter") String filter);

    @PUT("/user/")
    @Headers("Content-Type: application/json")
    Call<String> starUser(@Query("id") String userId, @Body User user);

    @DELETE("/user/")
    @Headers("Content-Type: application/json")
    Call<String> unStarUser(@Query("id") String userId, @Query("star") String starId);

}
