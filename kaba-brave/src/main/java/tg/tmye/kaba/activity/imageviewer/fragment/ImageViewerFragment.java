package tg.tmye.kaba.activity.imageviewer.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.piasy.biv.view.BigImageView;

import tg.tmye.kaba.R;

/**
 * By abiguime on 19/07/2018.
 * email: 2597434002@qq.com
 */
public class ImageViewerFragment extends Fragment {

    public static ImageViewerFragment newInstance() {

        Bundle args = new Bundle();

        ImageViewerFragment fragment = new ImageViewerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.image_view_fragment, container, false);
    }

    BigImageView bigImageView;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        String url = "https://thumbs.dreamstime.com/b/group-happy-kids-christmas-hat-presents-isolated-white-background-holidays-new-year-mas-concept-48320320.jpg";
        bigImageView.showImage(Uri.parse(url));
    }

    private void initViews(View view) {
        bigImageView = view.findViewById(R.id.big_image);
    }
}
