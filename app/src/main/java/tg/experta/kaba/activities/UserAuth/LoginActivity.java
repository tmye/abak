package tg.experta.kaba.activities.UserAuth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import tg.experta.kaba.R;
import tg.experta.kaba._commons.intf.YesOrNo;
import tg.experta.kaba._commons.intf.YesOrNoWithResponse;
import tg.experta.kaba._commons.utils.UtilFunctions;
import tg.experta.kaba.activities.UserAcc.UserInteractionImpl;
import tg.experta.kaba.config.Config;
import tg.experta.kaba.config.Constant;
import tg.experta.kaba.syscore.MyKabaApp;

public class LoginActivity extends AppCompatActivity implements UserInteractionImpl.LoginImpl {

    private int dest = -1;
    private int prev = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_yellow_upward_navigation_24dp);

        // check launch origin
        dest = getIntent().getIntExtra(Config.HOME_SWITCH_FRAG_DESTINATION, -1);
        prev = getIntent().getIntExtra(Config.HOME_SWITCH_FRAG_PREVIOUS, -1);

        Intent data = new Intent();
        data.putExtra(Config.HOME_SWITCH_FRAG_DESTINATION, prev);
        setResult(Config.LOGIN_FAILURE, data);
    }


    public void iLogin(View view) {

        /* check the contents and validate */
        login("", "", new YesOrNoWithResponse() {

            @Override
            public void yes(String res) {
                String token = res;
                storeToken(token);
                /* store into app */
                ((MyKabaApp)getApplication()).setAuthToken(token);
                Intent data = new Intent();
                data.putExtra(Config.HOME_SWITCH_FRAG_DESTINATION, dest);
                setResult(Config.LOGIN_SUCCESS, data);
                finish();
            }

            @Override
            public void no(String res) {
                Intent data = new Intent();
                data.putExtra(Config.HOME_SWITCH_FRAG_DESTINATION, prev);
                setResult(Config.LOGIN_FAILURE, data);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            this.finish();
            return true;
        } else if (id == R.id.action_share) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void login(String username, String password, YesOrNoWithResponse yesOrNo) {

        /* send request down to network thread */
        yesOrNo.yes(UtilFunctions.randomToken()); // yes if the login is successfull
    }

    @Override
    public void checkLogin(YesOrNo yesOrNo) {

        /**
         * Token Length must also be 33
         * if token last chars = last 4 chars of the phone number, keep it and check later, else logout immediately
         **/

        String token = "";
        String phoneNumber = "";

        if (token.length() != 33) {
            if (phoneNumber.substring(3).equals(token.substring(28)))
                yesOrNo.yes();
            else yesOrNo.no();
        } else {
            yesOrNo.no();
        }
    }


    private void storeToken(String token) {
        SharedPreferences pref = getSharedPreferences(Config.SYS_SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Config.SYSTOKEN, token);
        editor.commit();
    }

}
