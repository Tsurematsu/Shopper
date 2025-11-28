package com.uts.shopper;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uts.shopper.App.AppSessionUserManager;
import com.uts.shopper.Components.ComponentNavbar;
import com.uts.shopper.Controllers.ControllerClient;
import com.uts.shopper.Models.ModelPersonas;
import com.uts.shopper.Models.ModelSessionUser;

import java.util.function.Consumer;

public class ClientPanelActivity extends AppCompatActivity {
    ControllerClient controllerClient = new ControllerClient();
    private AppSessionUserManager appSessionUserManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appSessionUserManager = new AppSessionUserManager(this);
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.client_panel_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ComponentNavbar.apply(this, "mas");

        ImageView volver = findViewById(R.id.volver);
        volver.setOnClickListener(e->{
            finish();
            overridePendingTransition(0, 0);
        });
        ImageView btnLogoutn = findViewById(R.id.cerrarSesion);
        btnLogoutn.setOnClickListener(e->{
            appSessionUserManager.clearUserSession();
            finish();
            overridePendingTransition(0, 0);
        });

        ModelSessionUser cacheUser = appSessionUserManager.getUserSession();
        EditText correo = findViewById(R.id.correo);
        TextView username = findViewById(R.id.username);
        TextView contrasena = findViewById(R.id.password);
        TextView confirmarContrasena = findViewById(R.id.confirmPassword);
        String template_username = String.valueOf(username.getText());
        int idUsuario = (int) Math.floor(Double.parseDouble(cacheUser.id));
        controllerClient.getPersona(String.valueOf(idUsuario), dataPerona->{
            runOnUiThread(()->{
                correo.setText(dataPerona.email);
                username.setText(template_username.replace("%usuario%", dataPerona.usuario));
            });
        });

        TextView guardarCambios = findViewById(R.id.guardarCambios);
        guardarCambios.setOnClickListener(e->{
            try {
                String text_correo = String.valueOf(correo.getText());
                String text_contrasena = String.valueOf(contrasena.getText());
                String text_confirmarContrasena = String.valueOf(confirmarContrasena.getText());
                Consumer<ModelPersonas> success = persona->{
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Persona actualizada correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                };
                Consumer<String> successError = messaje->{
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Error al actualizar " + messaje, Toast.LENGTH_SHORT).show();
                    });
                };
                controllerClient.updatePerdona(
                        String.valueOf(idUsuario),
                        text_correo,
                        text_contrasena,
                        text_confirmarContrasena,
                        success,
                        successError);
            } catch (Exception ex) {
                Log.e("APP_API_DEBUG", "(ClientPanelActivity)\n" + ex.getMessage());
            }
        });

        TextView eliminarUsuario = findViewById(R.id.eliminarUsuario);
        eliminarUsuario.setOnClickListener(e->{
            controllerClient.deletePersona(String.valueOf(idUsuario), exito -> {
                if (exito) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Persona eliminada correctamente", Toast.LENGTH_SHORT).show();
                        appSessionUserManager.clearUserSession();
                        finish();
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });
    }

}