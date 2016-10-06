package com.android.chickenbuzz.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.chickenbuzz.R;
import com.android.chickenbuzz.activity.BaseActivity;
import com.android.chickenbuzz.activity.FollowYouActivity;
import com.android.chickenbuzz.activity.SendingInviteActivity;
import com.android.chickenbuzz.beans.FollowYouUser;
import com.android.chickenbuzz.componenets.CustomSpinnerAdapter;
import com.android.chickenbuzz.componenets.DividerItemDecoration;
import com.android.chickenbuzz.global.ChickenBuzzApplication;
import com.android.chickenbuzz.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowYouFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowYouFragment extends Fragment implements View.OnClickListener{

    private BaseActivity mActivity;

    private Spinner mFolloYouSpinner;
    private CustomSpinnerAdapter mFollowYouAdapter;
    private String[] mFollowYouStrings;

    private RecyclerView mRecyclerView;
    private FollowYouAdapter mFollowMeAdapter;
    private List<FollowYouUser> mFollowYouList = new ArrayList<>();

    public FollowYouFragment() {
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
    public static FollowYouFragment newInstance(String param1, String param2) {
        FollowYouFragment fragment = new FollowYouFragment();
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
        View view=inflater.inflate(R.layout.activity_follow_you, container, false);

        initUIControls(view);

        return view;
    }

    private void initUIControls(View view) {
        mFolloYouSpinner = (Spinner) view.findViewById(R.id.follow_you_spinner);
        mFollowYouStrings = new String[]{"All", "Follow You 1","Follow You 2","Follow You 3","Follow You 4"};
        CustomSpinnerAdapter mspinnerAdapter = new CustomSpinnerAdapter(mActivity, mFollowYouStrings);
        mFolloYouSpinner.setAdapter(mspinnerAdapter);

        Button nextButton = (Button) view.findViewById(R.id.btn_next);
        if(((ChickenBuzzApplication)mActivity.getApplication()).getmUserAboutDetails() == null &&
                !Constant.IS_HOME_SCREEN_DISPLAYED) {
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setOnClickListener(this);
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

//        String[] settingslist = getResources().getStringArray(R.array.settings_array);

        mFollowMeAdapter = new FollowYouAdapter(mFollowYouList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mFollowMeAdapter);

        prepareFollowYouData();

    }

    private void prepareFollowYouData() {
        FollowYouUser followYou = new FollowYouUser("Follow You User 1");
        mFollowYouList.add(followYou);

        followYou = new FollowYouUser("Follow You User 2");
        mFollowYouList.add(followYou);

        followYou = new FollowYouUser("Follow You User 3");
        mFollowYouList.add(followYou);

        followYou = new FollowYouUser("Follow You User 4");
        mFollowYouList.add(followYou);

        followYou = new FollowYouUser("Follow You User 5");
        mFollowYouList.add(followYou);

        followYou = new FollowYouUser("Follow You User 6");
        mFollowYouList.add(followYou);

        followYou = new FollowYouUser("Follow You User 7");
        mFollowYouList.add(followYou);

        followYou = new FollowYouUser("Follow You User 8");
        mFollowYouList.add(followYou);

        followYou = new FollowYouUser("Follow You User 9");
        mFollowYouList.add(followYou);

        mFollowMeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_next:
                if(mActivity instanceof FollowYouActivity)
                    mActivity.finish();
                Intent intent = new Intent(mActivity, SendingInviteActivity.class);
                startActivity(intent);
                break;
        }
    }

    public class FollowYouAdapter extends RecyclerView.Adapter<FollowYouAdapter.MyViewHolder> {

        private List<FollowYouUser> moviesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title/*, year, genre*/;
            public RelativeLayout listItem;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.textView);
//                starImage = (ImageView) view.findViewById(R.id.star_image);
                listItem = (RelativeLayout) view.findViewById(R.id.follow_you_layout);

//                genre = (TextView) view.findViewById(R.id.genre);
//                year = (TextView) view.findViewById(R.id.year);
            }
        }

        public FollowYouAdapter(List<FollowYouUser> moviesList) {
            this.moviesList = moviesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.follow_you_list_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final FollowYouUser movie = moviesList.get(position);
            holder.title.setText(movie.getName());

            if(movie.getName().equalsIgnoreCase("Follow Me User 1") ||
                    movie.getName().equalsIgnoreCase("Follow Me User 6")){
//                holder.listItem.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            }
            else if(movie.getName().equalsIgnoreCase("Follow Me User 2") ||
                    movie.getName().equalsIgnoreCase("Follow Me User 4")){
                holder.listItem.setBackgroundColor(getResources().getColor(R.color.colorAccent));

            }
            else if(movie.getName().equalsIgnoreCase("Follow Me User 3") ||
                    movie.getName().equalsIgnoreCase("Follow Me User 5")){
                holder.listItem.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            holder.title.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP && motionEvent.getAction() != MotionEvent.ACTION_MOVE) {
                        mActivity.showToastMsg(mActivity, "Clicked on :" + movie.getName());
                    }
                    return true;
                }
            });
//            holder.genre.setText(movie.getGenre());
//            holder.year.setText(movie.getYear());
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }
    }

}
