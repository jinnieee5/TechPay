package crypto.base.baseexchange.views;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.zxing.Result;
import androidx.annotation.Nullable;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.utils.BaseActivity;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQRCodeScreen extends BaseActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_scan_qrcode);

        ViewGroup contentFrame = findViewById(R.id.frame_scanner);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final ImageView iv_flashlight = findViewById(R.id.iv_flashlight);
        iv_flashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mScannerView.getFlash()) {
                    mScannerView.setFlash(false);
                    iv_flashlight.setImageDrawable(getDrawable(R.drawable.flash_on));
                } else {
                    mScannerView.setFlash(true);
                    iv_flashlight.setImageDrawable(getDrawable(R.drawable.flash_off));
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        if (rawResult.getText().isEmpty()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScannerView.resumeCameraPreview(ScanQRCodeScreen.this);
                }
            }, 2000);
        } else {
            String address = rawResult.getText();
            String walletAddress = address;
            String amount = "";

            if (address.indexOf("?") > 0) {
                walletAddress = "" + address.substring(0, address.indexOf("?"));

                address = address.replaceAll("amount=","%");
                amount = "" + address.substring(address.indexOf("%") + 1);
            }

            SharedPrefUtils.saveToPrefs(ScanQRCodeScreen.this,SharedPrefUtils.tempWalletAddress,walletAddress);
            SharedPrefUtils.saveToPrefs(ScanQRCodeScreen.this,SharedPrefUtils.tempTransferAmount,amount);
            finish();
        }
    }
}
