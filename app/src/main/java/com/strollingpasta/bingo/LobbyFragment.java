package com.strollingpasta.bingo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.strollingpasta.bingo.databinding.FragmentCameraTestBinding;
import com.strollingpasta.bingo.databinding.FragmentLobbyBinding;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LobbyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LobbyFragment extends Fragment {

    // 뷰 바인딩
    FragmentLobbyBinding binding;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LobbyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LobbyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LobbyFragment newInstance(String param1, String param2) {
        LobbyFragment fragment = new LobbyFragment();
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
        binding = FragmentLobbyBinding.inflate(inflater, container, false);

        // 상위 액티비티 불러오기
        MainActivity activity = (MainActivity) getActivity();

        settingButtons(activity);
        getDT();

        return binding.getRoot();

    }

    private void settingButtons(MainActivity activity) {

        Button buttonStart = binding.lobbyBtnStart;
        Button buttonFriendMode = binding.friendGameBtn;

        // 메뉴 버튼에 리스너 연결
        buttonStart.setOnClickListener(new View.OnClickListener() { // 찰칵
            @Override
            public void onClick(View view) {
                activity.passToActivity(new BingoActivity());
            }
        });

        buttonFriendMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 선택지 목록
                String[] options = {"협동 모드", "경쟁 모드"};

                // 기본 선택 인덱스 (예: 첫 번째 항목)
                final int[] selectedIndex = {0};

                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("친구모드 선택")
                        .setSingleChoiceItems(options, selectedIndex[0], (dialog, which) -> {
                            selectedIndex[0] = which; // 선택된 항목 저장
                        })
                        .setPositiveButton("시작", (dialog, which) -> {
                            // 선택된 항목 처리
                            MainActivity activity = (MainActivity) getActivity();
                            if (activity != null) {
                                activity.passToActivity(new BingoActivity());
                            }

                        })
                        .setNegativeButton("취소", null)
                        .show();
            }
        });
    }


    public void getDT() {

        TextView textView = binding.lobbyTextDt;
        Calendar cal = Calendar.getInstance();
        int y, m, d, h, mi;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 +1
        d = cal.get(Calendar.DAY_OF_MONTH);
        h = cal.get(Calendar.HOUR_OF_DAY); // 24시간제
        mi = cal.get(Calendar.MINUTE);

        textView.setText(y + "년 " + m + "월 " + d + "일" );
    }


}

