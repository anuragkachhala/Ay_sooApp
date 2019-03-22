package com.ay_sooapp.RestApi;

import com.ay_sooapp.Model.RegistrationData;
import com.ay_sooapp.Model.User;
import com.ay_sooapp.Request.CreateAlertRequest;
import com.ay_sooapp.Request.DeleteAlertRequest;
import com.ay_sooapp.Request.ForgotPasswordRequest;
import com.ay_sooapp.Request.UpdateProfileRequest;
import com.ay_sooapp.Response.AlertDataResponse;
import com.ay_sooapp.Response.AlertDetailResponse;
import com.ay_sooapp.Response.LoginResponse;
import com.ay_sooapp.Response.RegistrationResponse;
import com.ay_sooapp.Response.ResponseBody;
import com.ay_sooapp.Response.UserProfileResponse;
import com.ay_sooapp.Response.WebSiteDetailsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ApiInterface {


    @Headers("Content-Type: application/json")
    @POST("o/v1/login")
    Call<LoginResponse> authenticateUser(@Body User user);


    @Headers("Content-Type: application/json")
    @POST("o/v1/user/registration")
    Call<RegistrationResponse> registerUser(@Body RegistrationData registrationData);


    @Headers("Content-Type: application/json")
    @POST("o/v1/forgotPassword")
    Call<ResponseBody> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);


    @POST("v1/user/alert")
    Call<ResponseBody> createAlert(@HeaderMap Map<String, String> headers, @Body CreateAlertRequest createAlertRequest);

    @GET("v1/website")
    Call<WebSiteDetailsResponse> getWebSiteDetails(@HeaderMap Map<String, String> headers);


    @GET("v1/user/profile")
    Call<UserProfileResponse> getUserProfileDetails(@HeaderMap Map<String, String> headers);


    @PATCH("v1/user/profile")
    Call<ResponseBody> updateUserProfileDetails(@HeaderMap Map<String, String> headers, @Body UpdateProfileRequest updateProfileRequest);


    @GET("v1/user/alert")
    Call<AlertDataResponse> getAllAlerts(@HeaderMap Map<String, String> headers);

    @GET("v1/user/alert/{id}/price")
    Call<AlertDetailResponse> getAlert(@HeaderMap Map<String, String> headers, @Path("id") long id);

    @POST("v1/current/{id}/price")
    Call<ResponseBody> getCurrentPrice(@HeaderMap Map<String, String> headers, @Path("id") int alertId);

    @PATCH("v1/user/alert")
    Call<ResponseBody> deleteAlert(@HeaderMap Map<String, String> headers, @Body DeleteAlertRequest deleteAlertRequest);

}
