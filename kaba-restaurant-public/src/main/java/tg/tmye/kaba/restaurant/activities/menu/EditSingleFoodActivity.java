package tg.tmye.kaba.restaurant.activities.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import tg.tmye.kaba.restaurant.R;
import tg.tmye.kaba.restaurant.data.Food.Restaurant_Menu_FoodEntity;


public class EditSingleFoodActivity extends AppCompatActivity {

    EditText ed_food_name;
    CheckBox checkBox_promotion, checkBox_active;
    Button bt_cancel, bt_confirm;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_single_food);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        Restaurant_Menu_FoodEntity food = getIntent().getParcelableExtra("food");

        toolbar.setTitle(food.name);

        ed_food_name = findViewById(R.id.ed_food_name);
        checkBox_active = findViewById(R.id.checkbox_food_active);
    checkBox_promotion = findViewById(R.id.checkbox_food_promotion);

        // add a listener on it
        bt_confirm = findViewById(R.id.bt_confirm);
        bt_cancel = findViewById(R.id.bt_cancel);

        //inflate data
        ed_food_name.setText(food.name);


        checkBox_active.setChecked(food.is_hidden == 1);
        checkBox_active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b)
                    checkBox_active.setText("YES");
                else
                    checkBox_active.setText("NO");
            }
        });
        checkBox_active.setChecked(food.is_hidden == 1);
        checkBox_active.setText(food.is_hidden == 1 ? "NO" : "YES");

        checkBox_promotion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    checkBox_promotion.setText("YES");
                else
                    checkBox_promotion.setText("NO");
            }
        });
        checkBox_promotion.setChecked(food.promotion == 1);
        checkBox_promotion.setText(food.promotion == 1 ? "YES" : "NO");

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
            }
        });
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
