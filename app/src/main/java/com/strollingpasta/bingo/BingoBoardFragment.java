package com.strollingpasta.bingo;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.strollingpasta.bingo.databinding.FragmentBingoBoardBinding;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BingoBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BingoBoardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentBingoBoardBinding binding;
    // 빙고 객체와 확인 여부
    ArrayList<String> dailyBingoList = new ArrayList<>();
    ArrayList<Boolean> dailyBingoListDone = new ArrayList<>();
    // 액티비티
    BingoActivity activity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BingoBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BingoBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BingoBoardFragment newInstance(String param1, String param2) {
        BingoBoardFragment fragment = new BingoBoardFragment();
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
        activity = (BingoActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBingoBoardBinding.inflate(inflater, container, false);
        // 나중에 DB 연동 할 때는 클래스 따로 뽑아서 구현
        // 날짜 체크하고 이니탈
        bingoInitialize();
        settingButtons();
        return binding.getRoot();
    }


    private void bingoInitialize() { // 하루 최초 시작 시

        // 초기화
        dailyBingoList = new ArrayList<>();
        dailyBingoListDone = new ArrayList<>();

        // 나중엔 temp를 DB에서 불러오게 될 듯
        // 랜덤으로 돌려서? 일단 갖고 온 건 랜덤으로 가져온 결과라고 치고...
        ArrayList<String> temp = new ArrayList<>(Arrays.asList("고양이", "개", "나무", "새", "검은 옷 사람", "파란색 표지판", "파란색 버스", "초록색 버스", "교회"));

        // 리스트에 넣기
        for (String st : temp) {
            dailyBingoList.add(st);
            dailyBingoListDone.add(false);
        }

        Log.d("Log", dailyBingoList.toString());
        Log.d("Log", dailyBingoListDone.toString());
    }


    private void bingoColoring(int index) {

        if (dailyBingoListDone.get(index)) {
            getActivity().runOnUiThread((Runnable) () -> {
                Toast.makeText(getContext(), "이미 체크한 빙고입니다.", Toast.LENGTH_SHORT).show();
            });
            return;
        }

        dailyBingoListDone.set(index, true);
        switch (index) {
            case 0 :
                if (bingoCheck(dailyBingoList.get(index)))
                    binding.bingoBtn1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bingo_done));
                break;
            case 1 :
                if (bingoCheck(dailyBingoList.get(index)))
                    binding.bingoBtn2.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bingo_done));
                break;
            case 2 :
                if (bingoCheck(dailyBingoList.get(index)))
                    binding.bingoBtn3.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bingo_done));
                break;
            case 3 :
                if (bingoCheck(dailyBingoList.get(index)))
                    binding.bingoBtn4.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bingo_done));
                break;
            case 4 :
                if (bingoCheck(dailyBingoList.get(index)))
                    binding.bingoBtn5.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bingo_done));
                break;
            case 5 :
                if (bingoCheck(dailyBingoList.get(index)))
                    binding.bingoBtn6.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bingo_done));
                break;
            case 6 :
                if (bingoCheck(dailyBingoList.get(index)))
                    binding.bingoBtn7.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bingo_done));
                break;
            case 7 :
                if (bingoCheck(dailyBingoList.get(index)))
                    binding.bingoBtn8.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bingo_done));
                break;
            case 8 :
                if (bingoCheck(dailyBingoList.get(index)))
                    binding.bingoBtn9.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bingo_done));
                break;
            default:
                break;
        }
    }
    
    private boolean bingoCheck(String object) {
        // 프래그먼트 넘기고,
        // 거기서 pass fail 정보 받아서 다시 리턴
        // 일단은...
        activity.passToFragment(BingoCameraFragment.newInstance(object));
        return true;
    }


    private void settingButtons() {
        Button button1 = binding.bingoBtn1;
        Button button2 = binding.bingoBtn2;
        Button button3 = binding.bingoBtn3;
        Button button4 = binding.bingoBtn4;
        Button button5 = binding.bingoBtn5;
        Button button6 = binding.bingoBtn6;
        Button button7 = binding.bingoBtn7;
        Button button8 = binding.bingoBtn8;
        Button button9 = binding.bingoBtn9;

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoColoring(0);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoColoring(1);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoColoring(2);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoColoring(3);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoColoring(4);
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoColoring(5);
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoColoring(6);
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoColoring(7);
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bingoColoring(8);
            }
        });
    }
}