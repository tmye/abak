package tg.tmye.kaba.activity.home.views.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.adapters.UserAccountAdapter;
import tg.tmye.kaba.activity.home.HomeActivity;
import tg.tmye.kaba.activity.home.contracts.F_UserMeContract;
import tg.tmye.kaba.data.customer.Customer;


public class F_MyAccount_4_Fragment extends BaseFragment implements F_UserMeContract.View {


    private static final int ROW_COUNT = 3;
    private OnFragmentInteractionListener mListener;

    /* views */
    private RecyclerView homeOptionsRecyclerView;
    private TextView tv_horoscope_date;
    private ImageView iv_horoscope_moon_sun;
    private LinearLayoutCompat lny_account_topup, lny_account_more, lny_account_balance;
    private TextView tv_customer_nickname;
    public TextView tv_customer_phonenumber;
    public TextView tv_customer_balance;

    public TextView tv_customer_horoscope;


    /* presenter */
    private F_UserMeContract.Presenter presenter;
    private Toolbar toolbar;

    public F_MyAccount_4_Fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static F_MyAccount_4_Fragment newInstance() {
        F_MyAccount_4_Fragment fragment = new F_MyAccount_4_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_4__my_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.empty);

        homeOptionsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), ROW_COUNT));
        homeOptionsRecyclerView.setAdapter(new UserAccountAdapter(getContext()));
        initDate();

        lny_account_balance.setOnClickListener((HomeActivity)getActivity());
        lny_account_more.setOnClickListener((HomeActivity)getActivity());
        lny_account_topup.setOnClickListener((HomeActivity)getActivity());
    }


    private void initDate () {

        Calendar instance = Calendar.getInstance();
        int day_of_week = instance.getTime().getDay();
        int day_of_month = instance.getTime().getDate();
        int month_of_y = instance.getTime().getMonth();
        int year = 1900+instance.getTime().getYear();

        String[] days_of_week = getResources().getStringArray(R.array.days_of_week);
        String[] months_of_year = getResources().getStringArray(R.array.months_of_year);

        String date = days_of_week[day_of_week]+", "+day_of_month+" "+months_of_year[month_of_y]+" "+year;
        tv_horoscope_date.setText(date);

        int hours = instance.getTime().getHours();
        if (hours >= 6 && hours <18) {
            iv_horoscope_moon_sun.setImageResource(R.drawable.ic_myaccount_horoscope_sun);
        } else {
            iv_horoscope_moon_sun.setImageResource(R.drawable.ic_myaccount_horoscope_moon);
        }
    }

    private void initViews(View rootView) {
        toolbar = rootView.findViewById(R.id.toolbar);
        homeOptionsRecyclerView = rootView.findViewById(R.id.rec_myaccount);
        tv_horoscope_date = rootView.findViewById(R.id.tv_myaccount_horoscope_date);
        iv_horoscope_moon_sun = rootView.findViewById(R.id.iv_myaccount_horoscope_sun_moon);
        lny_account_balance = rootView.findViewById(R.id.lny_account_balance);
        lny_account_more = rootView.findViewById(R.id.lny_account_more);
        lny_account_topup = rootView.findViewById(R.id.lny_account_topup);
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setPresenter(F_UserMeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void inflateCustomerInfo(Customer data) {
        tv_customer_nickname.setText(data.nickname);
        tv_customer_phonenumber.setText("XXXX"+data.phone_number.substring(4));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
