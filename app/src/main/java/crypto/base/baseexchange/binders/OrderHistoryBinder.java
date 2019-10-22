package crypto.base.baseexchange.binders;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.adapters.ClosedOrderHistoryAdapter;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.api.request.UserOrderListReq;
import crypto.base.baseexchange.api.response.UserClosedOrderList;
import crypto.base.baseexchange.databinding.LayoutOrderHistoryBinding;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import crypto.base.baseexchange.views.EnterScreen;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OrderHistoryBinder {
    private Context context;
    private LayoutOrderHistoryBinding binding;

    public OrderHistoryBinder(Context context, LayoutOrderHistoryBinding binding, String firstCoinTpspmid, String secondCoinTclmid) {
        this.context = context;
        this.binding = binding;

        setHeader();
        handleDrawer();
        onCalenderClick();
        onFilterMenuClick();
        getAllOrderHistory(firstCoinTpspmid,secondCoinTclmid);

        binding.layoutEmptyOrderList.setVisibility(View.GONE);
        binding.rvOrderHistoryList.setVisibility(View.GONE);
    }

    private void setHeader() {
        TextView tv_header = binding.layoutOrderHistoryHeader.findViewById(R.id.tv_header);
        ImageView iv_back = binding.layoutOrderHistoryHeader.findViewById(R.id.iv_back);
        ImageView iv_endIcon = binding.layoutOrderHistoryHeader.findViewById(R.id.iv_endIcon);

        tv_header.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.VISIBLE);
        iv_endIcon.setVisibility(View.VISIBLE);

        tv_header.setText(context.getString(R.string.header_orderHistory));

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });

        iv_endIcon.setImageDrawable(context.getDrawable(R.drawable.filter));
        iv_endIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.drawerLayout.isDrawerOpen(binding.drawerFilter)) binding.drawerLayout.closeDrawer(binding.drawerFilter);
                else binding.drawerLayout.openDrawer(binding.drawerFilter);
            }
        });
    }

    private void onCalenderClick() {
        binding.ivStartCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        try {
                            String date = dayOfMonth + " / " + (month + 1) + " / " + year;
                            SimpleDateFormat df = new SimpleDateFormat("dd / MM / yyyy");
                            Date newDate = df.parse(date);
                            df = new SimpleDateFormat("yyyy-MMM-dd");
                            date = df.format(newDate);
                            binding.tvStartDate.setText(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        context, datePickerListener, 2000, 01, 01);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        binding.ivEndCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        try {
                            String date = dayOfMonth + " / " + (month + 1) + " / " + year;
                            SimpleDateFormat df = new SimpleDateFormat("dd / MM / yyyy");
                            Date newDate = df.parse(date);
                            df = new SimpleDateFormat("yyyy-MMM-dd");
                            date = df.format(newDate);
                            binding.tvEndDate.setText(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        context, datePickerListener, 2000, 01, 01);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
    }

    private void handleDrawer() {
        binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {}

            @Override
            public void onDrawerStateChanged(int newState) {}
        });
    }

    private void onFilterMenuClick() {
        final TextView tv_1dayDateFilter = binding.layoutFilter.findViewById(R.id.tv_1dayDateFilter);
        final TextView tv_1weekDateFilter = binding.layoutFilter.findViewById(R.id.tv_1weekDateFilter);
        final TextView tv_1monthDateFilter = binding.layoutFilter.findViewById(R.id.tv_1monthDateFilter);
        final TextView tv_3monthsDateFilter = binding.layoutFilter.findViewById(R.id.tv_3monthsDateFilter);
        final TextView tv_firstCoinPairFilter = binding.layoutFilter.findViewById(R.id.tv_firstCoinPairFilter);
        final TextView tv_secondCoinPairFilter = binding.layoutFilter.findViewById(R.id.tv_secondCoinPairFilter);
        final TextView tv_bothTypeFilter = binding.layoutFilter.findViewById(R.id.tv_bothTypeFilter);
        final TextView tv_buyTypeFilter = binding.layoutFilter.findViewById(R.id.tv_buyTypeFilter);
        final TextView tv_sellTypeFilter = binding.layoutFilter.findViewById(R.id.tv_sellTypeFilter);

        tv_1dayDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_1dayDateFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_highlight));
                tv_1weekDateFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_normal));
                tv_1monthDateFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_normal));
                tv_3monthsDateFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_normal));
            }
        });
        tv_1weekDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_1dayDateFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_normal));
                tv_1weekDateFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_highlight));
                tv_1monthDateFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_normal));
                tv_3monthsDateFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_normal));
            }
        });
        tv_1monthDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_1dayDateFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_normal));
                tv_1weekDateFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_normal));
                tv_1monthDateFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_highlight));
                tv_3monthsDateFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_normal));
            }
        });
        tv_3monthsDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_1dayDateFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_normal));
                tv_1weekDateFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_normal));
                tv_1monthDateFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_normal));
                tv_3monthsDateFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_highlight));
            }
        });
        tv_firstCoinPairFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_firstCoinPairFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_highlight));
            }
        });
        tv_secondCoinPairFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_secondCoinPairFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_highlight));
            }
        });
        tv_bothTypeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_bothTypeFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_highlight));
                tv_buyTypeFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_normal));
                tv_sellTypeFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_normal));
            }
        });
        tv_buyTypeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_bothTypeFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_normal));
                tv_buyTypeFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_highlight));
                tv_sellTypeFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_normal));
            }
        });
        tv_sellTypeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_bothTypeFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_normal));
                tv_buyTypeFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_normal));
                tv_sellTypeFilter.setBackground(context.getResources().getDrawable(R.drawable.shape_rectanglefilled_highlight));
            }
        });
    }

    private void getAllOrderHistory(String firstCoinTpspmid, String secondCoinTclmid) {
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        UserOrderListReq userOrderListReq = new UserOrderListReq();
        userOrderListReq.setLoginToken(SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.LoginID));
        userOrderListReq.setTpspmid(firstCoinTpspmid);
        userOrderListReq.setTclmid(secondCoinTclmid);

        Single<JsonObject> observable = authApiHelper.getAllOrdersList(userOrderListReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject userOrderHistoryListRes) {
                        if (userOrderHistoryListRes.get("Flag").getAsString().equalsIgnoreCase("success")
                                && userOrderHistoryListRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {

                            JsonArray jArray = userOrderHistoryListRes.getAsJsonArray("SellClosedOrderData");
                            final Type listType = new TypeToken<List<UserClosedOrderList>>() {}.getType();
                            final List<UserClosedOrderList> openOrderList = new Gson().fromJson(jArray, listType);

                            if (!openOrderList.isEmpty()) {
                                binding.layoutEmptyOrderList.setVisibility(View.GONE);
                                binding.rvOrderHistoryList.setVisibility(View.VISIBLE);

                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                binding.rvOrderHistoryList.setLayoutManager(layoutManager);
                                binding.rvOrderHistoryList.setAdapter(new ClosedOrderHistoryAdapter(context, openOrderList));
                            } else {
                                Log.e("tradeBuyOrderLists","empty");
                                binding.layoutEmptyOrderList.setVisibility(View.VISIBLE);
                                binding.rvOrderHistoryList.setVisibility(View.GONE);
                            }

                        } else if(userOrderHistoryListRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(context,context.getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(context, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        } else BaseUtils.customToast(context, userOrderHistoryListRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }
}
