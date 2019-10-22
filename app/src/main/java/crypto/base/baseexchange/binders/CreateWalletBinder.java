package crypto.base.baseexchange.binders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.databinding.LayoutCreateWalletBinding;

public class CreateWalletBinder {
    private Context context;
    private LayoutCreateWalletBinding binding;

    public CreateWalletBinder(final Context context, LayoutCreateWalletBinding binding) {
        this.context = context;
        this.binding = binding;

        setHeader();
        String bottomText = "<font color='"+context.getResources().getColor(R.color.iconColor)+"'>"+context.getResources().getString(R.string.msg_createWallet)+"</font> " +
                "<font color='"+context.getResources().getColor(R.color.deepYellow)+"'>"+context.getResources().getString(R.string.app_name)+"</font>";
        binding.tvBottomText.setText(Html.fromHtml(bottomText));
        binding.tvTos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://techpay.io/terms-of-use")
                );
                context.startActivity(i);
            }
        });
    }

    private void setHeader() {
        TextView tv_header = binding.headerLayout.findViewById(R.id.tv_header);
        tv_header.setVisibility(View.VISIBLE);
        tv_header.setText(context.getResources().getString(R.string.header_createWallet));

        ImageView iv_endIcon = binding.headerLayout.findViewById(R.id.iv_endIcon);
        iv_endIcon.setVisibility(View.GONE);

        ImageView iv_back = binding.headerLayout.findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
            }
        });
    }
}
