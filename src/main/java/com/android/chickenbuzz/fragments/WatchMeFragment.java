package com.android.chickenbuzz.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.android.chickenbuzz.R;
import com.android.chickenbuzz.activity.BaseActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WatchMeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WatchMeFragment extends Fragment implements View.OnClickListener{

    private BaseActivity mActivity;
    private NumberPicker mMinutesPicker, mSecondsPicker;
    private Button mStartButton;

    public WatchMeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyLocationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WatchMeFragment newInstance(String param1, String param2) {
        WatchMeFragment fragment = new WatchMeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mActivity = ((BaseActivity) getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_watch_me, container, false);

        initUIControls(view);

        return view;
    }

    private void initUIControls(View view) {

        mMinutesPicker = (NumberPicker) view.findViewById(R.id.minutes_picker);
        mSecondsPicker = (NumberPicker) view.findViewById(R.id.seconds_picker);

        mMinutesPicker.setMinValue(0);
        mMinutesPicker.setMaxValue(60);

        mSecondsPicker.setMinValue(00);
        mSecondsPicker.setMaxValue(60);

        mStartButton = (Button) view.findViewById(R.id.btn_start);
        mStartButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_start:

                break;
        }
    }
}
