package crypto.base.baseexchange.binders;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.databinding.LayoutWalletTransactionBinding;
import crypto.base.baseexchange.views.WalletDepositHistoryScreen;
import crypto.base.baseexchange.views.WalletWithdrawalHistoryScreen;

public class WalletTransactionBinder {
    private Context context;
    private LayoutWalletTransactionBinding binding;
    private FragmentManager fragmentManager;

    public WalletTransactionBinder(Context context, LayoutWalletTransactionBinding binding, FragmentManager fragmentManager) {
        this.context = context;
        this.binding = binding;
        this.fragmentManager = fragmentManager;

        setHeader();
        setViewPagerTabs();
    }

    private void setHeader() {
        TextView tv_header = binding.layoutWalletHeader.findViewById(R.id.tv_header);
        ImageView iv_back = binding.layoutWalletHeader.findViewById(R.id.iv_back);
        ImageView iv_endIcon = binding.layoutWalletHeader.findViewById(R.id.iv_endIcon);

        tv_header.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.VISIBLE);
        iv_endIcon.setVisibility(View.GONE);

        tv_header.setText(context.getString(R.string.header_transactions));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });
    }

    private void setViewPagerTabs() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager);
        adapter.addFragment(new WalletDepositHistoryScreen(), "deposit");
        adapter.addFragment(new WalletWithdrawalHistoryScreen(), "withdrawal");

        binding.vpWalletTransactions.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        binding.vpWalletTransactions.setOffscreenPageLimit(0);
        binding.tabsWalletTransactions.setupWithViewPager(binding.vpWalletTransactions);

        TextView tabOne = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabOne.setText("Deposit");
        tabOne.setTextColor(context.getColor(R.color.white));
        tabOne.setTextSize(14);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tabOne.setTypeface(context.getResources().getFont(R.font.gotham_rounded_medium));
        }
        binding.tabsWalletTransactions.getTabAt(0).setCustomView(tabOne);

        final TextView tabTwo = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Withdrawal");
        tabTwo.setTextColor(Color.WHITE);
        tabTwo.setTextSize(14);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tabTwo.setTypeface(context.getResources().getFont(R.font.gotham_rounded_medium));
        }
        binding.tabsWalletTransactions.getTabAt(1).setCustomView(tabTwo);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) { super(manager); }

        @Override
        public Fragment getItem(int position) { return mFragmentList.get(position); }

        @Override
        public int getCount() { return mFragmentList.size(); }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title); }

        @Override
        public CharSequence getPageTitle(int position) { return mFragmentTitleList.get(position); }
    }
}
