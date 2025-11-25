package com.uts.shopper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uts.shopper.Acopladores.AcopladorMain;
import com.uts.shopper.App.AppSessionUserManager;
import com.uts.shopper.Auxiliar.AuxMain;
import com.uts.shopper.Components.ComponentNavbar;
import com.uts.shopper.Controllers.ControllerMain;
import com.uts.shopper.Models.ModelSessionUser;

public class MainActivity extends AppCompatActivity {
    private final AuxMain auxMain = new AuxMain(this);
    private final AcopladorMain acopladorMain = new AcopladorMain(this);
    private final ControllerMain controllerMain = new ControllerMain();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ComponentNavbar.apply(this, "inicio");
    }

    @Override
    protected void onResume() {
        super.onResume();
        auxMain.StartingConnection((url)->{
            controllerMain.CargarProductos((productos)->{
                acopladorMain.AddLayoutProductos(productos, (producto)->{
                    Intent intent = new Intent(this, HomeViewProductActivity.class);
                    intent.putExtra("PRODUCTO", producto);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                });
            });
        });
    }

}


