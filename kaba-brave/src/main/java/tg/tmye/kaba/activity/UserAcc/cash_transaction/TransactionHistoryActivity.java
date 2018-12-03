package tg.tmye.kaba.activity.UserAcc.cash_transaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.cviews.dialog.ForceLogoutDialogFragment;
import tg.tmye.kaba._commons.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba._commons.decorator.CommandInnerFoodLineDecorator;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.UserAcc.cash_transaction.contract.TransactionContract;
import tg.tmye.kaba.activity.UserAcc.cash_transaction.presenter.TransactionPresenter;
import tg.tmye.kaba.data.transaction.Transaction;
import tg.tmye.kaba.data.transaction.source.TransactionRepository;

public class TransactionHistoryActivity extends AppCompatActivity implements TransactionContract.View, View.OnClickListener {


    TransactionRepository repository;
    TransactionPresenter presenter;
    private TextView tv_message;
    private RecyclerView recyclerview;
    private LinearLayoutCompat lny_error;
    private Button bt_tryagain;

    TransactionHistoryAdapter adapter;

    private LoadingDialogFragment loadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_redprimary_upward_navigation_24dp);

        initViews();
        /*/mobile/api/user/transaction/history*/
        repository = new TransactionRepository(this);
        presenter = new TransactionPresenter(this, repository);

        presenter.loadTransactionHistoric();

        bt_tryagain.setOnClickListener(this);
    }

    private void initViews() {

        recyclerview = findViewById(R.id.recyclerview);
        lny_error = findViewById(R.id.lny_error);
        tv_message = findViewById(R.id.tv_message);
        bt_tryagain = findViewById(R.id.bt_tryagain);
    }

    @Override
    public void inflateTransactions(final List<Transaction> transactions) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                lny_error.setVisibility(View.GONE);

                /* transactions,, reverse array */
                List<Transaction> trans = UtilFunctions.reverseArray(transactions);

                adapter = new TransactionHistoryAdapter(TransactionHistoryActivity.this, trans);
                recyclerview.setLayoutManager(new LinearLayoutManager(TransactionHistoryActivity.this));

                Drawable line_divider = ContextCompat.getDrawable(TransactionHistoryActivity.this, R.drawable.command_inner_food_item_divider);
                recyclerview.addItemDecoration(new CommandInnerFoodLineDecorator(line_divider));

                recyclerview.setAdapter(adapter);
                recyclerview.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onSysError() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                recyclerview.setVisibility(View.GONE);

                lny_error.setVisibility(View.VISIBLE);
                tv_message.setVisibility(View.VISIBLE);
                bt_tryagain.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onNetworkError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                recyclerview.setVisibility(View.GONE);

                lny_error.setVisibility(View.VISIBLE);
                tv_message.setVisibility(View.VISIBLE);
                bt_tryagain.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void showLoading(final boolean isLoading) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingDialogFragment == null) {
                    if (isLoading) {
                        loadingDialogFragment = LoadingDialogFragment.newInstance(getString(R.string.content_on_loading));
                        showFragment(loadingDialogFragment, "loadingbox", true);
                    }
                } else {
                    if (isLoading) {
                        showFragment(loadingDialogFragment, "loadingbox",false);
                    } else {
                        hideFragment();
                    }
                }
            }
        });
    }

    private void hideFragment() {
        if (loadingDialogFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(loadingDialogFragment);
            loadingDialogFragment = null;
            ft.commitAllowingStateLoss();
        }
    }

    private void showFragment(LoadingDialogFragment loadingDialogFragment, String tag, boolean justCreated) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (justCreated == true)
            ft.add(loadingDialogFragment, tag);
        else
            ft.show(loadingDialogFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

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
    public void setPresenter(TransactionContract.Presenter presenter) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_tryagain:
                presenter.loadTransactionHistoric();
                break;
        }
    }

    @Override
    public void onLoggingTimeout() {
        ForceLogoutDialogFragment forceLogoutDialogFragment = ForceLogoutDialogFragment.newInstance();
        forceLogoutDialogFragment.show(getSupportFragmentManager(), ForceLogoutDialogFragment.TAG);
    }

    private class TransactionHistoryAdapter extends RecyclerView.Adapter<ViewHolder> {


        private final List<Transaction> transactions;
        private final Context context;

        public TransactionHistoryAdapter (Context context, List<Transaction> transactions) {
            this.context = context;
            this.transactions = transactions;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_history_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Transaction transaction = transactions.get(position);

            holder.tv_libelle.setText(UtilFunctions.timeStampToDate(context, transaction.created_at));
            holder.tv_amount.setText(transaction.value);

            if (transaction.type == -1) {
                holder.tv_type.setText("-");
                holder.tv_type.setTextColor(context.getResources().getColor(R.color.red));
                holder.tv_amount.setTextColor(context.getResources().getColor(R.color.red));
            } else {
                holder.tv_type.setText("+");
                holder.tv_type.setTextColor(context.getResources().getColor(R.color.black));
                holder.tv_amount.setTextColor(context.getResources().getColor(R.color.black));
            }

        }

        @Override
        public int getItemCount() {
            return transactions.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        /*  */
        TextView tv_type, tv_amount, tv_libelle;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_type = itemView.findViewById(R.id.tv_type);
            this.tv_amount = itemView.findViewById(R.id.tv_amount);
            this.tv_libelle = itemView.findViewById(R.id.tv_libelle);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.fade_out);
    }

}
