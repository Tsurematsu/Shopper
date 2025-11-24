private LinearLayout contenedorItems;


/*
    contenedorItems = findViewById(R.id.contenedorItems);

    // Agregar items dinámicamente
    agregarItem("Producto 1", "Descripción del producto 1");
    agregarItem("Producto 2", "Descripción del producto 2");
    agregarItem("Producto 3", "Descripción del producto 3");

    */




    /*
    private void agregarItem(String titulo, String descripcion) {
        // Inflar el layout del item
        View itemView = getLayoutInflater().inflate(R.layout.item_producto, contenedorItems, false);

        // Obtener referencias a los elementos
        TextView tvTitulo = itemView.findViewById(R.id.tvTitulo);
        TextView tvDescripcion = itemView.findViewById(R.id.tvDescripcion);

        // Configurar los datos
        tvTitulo.setText(titulo);
        tvDescripcion.setText(descripcion);

        // Agregar click listener si lo necesitas
        itemView.setOnClickListener(v -> {
            Toast.makeText(this, "Clicked: " + titulo, Toast.LENGTH_SHORT).show();
        });

        // Agregar el item al contenedor
        contenedorItems.addView(itemView);
    }
     */