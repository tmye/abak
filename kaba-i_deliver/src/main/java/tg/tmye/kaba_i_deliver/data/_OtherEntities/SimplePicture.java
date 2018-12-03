package tg.tmye.kaba_i_deliver.data._OtherEntities;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class SimplePicture {

    public String path;
    public String content_description;
    public String fileSize; // kb


    public static class Banner extends SimplePicture {
        /* when clicked, get to the destination page */
        public String articlePath;

        @Override
        public String toString() {
            return "Banner{" +
                    "path='" + path + '\'' +
                    ", content_description='" + content_description + '\'' +
                    ", fileSize='" + fileSize + '\'' +
                    ", articlePath='" + articlePath + '\'' +
                    '}';
        }
    }

    public static class KabaShowPic extends SimplePicture{
        /* when clicked, just preview */
        public KabaShowPic (String path) {
            this.path = path;
            this.content_description = "";
            this.fileSize = "";
        }

        @Override
        public String toString() {
            return "Banner{" +
                    "path='" + path + '\'' +
                    ", content_description='" + content_description + '\'' +
                    ", fileSize='" + fileSize + '\'' +
                    '}';
        }
    }

}
