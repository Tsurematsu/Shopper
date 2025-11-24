package com.uts.shopper.Auxiliar;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uts.shopper.R;
import com.uts.shopper.helpers.Fetch;

import java.util.function.Consumer;

public class Aux_Home {
    private AppCompatActivity parent = null;
    public Aux_Home(AppCompatActivity parent){
        this.parent = parent;
    }
    public void connection(){
        String[] posiblesServidores = {
                "http://localhost:8080", //Conexion para desarrollo local
                "http://192.168.0.35:8080", //Conexion de prueba en parcial
                "http://192.168.80.23:8080", // Conexion de test PC de mesa
                "http://10.11.9.31:8080", // Conexion PORTABLE
        };
        Fetch.findWorkingHost(posiblesServidores, (url) -> {
            parent.runOnUiThread(()->this.hacerPeticionDePrueba(url));}, () -> {
            parent.runOnUiThread(() -> {
                Log.e("API_DEBUG", "ERROR DE CONEXION CON EL SERVIDOR.");
                Toast.makeText(parent, "Servidor no disponible", Toast.LENGTH_LONG).show();
            });
        });
    }
    private void hacerPeticionDePrueba(String url) {
        Log.d("API_DEBUG", "Conectado a: " + url);
        Toast.makeText(parent, "Conectado a: " + url, Toast.LENGTH_SHORT).show();

        Fetch.GET("/api", (response) -> {
            if (response != null) {
                Log.d("API_DEBUG", "Respuesta cruda: " + response);
                parent.runOnUiThread(() -> {
                    Toast.makeText(parent, "Respuesta cruda: " + response, Toast.LENGTH_SHORT).show();
                });
            } else {
                Log.e("API_DEBUG", "Error en la petición");
            }
        });
    }
    public void loadProducts() {
        try {
            GridLayout contenedorItems = parent.findViewById(R.id.panelProducts);

            // ITEM 1
            View itemView = parent.getLayoutInflater().inflate(R.layout.component_home_card_product, contenedorItems, false);
            itemView.setLayoutParams(createGridParams()); // <--- Nuevos params
            contenedorItems.addView(itemView);

            // ITEM 2
            View itemView1 = parent.getLayoutInflater().inflate(R.layout.component_home_card_product, contenedorItems, false);
            itemView1.setLayoutParams(createGridParams()); // <--- Nuevos params
            contenedorItems.addView(itemView1);

            // ITEM 3
            View itemView2 = parent.getLayoutInflater().inflate(R.layout.component_home_card_product, contenedorItems, false);
            itemView2.setLayoutParams(createGridParams()); // <--- Nuevos params
            contenedorItems.addView(itemView2);

        } catch (Exception e) {
            Log.e("API_DEBUG", "Error en la petición:" + e.getMessage());
        }
    }

    // Función auxiliar para generar nuevos parámetros cada vez
    private GridLayout.LayoutParams createGridParams() {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0; // Para que el peso funcione
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.setMargins(4, 4, 4, 4);
        // Peso de columna 1 para que se distribuya equitativamente
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        return params;
    }

}


