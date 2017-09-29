package com.seoulapp.ssg.api;

import com.seoulapp.ssg.model.Model;
import com.seoulapp.ssg.model.Ssac;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Boram Moon on 2016-10-05.
 */
public interface VolunteerApiService {
    @GET("volunteer_list")
    Call<Model> getVolunteer_info();

    @FormUrlEncoded
    @POST("volunteer_join")
    Call<Model> joinVolunteer(@Field("vid") int vid, @Field("uid") String uid, @Field("uname") String uname, @Field("uphone") String phoneNumber);

    @FormUrlEncoded
    @POST("volunteer_detail")
    Call<Ssac> getSsacPictures(@Field("vid") int vid, @Field("uid") String uid);
}
