package crypto.base.baseexchange.binders;

import android.content.Context;
import crypto.base.baseexchange.databinding.LayoutHomeBinding;

public class HomeBinder {
    private Context context;
    private LayoutHomeBinding binding;

    public HomeBinder(final Context context, LayoutHomeBinding binding) {
        this.context = context;
        this.binding = binding;
    }
}
