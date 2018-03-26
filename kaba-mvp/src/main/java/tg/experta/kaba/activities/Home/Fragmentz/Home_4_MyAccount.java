package tg.experta.kaba.activities.Home.Fragmentz;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

import tg.experta.kaba.R;
import tg.experta.kaba.activities.UserAcc.MyAdressesActivity;
import tg.experta.kaba.activities.UserAcc.NewsFeedActivity;
import tg.experta.kaba.activities.UserAcc.PersonnalInfoActivity;
import tg.experta.kaba.activities.UserAcc.ServiceClientActivity;
import tg.experta.kaba.data.Feeds.NewsFeed;
import tg.experta.kaba.data.delivery.DeliveryAdress;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home_4_MyAccount.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home_4_MyAccount#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_4_MyAccount extends BaseFragment {


    private static final int ROW_COUNT = 3;
    private OnFragmentInteractionListener mListener;

    public Home_4_MyAccount() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Home_4_MyAccount newInstance() {
        Home_4_MyAccount fragment = new Home_4_MyAccount();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_4__my_account, container, false);
    }

    RecyclerView recyclerView;

    ImageView iv_horoscope_moon_sun;
    TextView tv_horoscope_date;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rec_myaccount);
        tv_horoscope_date = view.findViewById(R.id.tv_myaccount_horoscope_date);
        iv_horoscope_moon_sun = view.findViewById(R.id.iv_myaccount_horoscope_sun_moon);

        // init the adapter
        Context ctx = view.getContext();

        recyclerView.setLayoutManager(new GridLayoutManager(ctx, ROW_COUNT));
        recyclerView.setAdapter(new MyAccountAdapter(ctx));

        initDate();
    }

    private void initDate () {


        Calendar instance = Calendar.getInstance();
//        LocalDate.now().getDayOfWeek();
        int day_of_week = instance.getTime().getDay();
        int day_of_month = instance.getTime().getDate();
        int month_of_y = instance.getTime().getMonth();
        int year = 1900+instance.getTime().getYear();

        String[] days_of_week = getResources().getStringArray(R.array.days_of_week);
        String[] months_of_year = getResources().getStringArray(R.array.months_of_year);

        String date = days_of_week[day_of_week-1]+", "+day_of_month+" "+months_of_year[month_of_y]+" "+year;
        tv_horoscope_date.setText(date);

        int hours = instance.getTime().getHours();
        if (hours >= 6 && hours <18) {
            iv_horoscope_moon_sun.setImageResource(R.drawable.ic_myaccount_horoscope_sun);
        } else {
            iv_horoscope_moon_sun.setImageResource(R.drawable.ic_myaccount_horoscope_moon);
        }
    }


    private int getActionBarSize () {
        TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarSize;
    }

    private class MyAccountAdapter extends RecyclerView.Adapter<MyAccountAdapter.ViewHolder> {

        Context ctx;

        public MyAccountAdapter (Context ctx) {

            this.ctx = ctx;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_account_menu_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            switch (position) {
                case 0:
                    holder.tv_menu_title.setText(ctx.getResources().getText(R.string.myaccount_infos));
                    holder.tv_menu_title.setTextColor(ctx.getResources().getColor(R.color.colorPrimary_yellow));
                    holder.iv_menu_icon.setImageResource(R.drawable.ic_myaccount_infos);
                    holder.rootview.setOnClickListener(new OpenSubActivity(PersonnalInfoActivity.class));
                    break;
                case 1:
                    holder.tv_menu_title.setText(ctx.getResources().getText(R.string.myaccount_bon_dachat));
                    holder.tv_menu_title.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
                    holder.iv_menu_icon.setImageResource(R.drawable.ic_myaccount_bon_dachat);
                    break;
                case 2:
                    holder.tv_menu_title.setText(ctx.getResources().getText(R.string.myaccount_favorite_plat));
                    holder.tv_menu_title.setTextColor(ctx.getResources().getColor(R.color.colorPrimary_yellow));
                    holder.iv_menu_icon.setImageResource(R.drawable.ic_myaccount_favorite_food);
                    break;
                case 3:
                    holder.tv_menu_title.setText(ctx.getResources().getText(R.string.myaccount_delivery_address));
                    holder.tv_menu_title.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
                    holder.iv_menu_icon.setImageResource(R.drawable.ic_myaccount_addresses);
                    holder.rootview.setOnClickListener(new OpenSubActivity(MyAdressesActivity.class));
                    break;
                case 4:
                    holder.tv_menu_title.setText(ctx.getResources().getText(R.string.myaccount_client_service));
                    holder.tv_menu_title.setTextColor(ctx.getResources().getColor(R.color.colorPrimary_yellow));
                    holder.iv_menu_icon.setImageResource(R.drawable.ic_myaccount_client_service);
                    holder.rootview.setOnClickListener(new OpenSubActivity(ServiceClientActivity.class));
                    break;
                case 5:
                    holder.tv_menu_title.setText(ctx.getResources().getText(R.string.myaccount_newsletter));
                    holder.tv_menu_title.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
                    holder.iv_menu_icon.setImageResource(R.drawable.ic_myaccount_newsletter);
                    holder.rootview.setOnClickListener(new OpenSubActivity(NewsFeedActivity.class));
                    break;
                case 6:
                    holder.tv_menu_title.setText(ctx.getResources().getText(R.string.myaccount_shopping_card));
                    holder.tv_menu_title.setTextColor(ctx.getResources().getColor(R.color.colorPrimary_yellow));
                    holder.iv_menu_icon.setImageResource(R.drawable.ic_shopping_card_yellow_24dp);
                    break;
                case 7:
                    holder.tv_menu_title.setText(ctx.getResources().getText(R.string.myaccount_commands));
                    holder.tv_menu_title.setTextColor(ctx.getResources().getColor(R.color.colorPrimary_yellow));
                    holder.iv_menu_icon.setImageResource(R.drawable.ic_myaccount_list_command);
                    break;
                case 8:
                    holder.tv_menu_title.setText(ctx.getResources().getText(R.string.myaccount_settings));
                    holder.tv_menu_title.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
                    holder.iv_menu_icon.setImageResource(R.drawable.ic_myaccount_settings);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return 9;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            View rootview;
            ImageView iv_menu_icon;
            TextView tv_menu_title;

            public ViewHolder(View itemView) {
                super(itemView);
                this.rootview = itemView;
                this.iv_menu_icon = itemView.findViewById(R.id.iv_menu_icon);
                this.tv_menu_title = itemView.findViewById(R.id.tv_menu_title);
            }
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

    private class OpenSubActivity implements View.OnClickListener {

        private final Class<?> activity;

        public OpenSubActivity(Class<?> activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), activity);
            view.getContext().startActivity(intent);
        }
    }
}
