package com.strollingpasta.bingo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.credentials.GetCredentialRequest;
import android.os.Build;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.strollingpasta.bingo.databinding.ActivityMainBinding;
import com.strollingpasta.bingo.databinding.ActivityTitleBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class TitleActivity extends AppCompatActivity {

    // 최초 실행시 보이는 타이틀 화면
    // 여기서 권한 설정, 구글 로그인 등등 다이얼 로그로...

    private PermissionChecker permissionChecker;
    private ActivityTitleBinding binding;
    private FirebaseConnector firebaseConnector;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionChecker = new PermissionChecker(this, this);

        // 뷰 바인드
        binding = ActivityTitleBinding.inflate(getLayoutInflater());

        // 파이어베이스 초기화
        firebaseAuth = FirebaseAuth.getInstance();

        settingButtons();

        if (!permissionChecker.checkPermission())
            permissionChecker.requestPermissions();

        setContentView(binding.getRoot());
    }

    private void googleSignin() {
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setAutoSelectEnabled(true)
                .build();
    }

    protected void settingButtons() {

        // 버튼 선언
        ImageButton buttonSetup = binding.titleBtnSetup;
        Button buttonStart = binding.titleBtnStart;

        // 버튼에 리스너 연결
        buttonSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 다이얼로그로 볼륨 / 진동 / 로그인 / 등등등등
                Toast.makeText(getApplicationContext(), "설정 미구현", Toast.LENGTH_SHORT).show();
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseConnector = new FirebaseConnector();
                firebaseConnector.ConnectingTest();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("Log", "permission granted");
        }
        else {
            Toast.makeText(getApplicationContext(), "권한 설정 실패", Toast.LENGTH_SHORT).show();
            Log.d("Log", "permission denied");
        }

    }

}