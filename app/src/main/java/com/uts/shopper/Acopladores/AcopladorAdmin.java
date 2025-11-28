package com.uts.shopper.Acopladores;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uts.shopper.AdminPanelActivity;
import com.uts.shopper.Models.ModelProducto;
import com.uts.shopper.Models.ParamActionContinue;
import com.uts.shopper.R;
import com.uts.shopper.helpers.TextHelper;

import java.util.ArrayList;
import java.util.function.Consumer;

public class AcopladorAdmin {
    AdminPanelActivity parent = null;
    public AcopladorAdmin(AdminPanelActivity parent){
        this.parent = parent;
    }
    public void loadProducts(ArrayList<ModelProducto> listaProductos, Consumer<ModelProducto> ActionClick, Consumer<ParamActionContinue> ActionDelete){
        parent.runOnUiThread(() -> {
            try {
                LinearLayout contenedorItems = parent.findViewById(R.id.contenedorProductos);
                contenedorItems.removeAllViews();
                for (ModelProducto producto : listaProductos) {
                    View itemView = parent.getLayoutInflater().inflate(R.layout.component_admin_card, contenedorItems, false);

                    TextView tituloProducto = itemView.findViewById(R.id.tituloProducto);
                    TextView dexcripcionProducto = itemView.findViewById(R.id.dexcripcionProducto);

                    TextView precioUnitario = itemView.findViewById(R.id.precioUnitario);
                    String template_precioUnitario = String.valueOf(precioUnitario.getText());

                    TextView costoEnvio = itemView.findViewById(R.id.costoEnvio);
                    String template_costoEnvio = String.valueOf(costoEnvio.getText());

                    TextView calificacion = itemView.findViewById(R.id.calificacion);
                    String template_calificacion = String.valueOf(calificacion.getText());

                    TextView cantidad = itemView.findViewById(R.id.cantidad);
                    String template_cantidad = String.valueOf(cantidad.getText());

                    tituloProducto.setText(String.valueOf(producto.titulo));
                    dexcripcionProducto.setText(String.valueOf(producto.descripcion));

                    String typed_precio_unitario = "$" + TextHelper.formatearNumero(String.valueOf(producto.precioUnitairo));
                    precioUnitario.setText(template_precioUnitario.replace("(0)", typed_precio_unitario));

                    String typed_costo_envio = "$" + TextHelper.formatearNumero(String.valueOf(producto.costoEnvio));
                    costoEnvio.setText(template_costoEnvio.replace("(0)", typed_costo_envio));

                    calificacion.setText(template_calificacion.replace("(0)", String.valueOf(producto.calificacion)));

                    cantidad.setText(template_cantidad.replace("(0)", String.valueOf(producto.cantidad)));

                    ImageView imgProduct = itemView.findViewById(R.id.imgProduct);

                    Glide.with(parent)
                            .load(String.valueOf(producto.imagenUrl))
                            .placeholder(R.drawable.icon_download)
                            .error(R.drawable.icon_download)
                            .centerCrop()
                            .into(imgProduct);

                    Button btnDelete = itemView.findViewById(R.id.deleteProduct);
                    btnDelete.setOnClickListener(e->{

                        ParamActionContinue newContinue = new ParamActionContinue();
                        newContinue.producto = producto;
                        newContinue.continueAction = ()->{
                            itemView.animate()
                                    .alpha(0f)
                                    .setDuration(300)
                                    .withEndAction(() -> {
                                        contenedorItems.removeView(itemView);
                                        listaProductos.remove(producto);
                                    })
                                    .start();
                        };
                        ActionDelete.accept(newContinue);
                    });

                    itemView.setOnClickListener(e->{
                        ActionClick.accept(producto);
                    });

                    contenedorItems.addView(itemView);
                }

            } catch (Exception e) {
                Log.e("APP_API_DEBUG", "AcopladorAdmin ->" + e.getMessage() + "\n" + e.getLocalizedMessage() );
            }
        });
    }
}
