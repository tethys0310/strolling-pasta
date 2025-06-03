package com.strollingpasta.bingo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApiService {
    @GET("interpreter")
    Call<OverpassResponse> getQuery(@Query("data") String query);
}
