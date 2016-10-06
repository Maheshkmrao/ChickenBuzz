package com.android.chickenbuzz.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.chickenbuzz.R;
import com.android.chickenbuzz.activity.BaseActivity;
import com.android.chickenbuzz.activity.MyLocationsActivity;
import com.android.chickenbuzz.activity.SendingInviteActivity;
import com.android.chickenbuzz.beans.UserContact;
import com.android.chickenbuzz.componenets.AlphabetSideSelector;
import com.android.chickenbuzz.global.ChickenBuzzApplication;
import com.android.chickenbuzz.utils.Constant;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SendingInviteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SendingInviteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendingInviteFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private BaseActivity mActivity;

    private ListView listView;
    private CustomAdapter adapter;
    private Button button_next;

    private ArrayList<UserContact> allContacts = new ArrayList<UserContact>();
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS_PHONE = 100;

    String[] mProjection;
    private EditText mSearchView;
    private AlphabetSideSelector sideSelector;
    boolean isSearching;
    private Cursor mContactsCursor;

    public SendingInviteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SendingInviteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SendingInviteFragment newInstance(String param1, String param2) {
        SendingInviteFragment fragment = new SendingInviteFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = ((BaseActivity) getActivity());
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            checkContactsPermission();
        }
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
        View view=inflater.inflate(R.layout.activity_sending_invite, container, false);

        initUIControls(view);

        return view;
    }

    private void initUIControls(View view) {
        mSearchView = (EditText) view.findViewById(R.id.searchview);
        button_next = (Button) view.findViewById(R.id.btn_next);
        if(((ChickenBuzzApplication)mActivity.getApplication()).getmUserAboutDetails() == null &&
                !Constant.IS_HOME_SCREEN_DISPLAYED) {
            button_next.setVisibility(View.VISIBLE);
            button_next.setOnClickListener(this);
        }

        listView = (ListView) view.findViewById(R.id.listview);
        sideSelector = (AlphabetSideSelector) view.findViewById(R.id.side_selector);

        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            fetchContacts();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            if(mContactsCursor == null)
                fetchContacts();
        }
    }

    private boolean checkContactsPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS_PHONE);

//            }

            return false;
        }
        else return true;
    }

    public void fetchContacts() {
        mProjection = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE
        };

        mContactsCursor = mActivity.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                mProjection,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        );

        if (mContactsCursor.moveToFirst()) {
            String contactname;
            String cphoneNumber;
            String phoneType;

            allContacts = new ArrayList<UserContact>();
            do {

                UserContact bean = new UserContact();

                contactname = mContactsCursor.getString( mContactsCursor.getColumnIndex(mProjection[0]) );
                cphoneNumber = mContactsCursor.getString( mContactsCursor.getColumnIndex(mProjection[1]) );
                phoneType = mContactsCursor.getString( mContactsCursor.getColumnIndex(mProjection[2]) );
                Log.d("Outside cname", "ts" + contactname);
                Log.d("Outside cno", "ts" + cphoneNumber);
                if ((contactname == " " || contactname == null)
                        && (cphoneNumber == " " || cphoneNumber == null)) {
                    // displayAlert1();

                } else {
                    bean.setName(contactname);
                    bean.setPhoneNo(cphoneNumber);
                    bean.setPhoneType(phoneType);

                    allContacts.add(bean);
                }
            } while (mContactsCursor.moveToNext());

        }

