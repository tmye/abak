package tg.tmye.kaba.activity.UserAcc;


import tg.tmye.kaba._commons.intf.YesOrNo;
import tg.tmye.kaba._commons.intf.YesOrNoWithResponse;

/**
 * By abiguime on 08/02/2018.
 * email: 2597434002@qq.com
 */

public class UserInteractionImpl {


    public interface LoginImpl {

        /**
         * params:
         *  - phone_number
         *  - password
         **/
        void login(String username, String password, YesOrNoWithResponse yesOrNo);

        /**
         * params:
         *  - token
         *  - yesOrNo
         **/
        void checkLogin(YesOrNo YesOrNoWithResponse);
    }


    public interface LogoutImpl{

        /**
         * params:
         *  - username
         *  - password
         **/
        void logout(String token, YesOrNoWithResponse yesOrNo);
    }

    public interface RegisterImpl {

        /**
         * params:
         *  - phone_number
         *  - password
         **/
        void register(String username, String password, YesOrNoWithResponse yesOrNo);
    }

}
