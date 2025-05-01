package com.strollingpasta.bingo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;


public class LobbyActivity extends AppCompatActivity {
    Button friend_game_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lobby);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.friend_game_btn, new LobbyFragment())
                .commit();

        friend_game_btn = (Button)findViewById(R.id.friend_game_btn);

        friend_game_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                AlertDialog.Builder menu = new AlertDialog.Builder(LobbyActivity.this);
                menu.setIcon(R.mipmap.ic_launcher);
                menu.setTitle("친구와 함께 게임모드"); // 제목
                menu.setMessage("context"); // 문구

                // 확인버튼
                menu.setPositiveButton("결정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dialog 제거
                        dialog.dismiss();

                    }
                });

                // 취소버튼
                menu.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        // dialog 제거
                        dialog.dismiss();
                    }
                });

                menu.show();
            }
        });
    }

}