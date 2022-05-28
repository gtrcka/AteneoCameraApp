package xyz.dev3k.ateneo2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    int REQUEST_CODE = 200;
    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verificarPermisos();
    }
    //Intent CaptureActivity
    public void launchCaptureActivity(View view) {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivity(intent);
    }
    //Intent AnalysisActivity
    public void launchAnalysisActivity(View view) {
        Intent intent = new Intent(this, AnalysisActivity.class);
        startActivity(intent);
    }

    //Solicitud de permisos
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verificarPermisos() {
        int permisoCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permisoMicrophone = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int permisoWriteStorage =ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permisoReadStorage =ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permisoWriteStorage == PackageManager.PERMISSION_GRANTED &&permisoCamera == PackageManager.PERMISSION_GRANTED && permisoMicrophone == PackageManager.PERMISSION_GRANTED && permisoReadStorage == PackageManager.PERMISSION_GRANTED){

        }else{
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, REQUEST_CODE);
        }
    }
}