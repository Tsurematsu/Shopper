package com.uts.shopper.Controllers;

import android.util.Log;

import com.uts.shopper.Models.ModelPersonas;
import com.uts.shopper.Models.ModelUpdatePersonaRequest;
import com.uts.shopper.helpers.Fetch;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ControllerClient {
    public void getPersona(String id, Consumer<ModelPersonas> success){
        Fetch.GetMap("/api/personas/obtenerPersona/" + id, response->{
            try {
                ModelPersonas getPersona = new ModelPersonas();
                String email = (String) response.get("email");
                String usuario = (String) response.get("usuario");
                String idUser = String.valueOf((int) Math.floor(Double.parseDouble(String.valueOf(response.get("id")))));

                getPersona.id = idUser;
                getPersona.email = email;
                getPersona.usuario = usuario;

                success.accept(getPersona);
            } catch (Exception e) {
                Log.d("APP_API_DEBUG", "(ControllerClient) \n " + e.getMessage());
            }
        });
    }

    public void updatePerdona(String id, String correo, String contrasena, String confirmContrasena, Consumer<ModelPersonas> success, Consumer<String> errorSuccess){
        try {
            ModelUpdatePersonaRequest updateRequest = new ModelUpdatePersonaRequest();

            // 1. Asignar variables correctamente (NO entre comillas)
            if (correo != null && !correo.isEmpty()) {
                updateRequest.email = correo;
            }
            if (contrasena != null && !contrasena.isEmpty()) {
                updateRequest.contrasena_actual = contrasena;
            }
            if (confirmContrasena != null && !confirmContrasena.isEmpty()) {
                updateRequest.nueva_contrasena = confirmContrasena;
            }

            Fetch.PostMap(updateRequest, "/api/personas/actualizarPersona/" + id, response -> {

                // 3. Validar que la respuesta no sea nula antes de usarla (seguridad extra)
                if (response == null) {
                    Log.e("APP_API_DEBUG", "Error: La respuesta del servidor es NULL");
                    errorSuccess.accept(String.valueOf("efectuacion fallida"));
                    return;
                }

                if (response.containsKey("persona")) {
                    try {
                        // 4. TU SOLUCIÓN: Extraer el sub-objeto (Casting seguro)
                        Map<String, Object> personaOnResponse = (Map<String, Object>) response.get("persona");

                        // Validar que el sub-objeto no sea null
                        if (personaOnResponse != null) {
                            ModelPersonas persona = new ModelPersonas();

                            // Parseo seguro del ID
                            int idUsuario = (int) Math.floor(Double.parseDouble(String.valueOf(personaOnResponse.get("id"))));
                            persona.id = String.valueOf(idUsuario);

                            persona.usuario = String.valueOf(personaOnResponse.get("usuario"));
                            persona.email = String.valueOf(personaOnResponse.get("email"));

                            // Retornar éxito
                            success.accept(persona);
                        }
                    } catch (Exception e) {
                        Log.e("APP_API_DEBUG", "Error procesando JSON (/api/personas/actualizarPersona)\n" + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    errorSuccess.accept(String.valueOf(response.get("error")));
                    Log.d("APP_API_DEBUG", "Servidor respondió sin objeto persona: " + response.get("error"));
                }
            });
        } catch (Exception e) {
            Log.e("APP_API_DEBUG", "(ControllerClient) Error general:\n" + e.getMessage());
        }
    }

    // Método para eliminar usando Fetch.GetMap
    public void deletePersona(String id, Consumer<Boolean> success) {
        // Nota: Asegúrate de que la ruta en PHP acepte GET o DELETE según configures
        String url = "/api/personas/eliminarPersona/" + id;

        Fetch.GetMap(url, response -> {
            try {
                // 1. Validar que la respuesta no sea nula
                if (response == null) {
                    Log.e("APP_API_DEBUG", "Error: Respuesta del servidor es NULL al eliminar");
                    success.accept(false);
                    return;
                }

                // 2. Verificar si hubo error en el servidor
                if (response.containsKey("error")) {
                    Log.e("APP_API_DEBUG", "Error al eliminar: " + response.get("error"));
                    success.accept(false);
                } else {
                    // 3. Si hay mensaje de éxito o no hay errores, asumimos éxito
                    // Tu PHP devuelve: "message": "Persona eliminada..."
                    if (response.containsKey("message")) {
                        Log.d("APP_API_DEBUG", "Eliminado: " + response.get("message"));
                        success.accept(true);
                    } else {
                        success.accept(false);
                    }
                }

            } catch (Exception e) {
                Log.e("APP_API_DEBUG", "(deletePersona) Exception: " + e.getMessage());
                success.accept(false);
            }
        });
    }
}
