package tg.tmye.kaba._commons.intf;

/**
 * By abiguime on 20/12/2017.
 * email: 2597434002@qq.com
 */

public interface YesOrNoWithResponse<T> {

    void yes(T data, boolean isFromOnline);
    void no(Object data, boolean isFromOnline);
    void onLoggingTimeout();
}
