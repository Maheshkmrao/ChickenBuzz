package com.android.chickenbuzz.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.chickenbuzz.R;
import com.android.chickenbuzz.activity.BaseActivity;
import com.android.chickenbuzz.activity.SendingInviteActivity;
import com.android.chickenbuzz.beans.FollowMeUser;
import com.android.chickenbuzz.componenets.DividerItemDecoration;
import com.android.chickenbuzz.global.ChickenBuzzApplication;
import com.android.chickenbuzz.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InviteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InviteFragment extends Fragment implements View.OnClickListener{

    private BaseActivity mActivity;
    private RecyclerView mRecyclerView;
    private InviteUserAdapter mFollowMeAdapter;
    private List<FollowMeUser> mInviteUsersList = new ArrayList<>();

    public InviteFragment() {
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
    public static InviteFragment newInstance(String param1, String param2) {
        InviteFragment fragment = new InviteFragment();
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
        View view=inflater.inflate(R.layout.activity_follow_me, container, false);

        initUIControls(view);

        return view;
    }

    private void initUIControls(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        Button nextButton = (Button) view.findViewById(R.id.btn_next);
        if(((ChickenBuzzApplication)mActivity.getApplication()).getmUserAboutDetails() == null &&
                !Constant.IS_HOME_SCREEN_DISPLAYED) {
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setOnClickListener(this);
        }

        mFollowMeAdapter = new InviteUserAdapter(mInviteUsersList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mFollowMeAdapter);

        prepareFollowMeData();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, SendingInviteActivity.class);
                startActivity(intent);
            }
        });

    }

    private void prepareFollowMeData() {
        FollowMeUser inviteUser = new FollowMeUser("Follow Me User 1");
        mInviteUsersList.add(inviteUser);

        inviteUser = new FollowMeUser("Follow Me User 2");
        mInviteUsersList.add(inviteUser);

        inviteUser = new FollowMeUser("Follow Me User 3");
        mInviteUsersList.add(inviteUser);

        inviteUser = new FollowMeUser("Follow Me User 4");
        mInviteUsersList.add(inviteUser);

        inviteUser = new FollowMeUser("Follow Me User 5");
        mInviteUsersList.add(inviteUser);

        inviteUser = new FollowMeUser("Follow Me User 6");
        mInviteUsersList.add(inviteUser);

        inviteUser = new FollowMeUser("Follow Me User 7");
        mInviteUsersList.add(inviteUser);

        inviteUser = new FollowMeUser("Follow Me User 8");
        mInviteUsersList.add(inviteUser);

        inviteUser = new FollowMeUser("Follow Me User 9");
        mInviteUsersList.add(inviteUser);

        mFollowMeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_next:

                break;
        }
    }

    public class InviteUserAdapter extends RecyclerView.Adapter<InviteUserAdapter.MyViewHolder> {

        private List<FollowMeUser> moviesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title/*, year, genre*/;
            public ImageView starImage;
            public ImageButton deleteicon;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.textView);
                starImage = (ImageView) view.findViewById(R.id.star_image);
                deleteicon = (ImageButton) view.findViewById(R.id.deleteicon);

//                genre = (TextView) view.findViewById(R.id.genre);
//                year = (TextView) view.findViewById(R.id.year);
            }
        }

        public InviteUserAdapter(List<FollowMeUser> moviesList) {
            this.moviesList = moviesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.follow_me_list_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            FollowMeUser movie = moviesList.get(position);
            holder.title.setText(movie.getName());
            holder.deleteicon.setVisibility(View.GONE);

            if(movie.getName().equalsIgnoreCase("Follow Me User 2") ||
                    movie.getName().equalsIgnoreCase("Follow Me User 4")){
                holder.starImage.setColorFilter(getResources().getColor(R.color.colorPrimary));
            }
            else if(movie.getName().equalsIgnoreCase("Follow Me User 3") ||
                    movie.getName().equalsIgnoreCase("Follow Me User 5")){
                holder.starImage.setColorFilter(getResources().getColor(R.color.colorAccent));
            }
//            holder.genre.setText(movie.getGenre());
//            holder.year.setText(movie.getYear());
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }
    }

}
