package com.uts.shopper;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uts.shopper.Controllers.ControllerRegister;
import com.uts.shopper.Models.ModelRequestRegisterUser;

public class UserRegisterActivity extends AppCompatActivity {
    private final ControllerRegister controllerRegister = new ControllerRegister();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_register_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView btnVolver = findViewById(R.id.volver);
        btnVolver.setOnClickListener(e->{
            finish();
        });

        TextView btnRegistrar = findViewById(R.id.btn_registrar);
        btnRegistrar.setOnClickListener(e->{
            EditText correo = findViewById(R.id.correo);
            EditText nombre = findViewById(R.id.nombre);
            EditText password = findViewById(R.id.password);
            CheckBox isAdmin = findViewById(R.id.isAdmin);
            String text_correo = String.valueOf(correo.getText());
            String text_nombre = String.valueOf(nombre.getText());
            String text_password = String.valueOf(password.getText());
            boolean bool_isAdmin = isAdmin.isChecked();

            ModelRequestRegisterUser userData = new ModelRequestRegisterUser(
                    text_correo,
                    text_nombre,
                    text_password,
                    bool_isAdmin
            );
            controllerRegister.RegistoPersona(userData, (succes)->{
                runOnUiThread(() -> {
                    if (succes){
                        finish();
                    }else {
                        new AlertDialog.Builder(UserRegisterActivity.this)
                                .setTitle("Registro")
                                .setMessage("Hay campos invalidos")
                                .setPositiveButton("OK", (dialog, which) -> {
                                    // Acción al presionar OK
                                    dialog.dismiss();
                                }).show();
                    }
                });
            });
        });
    }
}