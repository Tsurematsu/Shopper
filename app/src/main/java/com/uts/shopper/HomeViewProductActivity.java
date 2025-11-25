    package com.uts.shopper;

    import android.os.Bundle;
    import android.util.Log;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.activity.EdgeToEdge;
    import androidx.activity.OnBackPressedCallback;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;

    import com.bumptech.glide.Glide;
    import com.uts.shopper.App.AppSessionCarritoManager;
    import com.uts.shopper.Models.ModelCarrito;
    import com.uts.shopper.Models.ModelProducto;
    import com.uts.shopper.helpers.TextHelper;

    import java.util.ArrayList;

    public class HomeViewProductActivity extends AppCompatActivity {
        AppSessionCarritoManager appSessionCarritoManager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            appSessionCarritoManager = new AppSessionCarritoManager(this);
            EdgeToEdge.enable(this);
            setContentView(R.layout.home_view_product_activity);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Manejar el botón atrás moderno
            getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    finish();
                    overridePendingTransition(0, 0);
                }
            });
            ImageView volver = findViewById(R.id.volver);
            volver.setOnClickListener(v->{
                finish();
                overridePendingTransition(0, 0);
            });

            ModelProducto modelProducto = (ModelProducto) getIntent().getSerializableExtra("PRODUCTO", ModelProducto.class);
            if (modelProducto != null) {
               try {
                   ((TextView) findViewById(R.id.tileProduct)).setText(modelProducto.titulo);
                   Glide.with(this)
                           .load(modelProducto.imagenUrl)
                           .placeholder(R.drawable.icon_download)
                           .error(R.drawable.icon_download)
                           .centerCrop()
                           .into(((ImageView) findViewById(R.id.imageProduct)));
                   String formatPricing = "$" + TextHelper.formatearNumero(String.valueOf(modelProducto.precioUnitairo));
                   ((TextView) findViewById(R.id.precioUnitario)).setText(formatPricing);
                   ((TextView) findViewById(R.id.calificacion)).setText(String.valueOf(modelProducto.calificacion));
                   ((TextView) findViewById(R.id.textDescription)).setText(modelProducto.descripcion);

               } catch (Exception e) {
                   Log.d("APP_API_DEBUG", e.getMessage());
               }

            }

            TextView btnAddtoCard = findViewById(R.id.btnAddtoCard);
            btnAddtoCard.setOnClickListener(e->{
                try {
                    ArrayList<ModelCarrito> carritoList = appSessionCarritoManager.getCarritoList();
                    Log.e("APP_API_DEBUG", "Elementos actuales:" + String.valueOf(carritoList.size()));
                } catch (Exception ex) {
                    Log.e("APP_API_DEBUG", "Error al obtener producto ->" + ex.getMessage());

                    ModelCarrito producto = new ModelCarrito(
                            modelProducto.id,
                            modelProducto.titulo,
                            1,
                            modelProducto.costoEnvio,
                            modelProducto.precioUnitairo,
                            modelProducto.imagenUrl
                    );
                    ArrayList<ModelCarrito> carList = new ArrayList<>();
                    carList.add(producto);

                    try {
                        appSessionCarritoManager.setCarritoList(carList);
                    } catch (Exception exc) {
                        Log.e("APP_API_DEBUG", "Error al añadir producto al carrito ->" + exc.getMessage());
                    }
                }
            });

        }
    }