//        if(allContacts != null && allContacts.size() > 0)
//            mActivity.showToastMsg(mActivity, "Contact Length ::" + allContacts.size());

        adapter = new CustomAdapter(mActivity, allContacts);

        // Create the list view and bind the adapter
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);

        setupSearchView();

        sideSelector.setListView(listView);
    }

    private void setupSearchView()
    {
//        mSearchView.setIconifiedByDefault(false);
////        mSearchView.setOnQueryTextListener(this);
//        mSearchView.setSubmitButtonEnabled(true);
//        mSearchView.setQueryHint("Search Here");

        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isSearching = true;
                if (TextUtils.isEmpty(charSequence.toString())) {
//                    listView.clearTextFilter();
                    isSearching = false;
                    sideSelector.setVisibility(View.VISIBLE);
                    adapter.setmRecordsArrayList(allContacts);
                    adapter.notifyDataSetChanged();
                } else {
                    sideSelector.setVisibility(View.GONE);
                    getContactsByName(charSequence.toString());

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        mActivity.showToastMsg(mActivity, "Contacts Permission Request Callback");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchContacts();
                } else {
                    mActivity.showToastMsg(mActivity, getString(R.string.err_msg_permission_contacts));
                }
                return;
            }
        }
    }

    private void getContactsByName(String name) {
        ArrayList<UserContact> mSearchRecords = new ArrayList<UserContact>();
        if (allContacts != null && allContacts.size() > 0) {
            for (UserContact bean : allContacts) {
                if (bean.getName().toLowerCase().indexOf(name.toLowerCase()) != -1)
                    mSearchRecords.add(bean);
            }
        }
        if (mSearchRecords.size() > 0) {
            adapter.setmRecordsArrayList(mSearchRecords);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                if(mActivity instanceof SendingInviteActivity)
                    mActivity.finish();
                Intent intent = new Intent(mActivity, MyLocationsActivity.class);
                startActivity(intent);
                break;

        }
    }

    public class CustomAdapter extends BaseAdapter implements SectionIndexer, Filterable {

        private Context mContext;
        //        private Cursor mCursor;
        private ArrayList<UserContact> mAllContacts;

        // State of the row that needs to show separator
        private static final int SECTIONED_STATE = 1;
        // State of the row that need not show separator
        private static final int REGULAR_STATE = 2;
        // Cache row states based on positions
        private int[] mRowStates;

        public CustomAdapter(Context context, ArrayList<UserContact> allContacts) {
            mContext = context;
//            mCursor = mContactsCursor;
            mAllContacts = allContacts;
            mRowStates = new int[getCount()];
        }

        public void setmRecordsArrayList(ArrayList<UserContact> mRecordsArrayList) {
            this.mAllContacts = mRecordsArrayList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return (mAllContacts != null ? mAllContacts.size() : 0);
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view;
            boolean showSeparator = false;

            UserContact bean = (UserContact) mAllContacts.get(position);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.contact_item, null);
            }
            else {
                view = convertView;
            }

            // Set contact name and number
            TextView contactNameView = (TextView) view.findViewById(R.id.contact_name);
            TextView phoneNumberView = (TextView) view.findViewById(R.id.phone_number);
            TextView phoneTypeView = (TextView) view.findViewById(R.id.phone_type);

//            String name = mCursor.getString( mCursor.getColumnIndex(mProjection[0]) );
//            String number = mCursor.getString( mCursor.getColumnIndex(mProjection[1]) );
//            String type = mCursor.getString( mCursor.getColumnIndex(mProjection[2]) );

            String name = bean.getName();
            String number = bean.getPhoneNo();
            String type = bean.getPhoneType();

            contactNameView.setText( name );
            phoneNumberView.setText( number );
            phoneTypeView.setText( type );
            switch (type) {
                case ""+ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                    phoneTypeView.setText( "HOME" );
                    break;
                case ""+ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                    phoneTypeView.setText( "MOBILE" );
                    break;
                case ""+ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                    phoneTypeView.setText( "WORK" );
                    break;
                case ""+ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                case ""+ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                    phoneTypeView.setText( "FAX" );
                    break;

            }


            // Show separator ?

            switch (mRowStates[position]) {

                case SECTIONED_STATE:
                    showSeparator = true;
                    break;

                case REGULAR_STATE:
                    showSeparator = false;
                    break;

                default:

                    if (position == 0) {
                        showSeparator = true;
                    }
                    else {
//                        mCursor.moveToPosition(position - 1);

                        UserContact prevCont = mAllContacts.get(position - 1);

                        String previousName = prevCont.getName();
                        char[] previousNameArray = previousName.toCharArray();
                        char[] nameArray = name.toCharArray();
                        char[] typeArray = type.toCharArray();

                        if (nameArray[0] != previousNameArray[0]) {
                            showSeparator = true;
                        }

                    }

                    // Cache it
                    mRowStates[position] = showSeparator ? SECTIONED_STATE : REGULAR_STATE;

                    break;
            }

            TextView separatorView = (TextView) view.findViewById(R.id.separator);

            if (showSeparator && !isSearching) {
                separatorView.setText(name.toCharArray(), 0, 1);
                separatorView.setVisibility(View.VISIBLE);
            }
            else {
                view.findViewById(R.id.separator).setVisibility(View.GONE);
            }

            return view;
        }

        @Override
        public Object[] getSections() {
            String[] chars = new String[0];
            if(!isSearching) {
                chars = new String[AlphabetSideSelector.ALPHABET.length];
                for (int i = 0; i < AlphabetSideSelector.ALPHABET.length; i++) {
                    chars[i] = String.valueOf(AlphabetSideSelector.ALPHABET[i]);
                }
            }
            return chars;
        }

        @Override
        public int getPositionForSection(int i) {
            return (int) (getCount() * ((float)i/(float)getSections().length));
        }

        @Override
        public int getSectionForPosition(int i) {
            return 0;
        }

        @Override
        public Filter getFilter() {
            return null;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
