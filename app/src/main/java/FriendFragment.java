package com.strollingpasta.bingo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import java.security.SecureRandom;

public class FriendFragment extends Fragment {

    public FriendFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // fragment_friend.xml 레이아웃 연결
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button codeBtn = view.findViewById(R.id.code_btn);

        codeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = generateRandomCode();

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("내 코드");
                builder.setMessage("내 친구 코드: " + code);

                builder.setPositiveButton("복사", (dialog, which) -> {
                    ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("code", code);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(requireContext(), "코드가 클립보드에 복사되었습니다", Toast.LENGTH_SHORT).show();
                });

                builder.setNegativeButton("닫기", null);

                builder.show();
            }
        });
    }

    // 랜덤 코드 생성 메서드
    private String generateRandomCode() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int CODE_LENGTH = 6;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }
}