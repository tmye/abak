package tg.tmye.kaba.restaurant.activities.menu;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import tg.tmye.kaba.restaurant.R;
import tg.tmye.kaba.restaurant.data.Menu.Restaurant_SubMenuEntity;

public class EditSingleMenuActivity extends AppCompatActivity {

    EditText ed_menu_name;
    CheckBox checkBox;
    Button bt_cancel, bt_confirm;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_single_menu);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        Restaurant_SubMenuEntity menu = getIntent().getParcelableExtra("menu");

        toolbar.setTitle(menu.name);

        ed_menu_name = findViewById(R.id.ed_menu_name);
        checkBox = findViewById(R.id.checkbox_menu_active);

        // add a listener on it
        bt_confirm = findViewById(R.id.bt_confirm);
        bt_cancel = findViewById(R.id.bt_cancel);

        //inflate data
        ed_menu_name.setText(menu.name);
        checkBox.setChecked(menu.is_hidden == 1);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b)
                    checkBox.setText("YES");
                else
                    checkBox.setText("NO");
            }
        });

        checkBox.setChecked(menu.is_hidden == 1);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch a presenter and upload this.
                _launchAndConfirm();
            }
        });
    }

    private void _launchAndConfirm() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

}
