package tg.tmye.kaba.brave.one;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.imageviewer.fragment.ImageViewerFragment;

public class EmptyTestActivity extends AppCompatActivity {

    private String url = "https://thumbs.dreamstime.com/b/group-happy-kids-christmas-hat-presents-isolated-white-background-holidays-new-year-mas-concept-48320320.jpg";

    private ImageViewerFragment frg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_test);

    }

    public void playsound(View view) {
        UtilFunctions.playPurchaseSuccessSound(this);

        if (frg == null) {

            frg = ImageViewerFragment.newInstance();
            getSupportFragmentManager().beginTransaction().show(frg).commit();
        } else {

            getSupportFragmentManager().beginTransaction().hide(frg).commit();
            frg = null;
        }

    }
}
