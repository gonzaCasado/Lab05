package dam.isi.frsf.utn.edu.ar.lab05;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class GestionProyectos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyectos);

        Spinner proyectos = (Spinner) findViewById(R.id.spinnerProyectos);
        TextView proyectoSeleccionado = (TextView) findViewById(R.id.textViewProyecto);
        Button btnVerTareas = (Button) findViewById(R.id.buttonVerTareas);
        Button btnEliminar = (Button) findViewById(R.id.buttonEliminarProyecto);
        Button btnCrearProyecto = (Button) findViewById(R.id.buttonCrearProyecto);

        ArrayList<String> listaProyectos = new ArrayList<>();
        listaProyectos.add("Seleccione un proyecto");
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,listaProyectos);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        proyectos.setAdapter(spinnerAdapter);

    }
}
