package com.strollingpasta.bingo;

import static android.content.ContentValues.TAG;

import static java.lang.Thread.sleep;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.strollingpasta.bingo.databinding.FragmentBingoBoardBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


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
    /*boolean isStream = false; // 강
    boolean isUniversity = false; // 대학교
    boolean isCity = false; // 도시*/
    ArrayList<Boolean> isRegion = new ArrayList<>();
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

        // 문서 아이디로 문서 유무 확인해서 리전 체크 여부 확인
        firebaseConnector.checkDocumentReference("nr6eHvz5KDa2sDroaPVY", exists -> {
            if (exists) {
                Log.d(TAG, "[checkDocumentReference] 아이디 일치하는 문서 확인");
                //if (progressDialog.isShowing()) progressDialog.dismiss();
                //connectFireBase();
                findRegion();
            } else {
                Log.d(TAG, "[checkDocumentReference] 아이디 일치하는 문서 확인 불가능");
                // 문서 없는 경우 처리
                findRegion();
            }
        });

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
            bingoNaming(i);
            if (dailyBingoListDone.get(i)) {
                bingoColoring(i);
                --dailyBingoListDoneCounter;
            }
        }
        Log.d(TAG, "[bingoInitialize] 빙고 초기화 완료");
        binding.textView.setText("남은 빙고 개수 : " + dailyBingoListDoneCounter);
    }

    // 인덱스 넣으면 빙고 칸 이름 변경
    private void bingoNaming(int index) {

        switch (index) {
            case 0:
                binding.bingoBtn1.setText(bingo.getBingoList().get(index));
                Log.d(TAG, "[bingoNaming] " + bingo.getBingoList().get(index));
                break;
            case 1:
                binding.bingoBtn2.setText(bingo.getBingoList().get(index));
                break;
            case 2:
                binding.bingoBtn3.setText(bingo.getBingoList().get(index));
                break;
            case 3:
                binding.bingoBtn4.setText(bingo.getBingoList().get(index));
                break;
            case 4:
                binding.bingoBtn5.setText(bingo.getBingoList().get(index));
                break;
            case 5:
                binding.bingoBtn6.setText(bingo.getBingoList().get(index));
                break;
            case 6:
                binding.bingoBtn7.setText(bingo.getBingoList().get(index));
                break;
            case 7:
                binding.bingoBtn8.setText(bingo.getBingoList().get(index));
                break;
            case 8:
                binding.bingoBtn9.setText(bingo.getBingoList().get(index));
                break;
            default:
                break;
        }
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
       new Thread(new Runnable() { // 동기식으로 작동, 스레드 분리
            @Override
            public void run() {
                RetrofitApiCall apiCall = new RetrofitApiCall();
                isRegion = apiCall.sendQuery(queries);

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
        String query3 = String.format("[out:json];node[\"place\"=\"borough\"](around:5000, %f, %f);out;", lat, lon); // 도시(구)
        ArrayList<String> queries = new ArrayList<String>();
        queries.add(query1);
        queries.add(query2);
        queries.add(query3);

        return queries;
    }

    private void connectFireBase() {

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("빙고 생성중...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Log.d(TAG, "[ConnectFireBase] isRegion: " + isRegion.toString());

        DocumentReference documentReference = firebaseConnector.getDocumentReference("nr6eHvz5KDa2sDroaPVY");
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    if (document.get("bingoObject") == null) {
                        // bingoObject 없으면 새로 만들고 그 후 처리
                        makeNewBingo(() -> {
                            // bingoObject 채운 후 나머지 처리
                            Log.d(TAG, "[connectFirebase] 콜백 완료");
                            bingo.setId("nr6eHvz5KDa2sDroaPVY");
                            Timestamp tmp = (Timestamp) document.getData().get("date");
                            bingo.setDate(tmp.toDate());
                            bingo.setBingoList(bingo.getBingoList()); // 이미 만들어졌음
                            documentReference.update("bingoObject", bingo.getBingoList());

                            bingo.setBingoListDone(new ArrayList<>(Arrays.asList(
                                    false, false, false, false, false, false, false, false, false)));
                            documentReference.update("bingoCheck", bingo.getBingoListDone());

                            Log.d(TAG, bingo.toString());
                            bingoInitialize(bingo);
                            if (progressDialog.isShowing()) progressDialog.dismiss();
                        });
                    } else {
                        // 기존 값 있을 경우 바로 사용
                        bingo.setId("nr6eHvz5KDa2sDroaPVY");
                        Timestamp tmp = (Timestamp) document.getData().get("date");
                        bingo.setDate(tmp.toDate());
                        bingo.setBingoList((ArrayList<String>) document.getData().get("bingoObject"));
                        bingo.setBingoListDone((ArrayList<Boolean>) document.getData().get("bingoCheck"));
                        Log.d(TAG, "[connectFirebase] " + bingo.toString());
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        bingoInitialize(bingo);
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    private void makeNewBingo(Runnable onComplete) {

        Random random = new Random();
        AtomicInteger count = new AtomicInteger(0);
        Log.d(TAG, "[makeNewBingo] 호출됨");

        while (count.get() < 30) { // 30번까지만 시도
            Log.d(TAG, "[makeNewBingo] 반복 " + count + "회");
            DocumentReference docRef = firebaseConnector.getDocumentReferenceObjects(Integer.toString(random.nextInt(20)));
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "[makeNewBingo] 반복 " + count + "회 문서호출 성공 : " + document.getString("name_kr"));
                    if (!isRegion.get(0)) { //stream
                        if (Boolean.TRUE.equals(document.getBoolean("river"))) return;
                    }
                    if (!isRegion.get(1)) { //city
                        if (Boolean.TRUE.equals(document.getBoolean("city"))) return;
                    }
                    if (!isRegion.get(2)) { //university
                        if (Boolean.TRUE.equals(document.getBoolean("school"))) return;
                    }
                    bingo.getBingoList().add(document.getString("name_kr"));
                    synchronized (bingo) {
                        if (bingo.getBingoList().size() > 9) {
                            do {
                                bingo.getBingoList().remove(bingo.getBingoList().size()-1);
                            } while(bingo.getBingoList().size() == 10);
                            onComplete.run(); // 9개 다 모이면 콜백 실행
                            Log.d(TAG, "[makeNewBingo] 콜백 날림!");
                        }
                    }
                }
            });

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count.incrementAndGet();
        }
    }
}