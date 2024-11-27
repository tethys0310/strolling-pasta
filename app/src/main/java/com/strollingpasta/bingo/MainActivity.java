package com.strollingpasta.bingo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.strollingpasta.bingo.databinding.ActivityMainBinding;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // 뷰 바인딩
    private ActivityMainBinding binding;

    // 텍스트 뷰 (날짜)
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 뷰 바인딩
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        settingButtons();

        textView = findViewById(R.id.textView);

        getDT();
    }

    // 초기 화면에서 메뉴버튼에 리스너 부여
    protected void settingButtons() {

        // 메뉴 버튼 선언
        Button buttonBook = binding.mainBtnBook;
        Button buttonStart = binding.button;
        Button buttonWeek = binding.mainBtnWeek;
        Button buttonArchive = binding.mainBtnArchive;

        // 메뉴 버튼에 리스너 연결
        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passToFragment(new BingoTestFragment());
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passToActivity(new BingoActivity());
            }
        });

        buttonWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "미구현", Toast.LENGTH_SHORT).show();
            }
        });

        buttonArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "미구현", Toast.LENGTH_SHORT).show();
            }
        });

    }


    // 메인에서 프래그먼트 호출
    public void passToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.mainFrame.getId(), fragment);
        fragmentTransaction.commit();
    }

    // 메인에서 액티비티 호출
    public void passToActivity(Activity activity) {
        Intent intent = new Intent(getApplicationContext(), activity.getClass());
        startActivity(intent);
        finish();
    }

    public void getDT() {
        Calendar cal = Calendar.getInstance();
        int y, m, d, h, mi, s;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 +1
        d = cal.get(Calendar.DAY_OF_MONTH);
        h = cal.get(Calendar.HOUR_OF_DAY); // 24시간제
        mi = cal.get(Calendar.MINUTE);
        s = cal.get(Calendar.SECOND);

        textView.setText(y + "년" + m + "월" + d + "일" );
    }
}