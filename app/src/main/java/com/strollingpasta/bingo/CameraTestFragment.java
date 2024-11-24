package com.strollingpasta.bingo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strollingpasta.bingo.databinding.FragmentBingoTestBinding;
import com.strollingpasta.bingo.databinding.FragmentCameraTestBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraTestFragment extends Fragment {

    // 여기에서 인공지능 모델 돌리기도 해야 할 듯?


    // 뷰 바인딩
    private FragmentCameraTestBinding binding;

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
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}