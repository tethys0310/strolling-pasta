package com.strollingpasta.bingo;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FirebaseConnector {

    FirebaseFirestore db;
    public DocumentReference documentReference;

    FirebaseConnector() {

        db = FirebaseFirestore.getInstance();

    }
    
    // id 입력하면 id에 맞는 빙고 정보 가지고 올 수 있게...
    public DocumentReference fillBingoData(String id) {
        documentReference = db.collection("bingo").document(id);
        return documentReference;
    }

    /*
    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        bingo.setId(id);
                        // 타임스탬프 date로 변환
                        Timestamp tmp = (Timestamp)document.getData().get("date");
                        bingo.setDate(tmp.toDate());
                        // 빙고 관련 정보들
                        bingo.setBingoList((ArrayList<String>) document.getData().get("bingoObject"));
                        bingo.setBingoListDone((ArrayList<Boolean>) document.getData().get("bingoCheck"));
                        Log.d(TAG, bingo.toString());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
     */


    // bingoObject 정보 출력하는 테스트성 코드
    public void ConnectingTest() {
        documentReference = db.collection("bingoObject").document("bingoObject");

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData() + ", id: " + document.getId());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


}
