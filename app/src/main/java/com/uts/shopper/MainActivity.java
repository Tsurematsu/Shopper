package com.uts.shopper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uts.shopper.Auxiliar.AuxNavbar;
import com.uts.shopper.Auxiliar.Aux_Home;
import com.uts.shopper.helpers.Fetch;
import com.uts.shopper.helpers.FileHelper;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static Aux_Home aux_home = null;
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

        if (aux_home == null){
            aux_home = new Aux_Home(this);
            aux_home.connection();
            aux_home.loadProducts();
        }
        new AuxNavbar(this);
    }
    private void hacerPeticionDePrueba(String url) {
        Log.d("API_DEBUG", "Conectado a: " + url);
        Toast.makeText(this, "Conectado a: " + url, Toast.LENGTH_SHORT).show();

        Fetch.GET("/api", (response) -> {
            if (response != null) {
                Log.d("API_DEBUG", "Respuesta cruda: " + response);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Respuesta cruda: " + response, Toast.LENGTH_SHORT).show();
                });
            } else {
                Log.e("API_DEBUG", "Error en la petición");
            }
        });
    }

}


