package tg.tmye.kaba._commons.cviews;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.piasy.biv.view.BigImageView;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.OnImageClickListener;
import tg.tmye.kaba._commons.utils.ImageViewUtil;
import tg.tmye.kaba.activity.FoodDetails.FoodDetailsActivity;
import tg.tmye.kaba.activity.ProgressPieIndicator;
import tg.tmye.kaba.activity.WebArticle.WebArticleActivity;
import tg.tmye.kaba.activity.menu.RestaurantMenuActivity;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.advert.AdsBanner;


public class PreviewImageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String IMG_URL = "param1";

    public static final String VIEW_INFO_EXTRA = "VIEW_INFO_EXTRA";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private Bundle mStartValues;

    // Bundle that will contain the transition end values

    // Animation constants
    private static final AccelerateDecelerateInterpolator DEFAULT_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    private static final int DEFAULT_DURATION = 300;

    private BigImageView mDestinationView;

    private Button bt_see_more;


    public PreviewImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param adBanner imageLink 1.
     * @param mStartValues
     * @return A new instance of fragment PreviewImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreviewImageFragment newInstance(AdsBanner adBanner, Bundle mStartValues) {
        PreviewImageFragment fragment = new PreviewImageFragment();
        Bundle args = new Bundle();
        args.putParcelable(IMG_URL, adBanner);
        fragment.setArguments(args);
        return fragment;
    }

    AdsBanner adBanner;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            adBanner = getArguments().getParcelable(IMG_URL);
            mStartValues = getArguments().getBundle(VIEW_INFO_EXTRA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preview_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String url = adBanner.pic;
        String description = adBanner.description;

        mDestinationView = view.findViewById(R.id.m_big_image);
        bt_see_more = view.findViewById(R.id.bt_see_more);

        mDestinationView.showImage(Uri.parse(url));

        TextView tv_description = view.findViewById(R.id.tv_description);
        if (description != null)
            tv_description.setText(description);

        if (!url.contains(Constant.SERVER_ADDRESS))
            url = Constant.SERVER_ADDRESS+"/"+url;

        if (url == null || "".equals(url)) {
            Toast.makeText(getContext(), getResources().getString(R.string.image_error), Toast.LENGTH_LONG).show();
            mDestinationView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getActivity().finish();
                }
            }, Toast.LENGTH_LONG);
        } else {
            mDestinationView.showImage(Uri.parse(url));
            mDestinationView.setProgressIndicator(new ProgressPieIndicator());
        }

        initBtSeeMore (adBanner, bt_see_more);
    }

    private void initBtSeeMore(final AdsBanner adBanner, final Button bt_see_more) {

        String title = "";

        bt_see_more.setVisibility(View.VISIBLE);
        switch (adBanner.type) {
            case AdsBanner.TYPE_ARTICLE:
                title = getResources().getString(R.string.see_more_article);
                break;
            case AdsBanner.TYPE_MENU:
                title = getResources().getString(R.string.see_more_menu);
                break;
            case AdsBanner.TYPE_REPAS:
                title = getResources().getString(R.string.see_more_food);
                break;
            case AdsBanner.TYPE_RESTAURANT:
                title = getResources().getString(R.string.see_more_restaurant);
                break;
            default:
                bt_see_more.setVisibility(View.GONE);
                break;
        }

        if (bt_see_more.getVisibility() == View.VISIBLE)
            bt_see_more.setText(title);

        bt_see_more.setOnClickListener(new OnImageClickListener() {
            @Override
            public void onClick(View view) {

                /* according to the type, we have a different action. */
                switch (adBanner.type) {
                    case AdsBanner.TYPE_ARTICLE:
                        /* launch article */
                        Intent article_intent = new Intent(getActivity(), WebArticleActivity.class);
                        article_intent.putExtra(WebArticleActivity.ARTICLE_ID, adBanner.entity_id);
                        startActivity(article_intent);
                        getActivity().finish();
                        break;
                    case AdsBanner.TYPE_MENU:
                        /* scroll right to the menu -- with an indication */
//                        Intent menu_intent = new Intent(getActivity(), RestaurantMenuActivity.class);
//                        startActivity(menu_intent);
                        break;
                    case AdsBanner.TYPE_REPAS:
                        Intent food_details_intent = new Intent(getActivity(), FoodDetailsActivity.class);
                        food_details_intent.putExtra(FoodDetailsActivity.FOOD_ID, adBanner.entity_id);
                        food_details_intent.putExtra(FoodDetailsActivity.HAS_SENT_ID, true);
                        startActivity(food_details_intent);
                        getActivity().finish();
                        /* implement opening food with id only, and also, implemeting the reduction thing - reduire le prix de la chose. */
                        break;
                    case AdsBanner.TYPE_RESTAURANT:
                        /* get in a restaurant when the principal card at the top  has to be an advertising one with expiration date.
                         *      - no clicking after a certain time */
                        break;
                }
            }
        });

    }

}
