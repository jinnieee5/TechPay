package crypto.base.baseexchange.binders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.List;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.adapters.TicketCategoryAdapter;
import crypto.base.baseexchange.adapters.TicketPriorityAdapter;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.api.request.GenerateTicketReq;
import crypto.base.baseexchange.api.response.TicketCategoryList;
import crypto.base.baseexchange.api.response.TicketPriorityList;
import crypto.base.baseexchange.databinding.LayoutCreateTicketBinding;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import crypto.base.baseexchange.views.EnterScreen;
import crypto.base.baseexchange.views.TicketListScreen;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreateTicketBinder {
    private Context context;
    private LayoutCreateTicketBinding binding;
    private ProgressDialog dialog1, dialog2;
    private String catName, catID, priorityValue;

    public CreateTicketBinder(final Context context, final LayoutCreateTicketBinding binding) {
        this.context = context;
        this.binding = binding;
        setHeader();
        getTicketDetails();

        binding.tvViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, TicketListScreen.class));
            }
        });

        binding.ivDelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ivTicket1.setImageBitmap(null);
                binding.frameTicket1.setVisibility(View.GONE);
            }
        });
        binding.ivDelete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ivTicket2.setImageBitmap(null);
                binding.frameTicket2.setVisibility(View.GONE);
            }
        });
        binding.ivDelete3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ivTicket3.setImageBitmap(null);
                binding.frameTicket3.setVisibility(View.GONE);
            }
        });

        binding.spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_name = view.findViewById(R.id.tv_name);
                TextView tv_id = view.findViewById(R.id.tv_id);

                catName = tv_name.getText().toString();
                catID = tv_id.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.spnPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_id = view.findViewById(R.id.tv_id);
                priorityValue = tv_id.getText().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.frameSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.edSubject.getText().toString().isEmpty()) {
                    binding.edSubject.requestFocus();
                    binding.edSubject.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    binding.tvSubject.setTextColor(context.getColor(R.color.deepRed));
                    BaseUtils.customToast(context,"Subject is required.");
                } else if (binding.edMessage.getText().toString().isEmpty()) {
                    binding.edMessage.requestFocus();
                    binding.edMessage.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    binding.tvMessage.setTextColor(context.getColor(R.color.deepRed));
                    BaseUtils.customToast(context,"Message is required.");
                } else {
                    String subject = binding.edSubject.getText().toString();
                    String messgae = binding.edMessage.getText().toString();
                    generateTicket(subject,messgae);
                }
            }
        });

        binding.edSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.tvSubject.setTextColor(context.getColor(R.color.textColorLight));
                binding.frameEdSubject.setBackground(context.getDrawable(R.drawable.shape_rectanglefilled_normal));
                binding.edSubject.setBackground(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.tvMessage.setTextColor(context.getColor(R.color.textColorLight));
                binding.frameEdMessage.setBackground(context.getDrawable(R.drawable.shape_rectanglefilled_normal));
                binding.edMessage.setBackground(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setHeader() {
        TextView tv_header = binding.includeHeader.findViewById(R.id.tv_header);
        tv_header.setVisibility(View.VISIBLE);
        tv_header.setText(context.getResources().getString(R.string.header_createTicket));

        ImageView iv_endIcon = binding.includeHeader.findViewById(R.id.iv_endIcon);
        iv_endIcon.setVisibility(View.GONE);

        ImageView iv_back = binding.includeHeader.findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
            }
        });
    }

    private void getTicketDetails() {
        dialog1 = BaseUtils.showProgressDialog(context,"Please wait");
        dialog1.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);

        Single<JsonObject> observable = authApiHelper.getCatPriorToGenerateTicket();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject catPriorToGenerateTicketRes) {
                        dialog1.dismiss();
                        if (catPriorToGenerateTicketRes.get("Flag").getAsString().equalsIgnoreCase("success")) {

                            JsonArray jArray1 = catPriorToGenerateTicketRes.getAsJsonArray("CategoryData");
                            final Type listType1 = new TypeToken<List<TicketCategoryList>>() {}.getType();
                            List<TicketCategoryList> categoryList = new Gson().fromJson(jArray1, listType1);

                            JsonArray jArray2 = catPriorToGenerateTicketRes.getAsJsonArray("PriorityData");
                            final Type listType2 = new TypeToken<List<TicketPriorityList>>() {}.getType();
                            List<TicketPriorityList> priorityList = new Gson().fromJson(jArray2, listType2);

                            TicketCategoryAdapter categoryAdapter = new TicketCategoryAdapter(context, categoryList);
                            binding.spnCategory.setAdapter(categoryAdapter);

                            TicketPriorityAdapter priorityAdapter = new TicketPriorityAdapter(context, priorityList);
                            binding.spnPriority.setAdapter(priorityAdapter);

                        } else BaseUtils.customToast(context, catPriorToGenerateTicketRes.get("Message").getAsString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog1.dismiss();
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private void generateTicket(String subject, String message) {
        BaseUtils.hideKeypad((Activity) context);
        dialog2 = BaseUtils.showProgressDialog(context, context.getResources().getString(R.string.msg_wait));
        dialog2.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        GenerateTicketReq generateTicketReq = new GenerateTicketReq();
        generateTicketReq.setLoginToken(SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.LoginID));
        generateTicketReq.setCategoryId(catID);
        generateTicketReq.setCategoryName(catName);
        generateTicketReq.setPriority(priorityValue);
        generateTicketReq.setSubject(subject);
        generateTicketReq.setMessage(message);

        if (binding.llTickets.getVisibility()==View.VISIBLE) {
            if (binding.frameTicket1.getVisibility()==View.VISIBLE) generateTicketReq.setImgPreviewFirst("data:image/jpeg;base64,"+getProfilePicString(binding.ivTicket1));
            else generateTicketReq.setImgPreviewFirst("");

            if (binding.frameTicket2.getVisibility()==View.VISIBLE) generateTicketReq.setImgPreviewSecond("data:image/jpeg;base64,"+getProfilePicString(binding.ivTicket2));
            else generateTicketReq.setImgPreviewSecond("");

            if (binding.frameTicket3.getVisibility()==View.VISIBLE) generateTicketReq.setImgPreviewThird("data:image/jpeg;base64,"+getProfilePicString(binding.ivTicket3));
            else generateTicketReq.setImgPreviewThird("");

        } else if (binding.llTickets.getVisibility()==View.GONE) {
            generateTicketReq.setImgPreviewFirst("");
            generateTicketReq.setImgPreviewSecond("");
            generateTicketReq.setImgPreviewThird("");
        }

        Single<JsonObject> observable = authApiHelper.generateTicket(generateTicketReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject generateTicketRes) {
                        dialog2.dismiss();
                        if (generateTicketRes.get("Flag").getAsString().equals("success")
                                && generateTicketRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {
                            BaseUtils.customToast(context, "Ticket created successfully.");

                            binding.edSubject.setText("");
                            binding.edMessage.setText("");
                            binding.llTickets.setVisibility(View.GONE);
                            binding.ivTicket1.setImageBitmap(null);
                            binding.ivTicket2.setImageBitmap(null);
                            binding.ivTicket3.setImageBitmap(null);

                            context.startActivity(new Intent(context,TicketListScreen.class));
                        } else if(generateTicketRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(context,context.getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(context, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        } else BaseUtils.customToast(context, generateTicketRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        dialog2.dismiss();
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private String getProfilePicString(ImageView imageView) {
        String profilePicString = "";
        try {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            profilePicString = Base64.encodeToString(byteArray, Base64.DEFAULT); }
        catch (Exception e) { e.printStackTrace(); }
        return profilePicString;
    }
}
