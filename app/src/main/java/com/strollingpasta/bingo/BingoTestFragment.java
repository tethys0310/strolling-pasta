package com.strollingpasta.bingo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.strollingpasta.bingo.databinding.FragmentBingoTestBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BingoTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BingoTestFragment extends Fragment {


    // 뷰 바인딩
    private FragmentBingoTestBinding binding;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BingoTestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BingoTestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BingoTestFragment newInstance(String param1, String param2) {
        BingoTestFragment fragment = new BingoTestFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBingoTestBinding.inflate(inflater, container, false);

        // 상위 액티비티 불러오기
        MainActivity activity = (MainActivity) getActivity();

        // 버튼 셋팅
        if (activity != null)
            settingButtons(activity);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // 초기 화면에서 메뉴버튼에 리스너 부여
    protected void settingButtons(MainActivity activity) {

        // 메뉴 버튼 선언
        Button buttonHello = binding.aiBtnHello;
        Button buttonCamera = binding.aiButtonCamera;

        // 메뉴 버튼에 리스너 연결

        buttonHello.setOnClickListener(new View.OnClickListener() { // 안녕~
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Hello from Fragment!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonCamera.setOnClickListener(new View.OnClickListener() { // 카메라 켜기
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "카메라 프래그먼트로 전환 후 거기서 카메라 띄우기", Toast.LENGTH_SHORT).show();
                activity.callCameraFragment(getParentFragmentManager());
            }
        });



    }

}