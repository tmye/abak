package tg.tmye.kaba.activity.home.views.fragment;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import tg.tmye.kaba._commons.baseviewstubfragment.BaseViewStubFragment;

/**
 * By abiguime on 10/01/2018.
 * email: 2597434002@qq.com
 */

public abstract class BaseFragment extends BaseViewStubFragment {

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation = super.onCreateAnimation(transit, enter, nextAnim);

        // HW layer support only exists on API 11+
        if (Build.VERSION.SDK_INT >= 11) {
            if (animation == null && nextAnim != 0) {
                animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
            }

            if (animation != null) {
                getView().setLayerType(View.LAYER_TYPE_HARDWARE, null);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        View view = getView();
                        if (view != null)
                            view.setLayerType(View.LAYER_TYPE_NONE, null);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    // ...other AnimationListener methods go here...
                });
            }
        }

        return animation;
    }
}
