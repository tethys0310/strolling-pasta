package com.strollingpasta.bingo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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

        Button friend_game_btn = findViewById(R.id.friend_game_btn);
        friend_game_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("LobbyActivity", "Button clicked");
                showDialog();
            }
        });
    }
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LobbyActivity.this);
        builder.setTitle("Hello")
                .setMessage("This is a dialog")
                .setPositiveButton("OK", null)
                .show();
    }
}