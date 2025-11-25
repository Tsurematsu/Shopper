package com.uts.shopper;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uts.shopper.Acopladores.AcopladorMain;
import com.uts.shopper.Auxiliar.AuxMain;
import com.uts.shopper.Controllers.ControllerMain;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        auxMain.StartingConnection((url)->{
            controllerMain.CargarProductos((productos)->{
                acopladorMain.AddLayoutProductos(productos, (index)->{

                });
            });
        });
    }

}


