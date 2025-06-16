package com.strollingpasta.bingo;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.strollingpasta.bingo.databinding.FragmentDebugTestBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DebugTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DebugTestFragment extends Fragment {


    // 뷰 바인딩
    private FragmentDebugTestBinding binding;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DebugTestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BingoTestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DebugTestFragment newInstance(String param1, String param2) {
        DebugTestFragment fragment = new DebugTestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDebugTestBinding.inflate(inflater, container, false);

        // 상위 액티비티 불러오기
        MainActivity activity = (MainActivity) getActivity();

        // 버튼 셋팅
        if (activity != null)
            settingButtons(activity);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // 초기 화면에서 메뉴버튼에 리스너 부여
    protected void settingButtons(MainActivity activity) {

        // 메뉴 버튼 선언
        Button buttonHello = binding.aiBtnHello;
        Button buttonCamera = binding.aiButtonCamera;
        Button buttonDB = binding.aiButtonDbconnect;
        Button buttonGPS = binding.aiButtonGps;
        Button buttonOverpass = binding.aiButtonOverpass;

        // 메뉴 버튼에 리스너 연결
        buttonHello.setOnClickListener(new View.OnClickListener() { // 안녕~
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Hello from Fragment!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonCamera.setOnClickListener(new View.OnClickListener() { // 카메라 켜기
            @Override
            public void onClick(View view) {
                // 카메라 프래그먼트로 전환 후 거기서 카메라 띄우기
                activity.passToFragment(new CameraTestFragment());
            }
        });

        buttonDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseConnector firebaseConnector = new FirebaseConnector();
                firebaseConnector.ConnectingTest();
                Toast.makeText(activity, "로그캣 확인하세요", Toast.LENGTH_SHORT).show();
            }
        });

        buttonGPS.setOnClickListener(view -> {
            LocationManager locationManager = activity.getLocationManager();

            if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    Toast.makeText(activity, "위도: " + lat + ", 경도: " + lon, Toast.LENGTH_SHORT).show();

                    // 위치 1회 수신 후 더 이상 수신하지 않도록 제거
                    locationManager.removeUpdates(this);
                }
            };


            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,      // 최소 시간
                    0,      // 최소 거리
                    locationListener
            );
        });

        buttonOverpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager locationManager = activity.getLocationManager();

                if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        String query = String.format("[out:json];way[\"waterway\"=\"stream\"](around:10000, %f, %f);out;", lat, lon);

                        Log.d(TAG, query);

                        //오버패스 호출?
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://overpass-api.de/api/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        RetrofitApiService service = retrofit.create(RetrofitApiService.class);

                        Call<OverpassResponse> call = service.getQuery(query);

                        call.enqueue(new Callback<OverpassResponse>() { // 비동기?
                            @Override
                            public void onResponse(Call<OverpassResponse> call, Response<OverpassResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    OverpassResponse answer = response.body(); // 답변 객체
                                    boolean hasStream = !answer.getElements().isEmpty(); // 쿼리 기준에 맞는 객체 존재 하는가?

                                    if (hasStream) { // Stream 있는 경우
                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        builder.setMessage(answer.printNames())
                                                .setTitle("근처에 하천 있음");
                                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // User taps OK button.
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    } else {
                                        // Stream 없는 경우 ... 범위가 넓어서 안 잡히면 GPS 오류
                                        Toast.makeText(activity, "근처에 하천이 없음", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(activity, "Response 존재 X", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<OverpassResponse> call, Throwable t) {
                                t.printStackTrace();
                                Toast.makeText(activity, "네트워크 오류", Toast.LENGTH_SHORT).show();
                            }
                        });

                        locationManager.removeUpdates(this);
                    }
                };

                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        0,      // 최소 시간
                        0,      // 최소 거리
                        locationListener
                );
            }
        });



    }

}