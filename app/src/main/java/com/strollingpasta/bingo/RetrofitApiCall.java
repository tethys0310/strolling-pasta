package com.strollingpasta.bingo;

import static android.content.ContentValues.TAG;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiCall {

    // 오버패스 호출
    public ArrayList<Boolean> sendQuery(ArrayList<String> queries) {

        ArrayList<Boolean> result = new ArrayList<Boolean>(Arrays.asList(false, false, false));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://overpass-api.de/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitApiService service = retrofit.create(RetrofitApiService.class);
        OverpassResponse answer;
        for (int i = 0; i < queries.size(); i++) {
            int index = i;
            Call<OverpassResponse> call = service.getQuery(queries.get(i));
            try {
                answer = call.execute().body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            boolean hasElement = !answer.getElements().isEmpty(); // 쿼리 기준에 맞는 객체 존재 하는가?
            if (hasElement) { // 객체가 있는 경우
                result.set(index, true);
                Log.d(TAG, "[APICall] index num " + index);
            }
        }
        return result;
    }
}
