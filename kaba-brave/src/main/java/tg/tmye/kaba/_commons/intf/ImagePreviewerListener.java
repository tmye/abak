package tg.tmye.kaba._commons.intf;

import android.view.View;

import tg.tmye.kaba.data.advert.AdsBanner;

/**
 * By abiguime on 2018/10/31.
 * email: 2597434002@qq.com
 */
public interface ImagePreviewerListener {

    void onShowPic (View originView, AdsBanner adsBanner);
    void onShowPic (View originView, AdsBanner[] adsBanners);
}
