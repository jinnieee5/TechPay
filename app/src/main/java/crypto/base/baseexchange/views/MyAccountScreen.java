package crypto.base.baseexchange.views;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.Permission;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.api.request.LoginTokenReq;
import crypto.base.baseexchange.api.request.UpdateProfilePicReq;
import crypto.base.baseexchange.api.request.UploadKycReq;
import crypto.base.baseexchange.api.response.KycData;
import crypto.base.baseexchange.binders.MyAccountBinder;
import crypto.base.baseexchange.databinding.LayoutAccountBinding;
import crypto.base.baseexchange.utils.BaseActivity;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyAccountScreen extends BaseActivity {
    private LayoutAccountBinding binding;
    private ProgressDialog dialogProfile, dialogKyc;
    private AlertDialog alertDialogIdProof, alertDialogPanCard;
    private int setProfilePic=0, setSelfPhotoWithID=0, setIdFrontImage=0, setIdBackImage=0, setPanCardImage=0;
    private ImageView iv_selfPhotoWithID, iv_idFrontImage, iv_idBackImage, iv_panCardImage,
                            iv_uploadedSelfPhotoWithID, iv_uploadedIdFrontImage, iv_uploadedIdBackImage, iv_uploadedPanCardImage;
    private TextView tv_headerselfPhotoWithID, tv_headerIdFrontImage, tv_headerIdBackImage, tv_headerPanCardImage;
    private LinearLayout ll_uploadedSelfPhotoWithID, ll_uploadedIdFrontImage, ll_uploadedIdBackImage, ll_uploadedPanCardImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_account);
        MyAccountBinder binder = new MyAccountBinder(this,binding);
        binding.setMyAccountScreen(binder);
        getKycDetails();

        binding.tvUploadProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MyAccountScreen.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MyAccountScreen.this,
                            new String[]{Manifest.permission.CAMERA},
                            200);
                } else {
                    setProfilePic=1;
                    setSelfPhotoWithID=0;
                    setIdFrontImage=0;
                    setIdBackImage=0;
                    setPanCardImage=0;
                    CropImage.startPickImageActivity(MyAccountScreen.this);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,}, 0);
            else startCropImageActivity(imageUri);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = Objects.requireNonNull(result).getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, new FileOutputStream(result.getUri().toString()));

                    checkPicToSet(bitmap);
                    Log.e("MyAccountScreen","try block");
                }
                catch (IOException e) {
                    checkPicToSet(result.getUri());
                    Log.e("MyAccountScreen","catch block");
                }
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(MyAccountScreen.this);
    }

    private void checkPicToSet(Bitmap bitmap) {
        if (setProfilePic==1) {
            binding.profilePic.setImageBitmap(bitmap);
            updateProfilePic();
            Log.e("checkPicToSet","setProfilePic");
        } else if (setSelfPhotoWithID==1) {

            iv_selfPhotoWithID.setVisibility(View.GONE);
            ll_uploadedSelfPhotoWithID.setVisibility(View.VISIBLE);
            tv_headerselfPhotoWithID.setTextColor(getColor(R.color.walletBackground));

            iv_uploadedSelfPhotoWithID.setImageBitmap(bitmap);

            Log.e("checkPicToSet","setSelfPhotoWithID");
        } else if (setIdFrontImage==1) {

            iv_idFrontImage.setVisibility(View.GONE);
            ll_uploadedIdFrontImage.setVisibility(View.VISIBLE);
            tv_headerIdFrontImage.setTextColor(getColor(R.color.walletBackground));

            iv_uploadedIdFrontImage.setImageBitmap(bitmap);

            Log.e("checkPicToSet","setIdFrontImage");
        } else if (setIdBackImage==1) {

            iv_idBackImage.setVisibility(View.GONE);
            ll_uploadedIdBackImage.setVisibility(View.VISIBLE);
            tv_headerIdBackImage.setTextColor(getColor(R.color.walletBackground));

            iv_uploadedIdBackImage.setImageBitmap(bitmap);

            Log.e("checkPicToSet","setIdBackImage");
        } else if (setPanCardImage==1) {

            iv_panCardImage.setVisibility(View.GONE);
            ll_uploadedPanCardImage.setVisibility(View.VISIBLE);
            tv_headerPanCardImage.setTextColor(getColor(R.color.walletBackground));

            iv_uploadedPanCardImage.setImageBitmap(bitmap);
            Log.e("checkPicToSet","setPanCardImage");
        }
    }

    private void checkPicToSet(Uri uri) {
        if (setProfilePic==1) {
            Log.e("checkPicToSet","setProfilePic");
            binding.profilePic.setImageURI(uri);
            updateProfilePic();
        } else if (setSelfPhotoWithID==1) {

            iv_selfPhotoWithID.setVisibility(View.GONE);
            ll_uploadedSelfPhotoWithID.setVisibility(View.VISIBLE);
            tv_headerselfPhotoWithID.setTextColor(getColor(R.color.walletBackground));

            iv_uploadedSelfPhotoWithID.setImageURI(uri);

            Log.e("checkPicToSet","setSelfPhotoWithID");
        } else if (setIdFrontImage==1) {

            iv_idFrontImage.setVisibility(View.GONE);
            ll_uploadedIdFrontImage.setVisibility(View.VISIBLE);
            tv_headerIdFrontImage.setTextColor(getColor(R.color.walletBackground));

            iv_uploadedIdFrontImage.setImageURI(uri);

            Log.e("checkPicToSet","setIdFrontImage");
        } else if (setIdBackImage==1) {

            iv_idBackImage.setVisibility(View.GONE);
            ll_uploadedIdBackImage.setVisibility(View.VISIBLE);
            tv_headerIdBackImage.setTextColor(getColor(R.color.walletBackground));

            iv_uploadedIdBackImage.setImageURI(uri);

            Log.e("checkPicToSet","setIdBackImage");
        } else if (setPanCardImage==1) {

            iv_panCardImage.setVisibility(View.GONE);
            ll_uploadedPanCardImage.setVisibility(View.VISIBLE);
            tv_headerPanCardImage.setTextColor(getColor(R.color.walletBackground));

            iv_uploadedPanCardImage.setImageURI(uri);
            Log.e("checkPicToSet","setPanCardImage");
        }
    }

    private void updateProfilePic() {
        hideKeypad();
        dialogProfile = BaseUtils.showProgressDialog(MyAccountScreen.this,"Please wait");
        dialogProfile.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(MyAccountScreen.this).create(AuthApiHelper.class);
        UpdateProfilePicReq updateProfilePicReq = new UpdateProfilePicReq();
        updateProfilePicReq.setLoginToken(SharedPrefUtils.getFromPrefs(MyAccountScreen.this,SharedPrefUtils.LoginID));
        updateProfilePicReq.setMemberImg("data:image/jpeg;base64,"+getProfilePicString(binding.profilePic));

        Single<JsonObject> observable = authApiHelper.updateProfilePic(updateProfilePicReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject getProfileDetailRes) {
                        dialogProfile.dismiss();
                        if (getProfileDetailRes.get("Flag").getAsString().equalsIgnoreCase("success")
                                && getProfileDetailRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {
                            BaseUtils.customToast(MyAccountScreen.this, "Profile pic updated successfully.");
                        } else if(getProfileDetailRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(MyAccountScreen.this,getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(MyAccountScreen.this,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(MyAccountScreen.this,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(MyAccountScreen.this, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else BaseUtils.customToast(MyAccountScreen.this, getProfileDetailRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        dialogProfile.dismiss();
                        BaseUtils.customToast(MyAccountScreen.this, getResources().getString(R.string.errorOccur));
                    }
                });
    }

    public String getProfilePicString(ImageView imageView) {
        String profilePicString = "";
        try {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            profilePicString = Base64.encodeToString(byteArray, Base64.DEFAULT);
            return profilePicString;
        }
        catch (Exception e) { e.printStackTrace(); }
        return profilePicString;
    }

    private void uploadKycIdProofDialog(String dialogIdType, String dialogIdRef, final String dialogDob, final String dialogPanNo,
                                        String dialogSelfImage, String dialogFrontImage, String dialogBackImage, final String dialogPanImage) {
        View updateKycIdProofLayout = LayoutInflater.from(MyAccountScreen.this).inflate(R.layout.dialog_updatekyc_idproof,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(MyAccountScreen.this);
        builder.setView(updateKycIdProofLayout);

        alertDialogIdProof = builder.create();
        alertDialogIdProof.show();
        //Objects.requireNonNull(alertDialogIdProof.getWindow()).setLayout(700,1200);

        final TextView frame_idProofType = updateKycIdProofLayout.findViewById(R.id.frame_idProofType);
        final TextView frame_idProofRef = updateKycIdProofLayout.findViewById(R.id.frame_idProofRef);

        final EditText ed_idProofType = updateKycIdProofLayout.findViewById(R.id.ed_idProofType);
        final EditText ed_idProofRef = updateKycIdProofLayout.findViewById(R.id.ed_idProofRef);

        iv_selfPhotoWithID = updateKycIdProofLayout.findViewById(R.id.iv_selfPhotoWithID);
        iv_idFrontImage = updateKycIdProofLayout.findViewById(R.id.iv_idFrontImage);
        iv_idBackImage = updateKycIdProofLayout.findViewById(R.id.iv_idBackImage);

        ll_uploadedSelfPhotoWithID = updateKycIdProofLayout.findViewById(R.id.ll_uploadedSelfPhotoWithID);
        ll_uploadedIdFrontImage = updateKycIdProofLayout.findViewById(R.id.ll_uploadedIdFrontImage);
        ll_uploadedIdBackImage = updateKycIdProofLayout.findViewById(R.id.ll_uploadedIdBackImage);

        iv_uploadedSelfPhotoWithID = updateKycIdProofLayout.findViewById(R.id.iv_uploadedSelfPhotoWithID);
        iv_uploadedIdFrontImage = updateKycIdProofLayout.findViewById(R.id.iv_uploadedIdFrontImage);
        iv_uploadedIdBackImage = updateKycIdProofLayout.findViewById(R.id.iv_uploadedIdBackImage);

        tv_headerselfPhotoWithID = updateKycIdProofLayout.findViewById(R.id.tv_headerselfPhotoWithID);
        tv_headerIdFrontImage = updateKycIdProofLayout.findViewById(R.id.tv_headerIdFrontImage);
        tv_headerIdBackImage = updateKycIdProofLayout.findViewById(R.id.tv_headerIdBackImage);

        TextView tv_changeSelfPhotoWithID = updateKycIdProofLayout.findViewById(R.id.tv_changeSelfPhotoWithID);
        tv_changeSelfPhotoWithID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_uploadedSelfPhotoWithID.setVisibility(View.GONE);
                iv_selfPhotoWithID.setVisibility(View.VISIBLE);
            }
        });

        TextView tv_changeIdFrontImage = updateKycIdProofLayout.findViewById(R.id.tv_changeIdFrontImage);
        tv_changeIdFrontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_uploadedIdFrontImage.setVisibility(View.GONE);
                iv_idFrontImage.setVisibility(View.VISIBLE);
            }
        });

        TextView tv_changeIdBackImage = updateKycIdProofLayout.findViewById(R.id.tv_changeIdBackImage);
        tv_changeIdBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_uploadedIdBackImage.setVisibility(View.GONE);
                iv_idBackImage.setVisibility(View.VISIBLE);
            }
        });

        ImageView iv_cross = updateKycIdProofLayout.findViewById(R.id.iv_cross);
        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseUtils.hideKeypad(MyAccountScreen.this);
                alertDialogIdProof.dismiss();
                BaseUtils.customToast(MyAccountScreen.this,"cancelled.");
            }
        });

        iv_selfPhotoWithID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfilePic=0;
                setSelfPhotoWithID=1;
                setIdFrontImage=0;
                setIdBackImage=0;
                setPanCardImage=0;
                CropImage.startPickImageActivity(MyAccountScreen.this);
            }
        });

        iv_idFrontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfilePic=0;
                setSelfPhotoWithID=0;
                setIdFrontImage=1;
                setIdBackImage=0;
                setPanCardImage=0;
                CropImage.startPickImageActivity(MyAccountScreen.this);
            }
        });

        iv_idBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfilePic=0;
                setSelfPhotoWithID=0;
                setIdFrontImage=0;
                setIdBackImage=1;
                setPanCardImage=0;
                CropImage.startPickImageActivity(MyAccountScreen.this);
            }
        });

        if (!dialogIdType.isEmpty()) ed_idProofType.setText(dialogIdType);
        if (!dialogIdRef.isEmpty()) ed_idProofRef.setText(dialogIdRef);
        if (!dialogSelfImage.isEmpty()) {
            iv_selfPhotoWithID.setVisibility(View.GONE);
            ll_uploadedSelfPhotoWithID.setVisibility(View.VISIBLE);
            Picasso.get().load(dialogSelfImage).placeholder(R.drawable.camera).error(R.drawable.camera).into(iv_uploadedSelfPhotoWithID);
        }
        if (!dialogFrontImage.isEmpty()) {
            iv_idFrontImage.setVisibility(View.GONE);
            ll_uploadedIdFrontImage.setVisibility(View.VISIBLE);
            Picasso.get().load(dialogFrontImage).placeholder(R.drawable.camera).error(R.drawable.camera).into(iv_uploadedIdFrontImage);
        }
        if (!dialogBackImage.isEmpty()) {
            iv_idBackImage.setVisibility(View.GONE);
            ll_uploadedIdBackImage.setVisibility(View.VISIBLE);
            Picasso.get().load(dialogBackImage).placeholder(R.drawable.camera).error(R.drawable.camera).into(iv_uploadedIdBackImage);
        }

        FrameLayout frame_next = updateKycIdProofLayout.findViewById(R.id.frame_next);
        frame_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_idProofType.getText().toString().isEmpty()) {
                    ed_idProofType.requestFocus();
                    ed_idProofType.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_idProofType.setTextColor(getColor(R.color.deepRed));
                    BaseUtils.customToast(MyAccountScreen.this,"ID-Proof type is required.");
                } else if (ed_idProofRef.getText().toString().isEmpty()) {
                    ed_idProofRef.requestFocus();
                    ed_idProofRef.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_idProofRef.setTextColor(getColor(R.color.deepRed));
                    BaseUtils.customToast(MyAccountScreen.this,"ID-Proof reference number is required.");
                } else if (ll_uploadedSelfPhotoWithID.getVisibility()==View.GONE) {
                    tv_headerselfPhotoWithID.setTextColor(getColor(R.color.deepRed));
                    BaseUtils.customToast(MyAccountScreen.this,"Self Photograph With ID Card is required.");
                } else if (ll_uploadedIdFrontImage.getVisibility()==View.GONE) {
                    tv_headerIdFrontImage.setTextColor(getColor(R.color.deepRed));
                    BaseUtils.customToast(MyAccountScreen.this,"ID Card Front Image is required.");
                } else if (ll_uploadedIdBackImage.getVisibility()==View.GONE) {
                    tv_headerIdBackImage.setTextColor(getColor(R.color.deepRed));
                    BaseUtils.customToast(MyAccountScreen.this,"ID Card Back Image is required.");
                } else {
                    String idType = ed_idProofType.getText().toString();
                    String idRef = ed_idProofRef.getText().toString();
                    uploadKycPanCardDialog(idType, idRef, dialogDob, dialogPanNo, dialogPanImage);
                }
            }
        });

        ed_idProofType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ed_idProofType.setBackground(getDrawable(R.drawable.shape_bottomline_themelightcolor));
                frame_idProofType.setTextColor(getColor(R.color.walletBackground));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ed_idProofRef.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ed_idProofRef.setBackground(getDrawable(R.drawable.shape_bottomline_themelightcolor));
                frame_idProofRef.setTextColor(getColor(R.color.walletBackground));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void uploadKycPanCardDialog(final String idType, final String idRef, String dialogDob, String dialogPanNo, String dialogPanImage) {
        View updateKycPanCardLayout = LayoutInflater.from(MyAccountScreen.this).inflate(R.layout.dialog_updatekyc_pancard,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(MyAccountScreen.this);
        builder.setView(updateKycPanCardLayout);

        alertDialogPanCard = builder.create();
        alertDialogPanCard.show();
        //Objects.requireNonNull(alertDialogPanCard.getWindow()).setLayout(700,1200);

        final TextView frame_dob = updateKycPanCardLayout.findViewById(R.id.frame_dob);
        final TextView frame_panCardNo = updateKycPanCardLayout.findViewById(R.id.frame_panCardNo);

        final EditText ed_dob = updateKycPanCardLayout.findViewById(R.id.ed_dob);
        final EditText ed_panCardNo = updateKycPanCardLayout.findViewById(R.id.ed_panCardNo);

        iv_panCardImage = updateKycPanCardLayout.findViewById(R.id.iv_panCardImage);
        ll_uploadedPanCardImage = updateKycPanCardLayout.findViewById(R.id.ll_uploadedPanCardImage);
        iv_uploadedPanCardImage = updateKycPanCardLayout.findViewById(R.id.iv_uploadedPanCardImage);
        tv_headerPanCardImage = updateKycPanCardLayout.findViewById(R.id.tv_headerPanCardImage);

        TextView tv_changePanCardImage = updateKycPanCardLayout.findViewById(R.id.tv_changePanCardImage);
        tv_changePanCardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_uploadedPanCardImage.setVisibility(View.GONE);
                iv_panCardImage.setVisibility(View.VISIBLE);
            }
        });

        ImageView iv_cross = updateKycPanCardLayout.findViewById(R.id.iv_cross);
        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogIdProof.dismiss();
                alertDialogPanCard.dismiss();
                BaseUtils.customToast(MyAccountScreen.this,"cancelled.");
            }
        });

        iv_panCardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfilePic=0;
                setSelfPhotoWithID=0;
                setIdFrontImage=0;
                setIdBackImage=0;
                setPanCardImage=1;
                CropImage.startPickImageActivity(MyAccountScreen.this);
            }
        });

        ImageView iv_calender = updateKycPanCardLayout.findViewById(R.id.iv_calender);
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
                            df = new SimpleDateFormat("yyyy / MMM / dd");
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
                        MyAccountScreen.this, datePickerListener, 1980, 01, 01);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        ImageView iv_panCardImage = updateKycPanCardLayout.findViewById(R.id.iv_panCardImage);
        iv_panCardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfilePic=0;
                setSelfPhotoWithID=0;
                setIdFrontImage=0;
                setIdBackImage=0;
                setPanCardImage=1;
                CropImage.startPickImageActivity(MyAccountScreen.this);
            }
        });

        if (!dialogDob.isEmpty()) ed_dob.setText(dialogDob);
        if (!dialogPanNo.isEmpty()) ed_panCardNo.setText(dialogPanNo);
        if (!dialogPanImage.isEmpty()) {
            iv_panCardImage.setVisibility(View.GONE);
            ll_uploadedPanCardImage.setVisibility(View.VISIBLE);
            Picasso.get().load(dialogPanImage).placeholder(R.drawable.camera).error(R.drawable.camera).into(iv_uploadedPanCardImage);
        }

        FrameLayout frame_submit = updateKycPanCardLayout.findViewById(R.id.frame_submit);
        frame_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_dob.getText().toString().isEmpty()) {
                    ed_dob.requestFocus();
                    ed_dob.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_dob.setTextColor(getColor(R.color.deepRed));
                    BaseUtils.customToast(MyAccountScreen.this,"ID-Proof type is required.");
                } else if (ed_panCardNo.getText().toString().isEmpty()) {
                    ed_panCardNo.requestFocus();
                    ed_panCardNo.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_panCardNo.setTextColor(getColor(R.color.deepRed));
                    BaseUtils.customToast(MyAccountScreen.this,"ID-Proof reference number is required.");
                } else if (ll_uploadedPanCardImage.getVisibility()==View.GONE) {
                    tv_headerPanCardImage.setTextColor(getColor(R.color.deepRed));
                    BaseUtils.customToast(MyAccountScreen.this,"PAN Card Image is required.");
                } else {
                    String dob = ed_dob.getText().toString();
                    String panNo = ed_panCardNo.getText().toString();
                    updateKYC(idType, idRef, dob, panNo);
                }
            }
        });

        ed_panCardNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ed_panCardNo.setBackground(getDrawable(R.drawable.shape_bottomline_themelightcolor));
                frame_panCardNo.setTextColor(getColor(R.color.walletBackground));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ed_dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ed_dob.setBackground(getDrawable(R.drawable.shape_bottomline_themelightcolor));
                frame_dob.setTextColor(getColor(R.color.walletBackground));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void updateKYC(String idType, String idRef, String dob, String panNo) {
        hideKeypad();
        dialogKyc = BaseUtils.showProgressDialog(MyAccountScreen.this,"Please wait");
        dialogKyc.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(MyAccountScreen.this).create(AuthApiHelper.class);
        UploadKycReq uploadKycReq = new UploadKycReq();
        uploadKycReq.setLoginToken(SharedPrefUtils.getFromPrefs(MyAccountScreen.this,SharedPrefUtils.LoginID));
        uploadKycReq.setSelfPhotoGraphWithIdCard("data:image/jpeg;base64,"+getProfilePicString(iv_uploadedSelfPhotoWithID));
        uploadKycReq.setIdCardFrontImage("data:image/jpeg;base64,"+getProfilePicString(iv_uploadedIdFrontImage));
        uploadKycReq.setIdCardBackImage("data:image/jpeg;base64,"+getProfilePicString(iv_uploadedIdBackImage));
        //uploadKycReq.setPanCardImage("data:image/jpeg;base64,"+getProfilePicString(iv_uploadedPanCardImage));
        uploadKycReq.setPanCardImage("");
        uploadKycReq.setPanNo(panNo);
        uploadKycReq.setDOB(dob);
        uploadKycReq.setIdProofType(idType);
        uploadKycReq.setIdProofRefNo(idRef);

        Single<JsonObject> observable = authApiHelper.updateKYC(uploadKycReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject getProfileDetailRes) {
                        dialogKyc.dismiss();
                        alertDialogIdProof.dismiss();
                        //alertDialogPanCard.dismiss();

                        if (getProfileDetailRes.get("Flag").getAsString().equalsIgnoreCase("success")
                                && getProfileDetailRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {
                            getKycDetails();
                            BaseUtils.customToast(MyAccountScreen.this, "KYC uploaded");
                        } else if(getProfileDetailRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(MyAccountScreen.this,getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(MyAccountScreen.this,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(MyAccountScreen.this,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(MyAccountScreen.this, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            BaseUtils.customToast(MyAccountScreen.this, getProfileDetailRes.get("Message").getAsString());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        dialogKyc.dismiss();
                        BaseUtils.customToast(MyAccountScreen.this, getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private void getKycDetails() {
        BaseUtils.hideKeypad(MyAccountScreen.this);
        AuthApiHelper authApiHelper = ApiClient.getClient(MyAccountScreen.this).create(AuthApiHelper.class);
        LoginTokenReq loginTokenReq = new LoginTokenReq();
        loginTokenReq.setLoginToken(SharedPrefUtils.getFromPrefs(MyAccountScreen.this,SharedPrefUtils.LoginID));

        Single<JsonObject> observable = authApiHelper.getKycDetails(loginTokenReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject getKycDetailRes) {
                        if (getKycDetailRes.get("Flag").getAsString().equalsIgnoreCase("success")
                                && getKycDetailRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {

                            JsonObject jObject = getKycDetailRes.getAsJsonObject("Data");
                            final Type listType = new TypeToken<KycData>() {}.getType();
                            final KycData kycData = new Gson().fromJson(jObject, listType);

                            //0 - pending   (yellow)	-> editable
                            //1 - approved  (red)	    -> non-editable
                            //2 - rejected  (green) 	-> editable

                            String status="";
                            if (kycData.getKycStatus()==0) {
                                status = "<font color='"+getResources().getColor(R.color.deepYellow)+"'>pending</font>";
                                binding.ivEditKyc.setVisibility(View.VISIBLE);
                            } else if (kycData.getKycStatus()==1) {
                                status = "<font color='"+getResources()
                                        .getColor(R.color.deepGreen)+"'>approved</font>";
                                binding.ivEditKyc.setVisibility(View.GONE);
                            } else if (kycData.getKycStatus()==2) {
                                status = "<font color='"+getResources().getColor(R.color.deepRed)+"'>rejected</font>";
                                binding.ivEditKyc.setVisibility(View.VISIBLE);
                            }

                            binding.tvKycHeader.setText(Html.fromHtml("KYC ( "+status+" )"));

                            if (kycData.getIdProofType().isEmpty()) binding.tvKycIdProofType.setText("-");
                            else binding.tvKycIdProofType.setText(kycData.getIdProofType());

                            if (kycData.getIdProofRefNo().isEmpty()) binding.tvKycIdProofReference.setText("-");
                            else binding.tvKycIdProofReference.setText(kycData.getIdProofRefNo());

                            if (kycData.getDob().isEmpty()) binding.tvKycDob.setText("-");
                            else binding.tvKycDob.setText(kycData.getDob());

                            if (kycData.getPanNo().isEmpty()) binding.tvKycPanNo.setText("-");
                            else binding.tvKycPanNo.setText(kycData.getPanNo());

                            if (!kycData.getSelfPhotoGraphWithIdCard().isEmpty()) {
                                binding.ivKycSelfPhoto.setVisibility(View.VISIBLE);
                                Picasso.get().load(kycData.getSelfPhotoGraphWithIdCard()).placeholder(R.drawable.camera).error(R.drawable.camera).into(binding.ivKycSelfPhoto);
                            }
                            else binding.ivKycSelfPhoto.setVisibility(View.GONE);

                            if (!kycData.getIdCardFrontImage().isEmpty()) {
                                binding.ivKycIdFrontPhoto.setVisibility(View.VISIBLE);
                                Picasso.get().load(kycData.getIdCardFrontImage()).placeholder(R.drawable.camera).error(R.drawable.camera).into(binding.ivKycIdFrontPhoto);
                            }
                            else binding.ivKycIdFrontPhoto.setVisibility(View.GONE);

                            if (!kycData.getIdCardBackImage().isEmpty()) {
                                binding.ivKycIdBackPhoto.setVisibility(View.VISIBLE);
                                Picasso.get().load(kycData.getIdCardBackImage()).placeholder(R.drawable.camera).error(R.drawable.camera).into(binding.ivKycIdBackPhoto);
                            }
                            else binding.ivKycIdBackPhoto.setVisibility(View.GONE);

                            if (!kycData.getPanCardImage().isEmpty()) {
                                binding.ivKycPanPhoto.setVisibility(View.VISIBLE);
                                Picasso.get().load(kycData.getPanCardImage()).placeholder(R.drawable.camera).error(R.drawable.camera).into(binding.ivKycPanPhoto);
                            }
                            else binding.ivKycPanPhoto.setVisibility(View.GONE);

                            binding.ivEditKyc.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String dialogIdType = kycData.getIdProofType();
                                    String dialogIdRef = kycData.getIdProofRefNo();
                                    String dialogDob = kycData.getDob();
                                    String dialogPanNo = kycData.getPanNo();
                                    String dialogSelfImage = kycData.getSelfPhotoGraphWithIdCard();
                                    String dialogFrontImage = kycData.getIdCardFrontImage();
                                    String dialogBackImage = kycData.getIdCardBackImage();
                                    String dialogPanImage = kycData.getPanCardImage();
                                    uploadKycDialog(dialogIdType, dialogIdRef, dialogDob, dialogPanNo, dialogSelfImage, dialogFrontImage, dialogBackImage, dialogPanImage);
                                }
                            });

                        } else if(getKycDetailRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(MyAccountScreen.this,getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(MyAccountScreen.this,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(MyAccountScreen.this,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(MyAccountScreen.this, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else BaseUtils.customToast(MyAccountScreen.this, getKycDetailRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        BaseUtils.customToast(MyAccountScreen.this, getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private void uploadKycDialog(String dialogIdType, String dialogIdRef, final String dialogDob, final String dialogPanNo,
                                        String dialogSelfImage, String dialogFrontImage, String dialogBackImage, final String dialogPanImage) {
        View updateKycIdProofLayout = LayoutInflater.from(MyAccountScreen.this).inflate(R.layout.dialog_updatekyc_idproof,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(MyAccountScreen.this);
        builder.setView(updateKycIdProofLayout);

        alertDialogIdProof = builder.create();
        alertDialogIdProof.show();
        //Objects.requireNonNull(alertDialogIdProof.getWindow()).setLayout(700,1200);

        final TextView frame_idProofType = updateKycIdProofLayout.findViewById(R.id.frame_idProofType);
        final TextView frame_idProofRef = updateKycIdProofLayout.findViewById(R.id.frame_idProofRef);

        final EditText ed_idProofType = updateKycIdProofLayout.findViewById(R.id.ed_idProofType);
        final EditText ed_idProofRef = updateKycIdProofLayout.findViewById(R.id.ed_idProofRef);

        final Spinner spn_idProofType = updateKycIdProofLayout.findViewById(R.id.spn_idProofType);

        iv_selfPhotoWithID = updateKycIdProofLayout.findViewById(R.id.iv_selfPhotoWithID);
        iv_idFrontImage = updateKycIdProofLayout.findViewById(R.id.iv_idFrontImage);
        iv_idBackImage = updateKycIdProofLayout.findViewById(R.id.iv_idBackImage);

        ll_uploadedSelfPhotoWithID = updateKycIdProofLayout.findViewById(R.id.ll_uploadedSelfPhotoWithID);
        ll_uploadedIdFrontImage = updateKycIdProofLayout.findViewById(R.id.ll_uploadedIdFrontImage);
        ll_uploadedIdBackImage = updateKycIdProofLayout.findViewById(R.id.ll_uploadedIdBackImage);

        iv_uploadedSelfPhotoWithID = updateKycIdProofLayout.findViewById(R.id.iv_uploadedSelfPhotoWithID);
        iv_uploadedIdFrontImage = updateKycIdProofLayout.findViewById(R.id.iv_uploadedIdFrontImage);
        iv_uploadedIdBackImage = updateKycIdProofLayout.findViewById(R.id.iv_uploadedIdBackImage);

        tv_headerselfPhotoWithID = updateKycIdProofLayout.findViewById(R.id.tv_headerselfPhotoWithID);
        tv_headerIdFrontImage = updateKycIdProofLayout.findViewById(R.id.tv_headerIdFrontImage);
        tv_headerIdBackImage = updateKycIdProofLayout.findViewById(R.id.tv_headerIdBackImage);

        TextView tv_changeSelfPhotoWithID = updateKycIdProofLayout.findViewById(R.id.tv_changeSelfPhotoWithID);
        tv_changeSelfPhotoWithID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_uploadedSelfPhotoWithID.setVisibility(View.GONE);
                iv_selfPhotoWithID.setVisibility(View.VISIBLE);
            }
        });

        TextView tv_changeIdFrontImage = updateKycIdProofLayout.findViewById(R.id.tv_changeIdFrontImage);
        tv_changeIdFrontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_uploadedIdFrontImage.setVisibility(View.GONE);
                iv_idFrontImage.setVisibility(View.VISIBLE);
            }
        });

        TextView tv_changeIdBackImage = updateKycIdProofLayout.findViewById(R.id.tv_changeIdBackImage);
        tv_changeIdBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_uploadedIdBackImage.setVisibility(View.GONE);
                iv_idBackImage.setVisibility(View.VISIBLE);
            }
        });

        ImageView iv_cross = updateKycIdProofLayout.findViewById(R.id.iv_cross);
        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseUtils.hideKeypad(MyAccountScreen.this);
                alertDialogIdProof.dismiss();
                BaseUtils.customToast(MyAccountScreen.this,"cancelled.");
            }
        });

        iv_selfPhotoWithID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MyAccountScreen.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MyAccountScreen.this,
                            new String[]{Manifest.permission.CAMERA},
                            200);
                }else {
                    setProfilePic=0;
                    setSelfPhotoWithID=1;
                    setIdFrontImage=0;
                    setIdBackImage=0;
                    setPanCardImage=0;
                    CropImage.startPickImageActivity(MyAccountScreen.this);
                }
            }
        });

        iv_idFrontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MyAccountScreen.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MyAccountScreen.this,
                            new String[]{Manifest.permission.CAMERA},
                            200);
                }else {
                    setProfilePic=0;
                    setSelfPhotoWithID=0;
                    setIdFrontImage=1;
                    setIdBackImage=0;
                    setPanCardImage=0;
                    CropImage.startPickImageActivity(MyAccountScreen.this);
                }
            }
        });

        iv_idBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MyAccountScreen.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MyAccountScreen.this,
                            new String[]{Manifest.permission.CAMERA},
                            200);
                }else {
                    setProfilePic=0;
                    setSelfPhotoWithID=0;
                    setIdFrontImage=0;
                    setIdBackImage=1;
                    setPanCardImage=0;
                    CropImage.startPickImageActivity(MyAccountScreen.this);
                }
            }
        });

        if (!dialogIdType.isEmpty()) ed_idProofType.setText(dialogIdType);
        if (!dialogIdRef.isEmpty()) ed_idProofRef.setText(dialogIdRef);
        if (!dialogSelfImage.isEmpty()) {
            iv_selfPhotoWithID.setVisibility(View.GONE);
            ll_uploadedSelfPhotoWithID.setVisibility(View.VISIBLE);
            Picasso.get().load(dialogSelfImage).placeholder(R.drawable.camera).error(R.drawable.camera).into(iv_uploadedSelfPhotoWithID);
        }
        if (!dialogFrontImage.isEmpty()) {
            iv_idFrontImage.setVisibility(View.GONE);
            ll_uploadedIdFrontImage.setVisibility(View.VISIBLE);
            Picasso.get().load(dialogFrontImage).placeholder(R.drawable.camera).error(R.drawable.camera).into(iv_uploadedIdFrontImage);
        }
        if (!dialogBackImage.isEmpty()) {
            iv_idBackImage.setVisibility(View.GONE);
            ll_uploadedIdBackImage.setVisibility(View.VISIBLE);
            Picasso.get().load(dialogBackImage).placeholder(R.drawable.camera).error(R.drawable.camera).into(iv_uploadedIdBackImage);
        }

        FrameLayout frame_next = updateKycIdProofLayout.findViewById(R.id.frame_next);
        frame_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (ed_idProofType.getText().toString().isEmpty()) {
                    ed_idProofType.requestFocus();
                    ed_idProofType.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_idProofType.setTextColor(getColor(R.color.deepRed));
                    BaseUtils.customToast(MyAccountScreen.this,"ID-Proof type is required.");
                } else */

                if (ed_idProofRef.getText().toString().isEmpty()) {
                    ed_idProofRef.requestFocus();
                    ed_idProofRef.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_idProofRef.setTextColor(getColor(R.color.deepRed));
                    BaseUtils.customToast(MyAccountScreen.this,"ID-Proof reference number is required.");
                } else if (ll_uploadedSelfPhotoWithID.getVisibility()==View.GONE) {
                    tv_headerselfPhotoWithID.setTextColor(getColor(R.color.deepRed));
                    BaseUtils.customToast(MyAccountScreen.this,"Self Photograph With ID Card is required.");
                } else if (ll_uploadedIdFrontImage.getVisibility()==View.GONE) {
                    tv_headerIdFrontImage.setTextColor(getColor(R.color.deepRed));
                    BaseUtils.customToast(MyAccountScreen.this,"ID Card Front Image is required.");
                } else if (ll_uploadedIdBackImage.getVisibility()==View.GONE) {
                    tv_headerIdBackImage.setTextColor(getColor(R.color.deepRed));
                    BaseUtils.customToast(MyAccountScreen.this,"ID Card Back Image is required.");
                } else {
                    String idType = ed_idProofType.getText().toString();
                    String idRef = ed_idProofRef.getText().toString();

                    //Toast.makeText(MyAccountScreen.this, "id - "+spn_idProofType.getSelectedItemPosition(), Toast.LENGTH_SHORT).show();

                    if (spn_idProofType.getSelectedItemPosition()==0) {
                        updateKYC("Passport", idRef, "", "");
                    } else if (spn_idProofType.getSelectedItemPosition()==1) {
                        updateKYC("Driving Licence", idRef, "", "");
                    } else if (spn_idProofType.getSelectedItemPosition()==2) {
                        updateKYC("National Identitification", idRef, "", "");
                    }
                }
            }
        });

        ed_idProofType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ed_idProofType.setBackground(getDrawable(R.drawable.shape_bottomline_themelightcolor));
                frame_idProofType.setTextColor(getColor(R.color.walletBackground));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ed_idProofRef.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ed_idProofRef.setBackground(getDrawable(R.drawable.shape_bottomline_themelightcolor));
                frame_idProofRef.setTextColor(getColor(R.color.walletBackground));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
