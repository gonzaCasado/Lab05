package dam.isi.frsf.utn.edu.ar.lab05;


import android.app.AlertDialog;
import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Prioridad;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;

public class AltaTareaActivity extends AppCompatActivity {

    String nombre;
    final ArrayList<String> listaResponsables = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_tarea);

    }

    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_alta_tarea);
        Button btnGuardar = (Button)findViewById(R.id.btnGuardar);
        Button btnCancelar = (Button)findViewById(R.id.btnCanelar);

        final EditText editDescripcion = (EditText)findViewById(R.id.editText);
        final EditText horasEstimadas = (EditText)findViewById(R.id.editText2);
        final Spinner responsable = (Spinner) findViewById(R.id.spinner);

        final ProyectoDAO myDao = new ProyectoDAO(this);
        List<Usuario> usuarios = myDao.listarUsuarios();

        Bundle extras = getIntent().getExtras();
        final int tareaAEditar = extras.getInt("ID_TAREA");

        for(int i = 0; i < usuarios.size(); i++){
            listaResponsables.add(usuarios.get(i).getNombre());
        }
        listaResponsables.add("ingrese nombre de contacto");
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,listaResponsables);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        responsable.setAdapter(spinnerAdapter);

        responsable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        createDialogo();
                        buscarContactos(nombre);
                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if(tareaAEditar!=0) {

            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Proyecto proyecto = myDao.obtenerProyecto();
                    Usuario user = myDao.obtenerUsuario(responsable.getSelectedItem().toString());
                    if((horasEstimadas.getText().length()!=0)&&editDescripcion.getText().length()!=0){
                        myDao.editarTarea(tareaAEditar,Integer.parseInt(horasEstimadas.getText().toString()), editDescripcion.getText().toString(), user );
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Ingrese descripción y horas estimadas",Toast.LENGTH_LONG).show();
                    }

                }
            });


        }
        else{

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Proyecto proyecto = myDao.obtenerProyecto();
                Prioridad prioridad = new Prioridad(4, "Urgente");
                Usuario user = myDao.obtenerUsuario(responsable.getSelectedItem().toString());
                if((horasEstimadas.getText().length()!=0)&&editDescripcion.getText().length()!=0){
                    Tarea tarea = new Tarea (1, false, Integer.parseInt(horasEstimadas.getText().toString()), 0, editDescripcion.getText().toString(), proyecto, prioridad, user);
                    myDao.nuevaTarea(tarea);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Id tarea: "+ tarea.getId(),Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(getApplicationContext(),"Ingrese descripción y horas estimadas",Toast.LENGTH_LONG).show();
                }
            }
        });

        }


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void createDialogo() {
        final AlertDialog OptionDialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_contact, null);


        OptionDialog.setView(v);

        Button buscar = (Button) v.findViewById(R.id.buscar_boton);

        buscar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText edit=(EditText) OptionDialog.findViewById(R.id.nombre_input);
                        String text=edit.getText().toString();
                        OptionDialog.dismiss();

                        nombre=text;
                        buscarContactos(nombre);

                    }
                }

        );
        OptionDialog.show();

    }

    public void buscarContactos(String nombreBuscado){
        JSONArray arr = new JSONArray();
        final StringBuilder resultado = new StringBuilder();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        // consulta ejemplo buscando por nombre visualizado en los contactos agregados
        Cursor c =this.getContentResolver().query(uri, null, ContactsContract.Contacts.DISPLAY_NAME+" LIKE '"+nombreBuscado+"%'", null, sortOrder);
        int count = c.getColumnCount();
        int fila = 0;
        String[] columnas= new String[count];
        try {
            while(c.moveToNext()) {
                JSONObject unContacto = new JSONObject();
                for(int i = 0; (i < count );  i++) {
                    if(fila== 0)columnas[i]=c.getColumnName(i);
                    unContacto.put(columnas[i],c.getString(i));
                }
                Log.d("TEST-ARR",unContacto.toString());
                arr.put(fila,unContacto);
                fila++;
                Log.d("TEST-ARR","fila : "+fila);

                // elegir columnas de ejemplo
                resultado.append(unContacto.get("display_name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("TEST-ARR",arr.toString());
        for(int i=0;i<fila;i++){
            try {
                listaResponsables.add(arr.getJSONObject(i).get("sort_key").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
