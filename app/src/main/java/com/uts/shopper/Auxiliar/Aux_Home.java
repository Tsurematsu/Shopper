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


