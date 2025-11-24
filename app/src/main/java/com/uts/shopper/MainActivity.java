package com.uts.shopper;

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

import com.uts.shopper.helpers.Fetch;
import com.uts.shopper.helpers.FileHelper;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private FileHelper fileHelper;
    private ImageView imageView;
    private TextView txtRuta;
    private LinearLayout contenedorItems;

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

        String[] posiblesServidores = {
                "http://192.168.80.23:8080",
                "http://localhost:8080",
        };
        Fetch.findWorkingHost(posiblesServidores, (url) -> {
            runOnUiThread(() -> {
                Log.d("API_DEBUG", "Conectado a: " + url);
                Toast.makeText(this, "Conectado a: " + url, Toast.LENGTH_SHORT).show();
                hacerPeticionDePrueba();
            });}, () -> {runOnUiThread(() -> {
                Log.e("API_DEBUG", "Ningún servidor respondió.");
                Toast.makeText(this, "Error: No se pudo conectar a ningún servidor", Toast.LENGTH_LONG).show();
            });
        });

        /*
        contenedorItems = findViewById(R.id.contenedorItems);

        // Agregar items dinámicamente
        agregarItem("Producto 1", "Descripción del producto 1");
        agregarItem("Producto 2", "Descripción del producto 2");
        agregarItem("Producto 3", "Descripción del producto 3");

        */

    }

    private void hacerPeticionDePrueba() {
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

    /*
    private void agregarItem(String titulo, String descripcion) {
        // Inflar el layout del item
        View itemView = getLayoutInflater().inflate(R.layout.item_producto, contenedorItems, false);

        // Obtener referencias a los elementos
        TextView tvTitulo = itemView.findViewById(R.id.tvTitulo);
        TextView tvDescripcion = itemView.findViewById(R.id.tvDescripcion);

        // Configurar los datos
        tvTitulo.setText(titulo);
        tvDescripcion.setText(descripcion);

        // Agregar click listener si lo necesitas
        itemView.setOnClickListener(v -> {
            Toast.makeText(this, "Clicked: " + titulo, Toast.LENGTH_SHORT).show();
        });

        // Agregar el item al contenedor
        contenedorItems.addView(itemView);
    }
     */

}