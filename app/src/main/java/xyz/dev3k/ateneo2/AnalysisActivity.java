package xyz.dev3k.ateneo2;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.List;

public class AnalysisActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView image;
    private InputImage imageInput;
    private Task<Text> result;
    // To use default options:
    ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);

    TextRecognizer recognizer =
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        cargarImagen();
        image = (ImageView) findViewById(R.id.analysis_iv);

        Button buttonAnalyzer = findViewById(R.id.button_analizer);
        Button buttonTextRecognizer = findViewById(R.id.button_text_recognizer);

        buttonTextRecognizer.setOnClickListener(this);
        buttonAnalyzer.setOnClickListener(this);

    }

    public void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione la aplicación"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri path = data.getData();
            image.setImageURI(path);
            try {
                imageInput = InputImage.fromFilePath(this, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_analizer:
                etiquetarImagen();
                break;
            case R.id.button_text_recognizer:
                runTextRecognizer();
                break;
        }
    }

    public void etiquetarImagen() {
        labeler.process(imageInput)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @Override
                    public void onSuccess(List<ImageLabel> labels) {
                        Toast.makeText(AnalysisActivity.this, "Etiquetas:", Toast.LENGTH_SHORT).show();
                        for (ImageLabel label : labels) {
                            String text = label.getText();
                            float confidence = label.getConfidence();//grado de confianza de la etiqueta
                            int index = label.getIndex();
                            Toast.makeText(AnalysisActivity.this, text, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Etiqueta: " + labels);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AnalysisActivity.this, "Error, no se etiquetó la imagen", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void runTextRecognizer(){
         result =
                recognizer.process(imageInput)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                Toast.makeText(AnalysisActivity.this, "Se reconoció el texto", Toast.LENGTH_SHORT).show();
                                processTextRecognitionResult(visionText);
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AnalysisActivity.this, "Error, no se reconoció el texto", Toast.LENGTH_SHORT).show();
                                    }
                                });
    }

    private void processTextRecognitionResult(Text result){
        String resultText = result.getText();
        Toast.makeText(AnalysisActivity.this, resultText, Toast.LENGTH_SHORT).show();
        for (Text.TextBlock block : result.getTextBlocks()) {
            String blockText = block.getText();
            Point[] blockCornerPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();
            for (Text.Line line : block.getLines()) {
                String lineText = line.getText();
                Point[] lineCornerPoints = line.getCornerPoints();
                Rect lineFrame = line.getBoundingBox();
                for (Text.Element element : line.getElements()) {
                    String elementText = element.getText();
                    Point[] elementCornerPoints = element.getCornerPoints();
                    Rect elementFrame = element.getBoundingBox();
                }
            }
        }
    }
}