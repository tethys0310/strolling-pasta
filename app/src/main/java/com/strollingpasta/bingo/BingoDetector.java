package com.strollingpasta.bingo;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tflite.java.TfLite;

import org.jetbrains.annotations.NotNull;
import org.tensorflow.lite.InterpreterApi;
import org.tensorflow.lite.InterpreterApi.Options.TfLiteRuntime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BingoDetector {

    Context context;
    Task<Void> initializeTask;
    InterpreterApi interpreter;

    public BingoDetector(Context context) {
        this.context = context;

        initializeTask = TfLite.initialize(context);
        initializeTask.addOnSuccessListener(a -> {
                    interpreter = InterpreterApi.create(new File("ml/Yolo-NAS-Quantized.tflite"), // TFLite로 변환된 모델... 일단 YOLO_NAS 사용
                            new InterpreterApi.Options().setRuntime(TfLiteRuntime.FROM_SYSTEM_ONLY)); //Google play 서비스 런타임
                })
                .addOnFailureListener(e -> {
                    Log.e("Interpreter", String.format("Cannot initialize interpreter: %s", e.getMessage()));
                });
    }


    public void run() {

        float[] inputs = new float[]{};
        float[] outputs = new float[]{0f};

        interpreter.run(inputs, outputs);
    }

}
