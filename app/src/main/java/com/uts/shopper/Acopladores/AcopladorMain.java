package com.uts.shopper.Acopladores;

import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.uts.shopper.Models.ModelProducto;
import com.uts.shopper.R;
import com.uts.shopper.helpers.TextHelper;

import java.util.ArrayList;
import java.util.function.Consumer;

public class AcopladorMain {
    private AppCompatActivity parent = null;
    public AcopladorMain(AppCompatActivity parent ){
        this.parent = parent;
    }
    public void AddLayoutProductos(ArrayList<ModelProducto> modelProductos, Consumer<ModelProducto> ActionClick){
        parent.runOnUiThread(() -> {
            try {
                GridLayout contenedorItems = parent.findViewById(R.id.panelProducts);
                contenedorItems.removeAllViews();
                for (ModelProducto modelProducto : modelProductos) {
                    View itemView = parent.getLayoutInflater().inflate(R.layout.component_home_card_product, contenedorItems, false);
                    itemView.setLayoutParams(createGridParams());
                    TextView txtTitulo = itemView.findViewById(R.id.textTitle);
                    TextView txtDescripcion = itemView.findViewById(R.id.textDescription);
                    TextView txtPrecio = itemView.findViewById(R.id.textAmount);
                    ImageView imgProducto = itemView.findViewById(R.id.imageView);

                    txtTitulo.setText(modelProducto.titulo);
                    String resumeDescription = modelProducto.descripcion.substring(0, 32) + "...";
                    txtDescripcion.setText(resumeDescription);
                    String typePricing = "$" + TextHelper.formatearNumero(String.valueOf(modelProducto.precioUnitairo));
                    txtPrecio.setText(typePricing);
                    Glide.with(parent)
                            .load(modelProducto.imagenUrl)
                            .placeholder(R.drawable.icon_download)
                            .error(R.drawable.icon_download)
                            .centerCrop()
                            .into(imgProducto);

                    itemView.setOnClickListener(view -> {
                        ActionClick.accept(modelProducto);
                    });
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
