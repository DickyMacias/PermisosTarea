package mx.edu.utch.tis51m.ejemplo007;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class App extends AppCompatActivity implements View.OnClickListener{

    private static final int WRITE_EXTERNAL_STORAGE = 0;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private View layout;
    private Button btnFoto, btnGuardar;
    private EditText etxComenn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app);
        layout = findViewById(R.id.Llayout);
        btnFoto = (Button)findViewById(R.id.btnFoto);
        btnFoto.setOnClickListener(this);

        btnGuardar = (Button)findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(this);

        etxComenn = (EditText)findViewById(R.id.etxCommen);
    }

    private void checkPermissions(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            //requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Toast.makeText(this, "Permisos y memoria", Toast.LENGTH_SHORT).show();
                escriboComentario();
            }
            else {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Toast.makeText(this, "Permisos y memoria", Toast.LENGTH_SHORT).show();
                escriboComentario();
            }
            escriboComentario();
        }
        }else {
            int tienePermiso = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (tienePermiso != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(this, "Permisos y memoria", Toast.LENGTH_SHORT).show();
                    escriboComentario();
                }
            } else {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(this, "Permisos y memoria", Toast.LENGTH_SHORT).show();
                    escriboComentario();
                }
                escriboComentario();
            }
        }
    }

    private void verificarPermisos(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M) {
            abrirCamara();
        }else{
            int permisoCamara = checkSelfPermission(Manifest.permission.CAMERA);
            if(permisoCamara != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);
                abrirCamara();
            }else{
                abrirCamara();
            }
        }
    }

    private void abrirCamara(){
        Intent intento = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intento);
        Snackbar.make(layout,"Gracias!",Snackbar.LENGTH_SHORT).show();
    }

    private void escriboComentario(){
        try{
            File file = new File(Environment.getExternalStorageDirectory(),"comentarios.dat");
            boolean creado = file.createNewFile();
            if (file.exists()){
                OutputStream to = new FileOutputStream(file,true);
                to.write(etxComenn.getText().toString().getBytes());
                to.close();
                Toast.makeText(this,"Comentario Guardado....",Toast.LENGTH_SHORT).show();
            }
        }catch (IOException e){
            e.printStackTrace();
            Log.d(getTitle().toString(),e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == WRITE_EXTERNAL_STORAGE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d(getTitle().toString(),"Tenemos permiso en la SD");
            }else {
                Log.d(getTitle().toString(), "Sin permiso en la SD");
            }
        }else if(requestCode == REQUEST_CODE_ASK_PERMISSIONS){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d(getTitle().toString(),"Tenemos permiso en la camara");
            }else{
                Log.d(getTitle().toString(),"Sin permiso en la camara");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.equals(btnFoto)){
            verificarPermisos();
        }else{
            checkPermissions();
        }

    }


    public void configurar(){
        Intent intento = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intento.setData(Uri.parse("package"+getPackageName()));
        App.this.startActivity(intento);
    }
}
