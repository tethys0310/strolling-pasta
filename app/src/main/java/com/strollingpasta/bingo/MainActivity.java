package com.strollingpasta.bingo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.strollingpasta.bingo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // 뷰 바인딩
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 뷰 바인딩
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 프래그먼트
        FragmentManager fragmentManager = getSupportFragmentManager();
        
        settingButtons(fragmentManager);
    }

    // 초기 화면에서 메뉴버튼에 리스너 부여
    protected void settingButtons(FragmentManager fragmentManager) {

        // 메뉴 버튼 선언
        Button buttonTest = binding.mainBtnTest;

        // 메뉴 버튼에 리스너 연결
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(binding.mainFrame.getId(), new BingoTestFragment());
                fragmentTransaction.commit();
            }
        });

    }

    // 프래그먼트 내부에서 카메라 프래그먼트 호출하는 용도
    public void callCameraFragment(@NonNull FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.mainFrame.getId(), new CameraTestFragment());
        fragmentTransaction.commit();
    }

}