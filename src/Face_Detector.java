
import com.google.common.base.Converter;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.Frame;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;
import java.lang.Object;
import static jdk.nashorn.internal.objects.NativeRegExp.source;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nurha
 */
public class Face_Detector {
     private static final String CASCADE_FILE = "D:\\NetBeans\\Projects\\JavaCV_Grapper\\src\\haarcascade_frontalface_alt.xml";
      private static final int SCALE = 2;
      
      private Frame detected_image;
      private IplImage cropped  ;
      private Mat cropped_mat;
      private  IplImage resizedImage ;
      private CvRect for_name;
      private Mat named_image;
      
      OpenCVFrameConverter.ToMat converter_mat = new OpenCVFrameConverter.ToMat();
      
      
     public  void face_detector(Frame img){
        Loader.load(opencv_objdetect.class); 
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        
        IplImage origImg=converter.convertToIplImage(img);
        IplImage for_face_crop=converter.convertToIplImage(img);
        
        
        IplImage grayImg = cvCreateImage(cvGetSize(origImg), IPL_DEPTH_8U, 1);
        
        cvCvtColor(origImg, grayImg, CV_BGR2GRAY); 
        
         IplImage smallImg = IplImage.create(grayImg.width()/SCALE, 
                                        grayImg.height()/SCALE, IPL_DEPTH_8U, 1);
        cvResize(grayImg, smallImg, CV_INTER_LINEAR);
        
        cvEqualizeHist(smallImg, smallImg);
        
        CvMemStorage storage = CvMemStorage.create();
        
        CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad(CASCADE_FILE));
        if(cascade.isNull()){
            System.out.println("Cascade Yüklenemedi");
        }
        //System.out.println("Detecting faces...");
        CvSeq faces = cvHaarDetectObjects(smallImg, cascade, storage, 1.1, 5, 
                                        CV_HAAR_DO_CANNY_PRUNING);
                                        // CV_HAAR_DO_ROUGH_SEARCH);
                                        // 0);
        cvClearMemStorage(storage);
        int total = faces.total();
        //System.out.println("Found " + total + " face(s)");
            for (int i = 0; i < total; i++) {
                CvRect r = new CvRect(cvGetSeqElem(faces, i));
                this.for_name=r;

                cvRectangle(origImg, cvPoint( r.x()*SCALE, r.y()*SCALE ),    // undo the scaling
                    cvPoint( (r.x() + r.width())*SCALE, (r.y() + r.height())*SCALE ), 
                        CvScalar.RED, 6, CV_AA, 0);
                CvRect cropped_vector=new CvRect();
                cropped_vector.x((r.x()+3)*SCALE);
                cropped_vector.y((r.y()+3)*SCALE);
                cropped_vector.width((r.width()-5)*SCALE);
                cropped_vector.height( (r.height()-5)*SCALE);
                this.for_name=cropped_vector;
                cvSetImageROI(for_face_crop, cropped_vector);
                IplImage tmp = cvCreateImage(cvGetSize(for_face_crop),
                               for_face_crop.depth(),
                               for_face_crop.nChannels());
                cvCopy(for_face_crop, tmp, null);
                cvResetImageROI(for_face_crop);

                this.cropped = cvCloneImage(tmp);
                
                }
            this.detected_image=converter.convert(origImg);
        
         
     }
     public void put_name(String name){
         Point name_point=new Point();
         name_point.x(this.for_name.x()-10);
         name_point.y(this.for_name.y()-10);
         this.named_image=converter_mat.convert(this.detected_image);
         
         putText(this.named_image, name, name_point, CV_FONT_HERSHEY_COMPLEX_SMALL, 1.0, AbstractScalar.RED);
     }
     public void resize(){
         this.resizedImage = IplImage.create(248, 248, this.cropped.depth(), this.cropped.nChannels());
         cvResize(this.cropped,this.resizedImage);
     }
     
     public Frame get_Detected_Image(){
         return this.detected_image; 
     }
     
     public IplImage get_Cropped_Image(){
         return this.cropped;
     }
     
     public IplImage get_resized_image(){
         return this.resizedImage;
     }
     public Mat get_resized_mat_image(){
         return converter_mat.convert(converter_mat.convert(this.resizedImage));
     }
     public Frame get_named_image(){
         return converter_mat.convert(this.named_image);
     }
    
}
