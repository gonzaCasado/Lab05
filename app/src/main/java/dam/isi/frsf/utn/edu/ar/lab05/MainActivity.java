package dam.isi.frsf.utn.edu.ar.lab05;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;

public class MainActivity extends AppCompatActivity {

    private ListView lvTareas;
    private ProyectoDAO proyectoDAO;
    private Cursor cursor;
    private TareaCursorAdapter tca;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intActAlta= new Intent(MainActivity.this,AltaTareaActivity.class);
                intActAlta.putExtra("ID_TAREA", 0);
                startActivity(intActAlta);
            }
        });






        lvTareas = (ListView) findViewById(R.id.listaTareas);

        ListAdapter listAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        lvTareas.setAdapter( tca );

/*        lvTareas.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>parent, View view, int position, long id){
                String itemValue= (String) lvTareas.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), itemValue+"..."+position, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"TOASTT: ",Toast.LENGTH_LONG).show();

            }
        });*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LAB05-MAIN","en resume");
        proyectoDAO = new ProyectoDAO(MainActivity.this);
        proyectoDAO.open();
        cursor = proyectoDAO.listaTareas(1);
        Log.d("LAB05-MAIN","mediol "+cursor.getCount());

        tca = new TareaCursorAdapter(MainActivity.this,cursor,proyectoDAO);
        lvTareas.setAdapter(tca);
        lvTareas.deferNotifyDataSetChanged();
        Log.d("LAB05-MAIN","fin resume");

        lvTareas.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>parent, View view, int position, long id){
                String itemValue= (String) lvTareas.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), itemValue+"..."+position, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"TOASTT: ",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LAB05-MAIN","on pausa");

        if(cursor!=null) cursor.close();
        if(proyectoDAO!=null) proyectoDAO.close();
        Log.d("LAB05-MAIN","fin on pausa");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contextual, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo info){
        super.onCreateContextMenu(menu, view, info);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextual,menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.darBajaTarea:
                //llamar a borrarTarea(idTarea) de ProyectoDAO;
                Toast.makeText(this,"Usted ha eliminado la tarea: ",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onContextItemSelected(item);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        lvTareas.deferNotifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

}
