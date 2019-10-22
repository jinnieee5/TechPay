package crypto.base.baseexchange.views;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.binders.CreateTicketBinder;
import crypto.base.baseexchange.databinding.LayoutCreateTicketBinding;
import crypto.base.baseexchange.utils.BaseActivity;
import crypto.base.baseexchange.utils.BaseUtils;

public class CreateTicketScreen extends BaseActivity {
    private LayoutCreateTicketBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_create_ticket);
        final CreateTicketBinder binder = new CreateTicketBinder(this,binding);
        binding.setCreateTicketScreen(binder);

        binding.tvUploadAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.llTickets.getVisibility()==View.VISIBLE
                            && binding.frameTicket1.getVisibility()==View.VISIBLE
                            && binding.frameTicket2.getVisibility()==View.VISIBLE
                            && binding.frameTicket3.getVisibility()==View.VISIBLE) {
                        BaseUtils.customToast(CreateTicketScreen.this,"you can add max 3 attachments");
                } else CropImage.startPickImageActivity(CreateTicketScreen.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            else startCropImageActivity(imageUri);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(result.getUri().toString()));

                    setImageToView(bitmap);
                    Log.e("CreateTicketScreen","try block");
                }
                catch (IOException e) {
                    setImageToView(result.getUri());
                    Log.e("CreateTicketScreen","catch block");
                }
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(CreateTicketScreen.this);
    }

    private void setImageToView(Bitmap bitmap) {
        if (binding.llTickets.getVisibility()==View.VISIBLE) {
            if (binding.frameTicket1.getVisibility()==View.GONE) {
                binding.frameTicket1.setVisibility(View.VISIBLE);
                binding.ivTicket1.setImageBitmap(bitmap);
            } else if (binding.frameTicket2.getVisibility()==View.GONE) {
                binding.frameTicket2.setVisibility(View.VISIBLE);
                binding.ivTicket2.setImageBitmap(bitmap);
            } else if (binding.frameTicket3.getVisibility()==View.GONE) {
                binding.frameTicket3.setVisibility(View.VISIBLE);
                binding.ivTicket3.setImageBitmap(bitmap);
            }
        } else if (binding.llTickets.getVisibility()==View.GONE) {
            binding.llTickets.setVisibility(View.VISIBLE);
            binding.frameTicket1.setVisibility(View.VISIBLE);
            binding.frameTicket2.setVisibility(View.GONE);
            binding.frameTicket3.setVisibility(View.GONE);

            binding.ivTicket1.setImageBitmap(bitmap);
        }
    }

    private void setImageToView(Uri uri) {
        if (binding.llTickets.getVisibility()==View.VISIBLE) {
            if (binding.frameTicket1.getVisibility()==View.GONE) {
                binding.frameTicket1.setVisibility(View.VISIBLE);
                binding.ivTicket1.setImageURI(uri);
            } else if (binding.frameTicket2.getVisibility()==View.GONE) {
                binding.frameTicket2.setVisibility(View.VISIBLE);
                binding.ivTicket2.setImageURI(uri);
            } else if (binding.frameTicket3.getVisibility()==View.GONE) {
                binding.frameTicket3.setVisibility(View.VISIBLE);
                binding.ivTicket3.setImageURI(uri);
            }
        } else if (binding.llTickets.getVisibility()==View.GONE) {
            binding.llTickets.setVisibility(View.VISIBLE);
            binding.frameTicket1.setVisibility(View.VISIBLE);
            binding.frameTicket2.setVisibility(View.GONE);
            binding.frameTicket3.setVisibility(View.GONE);

            binding.ivTicket1.setImageURI(uri);
        }
    }
}
