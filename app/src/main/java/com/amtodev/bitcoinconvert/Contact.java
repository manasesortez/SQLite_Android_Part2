package com.amtodev.bitcoinconvert;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.amtodev.bitcoinconvert.Clases.ConexionSQLite;
import com.amtodev.bitcoinconvert.Clases.Configuraciones;

import java.util.ArrayList;

public class Contact extends AppCompatActivity {

    ConexionSQLite objConexion;
    final String NOMBRE_BASE_DATOS = "miagenda";
    Button botonAgregar, botonBuscar;
    EditText cajaBusquedaNombre, cajaBusquedaTelefono;
    ListView listaContactos;
    Configuraciones objConfiguracion;
    ArrayList<String> lista;
    ArrayAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        //Conexion a SQLITE
        objConfiguracion = new Configuraciones();
        objConexion = new ConexionSQLite(Contact.this, objConfiguracion.BD, null, 1);

        cajaBusquedaNombre = findViewById(R.id.txtCriterio);
        cajaBusquedaTelefono = findViewById(R.id.txtCriterioNumero);
        listaContactos = (ListView) findViewById(R.id.lvContactos);

        botonBuscar = findViewById(R.id.btnBuscar);
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar();
            }
        });

        botonAgregar = (Button) findViewById(R.id.btnAgregar);
        botonAgregar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent ventana = new Intent(Contact.this, Add_Contact.class);
                startActivity(ventana);
            }
        });

    }

    public void buscar(){
            lista = llenarLista();
            adaptador = new ArrayAdapter(Contact.this, android.R.layout.simple_list_item_1, lista);
            listaContactos.setAdapter(adaptador);
    }

    @Override
    protected void onResume() {
        super.onResume();
        buscar();
    }

    public ArrayList llenarLista(){
        ArrayList<String> miLista = new ArrayList<String>();
        SQLiteDatabase base = objConexion.getReadableDatabase();
        String consulta = "select id_contacto,nombre,telefono from contactos WHERE nombre LIKE '%"+ cajaBusquedaNombre.getText().toString() +"%' OR telefono LIKE '%"+ cajaBusquedaTelefono.getText().toString() +"%' " + " order by nombre ASC";
        @SuppressLint("Recycle") Cursor cadaRegistro = base.rawQuery(consulta, null);
        if(cadaRegistro.moveToFirst()){
            do{
                miLista.add(cadaRegistro.getString(1).toString()+ "-"+cadaRegistro.getString(2).toString());
            }while(cadaRegistro.moveToNext());
        }
        base.close();
        return miLista;
    }
}