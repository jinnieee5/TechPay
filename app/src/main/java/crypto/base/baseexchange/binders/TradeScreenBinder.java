package crypto.base.baseexchange.binders;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.adapters.MarketCoinListHeaderAdapter;
import crypto.base.baseexchange.adapters.SpinnerBuySellAdapter;
import crypto.base.baseexchange.adapters.TimeInForceSpinnerAdapter;
import crypto.base.baseexchange.adapters.TradeOpenOrderAdapter;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.api.request.BuySellReq;
import crypto.base.baseexchange.api.request.InterExchangeCoinMarketReq;
import crypto.base.baseexchange.api.request.InterExchangeParingListReq;
import crypto.base.baseexchange.api.request.LoginTokenReq;
import crypto.base.baseexchange.api.request.TradeOpenOrderRequest;
import crypto.base.baseexchange.api.response.CoinPairingList;
import crypto.base.baseexchange.api.response.MarketCoin;
import crypto.base.baseexchange.api.response.TimeInForceData;
import crypto.base.baseexchange.api.response.TradeOpenOrderList;
import crypto.base.baseexchange.api.response.TradePairingCoinDetail;
import crypto.base.baseexchange.databinding.LayoutTradeMainBinding;
import crypto.base.baseexchange.interfaces.CoinMarketListHeaderListener;
import crypto.base.baseexchange.interfaces.TradeOpenOrdersListener;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import crypto.base.baseexchange.views.EnterScreen;
import crypto.base.baseexchange.views.OpenOrderScreen;
import crypto.base.baseexchange.views.OrderHistoryScreen;
import crypto.base.baseexchange.views.PairingChartScreen;
import crypto.base.baseexchange.views.TradeOrderHistoryScreen;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TradeScreenBinder implements CoinMarketListHeaderListener, TradeOpenOrdersListener {
    private Context context;
    private LayoutTradeMainBinding binding;
    private FragmentManager fragmentManager;
    private ProgressDialog marketListProgressDialog, pairingListProgressDialog, buyCoinDialog, sellCoinDialog;
    private TextView tv_openOrders;
    private int ttifmid=1;
    private ImageView iv_headerChart;
    private String viewPagerSelection, headerSelection, spinnerSelection;
    private double buyAvailableBalance, sellAvailableBalance;

    public TradeScreenBinder(final Context context, LayoutTradeMainBinding binding, FragmentManager fragmentManager,
                             String viewPagerSelection, String headerSelection, String spinnerSelection) {
        this.context = context;
        this.binding = binding;
        this.fragmentManager = fragmentManager;
        this.viewPagerSelection = viewPagerSelection;
        this.headerSelection = headerSelection;
        this.spinnerSelection = spinnerSelection;

        setTradeSubHeader();
        getCoinMarketList();
        manageBlinkEffect();
    }

    private void setTradeHeader(final int pairingID,final String firstCoinCode, List<CoinPairingList> coinPairingList, final String firstCoinTpspmid) {
        iv_headerChart = binding.layoutTradeHeader.findViewById(R.id.iv_headerChart);
        Spinner coinPairingSpinner = binding.layoutTradeHeader.findViewById(R.id.coinPairingSpinner);
        final TextView tv_orderHistory = binding.layoutTradeHeader.findViewById(R.id.tv_orderHistory);

        if (!coinPairingList.isEmpty()) {
            SpinnerBuySellAdapter spinnerBuySellList = new SpinnerBuySellAdapter(context, firstCoinCode, coinPairingList);
            coinPairingSpinner.setAdapter(spinnerBuySellList);

            if (!spinnerSelection.isEmpty()) {
                int i = 0;
                for (CoinPairingList coinPairing : coinPairingList) {
                    if (spinnerSelection.equalsIgnoreCase(coinPairing.getCoinCode())) {
                        coinPairingSpinner.setSelection(i);
                    }
                    i++;
                }
            }
        }

        coinPairingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view!=null) {
                    TextView tv_secondCoinTCLMID = view.findViewById(R.id.tv_secondCoinTCLMID);
                    TextView tv_secondCoin = view.findViewById(R.id.tv_secondCoin);

                    final int secondCoinTCLMID = Integer.parseInt(tv_secondCoinTCLMID.getText().toString().trim());
                    final String secondCoinCode = tv_secondCoin.getText().toString();

                    coinMarketDetail(firstCoinCode, secondCoinCode, pairingID, firstCoinTpspmid, secondCoinTCLMID, 2, 2);
                    getTradeBuyOrderList(firstCoinTpspmid, secondCoinTCLMID+"");
                    getTradeSellOrderList(firstCoinTpspmid, secondCoinTCLMID+"");

                    tv_openOrders.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (SharedPrefUtils.getFromPrefs(context, SharedPrefUtils.isLogin).equals("0")) {
                                Intent intent = new Intent(context, EnterScreen.class);
                                context.startActivity(intent);
                            } else {
                                Intent intent = new Intent(context, OpenOrderScreen.class);
                                intent.putExtra("firstCoinTpspmid", "" + firstCoinTpspmid);
                                intent.putExtra("secondCoinTclmid", "" + secondCoinTCLMID);
                                context.startActivity(intent);
                            }
                        }
                    });

                    tv_orderHistory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (SharedPrefUtils.getFromPrefs(context, SharedPrefUtils.isLogin).equals("0")) {
                                Intent intent = new Intent(context, EnterScreen.class);
                                context.startActivity(intent);
                            } else {
                                Intent intent = new Intent(context, OrderHistoryScreen.class);
                                intent.putExtra("firstCoinTpspmid", "" + firstCoinTpspmid);
                                intent.putExtra("secondCoinTclmid", "" + secondCoinTCLMID);
                                context.startActivity(intent);
                            }
                        }
                    });

                    binding.frameTradeOrderHistory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, TradeOrderHistoryScreen.class);
                            intent.putExtra("firstCoinTpspmid",""+firstCoinTpspmid);
                            intent.putExtra("secondCoinTclmid",""+secondCoinTCLMID);
                            intent.putExtra("firstCoinCode",""+firstCoinCode);
                            intent.putExtra("secondCoinCode",""+secondCoinCode);

                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setTradeSubHeader() {
        TabLayout tabs_tradeSubHeader = binding.layoutTradeSubHeader.findViewById(R.id.tabs_tradeSubHeader);
        ViewPager vp_tradeSubHeader = binding.layoutTradeSubHeader.findViewById(R.id.vp_tradeSubHeader);
        tv_openOrders = binding.layoutTradeSubHeader.findViewById(R.id.tv_openOrders);

        //set blank view pager
        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager);
        adapter.addFragment(new Fragment(), "buy");
        adapter.addFragment(new Fragment(), "sell");

        vp_tradeSubHeader.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        vp_tradeSubHeader.setOffscreenPageLimit(0);
        tabs_tradeSubHeader.setupWithViewPager(vp_tradeSubHeader);

        //set tabs
        final TextView tabOne = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabOne.setText("Buy");
        tabOne.setTextColor(context.getColor(R.color.white));
        tabOne.setTextSize(14);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tabOne.setTypeface(context.getResources().getFont(R.font.gotham_rounded_medium));
        }
        tabs_tradeSubHeader.getTabAt(0).setCustomView(tabOne);

        final TextView tabTwo = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Sell");
        tabTwo.setTextColor(Color.WHITE);
        tabTwo.setTextSize(14);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tabTwo.setTypeface(context.getResources().getFont(R.font.gotham_rounded_medium));
        }
        tabs_tradeSubHeader.getTabAt(1).setCustomView(tabTwo);
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

    private void setTradeExchange(final String firstCoinTpspmid, final int secondCoinTCLMID, final String firstCoinCode, final String secondCoinCode, final TradePairingCoinDetail pairingCoinDetail,
                                   List<TimeInForceData> timeInForceDataList) {
        final FrameLayout frame_button = binding.layoutTradeExchange.findViewById(R.id.frame_button);
        final TextView tv_button = binding.layoutTradeExchange.findViewById(R.id.tv_button);
        final TextView tv_totalFeeHeader = binding.layoutTradeExchange.findViewById(R.id.tv_totalFeeHeader);
        //final TextView tv_basicFeeHeader = binding.layoutTradeExchange.findViewById(R.id.tv_basicFeeHeader);
        final TextView tv_secondCoinQuantityHeader = binding.layoutTradeExchange.findViewById(R.id.tv_secondCoinQuantityHeader);
        final TextView firstCoinBidPriceHeader = binding.layoutTradeExchange.findViewById(R.id.firstCoinBidPriceHeader);
        final TextView tv_availableBalance = binding.layoutTradeExchange.findViewById(R.id.tv_availableBalance);

        final EditText ed_secondCoinQuantity = binding.layoutTradeExchange.findViewById(R.id.ed_secondCoinQuantity);
        final EditText ed_firstCoinBidPrice = binding.layoutTradeExchange.findViewById(R.id.ed_firstCoinBidPrice);
        //final EditText ed_firstCoinBasicFee = binding.layoutTradeExchange.findViewById(R.id.ed_firstCoinBasicFee);
        final EditText ed_firstCoinTotalFee = binding.layoutTradeExchange.findViewById(R.id.ed_firstCoinTotalFee);
        final EditText ed_autocloseProfit = binding.layoutTradeExchange.findViewById(R.id.ed_autocloseProfit);
        final EditText ed_autocloseLoss = binding.layoutTradeExchange.findViewById(R.id.ed_autocloseLoss);

        final FrameLayout frame_secondCoinQuantityMinus = binding.layoutTradeExchange.findViewById(R.id.frame_secondCoinQuantityMinus);
        final FrameLayout frame_secondCoinQuantityPlus = binding.layoutTradeExchange.findViewById(R.id.frame_secondCoinQuantityPlus);
        final FrameLayout frame_firstCoinBidPriceMinus = binding.layoutTradeExchange.findViewById(R.id.frame_firstCoinBidPriceMinus);
        final FrameLayout frame_firstCoinBidPricePlus = binding.layoutTradeExchange.findViewById(R.id.frame_firstCoinBidPricePlus);

        final FrameLayout frame_25perc = binding.layoutTradeExchange.findViewById(R.id.frame_25perc);
        final FrameLayout frame_50perc = binding.layoutTradeExchange.findViewById(R.id.frame_50perc);
        final FrameLayout frame_75perc = binding.layoutTradeExchange.findViewById(R.id.frame_75perc);
        final FrameLayout frame_100perc = binding.layoutTradeExchange.findViewById(R.id.frame_100perc);

        final LinearLayout layout_percentage = binding.layoutTradeExchange.findViewById(R.id.layout_percentage);

        buyAvailableBalance = pairingCoinDetail.getBuyAvailableBalance();
        sellAvailableBalance = pairingCoinDetail.getSellAvailableBalance();

        if (SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.isLogin).isEmpty() || SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.isLogin).equalsIgnoreCase("0"))
            layout_percentage.setVisibility(View.GONE);
        else {
            layout_percentage.setVisibility(View.VISIBLE);
            tv_availableBalance.setText(String.format("%s %s", buyAvailableBalance, firstCoinCode));
        }

        final DecimalFormat df = new DecimalFormat("#.00000000");

        //TextView tv_firstCoinName2 = binding.layoutTradeExchange.findViewById(R.id.tv_firstCoinName2);
        TextView tv_firstCoinName3 = binding.layoutTradeExchange.findViewById(R.id.tv_firstCoinName3);

        tv_secondCoinQuantityHeader.setText(String.format("Quantity (%s) :", secondCoinCode));
        firstCoinBidPriceHeader.setText(String.format("Bid/Ask Price (%s) :", firstCoinCode));

        //tv_firstCoinName2.setText(firstCoinCode);
        tv_firstCoinName3.setText(firstCoinCode);

        Spinner spinner_timeInForce = binding.layoutTradeExchange.findViewById(R.id.spinner_timeInForce);
        TimeInForceSpinnerAdapter timeInForceSpinnerAdapter = new TimeInForceSpinnerAdapter(context, timeInForceDataList);
        spinner_timeInForce.setAdapter(timeInForceSpinnerAdapter);

        spinner_timeInForce.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_id = view.findViewById(R.id.tv_id);
                ttifmid = Integer.parseInt(tv_id.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        final TabLayout tabs_tradeSubHeader = binding.layoutTradeSubHeader.findViewById(R.id.tabs_tradeSubHeader);
        tabs_tradeSubHeader.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==0) {
                    tv_button.setText("Buy");
                    frame_button.setBackgroundColor(context.getColor(R.color.tradeBuyColor));
                    frame_25perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));
                    frame_50perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));
                    frame_75perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));
                    frame_100perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));
                    //tv_basicFeeHeader.setText(String.format("Basic Fee (%s%%) :", pairingCoinDetail.getTradingFees()));
                    //firstCoinBidPriceHeader.setText(String.format("Bid Price (%s) :", firstCoinCode));
                    //tv_totalFeeHeader.setText(String.format("Total+Fee (%s%%) :", pairingCoinDetail.getTradingFees()));
                    ed_secondCoinQuantity.setText("");
                    ed_firstCoinBidPrice.setText("");
                    //ed_firstCoinBasicFee.setText("");
                    ed_firstCoinTotalFee.setText("");
                    ed_autocloseProfit.setText("");
                    ed_autocloseLoss.setText("");

                    tv_availableBalance.setText(String.format("%s %s", buyAvailableBalance, firstCoinCode));
                } else if (tab.getPosition()==1) {
                    tv_button.setText("Sell");
                    frame_button.setBackgroundColor(context.getColor(R.color.tradeSellColor));
                    frame_25perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));
                    frame_50perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));
                    frame_75perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));
                    frame_100perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));
                    //tv_basicFeeHeader.setText(String.format("Basic Fee (%s%%) :", pairingCoinDetail.getTradingFees()));
                    //firstCoinBidPriceHeader.setText(String.format("Ask Price (%s) :", firstCoinCode));
                    //tv_totalFeeHeader.setText(String.format("Total-Fee (%s%%) :", pairingCoinDetail.getTradingFees()));
                    ed_secondCoinQuantity.setText("");
                    ed_firstCoinBidPrice.setText("");
                    //ed_firstCoinBasicFee.setText("");
                    ed_firstCoinTotalFee.setText("");
                    ed_autocloseProfit.setText("");
                    ed_autocloseLoss.setText("");

                    tv_availableBalance.setText(String.format("%s %s", sellAvailableBalance, secondCoinCode));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        if (!viewPagerSelection.isEmpty()) {
            if (viewPagerSelection.equalsIgnoreCase("buy")) tabs_tradeSubHeader.getTabAt(0).select();
            else if (viewPagerSelection.equalsIgnoreCase("sell")) tabs_tradeSubHeader.getTabAt(1).select();
        }

        frame_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefUtils.getFromPrefs(context, SharedPrefUtils.isLogin).equals("0")) {
                    Intent intent = new Intent(context, EnterScreen.class);
                    context.startActivity(intent);
                } else {
                    if (ed_secondCoinQuantity.getText().toString().isEmpty()) {
                        ed_secondCoinQuantity.requestFocus();
                        BaseUtils.customToast(context, "Please enter quantity");
                    } else if (ed_firstCoinBidPrice.getText().toString().isEmpty()) {
                        ed_firstCoinBidPrice.requestFocus();
                        BaseUtils.customToast(context, "Please enter BID price");
                    } else {
                        int tpspmid = Integer.parseInt(firstCoinTpspmid);
                        double tradeVolume = Double.parseDouble(ed_firstCoinTotalFee.getText().toString());
                        double tradeRate = Double.parseDouble(ed_secondCoinQuantity.getText().toString());
                        double tradeFeeVolume = 0;
                        double tradeFeePercentage = pairingCoinDetail.getTradingFees();
                        double autoCloseProfitPercentage = 0.00;
                        double autoCloseLossPercentage = 0.00;
                        if (!ed_autocloseProfit.getText().toString().isEmpty())
                            autoCloseProfitPercentage = Double.parseDouble(ed_autocloseProfit.getText().toString());
                        if (!ed_autocloseLoss.getText().toString().isEmpty())
                            autoCloseLossPercentage = Double.parseDouble(ed_autocloseLoss.getText().toString());

                        if (tabs_tradeSubHeader.getSelectedTabPosition() == 0)
                            callBuyCoin(secondCoinTCLMID, tradeVolume, tradeRate, tpspmid, tradeFeeVolume, tradeFeePercentage, ttifmid, autoCloseProfitPercentage, autoCloseLossPercentage);
                        else if (tabs_tradeSubHeader.getSelectedTabPosition() == 1)
                            callSellCoin(secondCoinTCLMID, tradeVolume, tradeRate, tpspmid, tradeFeeVolume, tradeFeePercentage, ttifmid, autoCloseProfitPercentage, autoCloseLossPercentage);
                    }
                }
            }
        });

        ed_secondCoinQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (SharedPrefUtils.getFromPrefs(context, SharedPrefUtils.isLogin).equals("1")) {
                        double maxRate = pairingCoinDetail.getBuyingMaxVolume();
                        double userRate = Double.parseDouble(s.toString().trim());
                        if (userRate > maxRate) {
                            BaseUtils.hideKeypad((Activity) context);
                            (new Handler()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ed_secondCoinQuantity.setText("");
                                    ed_firstCoinBidPrice.setText("");
                                    //ed_firstCoinBasicFee.setText("");
                                    ed_firstCoinTotalFee.setText("");
                                    ed_autocloseProfit.setText("");
                                    ed_autocloseLoss.setText("");
                                }
                            }, 1000);
                            BaseUtils.customToast(context, "Quantity should not be greater than " + maxRate);
                        } else {
                            ed_firstCoinBidPrice.setText(df.format(pairingCoinDetail.getPerCoinPriceInUSD()));

                            double firstCoinAmount = Double.parseDouble(ed_secondCoinQuantity.getText().toString()) * Double.parseDouble(ed_firstCoinBidPrice.getText().toString());
                            double basicFee = (firstCoinAmount * pairingCoinDetail.getTradingFees()) / 100;
                            //ed_firstCoinBasicFee.setText(df.format(basicFee));

                            double totalFee;
                            if (tabs_tradeSubHeader.getSelectedTabPosition() == 0) {
                                totalFee = firstCoinAmount + basicFee;
                                ed_firstCoinTotalFee.setText(df.format(totalFee));
                            } else if (tabs_tradeSubHeader.getSelectedTabPosition() == 1) {
                                totalFee = firstCoinAmount - basicFee;
                                ed_firstCoinTotalFee.setText(df.format(totalFee));
                            }
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ed_firstCoinBidPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (SharedPrefUtils.getFromPrefs(context, SharedPrefUtils.isLogin).equals("1")) {
                    if (s.length() > 0 && !ed_secondCoinQuantity.getText().toString().isEmpty()) {
                        double firstCoinAmount = Double.parseDouble(ed_secondCoinQuantity.getText().toString()) * Double.parseDouble(ed_firstCoinBidPrice.getText().toString());
                        double basicFee = (firstCoinAmount * pairingCoinDetail.getTradingFees()) / 100;
                        //ed_firstCoinBasicFee.setText(df.format(basicFee));

                        double totalFee;
                        if (tabs_tradeSubHeader.getSelectedTabPosition() == 0) {
                            totalFee = firstCoinAmount + basicFee;
                            ed_firstCoinTotalFee.setText(df.format(totalFee));
                        } else if (tabs_tradeSubHeader.getSelectedTabPosition() == 1) {
                            totalFee = firstCoinAmount - basicFee;
                            ed_firstCoinTotalFee.setText(df.format(totalFee));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        frame_secondCoinQuantityMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_secondCoinQuantity.getText().toString().isEmpty()) {
                    double qty = Double.parseDouble(ed_secondCoinQuantity.getText().toString());
                    qty = qty-1;

                    if (qty<0) {
                        BaseUtils.customToast(context,"Quantity should not be less than 0.00000001");
                        BaseUtils.hideKeypad((Activity) context);
                    }
                    else { ed_secondCoinQuantity.setText(String.format("%s", qty)); }
                }
            }
        });

        frame_secondCoinQuantityPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_secondCoinQuantity.getText().toString().isEmpty()) {
                    double qty = Double.parseDouble(ed_secondCoinQuantity.getText().toString());
                    qty = qty+1;
                    ed_secondCoinQuantity.setText(qty+"");
                }
            }
        });

        frame_firstCoinBidPriceMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_firstCoinBidPrice.getText().toString().isEmpty()) {
                    double bidPrice = Double.parseDouble(ed_firstCoinBidPrice.getText().toString());
                    bidPrice = bidPrice-1;

                    if (bidPrice<0) {
                        BaseUtils.customToast(context,"Bid/Ask should not be less than 0.00000001");
                        BaseUtils.hideKeypad((Activity) context);
                    }
                    else { ed_firstCoinBidPrice.setText(String.format("%s", bidPrice)); }
                }
            }
        });

        frame_firstCoinBidPricePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_firstCoinBidPrice.getText().toString().isEmpty()) {
                    double qty = Double.parseDouble(ed_firstCoinBidPrice.getText().toString());
                    qty = qty+1;
                    ed_firstCoinBidPrice.setText(qty+"");
                }
            }
        });

        frame_25perc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frame_25perc.setBackgroundColor(context.getColor(R.color.deepRed));
                frame_50perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));
                frame_75perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));
                frame_100perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));

                if (tabs_tradeSubHeader.getSelectedTabPosition()==0) {
                    ed_secondCoinQuantity.setText(df.format((buyAvailableBalance*25)/100));
                } else if (tabs_tradeSubHeader.getSelectedTabPosition()==1) {
                    ed_secondCoinQuantity.setText(df.format((sellAvailableBalance*25)/100));
                }
            }
        });

        frame_50perc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frame_25perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));
                frame_50perc.setBackgroundColor(context.getColor(R.color.deepRed));
                frame_75perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));
                frame_100perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));

                if (tabs_tradeSubHeader.getSelectedTabPosition()==0) {
                    ed_secondCoinQuantity.setText(df.format((buyAvailableBalance*50)/100));
                } else if (tabs_tradeSubHeader.getSelectedTabPosition()==1) {
                    ed_secondCoinQuantity.setText(df.format((sellAvailableBalance*50)/100));
                }
            }
        });

        frame_75perc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frame_25perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));
                frame_50perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));
                frame_75perc.setBackgroundColor(context.getColor(R.color.deepRed));
                frame_100perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));

                if (tabs_tradeSubHeader.getSelectedTabPosition()==0) {
                    ed_secondCoinQuantity.setText(df.format((buyAvailableBalance*75)/100));
                } else if (tabs_tradeSubHeader.getSelectedTabPosition()==1) {
                    ed_secondCoinQuantity.setText(df.format((sellAvailableBalance*75)/100));
                }
            }
        });

        frame_100perc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frame_25perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));
                frame_50perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));
                frame_75perc.setBackgroundColor(context.getColor(R.color.headerDarkColor));
                frame_100perc.setBackgroundColor(context.getColor(R.color.deepRed));

                if (tabs_tradeSubHeader.getSelectedTabPosition()==0) {
                    ed_secondCoinQuantity.setText(df.format((buyAvailableBalance*100)/100));
                } else if (tabs_tradeSubHeader.getSelectedTabPosition()==1) {
                    ed_secondCoinQuantity.setText(df.format((sellAvailableBalance*100)/100));
                }
            }
        });
    }

    private void setTradeExchange(TradeOpenOrderList openOrder) {
        final EditText ed_secondCoinQuantity = binding.layoutTradeExchange.findViewById(R.id.ed_secondCoinQuantity);
        final EditText ed_firstCoinBidPrice = binding.layoutTradeExchange.findViewById(R.id.ed_firstCoinBidPrice);
        final EditText ed_firstCoinTotalFee = binding.layoutTradeExchange.findViewById(R.id.ed_firstCoinTotalFee);

        final DecimalFormat df = new DecimalFormat("#.00000000");

        double coinQuantity = openOrder.getCoinPurchase();
        double coinBidPrice = openOrder.getTradeRate();
        double coinTotalFee = openOrder.getCoinPurchase() * openOrder.getTradeRate();

        ed_secondCoinQuantity.setText(df.format(coinQuantity));
        ed_firstCoinBidPrice.setText(df.format(coinBidPrice));
        ed_firstCoinTotalFee.setText(df.format(coinTotalFee));
    }

    private void getCoinMarketList() {
        BaseUtils.hideKeypad((Activity) context);
        marketListProgressDialog = BaseUtils.showProgressDialog(context,"Please wait");
        marketListProgressDialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        LoginTokenReq loginTokenReq = new LoginTokenReq();
        loginTokenReq.setLoginToken("");

        Single<JsonObject> observable = authApiHelper.getInterExchangeCoinList(loginTokenReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}
                    @Override
                    public void onSuccess(JsonObject coinLiveRatesRes) {
                        marketListProgressDialog.dismiss();
                        if (coinLiveRatesRes.get("Flag").getAsString().equalsIgnoreCase("success")) {
                            JsonArray jArray = coinLiveRatesRes.getAsJsonArray("Data");
                            final Type listType = new TypeToken<List<MarketCoin>>() {}.getType();
                            List<MarketCoin> marketCoinList = new Gson().fromJson(jArray, listType);

                            if (!marketCoinList.isEmpty()) {
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                                binding.rvCoinMarketList.setLayoutManager(layoutManager);

                                if (headerSelection.isEmpty()) {
                                    onMarketCoinClick(marketCoinList.get(0).getCoinCode(), marketCoinList.get(0).getPairingID(), marketCoinList.get(0).getTpspmid(),false);
                                    binding.rvCoinMarketList.setAdapter(new MarketCoinListHeaderAdapter(context,
                                            marketCoinList, TradeScreenBinder.this, marketCoinList.get(0).getCoinCode(), marketCoinList.get(0).getPairingID()));
                                } else {
                                    for (MarketCoin coin : marketCoinList) {
                                        if (headerSelection.equalsIgnoreCase(coin.getCoinCode())) {
                                            onMarketCoinClick(coin.getCoinCode(), coin.getPairingID(), coin.getTpspmid(),true);
                                            binding.rvCoinMarketList.setAdapter(new MarketCoinListHeaderAdapter(context,
                                                    marketCoinList, TradeScreenBinder.this, coin.getCoinCode(), coin.getPairingID()));
                                        }
                                    }
                                }
                            }

                        } else Log.e("Dashboard", "empty coin-live-rate-list, error in API");
                    }
                    @Override
                    public void onError(Throwable e) {
                        marketListProgressDialog.dismiss();
                        BaseUtils.customToast(context,context.getResources().getString(R.string.errorOccur));
                    }
                });
    }

    @Override
    public void onMarketCoinClick(String firstCoinCode, int pairingID, int tpspmid, boolean spinnerSelect) {
        coinPairingList(firstCoinCode, pairingID, tpspmid+"");
        if (!spinnerSelect) spinnerSelection="";
    }

    private void coinPairingList(final String firstCoinCode, final int pairingID, final String firstCoinTpspmid) {
        BaseUtils.hideKeypad((Activity) context);
        pairingListProgressDialog = BaseUtils.showProgressDialog(context,"Please wait");
        pairingListProgressDialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        InterExchangeParingListReq paringListReq = new InterExchangeParingListReq();
        paringListReq.setLoginToken("");
        paringListReq.setID(pairingID);

        Single<JsonObject> observable = authApiHelper.getInterExchangeCoinPairingList(paringListReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}
                    @Override
                    public void onSuccess(JsonObject pairingRes) {
                        pairingListProgressDialog.dismiss();
                        if (pairingRes.get("Flag").getAsString().equals("success")) {
                            JsonArray jArray = pairingRes.getAsJsonArray("Data");
                            final Type listType = new TypeToken<List<CoinPairingList>>() {}.getType();
                            List<CoinPairingList> coinPairingList = new Gson().fromJson(jArray, listType);

                            setTradeHeader(pairingID, firstCoinCode, coinPairingList, firstCoinTpspmid);
                        } else BaseUtils.customToast(context, pairingRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        pairingListProgressDialog.dismiss();
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private void coinMarketDetail(final String firstCoinCode, final String secondCoinCode, int paringID, final String firstCoinTpspmid, final int secondCoinTCLMID, int purchaseType, int purchaseTypeID) {
        BaseUtils.hideKeypad((Activity) context);
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        InterExchangeCoinMarketReq coinMarketReq = new InterExchangeCoinMarketReq();
        if (SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.isLogin).isEmpty() || SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.isLogin).equalsIgnoreCase("0"))
            coinMarketReq.setLoginToken("");
        else coinMarketReq.setLoginToken(SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.LoginID));
        coinMarketReq.setID(paringID);
        coinMarketReq.setTclmid(secondCoinTCLMID);
        coinMarketReq.setPurchaseType(purchaseType);
        coinMarketReq.setPurchaseTypeID(purchaseTypeID);

        Single<JsonObject> observable = authApiHelper.getInterExchangeCoinMarketDetailList(coinMarketReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject coinMarketRes) {
                        if (coinMarketRes.get("Flag").getAsString().equals("success")) {
                            JsonObject jObject = coinMarketRes.getAsJsonObject("Data");
                            final Type listType = new TypeToken<TradePairingCoinDetail>() {}.getType();
                            final TradePairingCoinDetail pairingCoinDetail = new Gson().fromJson(jObject, listType);

                            JsonArray jArray = coinMarketRes.getAsJsonArray("TimeInForceData");
                            final Type spinnerType = new TypeToken<List<TimeInForceData>>() {}.getType();
                            List<TimeInForceData> timeInForceDataList = new Gson().fromJson(jArray, spinnerType);

                            setTradeExchange(firstCoinTpspmid, secondCoinTCLMID, firstCoinCode, secondCoinCode, pairingCoinDetail, timeInForceDataList);
                            binding.tvFirstCoinPriceHeader1.setText(String.format("Price(%s)", firstCoinCode));
                            binding.tvFirstCoinPriceHeader2.setText(String.format("Price(%s)", firstCoinCode));
                            binding.tvSecondCoinAmountHeader1.setText(String.format("Ask(%s)", secondCoinCode));
                            binding.tvSecondCoinAmountHeader2.setText(String.format("BID(%s)", secondCoinCode));

                            iv_headerChart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, PairingChartScreen.class);
                                    intent.putExtra("firstCoinCode",firstCoinCode);
                                    intent.putExtra("secondCoinCode",secondCoinCode);
                                    intent.putExtra("pairingCoinDetail",pairingCoinDetail);
                                    context.startActivity(intent);
                                }
                            });

                        } else BaseUtils.customToast(context, coinMarketRes.get("Message").getAsString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private void getTradeBuyOrderList(String firstCoinTpspmid, String secondCoinTclmid) {
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        TradeOpenOrderRequest tradeOpenOrderRequest = new TradeOpenOrderRequest();
        tradeOpenOrderRequest.setLoginToken("");
        tradeOpenOrderRequest.setTpspmid(firstCoinTpspmid);
        tradeOpenOrderRequest.setTclmid(secondCoinTclmid);

        Single<JsonObject> observable = authApiHelper.tradeBuyOrders(tradeOpenOrderRequest);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject getKycDetailRes) {
                        if (getKycDetailRes.get("Flag").getAsString().equalsIgnoreCase("success")) {

                            JsonArray jArray = getKycDetailRes.getAsJsonArray("AllBuyData");
                            final Type listType = new TypeToken<List<TradeOpenOrderList>>() {}.getType();
                            final List<TradeOpenOrderList> tradeBuyOrderLists = new Gson().fromJson(jArray, listType);

                            if (!tradeBuyOrderLists.isEmpty()) {
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                binding.rvTradeMarketBuyOrders.setLayoutManager(layoutManager);
                                binding.rvTradeMarketBuyOrders.setAdapter(new TradeOpenOrderAdapter(context, tradeBuyOrderLists, "buy", TradeScreenBinder.this));

                                double totalPrice=0, totalBid=0;
                                for (TradeOpenOrderList data : tradeBuyOrderLists) {
                                    totalPrice = totalPrice + (data.getCoinPurchase()*data.getTradeRate());
                                    totalBid = totalBid + data.getCoinPurchase();
                                }

                                DecimalFormat df = new DecimalFormat("#.0000000");
                                binding.tvFirstCoinTotalBuyPrice.setText("Est. Price : "+df.format(totalPrice));
                                binding.tvSecondCoinTotalBuyBid.setText("Total BID : "+df.format(totalBid));

                            } else Log.e("tradeBuyOrderLists","empty");

                        } else BaseUtils.customToast(context, getKycDetailRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private void getTradeSellOrderList(String firstCoinTpspmid, String secondCoinTclmid) {
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        TradeOpenOrderRequest tradeOpenOrderRequest = new TradeOpenOrderRequest();
        tradeOpenOrderRequest.setLoginToken("");
        tradeOpenOrderRequest.setTpspmid(firstCoinTpspmid);
        tradeOpenOrderRequest.setTclmid(secondCoinTclmid);

        Single<JsonObject> observable = authApiHelper.tradeSellOrders(tradeOpenOrderRequest);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject getKycDetailRes) {
                        if (getKycDetailRes.get("Flag").getAsString().equalsIgnoreCase("success")) {

                            JsonArray jArray = getKycDetailRes.getAsJsonArray("AllSellData");
                            final Type listType = new TypeToken<List<TradeOpenOrderList>>() {}.getType();
                            final List<TradeOpenOrderList> tradeSellOrderLists = new Gson().fromJson(jArray, listType);

                            if (!tradeSellOrderLists.isEmpty()) {
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                binding.rvTradeMarketSellOrders.setLayoutManager(layoutManager);
                                binding.rvTradeMarketSellOrders.setAdapter(new TradeOpenOrderAdapter(context, tradeSellOrderLists, "sell", TradeScreenBinder.this));

                                double totalPrice=0, totalAsk=0;
                                for (TradeOpenOrderList data : tradeSellOrderLists) {
                                    totalPrice = totalPrice + (data.getCoinPurchase()*data.getTradeRate());
                                    totalAsk = totalAsk + data.getCoinPurchase();
                                }

                                DecimalFormat df = new DecimalFormat("#.0000000");
                                binding.tvFirstCoinTotalSellPrice.setText("Est. Price : "+df.format(totalPrice));
                                binding.tvSecondCoinTotalSellAsk.setText("Total ASK : "+df.format(totalAsk));

                            } else Log.e("rvTradeMarketSellOrders","empty");

                        } else BaseUtils.customToast(context, getKycDetailRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }

    @Override
    public void onTradeOpenOrdersClick(TradeOpenOrderList openOrder, String mode) {
        if (mode.equalsIgnoreCase("buy")) {
            viewPagerSelection = "sell";
            setTradeExchange(openOrder);
        } else if (mode.equalsIgnoreCase("sell")) {
            viewPagerSelection = "buy";
            setTradeExchange(openOrder);
        }
    }

    private void callBuyCoin(final int tclmid, double tradeVolume, double tradeRate,final int tpspmid, double tradeFeeVolume, double tradeFeePercentage, int ttifmid,
                                double autoCloseProfitPercentage, double autoCloseLossPercentage) {
        BaseUtils.hideKeypad((Activity) context);
        buyCoinDialog = BaseUtils.showProgressDialog(context, context.getResources().getString(R.string.msg_wait));
        buyCoinDialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        BuySellReq buyCoinReq = new BuySellReq();
        buyCoinReq.setLoginToken(SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.LoginID));
        buyCoinReq.setTclmid(tclmid);
        buyCoinReq.setTradeVolume(tradeVolume);
        buyCoinReq.setTradeRate(tradeRate);
        buyCoinReq.setTpspmid(tpspmid);
        buyCoinReq.setTradeFeeVolume(tradeFeeVolume);
        buyCoinReq.setTradeFeePercentage(tradeFeePercentage);
        buyCoinReq.setTtifmid(ttifmid);
        buyCoinReq.setAutoCloseProfitPercentage(autoCloseProfitPercentage);
        buyCoinReq.setAutoCloseLossPercentage(autoCloseLossPercentage);

        Single<JsonObject> observable = authApiHelper.buyCoin(buyCoinReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject buyCoinRes) {
                        buyCoinDialog.dismiss();
                        if (buyCoinRes.get("Flag").getAsString().equals("success")
                                && buyCoinRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {
                            BaseUtils.customToast(context, "Order placed successfully.");

                            Intent intent = new Intent(context, OpenOrderScreen.class);
                            intent.putExtra("firstCoinTpspmid",""+tpspmid);
                            intent.putExtra("secondCoinTclmid",""+tclmid);
                            context.startActivity(intent);
                        } else if(buyCoinRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(context,context.getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(context, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        } else {
                            BaseUtils.customToast(context, buyCoinRes.get("Message").getAsString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        buyCoinDialog.dismiss();
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private void callSellCoin(final int tclmid, double tradeVolume, double tradeRate,final int tpspmid, double tradeFeeVolume, double tradeFeePercentage, int ttifmid,
                              double autoCloseProfilePercentage, double autoCloseLossPercentage) {
        BaseUtils.hideKeypad((Activity) context);
        sellCoinDialog = BaseUtils.showProgressDialog(context, context.getResources().getString(R.string.msg_wait));
        sellCoinDialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        BuySellReq sellCoinReq = new BuySellReq();
        sellCoinReq.setLoginToken(SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.LoginID));
        sellCoinReq.setTclmid(tclmid);
        sellCoinReq.setTradeVolume(tradeVolume);
        sellCoinReq.setTradeRate(tradeRate);
        sellCoinReq.setTpspmid(tpspmid);
        sellCoinReq.setTradeFeeVolume(tradeFeeVolume);
        sellCoinReq.setTradeFeePercentage(tradeFeePercentage);
        sellCoinReq.setTtifmid(ttifmid);
        sellCoinReq.setAutoCloseProfitPercentage(autoCloseProfilePercentage);
        sellCoinReq.setAutoCloseLossPercentage(autoCloseLossPercentage);

        Single<JsonObject> observable = authApiHelper.sellCoin(sellCoinReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject sellCoinRes) {
                        sellCoinDialog.dismiss();
                        if (sellCoinRes.get("Flag").getAsString().equals("success")
                                && sellCoinRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {
                            BaseUtils.customToast(context, "Order placed successfully.");

                            Intent intent = new Intent(context, OpenOrderScreen.class);
                            intent.putExtra("firstCoinTpspmid",""+tpspmid);
                            intent.putExtra("secondCoinTclmid",""+tclmid);
                            context.startActivity(intent);
                        } else if(sellCoinRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(context,context.getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(context, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        } else {
                            BaseUtils.customToast(context, sellCoinRes.get("Message").getAsString());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        sellCoinDialog.dismiss();
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private void manageBlinkEffect() {
        ObjectAnimator anim = ObjectAnimator.ofInt(binding.tvTradeOrderHistory, "textColor", context.getResources().getColor(R.color.deepYellow), Color.WHITE,
                context.getResources().getColor(R.color.deepYellow));
        anim.setDuration(1000);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();
    }
}
