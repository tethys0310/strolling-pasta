package com.strollingpasta.bingo;

import static android.content.ContentValues.TAG;

import static java.lang.Thread.sleep;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.strollingpasta.bingo.databinding.FragmentBingoBoardBinding;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BingoBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BingoBoardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentBingoBoardBinding binding;
    // 리전 GPS
    LocationManager locationManager;
    boolean isStream = false; // 강
    boolean isUniversity = false; // 대학교
    boolean isCity = false; // 도시
    // 빙고 객체와 확인 여부
    final Bingo bingo = new Bingo();
    FirebaseConnector firebaseConnector;
    ArrayList<String> dailyBingoList = new ArrayList<>();
    ArrayList<Boolean> dailyBingoListDone = new ArrayList<>();
    int dailyBingoListDoneCounter = 9;
    ProgressDialog progressDialog;
    // 액티비티
    BingoActivity activity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BingoBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BingoBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BingoBoardFragment newInstance(String param1, String param2) {
        BingoBoardFragment fragment = new BingoBoardFragment();
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
        activity = (BingoActivity) getActivity();
        locationManager = activity.getLocationManager();
        firebaseConnector = activity.getFirebaseConnector();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBingoBoardBinding.inflate(inflater, container, false);

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("로딩중");
        progressDialog.setCancelable(false);
        progressDialog.show();

        findRegion();

        settingButtons();

        return binding.getRoot();
    }

    private void settingButtons() {
        Button button1 = binding.bingoBtn1;
        Button button2 = binding.bingoBtn2;
        Button button3 = binding.bingoBtn3;
        Button button4 = binding.bingoBtn4;
        Button button5 = binding.bingoBtn5;
        Button button6 = binding.bingoBtn6;
        Button button7 = binding.bingoBtn7;
        Button button8 = binding.bingoBtn8;
        Button button9 = binding.bingoBtn9;

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoCheck(0);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoCheck(1);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoCheck(2);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoCheck(3);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoCheck(4);
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoCheck(5);
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoCheck(6);
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoCheck(7);
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoCheck(8);
            }
        });
    }

    private void bingoCheck(int index) {

        //boolean[]형으로 변환해서 전달
        boolean[] array = new boolean[9];
        for (int i = 0; i < dailyBingoListDone.size(); i++) {
            array[i] = dailyBingoListDone.get(i);
        }

        // 프래그먼트 넘기기...?
        if (dailyBingoListDone.get(index)) {
            getActivity().runOnUiThread((Runnable) () -> {
                Toast.makeText(getContext(), "이미 체크한 빙고입니다.", Toast.LENGTH_SHORT).show();
            });
            return;
        }
        activity.passToFragment(BingoCameraFragment.newInstance(index, array));
    }


    @Override
    public void onStart() {
        super.onStart();
        bingoInitialize(bingo);
    }

    private void bingoInitialize(Bingo bingo) { // 빙고에서 가져온 데이터 맞춰 빙고판 설정

        // 초기화
        dailyBingoList = bingo.getBingoList();
        dailyBingoListDone = bingo.getBingoListDone();

        // 빙고 성공 여부 따라서 빙고판 채색
        for (int i = 0; i < dailyBingoListDone.size(); i++) {
            if (dailyBingoListDone.get(i)) {
                bingoColoring(i);
                --dailyBingoListDoneCounter;
            }
        }
        binding.textView.setText("남은 빙고 개수 : " + dailyBingoListDoneCounter);
    }

    // 인덱스 넣으면 빙고 칸 색칠
    private void bingoColoring(int index) {

        switch (index) {
            case 0:
                binding.bingoBtn1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bingo_done));
                break;
            case 1:
                binding.bingoBtn2.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bingo_done));
                break;
            case 2:
                binding.bingoBtn3.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bingo_done));
                break;
            case 3:
                binding.bingoBtn4.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bingo_done));
                break;
            case 4:
                binding.bingoBtn5.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bingo_done));
                break;
            case 5:
                binding.bingoBtn6.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bingo_done));
                break;
            case 6:
                binding.bingoBtn7.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bingo_done));
                break;
            case 7:
                binding.bingoBtn8.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bingo_done));
                break;
            case 8:
                binding.bingoBtn9.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bingo_done));
                break;
            default:
                break;
        }
    }

    // gps로 위치 추적, overpass api 접근해서 쿼리 얻기
    private void findRegion() {

        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                ArrayList<String> queries = querySetting(lat, lon);
                Log.d(TAG, queries.get(0));

                getApiCall(queries);
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
    }

    // 오버패스 호출
    public void getApiCall(ArrayList<String> queries) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://overpass-api.de/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        RetrofitApiService service = retrofit.create(RetrofitApiService.class);

        new Thread(new Runnable() { // 동기식으로 작동, 스레드 분리
            @Override
            public void run() {
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
                        switch (index) {
                            case 0:
                                isStream = true;
                                Log.d(TAG, "APICall: isStream True");
                                break;
                            case 1:
                                isUniversity = true;
                                Log.d(TAG, "APICall: isUniversity True");
                                break;
                            case 2:
                                isCity = true;
                                Log.d(TAG, "APICall: isCity True");
                                break;
                            default:
                                break;
                        }
                    }
                }
                activity.runOnUiThread(() -> {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    connectFireBase();
                });
            }
        }).start();
    }

    private ArrayList<String> querySetting(double lat, double lon) {
        String query1 = String.format("[out:json];way[\"waterway\"=\"stream\"](around:500, %f, %f);out;", lat, lon); // 강
        String query2 = String.format("[out:json];node[\"amenity\"=\"university\"](around:300, %f, %f);out;", lat, lon); // 학교
        String query3 = String.format("[out:json];way[\"waterway\"=\"stream\"](around:500, %f, %f);out;", lat, lon); // 도시
        ArrayList<String> queries = new ArrayList<String>();
        queries.add(query1);
        queries.add(query2);
        queries.add(query3);

        return queries;
    }

    private void connectFireBase() {
        // 리전 체크
        Log.d(TAG, "[ConnectFireBase] isStream: " + String.valueOf(isStream) + ", isCity: " + String.valueOf(isCity) + ", isUniversity: " + String.valueOf(isUniversity));

        // 원래는 날짜 체크 후 진행
        // 비동기식 구성이라 일단 꺼내놨는데 나중에 조금 손 볼 듯...
        DocumentReference documentReference = firebaseConnector.fillBingoData("nr6eHvz5KDa2sDroaPVY");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) { // 파이어베이스 연동 성공하면
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        bingo.setId("nr6eHvz5KDa2sDroaPVY");
                        // 타임스탬프 date로 변환
                        Timestamp tmp = (Timestamp) document.getData().get("date");
                        bingo.setDate(tmp.toDate());
                        // 빙고 관련 정보들
                        bingo.setBingoList((ArrayList<String>) document.getData().get("bingoObject"));
                        bingo.setBingoListDone((ArrayList<Boolean>) document.getData().get("bingoCheck"));
                        Log.d(TAG, bingo.toString());
                        bingoInitialize(bingo);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}