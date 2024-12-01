package com.strollingpasta.bingo;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.strollingpasta.bingo.databinding.ActivityBingoBinding;
import com.strollingpasta.bingo.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class BingoActivity extends AppCompatActivity {

    // 뷰 바인딩
    private ActivityBingoBinding binding;
    FirebaseConnector firebaseConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // 뷰 바인딩
        binding = ActivityBingoBinding.inflate(getLayoutInflater());
        firebaseConnector = new FirebaseConnector();
        settingButtons();
        passToFragment(new BingoBoardFragment());
        setContentView(binding.getRoot());

    }

    public FirebaseConnector getFirebaseConnector() {
        return firebaseConnector;
    }

    protected void settingButtons() {

        // 메뉴 버튼 선언
        ImageButton buttonBack = binding.bingoBtnBack;
        ImageButton buttonSetup = binding.bingoBtnSetup;
        ImageButton buttonReset = binding.bingoBtnReset;

        // 메뉴 버튼에 리스너 연결
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getFragments().get(0) instanceof BingoBoardFragment) {
                    passToActivity(new MainActivity());
                }
                else {
                    passToFragment(new BingoBoardFragment());
                }
            }
        });

        buttonSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 미구현
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            // 경고 다이얼로그 하나 있으면 좋을 것 같은데~
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getFragments().get(0) instanceof BingoBoardFragment) {
                    // 빙고 초기화
                    List<Boolean> list = new ArrayList<>(); // 리스트로 변환
                    for (int i = 0; i < 9; i++) {
                        list.add(false);
                    }

                    // DB에 성공 실패 여부 저장
                    DocumentReference documentReference = firebaseConnector.fillBingoData("nr6eHvz5KDa2sDroaPVY");
                    documentReference.update("bingoCheck", list)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!" + list.toString());
                                    passToFragment(new BingoBoardFragment());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                }
                else {
                    // 모델 연결하고 용도 생각해보기
                }
            }
        });

    }

    public void passToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.bingoFrame.getId(), fragment);
        fragmentTransaction.commit();
    }

    public void passToActivity(Activity activity) {
        Intent intent = new Intent(getApplicationContext(), activity.getClass());
        startActivity(intent);
        finish();
    }

}