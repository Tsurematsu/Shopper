package com.uts.shopper;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uts.shopper.App.AppSessionUserManager;
import com.uts.shopper.Components.ComponentNavbar;

public class AdminPanelActivity extends AppCompatActivity {
    AppSessionUserManager appSessionUserManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appSessionUserManager = new AppSessionUserManager(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_panel_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ComponentNavbar.apply(this, "mas");

        ImageView logout = findViewById(R.id.logout);
        logout.setOnClickListener(e->{
            appSessionUserManager.clearUserSession();
            finish();
        });
        ImageView volver = findViewById(R.id.volver);
        volver.setOnClickListener(e->{
            finish();
        });
    }
}