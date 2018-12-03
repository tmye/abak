package tg.tmye.kaba.activity.home.views.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.adapters.UserAccountAdapter;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.home.HomeActivity;
import tg.tmye.kaba.activity.home.contracts.F_UserMeContract;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.customer.Customer;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;


public class F_MyAccount_4_Fragment extends BaseFragment implements F_UserMeContract.View {


    private static final int ROW_COUNT = 3;
    private OnFragmentInteractionListener mListener;

    /* views */
    private RecyclerView homeOptionsRecyclerView;
    public TextView tv_customer_balance;
    private TextView tv_customer_nickname;
    public TextView tv_customer_phonenumber;
    private TextView tv_balance;

    public CircleImageView cic_profile;

    public ViewStub viewstub_myaccount;

    // private TextView tv_horoscope_date;
    private LinearLayoutCompat lny_account_topup, lny_account_balance;
    // private ImageView iv_horoscope_moon_sun;
//    public TextView tv_customer_horoscope;


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
    protected void onCreateViewAfterViewStubInflated(View inflatedView, Bundle savedInstanceState) {

    }

    @Override
    protected int getViewStubLayoutResource() {
        return R.layout.fragment_home_4__my_account;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.empty);


        /* create menu in stub */
       createMenuIntoStub(view);

//        initDate();

        lny_account_balance.setOnClickListener((HomeActivity)getActivity());
//        lny_account_more.setOnClickListener((HomeActivity)getActivity());
        lny_account_topup.setOnClickListener((HomeActivity)getActivity());

        if (presenter == null) {
            presenter = (F_UserMeContract.Presenter) ((HomeActivity) getActivity()).getPresenterWithNo(4);
        }
        if (presenter != null)
            presenter.start();

        if (tv_balance != null && getActivity() != null) {
            tv_balance.setText("XOF "+getBalance(getActivity()));
        }
    }

    private void createMenuIntoStub(View rootView) {

        viewstub_myaccount.setLayoutResource(R.layout.rec_myaccount_menu);
        viewstub_myaccount.inflate();

        homeOptionsRecyclerView = rootView.findViewById(R.id.rec_myaccount);

        homeOptionsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), ROW_COUNT));
        homeOptionsRecyclerView.setAdapter(new UserAccountAdapter(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();

        if (tv_balance != null && getActivity() != null) {
            tv_balance.setText("XOF "+getBalance(getActivity()));
        }
    }

    private String getBalance(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(Config.USER_SHARED_PREFS, Context.MODE_PRIVATE);
        String balance = preferences.getString(Config.BALANCE_FIELD, "");
        if ("".equals(balance)) {
            return "---";
        }
        return UtilFunctions.intToMoney(balance);
    }

    /* private void initDate () {

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
    }*/

    private void initViews(View rootView) {
        toolbar = rootView.findViewById(R.id.toolbar);
        lny_account_balance = rootView.findViewById(R.id.lny_account_balance);
//        lny_account_more = rootView.findViewById(R.id.lny_account_more);
        lny_account_topup = rootView.findViewById(R.id.lny_account_topup);

        tv_balance = rootView.findViewById(R.id.tv_balance);
        tv_customer_nickname = rootView.findViewById(R.id.tv_nickname);
        tv_customer_phonenumber = rootView.findViewById(R.id.tv_phone_number);
        cic_profile = rootView.findViewById(R.id.cic_profile);
        viewstub_myaccount = rootView.findViewById(R.id.viewstub_myaccount);

        //        tv_horoscope_date = rootView.findViewById(R.id.tv_myaccount_horoscope_date);
        //        iv_horoscope_moon_sun = rootView.findViewById(R.id.iv_myaccount_horoscope_sun_moon);
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
        if (tv_customer_nickname == null) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        tv_customer_nickname.setText(data.nickname);
        if (data.phone_number != null && data.phone_number.length() > 5) {
            tv_customer_phonenumber.setText("XXXX"+data.phone_number.substring(4));
        } else {
            tv_customer_phonenumber.setText("---");
        }

//        cic_profile
        GlideApp.with(getContext())
                .load(Constant.SERVER_ADDRESS + "/" + data.profilePicture)
                .transition(GenericTransitionOptions.with(((MyKabaApp) getContext().getApplicationContext()).getGlideAnimation()))
//                .placeholder(R.drawable.cover_pub_01)
                .centerCrop()
                .into(cic_profile);
    }

    @Override
    public void logout() {

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
        // TODO: Update argument type and restaurant_name
        void onFragmentInteraction(Uri uri);
    }
}
