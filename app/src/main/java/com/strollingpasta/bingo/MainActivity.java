package com.strollingpasta.bingo;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.strollingpasta.bingo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // 뷰 바인딩
    private ActivityMainBinding binding;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 뷰 바인딩
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        passToFragment(new LobbyFragment());

        settingButtons();
    }

    // 초기 화면에서 메뉴버튼에 리스너 부여
    protected void settingButtons() {

        // 메뉴 버튼 선언
        ImageButton buttonBook = binding.mainBtnBook;
        ImageButton buttonWeek = binding.mainBtnWeek;
        ImageButton buttonArchive = binding.mainBtnArchive;

        // 메뉴 버튼에 리스너 연결
        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "미구현", Toast.LENGTH_SHORT).show();
                passToFragment(new LobbyFragment());
            }
        });
        buttonBook.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getApplicationContext(), "디버그 모드", Toast.LENGTH_SHORT).show();
                passToFragment(new DebugTestFragment());
                return false;
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

    //gps
    public LocationManager getLocationManager() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager;
    }

}