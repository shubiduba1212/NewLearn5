package com.newrun5.save_pdf.service;

import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import ai.djl.translate.Batchifier;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CLIPImageEmbeddingTranslator implements Translator<Image, float[]> {

    @Override
    public float[] processOutput(TranslatorContext ctx, NDList list) {
        return list.singletonOrThrow().toFloatArray();
    }

    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {
        NDManager manager = ctx.getNDManager();

        // 이미지를 NDArray로 변환 후 크기 조정 및 텐서로 변환
        NDArray imageArray = input.toNDArray(manager);
        NDArray resizedImage = new Resize(224, 224).transform(imageArray);
        NDArray tensorImage = new ToTensor().transform(resizedImage);

        return new NDList(tensorImage);
    }

    @Override
    public Batchifier getBatchifier() {
        return null;  // 배치 처리가 필요 없는 경우 null 반환
    }
}

public class EmbeddingService {

    public float[] getImageEmbedding(String imagePath) throws ModelException, IOException, TranslateException {
        Path imgPath = Paths.get(imagePath);
        Image image = ImageFactory.getInstance().fromFile(imgPath);  // Image 객체로 로드

        // CLIP 모델 로딩
        Model model = Model.newInstance("clip");

        CLIPImageEmbeddingTranslator translator = new CLIPImageEmbeddingTranslator();

        float[] embeddings;
        try (Model zooModel = model) {
            try (ai.djl.inference.Predictor<Image, float[]> predictor = zooModel.newPredictor(translator)) {
                embeddings = predictor.predict(image);  // 이미지 예측 (임베딩 생성)
            }
        }

        return embeddings;
    }
}

