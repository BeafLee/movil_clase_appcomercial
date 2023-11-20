package com.example.appcomercial.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;
import com.example.appcomercial.R;
import com.example.appcomercial.VentasFragment;
import com.example.appcomercial.model.Producto;
import com.example.appcomercial.model.Sesion;
import com.example.appcomercial.retrofit.RetrofitClient;
import com.example.appcomercial.util.Helper;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ProductoVentaAdapter extends RecyclerView.Adapter<ProductoVentaAdapter.ProductoVentaViewHolder>{
    private Context context;
    private List<Producto> listaProductoVenta;
    public List<Producto> carritoVenta = new ArrayList<>();

    public ProductoVentaAdapter(final Context context, final List<Producto> listaProductoVenta) {
        this.context = context;
        this.listaProductoVenta = listaProductoVenta;
    }

    @NonNull
    @Override
    public ProductoVentaViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        //Enlazar el adaptador con el archivo cardview
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.producto_venta_cardview, parent, false);
        return new ProductoVentaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductoVentaViewHolder holder, final int position) {
        //Leer la cantidad del producto en caso ya estuviera cargado en el carrito
        final int cantidadCarrito = leerCantidadProductoCarrito(listaProductoVenta.get(position).getId());
        listaProductoVenta.get(position).setCantidad(cantidadCarrito);

        //Mostrar los datos del producto
        holder.bind(listaProductoVenta.get(position));
    }

    @Override
    public int getItemCount() {
        return listaProductoVenta.size();
    }

    public class ProductoVentaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgProducto;
        TextView nombreTextView, categoriaTextView, precioTextView, stockTextView, idTextView;

        public ProductoVentaViewHolder(@NonNull final View itemView) {
            super(itemView);
            imgProducto = itemView.findViewById(R.id.imgProducto);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            categoriaTextView = itemView.findViewById(R.id.categoriaTextView);
            precioTextView = itemView.findViewById(R.id.precioTextView);
            stockTextView = itemView.findViewById(R.id.stockTextView);
            idTextView = itemView.findViewById(R.id.idTextView);

            //Cardview reconozca el evento click
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            //Log.e("CLICK CARDVIEW", "Hiciste click en el cardview de productos");
            final Dialog dialog = new Dialog(context, androidx.appcompat.R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
            dialog.setContentView(R.layout.dialog_venta_ingresar_cantidad);
            dialog.setCancelable(true);

            //Configurar los controles del dialog
            final ImageView imgCantidadVenta = dialog.findViewById(R.id.imgCantidadVenta);
            final TextView txtVentaNombreProducto = dialog.findViewById(R.id.txtVentaNombreProducto);
            final EditText txtVentaCantidad = dialog.findViewById(R.id.txtVentaCantidad);
            final MaterialButton btnVentaCerrar = dialog.findViewById(R.id.btnVentaCerrar);
            final MaterialButton btnVentaAceptar = dialog.findViewById(R.id.btnVentaAceptar);
            final MaterialButton btnQuitarProducto = dialog.findViewById(R.id.btnQuitarProducto);


            //Mostrar los datos
            Glide.with(context)
                    .load(RetrofitClient.URL_API_SERVICE + listaProductoVenta.get(getAdapterPosition()).getFoto())
                    .into(imgCantidadVenta);
            txtVentaNombreProducto.setText(listaProductoVenta.get(getAdapterPosition()).getNombre());

            //Leer la cantidad actual que se ha ingresado en el producto y asignarla a la caja de texto
            txtVentaCantidad.setText(String.valueOf(listaProductoVenta.get(getAdapterPosition()).getCantidad()));

            /*Mostrar el botón btnQuitarProducto siempre y cuando la cantidad sea mayor a cero*/
            if (listaProductoVenta.get(getAdapterPosition()).getCantidad() > 0){
                btnQuitarProducto.setVisibility(View.VISIBLE); //Mostrar
            }else{
                btnQuitarProducto.setVisibility(View.GONE); //Ocultar
            }

            txtVentaCantidad.requestFocus();

            //Mostrar el teclado del móvil automáticamente
            Helper.mostarTeclado((Activity) context);

            //Implementar el cierre del teclado cuando el usuario hace clic fuera del dialog
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(final DialogInterface dialogInterface) {
                    //Ocultar el teclado
                    Helper.ocultarTeclado((Activity) context);
                }
            });

            //Implementar el botón cerrar
            btnVentaCerrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    //Ocultar el teclado
                    Helper.ocultarTeclado((Activity) context);
                    dialog.dismiss(); //Cerrar el dialog
                }
            });

            //Implementar el botón aceptar
            btnVentaAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    //Validar que se ingrese una cantidad
                    if (txtVentaCantidad.getText().toString().isEmpty()){
                        Helper.mensajeInformacion(context, "Verifique", "Debe ingresar una cantidad");
                        return;
                    }

                    //Cantidad ingresada para el producto
                    final int cantidad = Integer.parseInt(txtVentaCantidad.getText().toString());

                    //Validar la cantidad ingresada
                    if (cantidad<=0){
                        Helper.mensajeInformacion(context, "Verifique", "Ingrese cantidad mayor a cero");
                        return;
                    }

                    //Validar si hay stock
                    if (cantidad>listaProductoVenta.get(getAdapterPosition()).getStock()){
                        Helper.mensajeError
                                (
                                    context,
                                    "Verifique",
                                    "Stock insuficiente\n\nStock disponible: " + listaProductoVenta.get(getAdapterPosition()).getStock()
                                );
                        return;
                    }

                    //Asignar la cantidad al producto en la lista "listaProductoVenta", la cual se muestra en el RecyclerView
                    listaProductoVenta.get(getAdapterPosition()).setCantidad(cantidad);

                    //Asignar el producto al carrito
                    asignarProductoCarrito(listaProductoVenta.get(getAdapterPosition()));

                    //Refrescar los cambios en el recyclerView
                    notifyDataSetChanged();

                    //Ocultar el teclado que se encuentra visible
                    Helper.ocultarTeclado((Activity) context);

                    //Cerrar el dialog
                    dialog.dismiss();


                }
            });

            //Implementar el botón btnQuitarProducto
            btnQuitarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    //Obtener el id del producto a quitar del carrito
                    final int productoId = listaProductoVenta.get(getAdapterPosition()).getId();

                    //Quitar el producto del carrito
                    quitarProductoCarrito(productoId);

                    //Refrescar los cambios en el recyclerView
                    notifyDataSetChanged();

                    //Ocultar el teclado que se encuentra visible
                    Helper.ocultarTeclado((Activity) context);

                    //Cerrar el dialog
                    dialog.dismiss();

                }
            });


            //Mostrar el dialog
            dialog.show();


        }

        private void bind(final Producto producto){
            //Mostrar la foto del producto, la cual viene del ws
            Glide.with(context)
                    .load(RetrofitClient.URL_API_SERVICE + producto.getFoto())
                    .into(imgProducto);

            //Mostrar los otros datos del producto
            if (producto.getCantidad()>0){ //Significa que el vendedor ha ingresado una cantidad mayor que cero al producto
                nombreTextView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                nombreTextView.setText("(" + producto.getCantidad() + ") " + producto.getNombre());

            }else{
                nombreTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
                nombreTextView.setText(producto.getNombre());
            }

            categoriaTextView.setText(producto.getCategoria());
            precioTextView.setText("Precio: S/ " + String.valueOf(producto.getPrecio()));
            stockTextView.setText("Stock: " + String.valueOf(producto.getStock()));
            idTextView.setText("ID: " + String.valueOf(producto.getId()));
        }

    }

    private void asignarProductoCarrito(final Producto producto){
        /*Validar si el producto ya existe en el carrito*/
        for (int i = 0; i < carritoVenta.size(); i++) {
            if (carritoVenta.get(i).getId() == producto.getId()){
                //Si el producto ya se encuentra cargado en el carrito, entonces solo se modifica la cantidad
                carritoVenta.get(i).setCantidad(producto.getCantidad());
                return;
            }
        }

        //En caso sea un producto que no se encuentra en el carrito, entonces lo agregamos
        carritoVenta.add(producto);

        //Mostrar en el fab de VentasFragment, la cantidad de productos que tenemos agregados en el carrito
        VentasFragment.fab.setText("Carrito (" + carritoVenta.size() + ")");

    }

    private int leerCantidadProductoCarrito(final int productoId){
        //Revisar los productos que se encuentran cargados al carrito
        for (final Producto producto: carritoVenta){
            if (producto.getId() == productoId){
                //Retornar la cantidad del producto que se encuentra cargado en el carrito
                return producto.getCantidad();
            }
        }
        return 0; //Si el producto no se encuentra cargado en el carrito, retornar 0
    }

    private void quitarProductoCarrito(final int productoId){
        for (int i = 0; i < carritoVenta.size(); i++) {
            if (carritoVenta.get(i).getId() == productoId){
                carritoVenta.remove(i);
            }
        }

        //Mostrar en el fab de VentasFragment, la cantidad de productos que tenemos agregados en el carrito
        VentasFragment.fab.setText("Carrito (" + carritoVenta.size() + ")");
    }

    public double[] calcularTotales() {
        //Calcular: sub total, monto_igv, total_neto
        double subTotal = 0;
        double montoIgv = 0;
        double totalNeto = 0;

        //calculo del total neto
        for (final Producto producto: carritoVenta) {
            totalNeto += producto.getPrecio() * producto.getCantidad();
        }

        //Calculo del subtotal
        final double porcentajeIGV = Sesion.DATOS_SESION.getPorcentajeIgv();
        subTotal = totalNeto / (1 + (porcentajeIGV / 100));
        montoIgv = totalNeto - subTotal;

        return new double[]{subTotal, montoIgv, totalNeto};
    }


}
