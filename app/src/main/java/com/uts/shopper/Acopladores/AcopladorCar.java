package com.uts.shopper.Acopladores;

import android.util.Log;
import android.util.TypedValue; // Importante para convertir DP a Px
import android.view.View;
import android.view.ViewGroup; // Para los LayoutParams generales
import android.widget.LinearLayout; // Importante
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uts.shopper.HomeCarActivity;
import com.uts.shopper.Models.ModelCarrito;
import com.uts.shopper.R;
import com.uts.shopper.helpers.TextHelper;

import java.util.ArrayList;
import java.util.function.Consumer;

// (Si usas Glide, asegúrate de mantener las importaciones,
// aquí las dejo comentadas como en tu código original si no las usas aún)
// import com.bumptech.glide.Glide;

public class AcopladorCar {
    private HomeCarActivity parent = null;

    public AcopladorCar(HomeCarActivity parent){
        this.parent = parent;
    }

    public void AddLayoutCarrito(
            ArrayList<ModelCarrito> modelCarrito,
            Consumer<ModelCarrito> ActionClick,
            Consumer<ModelCarrito> ActionRemoved,
            Consumer<ArrayList<ModelCarrito>> UpdateElement
    ){
        parent.runOnUiThread(() -> {
            try {
                LinearLayout contenedorItems = parent.findViewById(R.id.contenedorCarListLayout);
                contenedorItems.removeAllViews();

                for (ModelCarrito producto : modelCarrito) {
                    View itemView = parent.getLayoutInflater().inflate(R.layout.component_home_car_item, contenedorItems, false);

                    itemView.setLayoutParams(createLinearLayoutParams());

                    TextView txt_itemCarTitle = itemView.findViewById(R.id.itemCarTitle);
                    TextView txt_itemCarUnidades = itemView.findViewById(R.id.itemCarUnidades);
                    TextView txt_itemCarPrecioEnvio = itemView.findViewById(R.id.itemCarPrecioEnvio);
                    TextView txt_itemCarPrecioUnitario = itemView.findViewById(R.id.itemCarPrecioUnitario);
                    TextView txt_itemCarSubtotal = itemView.findViewById(R.id.itemCarSubtotal);
                    ImageView img_itemCarImagen = itemView.findViewById(R.id.itemCarImagen);
                    ImageView img_itemCarRemoveItem = itemView.findViewById(R.id.itemCarRemoveItem);
                    ImageView img_btnAddItem = itemView.findViewById(R.id.btnAddItem);
                    ImageView img_btnSubstract = itemView.findViewById(R.id.btnSubstract);
                    Runnable loadData = ()->{
                        txt_itemCarTitle.setText(producto.titulo);

                        String typedUnidades = "Unidades (" + String.valueOf(producto.cantidadSeleccionada) + ")";
                        txt_itemCarUnidades.setText(typedUnidades);

                        String typedPricingEnvio = "Envio $" + TextHelper.formatearNumero(String.valueOf(producto.costoEnvio));
                        txt_itemCarPrecioEnvio.setText(typedPricingEnvio);

                        String typedPricinUnitario = "$" + TextHelper.formatearNumero(String.valueOf(producto.precioUnitairo));
                        txt_itemCarPrecioUnitario.setText(typedPricinUnitario);

                        Double calc_subTotal = producto.costoEnvio + (producto.precioUnitairo * producto.cantidadSeleccionada);
                        String typedPicingSubtotal = "Subtotal $" + TextHelper.formatearNumero(String.valueOf(calc_subTotal));
                        txt_itemCarSubtotal.setText(typedPicingSubtotal);

                    };
                    loadData.run();

                    Glide.with(parent)
                            .load(producto.imagenUrl)
                            .placeholder(R.drawable.icon_download)
                            .error(R.drawable.icon_download)
                            .centerCrop()
                            .into(img_itemCarImagen);

                    itemView.setOnClickListener(view -> {
                        ActionClick.accept(producto);
                    });

                    img_itemCarRemoveItem.setOnClickListener(e->{
                        ActionRemoved.accept(producto);
                    });

                    img_btnAddItem.setOnClickListener(view->{
                        if (producto.cantidadSeleccionada < 10){
                            producto.cantidadSeleccionada++;
                            loadData.run();
                            UpdateElement.accept(modelCarrito);
                        }
                    });

                    img_btnSubstract.setOnClickListener(view->{
                        if (producto.cantidadSeleccionada>1){
                            producto.cantidadSeleccionada--;
                            loadData.run();
                            UpdateElement.accept(modelCarrito);
                        }
                    });


                    contenedorItems.addView(itemView);
                }
            } catch (Exception e) {
                Log.e("APP_API_DEBUG", "Error en la petición:" + e.getMessage() + "\n" + e.toString());
            }
        });
    }

    private LinearLayout.LayoutParams createLinearLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int marginBottomInDp = 10;
        int marginBottomInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                marginBottomInDp,
                parent.getResources().getDisplayMetrics()
        );
        params.setMargins(0, 0, 0, marginBottomInPx);
        return params;
    }
}