package com.thenightisyoung.tsfonandroid;

import android.content.res.AssetManager;
import android.os.Trace;
import android.util.Log;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class MyTSFMatrixDemo {
    private static final String TAG = "MyTSFMatrixDemo";

    private static final String MODEL_FILE = "file:///android_asset/model.pb";

    // 定义数据的维度
    private static final int HEIGHT = 1;
    private static final int WIDTH = 2;

    // 模型中输入变量的名称
    private static final String inputName = "input";
    // 用于模型输入数据存储
    private float[] inputs = new float[HEIGHT * WIDTH];

    // 模型中输出变量的名称
    private static final String outputName = "output";
    // 用于模型输出数据存储
    private float[] outputs = new float[HEIGHT * WIDTH];

    TensorFlowInferenceInterface inferenceInterface;

    static {
        // 加载库文件
        System.loadLibrary("tensorflow_inference");
        Log.e(TAG,"libtensorflow_inference.so loading successfully.");
    }

    MyTSFMatrixDemo(AssetManager assetManager) {
        // 接口定义
        inferenceInterface = new TensorFlowInferenceInterface(assetManager,MODEL_FILE);
        Log.e(TAG, "Initialize TensorFlowInferenceInterface successfully.");
    }

    public float[] getAddResult() {
        // 初始化样例数据：
        inputs[0] = 1;
        inputs[1] = 3;

        // 将样例数据 feed 给 tensorflow model
        Trace.beginSection("feed");
        inferenceInterface.feed(inputName, inputs, WIDTH, HEIGHT);
        Trace.endSection();

        // 调用模型进行运算
        Trace.beginSection("run");
        String[] outputNames = new String[] {outputName};
        inferenceInterface.run(outputNames);
        Trace.endSection();

        // 将输出结果物存放到 outputs 中
        Trace.beginSection("fetch");
        inferenceInterface.fetch(outputName, outputs);
        Trace.endSection();

        return outputs;
    }
}
