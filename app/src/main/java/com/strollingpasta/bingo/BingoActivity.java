package com.strollingpasta.bingo;

import android.app.Activity;
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

import com.strollingpasta.bingo.databinding.ActivityBingoBinding;
import com.strollingpasta.bingo.databinding.ActivityMainBinding;

public class BingoActivity extends AppCompatActivity {

    // 뷰 바인딩
    private ActivityBingoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // 뷰 바인딩
        binding = ActivityBingoBinding.inflate(getLayoutInflater());
        settingButtons();

        setContentView(binding.getRoot());

    }

    protected void settingButtons() {

        // 메뉴 버튼 선언
        Button buttonBack = binding.bingoBtnBack;


        // 메뉴 버튼에 리스너 연결
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passToActivity(new MainActivity());
            }
        });

    }

    public void passToActivity(Activity activity) {
        Intent intent = new Intent(getApplicationContext(), activity.getClass());
        startActivity(intent);
        finish();
    }
}