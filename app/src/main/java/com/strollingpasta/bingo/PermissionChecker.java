package com.strollingpasta.bingo;

import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class PermissionChecker {

    // 권한 체크하고 부여 담당하는 클래스
    // 타이틀 액티비티랑 카메라 테스트 프레그먼트에서 호출될 예정

    private Context context;
    private Activity activity;
    private static ArrayList<String> permissionList; // 허가 안 난 권한

    private final String[] permissionsAfter13 = { // 티라미수(안드 13) 이상에서 필요한 권한
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET
    };

    private final String[] permissionsBefore13 = { // 티라미수(안드 13) 이하에서 필요한 권한
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET
    };

    public PermissionChecker(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public boolean checkPermission() { // 권한 모두 갖고 있나 체크

        int result;
        permissionList = new ArrayList<>();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // 안드 버전 체크
            for (String permission: permissionsAfter13) { // 13 이상
                result = ContextCompat.checkSelfPermission(context, permission);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
        }
        else {
            for (String permission : permissionsBefore13) { // 13 이하
                result = ContextCompat.checkSelfPermission(context, permission);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
        }
        return permissionList.isEmpty();
    }

    public void requestPermissions() {
        Log.d("TitleActivity", "권한 설정 안 된 목록" + permissionList.toString());
        String[] permissions = permissionList.toArray(new String[0]);
        ActivityCompat.requestPermissions(activity, permissions, 1);
    }

}
