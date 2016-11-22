package dam.isi.frsf.utn.edu.ar.lab05;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.dao.EjemploPost;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;

public class GestionProyectos extends AppCompatActivity {

    ArrayList<String> listaProyectos = new ArrayList<>();
    static List<Proyecto> proyectosServer = EjemploPost.traerProyectos();
    private String nombreProyectoNuevo;
    private static final int PROYECTO_ID = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyectos);

    }
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_proyectos);

        final Spinner proyectos = (Spinner) findViewById(R.id.spinnerProyectos);
        final TextView proyectoSeleccionado = (TextView) findViewById(R.id.textViewProyecto);
        Button btnVerTareas = (Button) findViewById(R.id.buttonVerTareas);
        Button btnEliminar = (Button) findViewById(R.id.buttonEliminarProyecto);
        Button btnCrearProyecto = (Button) findViewById(R.id.buttonCrearProyecto);

        listaProyectos.add("Seleccione un proyecto");
        for(int i=0;i<proyectosServer.size();i++){
            listaProyectos.add(proyectosServer.get(i).getNombre());
        }
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,listaProyectos);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        proyectos.setAdapter(spinnerAdapter);

        /**
         * Proyecto Seleccionado: mostrar el proyecto que se selecciono en el Spinner
         *
         * VER TAREAS: Mostrar el activity principal con las tareas de un proyecto determinado.
         *
         * ELIMINAR: Eliminar un proyecto y las tareas asociadas al mismo.
         *
         * CREAR: Crear un proyecto nuevo.
         */

        proyectos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case (0):
                        break;
                    default:
                        String p = proyectos.getItemAtPosition(position).toString();
                        proyectoSeleccionado.setText(p);


                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnCrearProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogo();
                if(nombreProyectoNuevo!=null){
                    Proyecto nuevo = new Proyecto(PROYECTO_ID, nombreProyectoNuevo);
                    EjemploPost.agregaProyecto("POST", nuevo);
                }
                else {Toast.makeText(getApplicationContext(),"Ingrese nombre de proyecto", Toast.LENGTH_LONG).show();}

            }
        });

    }

    public void createDialogo() {
        final AlertDialog OptionDialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_proyecto, null);


        OptionDialog.setView(v);

        Button crear = (Button) v.findViewById(R.id.agregar_boton);

        crear.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText edit=(EditText) OptionDialog.findViewById(R.id.proyecto_input);
                        String text=edit.getText().toString();
                        OptionDialog.dismiss();

                        nombreProyectoNuevo=text;
                        listaProyectos.add(nombreProyectoNuevo);

                    }
                }

        );
        OptionDialog.show();

    }
}
