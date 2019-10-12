package com.motamax.drawer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.motamax.R;
import com.motamax.activity.Login;
import static android.content.Context.MODE_PRIVATE;

public class Profile extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private static final String SHARED_PREF_NAME = "MotaMaxhpref";
    private static final String KEY_Email= "email";
    private OnFragmentInteractionListener mListener;
    LinearLayout p_sign;
    TextView profilephone;

    public Profile() {
    }

    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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
        View view = inflater.inflate(R.layout.profile, container, false);
        p_sign=(LinearLayout)view.findViewById(R.id.p_sign);
        p_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
            }
        });
        SharedPreferences prefs = getContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String phone = prefs.getString(KEY_Email, null);

        profilephone=(TextView)view.findViewById(R.id.p_phone);
        profilephone.setText(phone);

        return view;
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
