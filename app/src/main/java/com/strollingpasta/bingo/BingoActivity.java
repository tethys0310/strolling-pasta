package com.strollingpasta.bingo;

import android.os.Bundle;

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
        setContentView(binding.getRoot());

    }
}