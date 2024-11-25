package com.strollingpasta.bingo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraExecutor;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.resolutionselector.ResolutionSelector;
import androidx.camera.core.resolutionselector.ResolutionStrategy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.OutputOptions;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.strollingpasta.bingo.databinding.FragmentBingoTestBinding;
import com.strollingpasta.bingo.databinding.FragmentCameraTestBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraTestFragment extends Fragment {

    // 여기에서 인공지능 모델 돌리기도 해야 할 듯?


    // 뷰 바인딩
    private FragmentCameraTestBinding binding;
    private ProcessCameraProvider processCameraProvider;
    private ImageCapture imageCapture;
    private ImageCapture.OutputFileOptions outputFileOptions;
    private ExecutorService cameraExecutor;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CameraTestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CameraTestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CameraTestFragment newInstance(String param1, String param2) {
        CameraTestFragment fragment = new CameraTestFragment();

        ImageCapture imageCapture;

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCameraTestBinding.inflate(inflater, container, false);
        settingButtons();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startCamera();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cameraExecutor.shutdown();
        binding = null;
    }

    protected void settingButtons() {
        Button buttonCapture = binding.cameraTestBtnCapture;

        // 메뉴 버튼에 리스너 연결

        buttonCapture.setOnClickListener(new View.OnClickListener() { // 찰칵
            @Override
            public void onClick(View view) {
                takePhoto();
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
        PreviewView viewFinder = binding.cameraTestViewFinder;
        android.util.Size screenSize = new android.util.Size(720, 720); // 사진 비율
        ResolutionSelector resolutionSelector = new ResolutionSelector.Builder()
                .setResolutionStrategy(new ResolutionStrategy(screenSize, ResolutionStrategy.FALLBACK_RULE_NONE))
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

        imageCapture = new ImageCapture.Builder()
                .setTargetRotation(getView().getDisplay().getRotation())
                .setResolutionSelector(resolutionSelector)
                .build();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.KOREA).format(System.currentTimeMillis()));
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            String appName = requireContext().getString(R.string.app_name);
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/${appName}");
        }


        outputFileOptions = new ImageCapture.OutputFileOptions.Builder(requireContext().getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new ContentValues())
                .build();

        
        // 라이프사이클에 바인딩
        processCameraProvider.unbindAll(); // 이전 시행내역 언바인딩
        processCameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);


    }

    private void takePhoto() {

        imageCapture.takePicture(outputFileOptions, cameraExecutor,
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                        // insert your code here.
                        String msg = "Photo capture succeeded: " + outputFileResults.getSavedUri();
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