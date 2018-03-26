package tg.tmye.kaba.activity.UserAcc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import tg.tmye.kaba.R;


/**
 *  According to the field type, show an interface that can help modify it
 */
public class EditFieldActivity extends AppCompatActivity {


    public final static int SIMPLE_TEXT = 1;
    public final static int SIMPLE_NUMBER = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_field);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int type = 0;
        switch (type) {
            case SIMPLE_NUMBER:
                break;
            case SIMPLE_TEXT:
                break;
        }
    }

}
