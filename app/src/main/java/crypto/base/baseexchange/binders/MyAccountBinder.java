package crypto.base.baseexchange.binders;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import androidx.appcompat.app.AlertDialog;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.adapters.CountrySpinnerAdapter;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.api.request.LoginTokenReq;
import crypto.base.baseexchange.api.request.UpdateProfileReq;
import crypto.base.baseexchange.api.response.CountryList;
import crypto.base.baseexchange.api.response.KycData;
import crypto.base.baseexchange.api.response.ProfileData;
import crypto.base.baseexchange.databinding.LayoutAccountBinding;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import crypto.base.baseexchange.views.EnterScreen;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyAccountBinder {
    private Context context;
    private LayoutAccountBinding binding;
    private ProgressDialog dialog1, dialog2, dialog3;
    private AlertDialog alertDialogProfile;
    private List<CountryList> countryList;
    private String countryID;

    public MyAccountBinder(final Context context, LayoutAccountBinding binding) {
        this.context = context;
        this.binding = binding;

        setHeader();
        getProfilePic();
        getProfileDetails();
    }

    private void setHeader() {
        TextView tv_header = binding.includeHeader.findViewById(R.id.tv_header);
        tv_header.setVisibility(View.VISIBLE);
        tv_header.setText(context.getResources().getString(R.string.header_account));

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

    private void updateProfileDialog(String fname, String lname, String dob, String address, int cidForUpdateDialog, String pincode) {
        View updateProfileLayout = LayoutInflater.from(context).inflate(R.layout.dialog_update_profile,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(updateProfileLayout);

        alertDialogProfile = builder.create();
        alertDialogProfile.show();
        //Objects.requireNonNull(alertDialogProfile.getWindow()).setLayout(1000,1200);

        final TextView frame_fName = updateProfileLayout.findViewById(R.id.frame_fName);
        final TextView frame_lName = updateProfileLayout.findViewById(R.id.frame_lName);
        final TextView frame_dob = updateProfileLayout.findViewById(R.id.frame_dob);
        final TextView frame_address = updateProfileLayout.findViewById(R.id.frame_address);
        final TextView frame_pincode = updateProfileLayout.findViewById(R.id.frame_pincode);

        final FrameLayout frame_edFName = updateProfileLayout.findViewById(R.id.frame_edFName);
        final FrameLayout frame_edLName = updateProfileLayout.findViewById(R.id.frame_edLName);
        final FrameLayout frame_edAddress = updateProfileLayout.findViewById(R.id.frame_edAddress);
        final FrameLayout frame_edPincode = updateProfileLayout.findViewById(R.id.frame_edPincode);

        final EditText ed_fName = updateProfileLayout.findViewById(R.id.ed_fName);
        final EditText ed_lName = updateProfileLayout.findViewById(R.id.ed_lName);
        final EditText ed_dob = updateProfileLayout.findViewById(R.id.ed_dob);
        final EditText ed_address = updateProfileLayout.findViewById(R.id.ed_address);
        final EditText ed_pincode = updateProfileLayout.findViewById(R.id.ed_pincode);

        ed_fName.setText(fname);
        ed_lName.setText(lname);
        ed_dob.setText(dob);
        ed_address.setText(address);
        ed_pincode.setText(pincode);

        Spinner spn_country = updateProfileLayout.findViewById(R.id.spn_country);
        CountrySpinnerAdapter countrySpinnerAdapter = new CountrySpinnerAdapter(context, countryList);
        spn_country.setAdapter(countrySpinnerAdapter);

        int i=0;
        for (CountryList country : countryList) {
            if (cidForUpdateDialog==country.getCID()) { spn_country.setSelection(i); break; }
            i++;
        }

        spn_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_id = view.findViewById(R.id.tv_id);
                countryID = tv_id.getText().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ImageView iv_calender = updateProfileLayout.findViewById(R.id.iv_calender);
        iv_calender.setOnClickListener(new View.OnClickListener() {
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
                            ed_dob.setText(date);
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

        ImageView iv_cross = updateProfileLayout.findViewById(R.id.iv_cross);
        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseUtils.hideKeypad((Activity) context);
                alertDialogProfile.dismiss();
            }
        });

        FrameLayout frame_updateProfile = updateProfileLayout.findViewById(R.id.frame_updateProfile);
        frame_updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_fName.getText().toString().isEmpty()) {
                    ed_fName.requestFocus();
                    ed_fName.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_fName.setTextColor(context.getColor(R.color.deepRed));
                    BaseUtils.customToast(context,"First name is required.");
                } else if (ed_lName.getText().toString().isEmpty()) {
                    ed_lName.requestFocus();
                    ed_lName.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_lName.setTextColor(context.getColor(R.color.deepRed));
                    BaseUtils.customToast(context,"Last name is required.");
                } else if (ed_dob.getText().toString().isEmpty()) {
                    ed_dob.requestFocus();
                    ed_dob.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_dob.setTextColor(context.getColor(R.color.deepRed));
                    BaseUtils.customToast(context,"Date of birth is required.");
                } else if (ed_address.getText().toString().isEmpty()) {
                    ed_address.requestFocus();
                    ed_address.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_address.setTextColor(context.getColor(R.color.deepRed));
                    BaseUtils.customToast(context,"Address is required.");
                }  else if (ed_pincode.getText().toString().isEmpty()) {
                    ed_pincode.requestFocus();
                    ed_pincode.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_pincode.setTextColor(context.getColor(R.color.deepRed));
                    BaseUtils.customToast(context,"Pincode is required.");
                } else {
                    String fname = ed_fName.getText().toString();
                    String lname = ed_lName.getText().toString();
                    String dob = ed_dob.getText().toString();
                    String address = ed_address.getText().toString();
                    String pincode = ed_pincode.getText().toString();

                    updateProfileDetails(fname,lname,dob,address,countryID,pincode);
                }
            }
        });

        ed_fName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                frame_fName.setTextColor(context.getColor(R.color.textColorLight));
                frame_edFName.setBackground(context.getDrawable(R.drawable.shape_rectanglefilled_normal));
                ed_fName.setBackground(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ed_lName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                frame_lName.setTextColor(context.getColor(R.color.textColorLight));
                frame_edLName.setBackground(context.getDrawable(R.drawable.shape_rectanglefilled_normal));
                ed_lName.setBackground(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ed_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                frame_address.setTextColor(context.getColor(R.color.textColorLight));
                frame_edAddress.setBackground(context.getDrawable(R.drawable.shape_rectanglefilled_normal));
                ed_address.setBackground(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ed_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                frame_pincode.setTextColor(context.getColor(R.color.textColorLight));
                frame_edPincode.setBackground(context.getDrawable(R.drawable.shape_rectanglefilled_normal));
                ed_pincode.setBackground(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void getProfileDetails() {
        BaseUtils.hideKeypad((Activity) context);
        dialog1 = BaseUtils.showProgressDialog(context,"Please wait");
        dialog1.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        LoginTokenReq loginTokenReq = new LoginTokenReq();
        loginTokenReq.setLoginToken(SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.LoginID));

        Single<JsonObject> observable = authApiHelper.getProfileDetails(loginTokenReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject getProfileDetailRes) {
                        dialog1.dismiss();
                        if (getProfileDetailRes.get("Flag").getAsString().equalsIgnoreCase("success")
                                && getProfileDetailRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {
                            JsonArray jArray = getProfileDetailRes.getAsJsonArray("CountryData");
                            final Type listType1 = new TypeToken<List<CountryList>>() {}.getType();
                            countryList = new Gson().fromJson(jArray, listType1);

                            JsonObject jObject = getProfileDetailRes.getAsJsonObject("ProfileData");
                            final Type listType2 = new TypeToken<ProfileData>() {}.getType();
                            ProfileData profileData = new Gson().fromJson(jObject, listType2);

                            final String fname = profileData.getFname();
                            final String lname = profileData.getLname();
                            final String dob = profileData.getDOB();
                            final String address = profileData.getAddress();
                            final int cid = profileData.getCID();
                            final String pincode = profileData.getPincode();

                            for (CountryList country : countryList) {
                                if (cid==country.getCID()) {
                                    binding.tvCountry.setText(country.getCountryName());
                                    break;
                                }
                            }

                            if (profileData.getEmailID().isEmpty()) binding.tvEmailID.setText("-");
                            else binding.tvEmailID.setText(profileData.getEmailID());

                            if (fname.isEmpty()) binding.tvFName.setText("-");
                            else binding.tvFName.setText(fname);

                            if (lname.isEmpty()) binding.tvLName.setText("-");
                            else binding.tvLName.setText(lname);

                            if (dob.isEmpty()) binding.tvDob.setText("-");
                            else binding.tvDob.setText(dob);

                            if (address.isEmpty()) binding.tvAddress.setText("-");
                            else binding.tvAddress.setText(address);

                            if (pincode.isEmpty()) binding.tvPincode.setText("-");
                            else binding.tvPincode.setText(pincode);

                            binding.ivEditProfile.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!countryList.isEmpty()) updateProfileDialog(fname,lname,dob,address,cid,pincode);
                                }
                            });

                        } else if(getProfileDetailRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(context,context.getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(context, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        } else BaseUtils.customToast(context, getProfileDetailRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        dialog1.dismiss();
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private void updateProfileDetails(String fname, String lname, String dob, String address, String cid, String pincode) {
        BaseUtils.hideKeypad((Activity) context);
        dialog2 = BaseUtils.showProgressDialog(context,"Please wait");
        dialog2.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        UpdateProfileReq updateProfileReq = new UpdateProfileReq();
        updateProfileReq.setLoginToken(SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.LoginID));
        updateProfileReq.setfName(fname);
        updateProfileReq.setlName(lname);
        updateProfileReq.setDob(dob);
        updateProfileReq.setAddress(address);
        updateProfileReq.setCID(cid);
        updateProfileReq.setPincode(pincode);

        Single<JsonObject> observable = authApiHelper.updateProfileDetails(updateProfileReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}
                    @Override
                    public void onSuccess(JsonObject updateProfileRes) {
                        dialog2.dismiss();
                        if (updateProfileRes.get("Flag").getAsString().equalsIgnoreCase("success")
                                && updateProfileRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {
                            getProfileDetails();
                            BaseUtils.customToast(context, "Profile updated successfully.");
                            alertDialogProfile.dismiss();
                        } else if(updateProfileRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(context,context.getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(context, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        } else BaseUtils.customToast(context, updateProfileRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        dialog2.dismiss();
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private void getProfilePic() {
        BaseUtils.hideKeypad((Activity) context);
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        LoginTokenReq loginTokenReq = new LoginTokenReq();
        loginTokenReq.setLoginToken(SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.LoginID));

        Single<JsonObject> observable = authApiHelper.getProfilePic(loginTokenReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject getProfileDetailRes) {
                        if (getProfileDetailRes.get("Flag").getAsString().equalsIgnoreCase("success")
                                && getProfileDetailRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {
                            String imageURL = getProfileDetailRes.get("Message").getAsString();

                            if (!imageURL.isEmpty()) {
                                SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.ProfilePic,imageURL);
                                Picasso.get().load(imageURL).placeholder(R.drawable.user).error(R.drawable.user).into(binding.profilePic);
                            }

                        } else if(getProfileDetailRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(context,context.getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(context, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        } else BaseUtils.customToast(context, getProfileDetailRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }
}
