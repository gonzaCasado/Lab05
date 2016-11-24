package dam.isi.frsf.utn.edu.ar.lab05;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;


import java.util.Arrays;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.dao.EjemploPost;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;

public class MainActivity extends AppCompatActivity {

    private ListView lvTareas;
    private ProyectoDAO proyectoDAO;
    private Cursor cursor;
    private TareaCursorAdapter tca;
    private AdapterTarea adapterTarea;
    private boolean flagPermisoPedido;
    private static final int PERMISSION_REQUEST_CONTACT =999;
    private List<Tarea> listaTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        askForContactPermission();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intActAlta= new Intent(MainActivity.this,AltaTareaActivity.class);
                intActAlta.putExtra("ID_TAREA", 0);
                startActivity(intActAlta);
            }
        });
        //listaTareas = EjemploPost.leerNoticias();
        //adapterTarea = new AdapterTarea(MainActivity.this,listaTareas);

        lvTareas = (ListView) findViewById(R.id.listaTareas);
        //lvTareas.setAdapter( tca );
        lvTareas.setAdapter(adapterTarea);


    }


    @Override
    protected void onResume() {
        super.onResume();
        proyectoDAO = new ProyectoDAO(MainActivity.this);
        proyectoDAO.open();
        cursor = proyectoDAO.listaTareas(1);

       // tca = new TareaCursorAdapter(MainActivity.this,cursor,proyectoDAO);
        listaTareas = EjemploPost.leerNoticias();
        adapterTarea = new AdapterTarea(MainActivity.this,listaTareas);

        //lvTareas.setAdapter(tca);
        lvTareas.setAdapter(adapterTarea);
        lvTareas.deferNotifyDataSetChanged();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if(cursor!=null) cursor.close();
        if(proyectoDAO!=null) proyectoDAO.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contextual, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Proyectos) {
            Intent intent = new Intent(getApplicationContext(),GestionProyectos.class);
            startActivity(intent);



            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void askForContactPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CALL_PHONE)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Permisos Peligrosos!!!");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Puedo acceder a un permiso peligroso???");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            flagPermisoPedido=true;
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS,Manifest.permission.GET_ACCOUNTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                } else {
                    flagPermisoPedido=true;
                    ActivityCompat.requestPermissions(this,
                            new String[]
                                    {Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS,Manifest.permission.GET_ACCOUNTS}
                            , PERMISSION_REQUEST_CONTACT);
                }

            }
        }
    }

    public void hacerAlgoQueRequeriaPermisosPeligrosos(){
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("ESCRIBIR_JSON","req code"+requestCode+ " "+ Arrays.toString(permissions)+" ** "+Arrays.toString(grantResults));
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hacerAlgoQueRequeriaPermisosPeligrosos();
                } else {
                    Toast.makeText(this, "No permission for contacts", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }

    }

}
