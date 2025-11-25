package com.uts.shopper;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uts.shopper.App.AppSessionUserManager;

public class AdminPanelActivity extends AppCompatActivity {
    AppSessionUserManager appSessionUserManager = new AppSessionUserManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_panel_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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