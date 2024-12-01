package com.strollingpasta.bingo;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.resolutionselector.AspectRatioStrategy;
import androidx.camera.core.resolutionselector.ResolutionSelector;
import androidx.camera.core.resolutionselector.ResolutionStrategy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.strollingpasta.bingo.databinding.FragmentBingoBoardBinding;
import com.strollingpasta.bingo.databinding.FragmentBingoCameraBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BingoCameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BingoCameraFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String INDEX = "0";
    private static final String BINGO = "1";
    
    // 뷰 바인딩
    private FragmentBingoCameraBinding binding;

    //카메라X 관련
    private ProcessCameraProvider processCameraProvider;
    private ImageCapture imageCapture;
    private ImageCapture.OutputFileOptions outputFileOptions;
    private ExecutorService cameraExecutor;

    // 액티비티 전환용 상위 액티비티
    BingoActivity activity;
    FirebaseConnector firebaseConnector;

    // TODO: Rename and change types of parameters
    private int index;
    private boolean[] bingo;

    public BingoCameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BingoCameraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BingoCameraFragment newInstance(int index, boolean[] bingo) {
        BingoCameraFragment fragment = new BingoCameraFragment();
        Bundle args = new Bundle();
        args.putInt(INDEX, index);
        args.putBooleanArray(BINGO, bingo);
        Log.d(TAG, "데이터 잘 넣었는지 확인" + index + bingo[index]);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(INDEX);
            bingo = getArguments().getBooleanArray(BINGO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBingoCameraBinding.inflate(inflater, container, false);
        activity = (BingoActivity) getActivity();
        firebaseConnector = activity.getFirebaseConnector();
        settingButtons();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startCamera();
    }

    private void settingButtons() {

        Button buttonCapture = binding.cameraBtnCapture;

        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = getArguments().getInt(INDEX);
                bingo = getArguments().getBooleanArray(BINGO);
                Log.d(TAG, "데이터 전달 확인" + index + bingo[index]);

                takePhoto();

                // 인공지능 모델로 체크

                // 성공 실패 여부 판정
                // 일단은 성공 고정
                bingo[index] = true;

                List<Boolean> list = new ArrayList<>(); // 리스트로 변환
                for (boolean b : bingo) {
                    list.add(b);
                }

                // DB에 성공 실패 여부 저장
                DocumentReference documentReference = firebaseConnector.fillBingoData("nr6eHvz5KDa2sDroaPVY");
                documentReference.update("bingoCheck", list)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "DocumentSnapshot successfully written!" + index + bingo[index]);
                                activity.passToFragment(new BingoBoardFragment());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }
        });

    }

    private void startCamera() { // 카메라X 실행

        cameraExecutor = Executors.newSingleThreadExecutor();

        // 카메라 프로바이더 초기화
        try {
            processCameraProvider = ProcessCameraProvider.getInstance(getActivity()).get();
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        // 뷰파인더 바인딩
        PreviewView viewFinder = binding.cameraViewFinder;
        android.util.Size screenSize = new android.util.Size(720, 720); // 사진 비율
        ResolutionSelector resolutionSelector = new ResolutionSelector.Builder()
                .setResolutionStrategy(new ResolutionStrategy(screenSize, ResolutionStrategy.FALLBACK_RULE_CLOSEST_LOWER))
                .build();

        // 뷰파인더 설정
        viewFinder.setScaleType(PreviewView.ScaleType.FIT_CENTER); //미리보기
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK) // 후면? 전면?
                .build();
        Preview preview = new Preview.Builder()
                .setResolutionSelector(resolutionSelector)
                .build();
        preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

        // 이미지 캡처 설정
        imageCapture = new ImageCapture.Builder()
                .setTargetRotation(getView().getDisplay().getRotation())
                .setResolutionSelector(resolutionSelector)
                .build();


        // 라이프사이클에 바인딩
        processCameraProvider.unbindAll(); // 이전 시행내역 언바인딩
        processCameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);


    }

    private void takePhoto() { // 찰칵

        // 컨텐츠 벨류 설정
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.KOREA).format(System.currentTimeMillis()));
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/StrollingPasta");

        // 아웃풋 파일 옵션 설정
        outputFileOptions = new ImageCapture.OutputFileOptions.Builder(requireContext().getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                .build();

        imageCapture.takePicture(outputFileOptions, cameraExecutor,
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                        String msg = "Photo capture succeeded: " + index;
                        getActivity().runOnUiThread((Runnable) () -> {
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onError(ImageCaptureException error) {
                        // insert your code here.
                        Log.e("CAMERA", "Photo capture failed : ${error.message}", error);
                    }
                }
        );
    }
}