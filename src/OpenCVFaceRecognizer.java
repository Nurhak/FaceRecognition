/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

 */
import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;
import static jdk.nashorn.internal.objects.NativeRegExp.test;

import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
//import static org.bytedeco.javacpp.opencv_face.createFisherFaceRecognizer;
//import static org.bytedeco.javacpp.opencv_face.createEigenFaceRecognizer;
// import static org.bytedeco.javacpp.opencv_face.createLBPHFaceRecognizer;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import org.bytedeco.javacpp.opencv_face.LBPHFaceRecognizer;
import static org.bytedeco.javacpp.opencv_face.createEigenFaceRecognizer;
import static org.bytedeco.javacpp.opencv_face.createFisherFaceRecognizer;
import static org.bytedeco.javacpp.opencv_face.createLBPHFaceRecognizer;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGRA2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.equalizeHist;
import org.bytedeco.javacv.OpenCVFrameConverter;


/**
 *
 * @author nurha
 */
public class OpenCVFaceRecognizer {

    /**
     * @param args the command line arguments
     */
    public int face_recognize(Mat resized) {
        // TODO code application logic here
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        String trainingDir = "D:\\NetBeans\\Projects\\JavaCV_Grapper\\database";
        Mat testImage = new Mat();
         // Convert the current frame to grayscale:
         cvtColor(resized, testImage, COLOR_BGRA2GRAY);
         equalizeHist(testImage, testImage);
        
        

        File root = new File(trainingDir);

        FilenameFilter imgFilter = (File dir, String name) -> {
            name = name.toLowerCase();
            return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
          };

        File[] imageFiles = root.listFiles(imgFilter);

        MatVector images = new MatVector(imageFiles.length);

        Mat labels = new Mat(imageFiles.length, 1, CV_32SC1);
        IntBuffer labelsBuf = labels.getIntBuffer();

        int counter = 0;

        for (File image : imageFiles) {
            Mat img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);

            int label = Integer.parseInt(image.getName().split("\\-")[0]);

            images.put(counter, img);

            labelsBuf.put(counter, label);

            counter++;
        }
        
        
        FaceRecognizer faceRecognizer=createFisherFaceRecognizer();
        //LBPHFaceRecognizer faceRecognizer = createLBPHFaceRecognizer();
         //FaceRecognizer faceRecognizer = createEigenFaceRecognizer();
        // FaceRecognizer faceRecognizer = createLBPHFaceRecognizer();

        faceRecognizer.train(images,labels);

        int predictedLabel = faceRecognizer.predict(testImage);

        //System.out.println("Predicted label: " + predictedLabel);
        
        return predictedLabel;
    }
    
}
