package tg.tmye.kaba.data.article;

import java.util.List;

/**
 * By abiguime on 02/05/2018.
 * email: 2597434002@qq.com
 */

public class WebArticle {

    public int id;
    public ArticleContent article;
    public String udpatedAt;

    public int likeCount;

    /* public finish action */

    public static class WebBloc {

        public static final int SINGLE_IMAGE = 94;
        public static final int TEXT_CONTENT = 96;

        public static final int OPEN_RESTAURANT_ACTION = 98;
        public static final int OPEN_FOOD_ACTION = 97;
        public static final int OPEN_MENU_ACTION = 90;

        /* type ? content ?  style ? */
        public int type;
        public String data;
    }

    public class ArticleContent {

        public List<WebBloc> content;
        public String title;
        public String[] image_title;
        public String udpatedAt;
    }

}
