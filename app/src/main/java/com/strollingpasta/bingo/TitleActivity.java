package com.strollingpasta.bingo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.strollingpasta.bingo.databinding.ActivityMainBinding;
import com.strollingpasta.bingo.databinding.ActivityTitleBinding;

public class TitleActivity extends AppCompatActivity {

    // 최초 실행시 보이는 타이틀 화면
    // 여기서 권한 설정, 구글 로그인 등등 다이얼 로그로...

    private ActivityTitleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTitleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        settingButtons();
    }

    protected void settingButtons() {

        // 버튼 선언
        Button buttonSetup = binding.titleBtnSetup;
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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}