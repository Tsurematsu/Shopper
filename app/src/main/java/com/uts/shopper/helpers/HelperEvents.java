package com.uts.shopper.helpers;

import android.view.View;
import java.util.function.Consumer;

public class HelperEvents {

    // Click simple con Consumer
    public static void onClick(View element, Consumer<View> action) {
        element.setOnClickListener(action::accept);
    }

    // Click simple con Runnable (si no necesitas el View)
    public static void onClick(View element, Runnable action) {
        element.setOnClickListener(v -> action.run());
    }

    // Click largo
    public static void onLongClick(View element, Consumer<View> action) {
        element.setOnLongClickListener(v -> {
            action.accept(v);
            return true;
        });
    }

    // Click con debounce (evita múltiples clicks rápidos)
    public static void onClickDebounce(View element, Consumer<View> action, long debounceTime) {
        element.setOnClickListener(new View.OnClickListener() {
            private long lastClickTime = 0;

            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                if (now - lastClickTime >= debounceTime) {
                    lastClickTime = now;
                    action.accept(v);
                }
            }
        });
    }
}