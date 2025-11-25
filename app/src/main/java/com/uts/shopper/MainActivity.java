package com.uts.shopper;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uts.shopper.Auxiliar.AuxMain;
import com.uts.shopper.Controllers.ControllerApp;
import com.uts.shopper.Controllers.ControllerMain;
import com.uts.shopper.helpers.Fetch;

public class MainActivity extends AppCompatActivity {
    private final AuxMain auxMain = new AuxMain(this);
    private final ControllerApp controllerApp = new ControllerApp();
    private final ControllerMain controllerMain = new ControllerMain(this);
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
        controllerApp.startApp(()->{
            auxMain.StartingConnection(controllerApp::testUrlApi);
        });

        controllerMain.panelOnSelectProduct = ()->{

        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        auxMain.StartingConnection(controllerMain::CargarProductos);
    }

}


