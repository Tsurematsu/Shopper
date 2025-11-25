package com.uts.shopper.Acopladores;

import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uts.shopper.Models.Producto;
import com.uts.shopper.R;

import java.util.ArrayList;
import java.util.function.Consumer;

public class AcopladorMain {
    private AppCompatActivity parent = null;
    public AcopladorMain(AppCompatActivity parent ){
        this.parent = parent;
    }
    public void AddLayoutProductos(ArrayList<Producto> productos, Consumer<Integer> ActionClick){
        parent.runOnUiThread(() -> {
            try {
                GridLayout contenedorItems = parent.findViewById(R.id.panelProducts);
                contenedorItems.removeAllViews();
                for (Producto producto: productos) {
                    View itemView = parent.getLayoutInflater().inflate(R.layout.component_home_card_product, contenedorItems, false);
                    itemView.setLayoutParams(createGridParams());
                    contenedorItems.addView(itemView);
                }
            } catch (Exception e) {
                Log.e("APP_API_DEBUG", "Error en la petición:" + e.getMessage());
            }
        });
    }
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
