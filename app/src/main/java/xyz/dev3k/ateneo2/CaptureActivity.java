package xyz.dev3k.ateneo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraState;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;


public class CaptureActivity extends AppCompatActivity implements View.OnClickListener {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    PreviewView previewView;
    Button imageCaptureButton, videoCaptureButton;
    private ImageCapture imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        imageCaptureButton = findViewById(R.id.image_capture_button);
        videoCaptureButton = findViewById(R.id.video_capture_button);
        previewView = findViewById(R.id.viewFinder);

        imageCaptureButton.setOnClickListener(this);
        videoCaptureButton.setOnClickListener(this);

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(()->{
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                starCameraX(cameraProvider);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());
    }

    private Executor getExecutor(){
        return ContextCompat.getMainExecutor(this);
    }

    private void starCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        //Camera selector use case
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        //Preview use case
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        //Image capture use case
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.image_capture_button:
                capturePhoto();
                break;
            case R.id.video_capture_button:
                break;
        }
    }

    private void capturePhoto() {
        //Directorio donde guarda las fotos
        File photoDir = new File("/mnt/sdcard/Pictures/");

        if (!photoDir.exists()){
            photoDir.mkdir();
            Date date = new Date();
            String timestamp = String.valueOf(date.getTime());
            //afignop el tiempo actual en la foto
            String photoFilePath = photoDir.getAbsolutePath() + "/" + timestamp + ".jpg";

            File photoFile = new File(photoFilePath);

            imageCapture.takePicture(
                    new ImageCapture.OutputFileOptions.Builder(photoFile).build(),
                    getExecutor(),
                    new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                            Toast.makeText(CaptureActivity.this, "Foto guardada", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException exception) {
                            Toast.makeText(CaptureActivity.this, "Error al guardar foto: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}