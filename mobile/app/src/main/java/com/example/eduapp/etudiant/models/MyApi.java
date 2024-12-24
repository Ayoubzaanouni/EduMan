package com.example.eduapp.etudiant.models;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MyApi {

    // Define API endpoint for file upload
    @Multipart
    @POST("file")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file);

    // You can also define additional endpoints for other API operations
    // ...
}
