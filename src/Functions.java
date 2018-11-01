
import java.io.File;
import java.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nurhak
 */
public class Functions {
    private String filename;
    private static final String main_path="D:\\NetBeans\\Projects\\JavaCV_Grapper\\database";
    private final File klasor = new File(main_path);
    private static int person_number=1;
    private String name;
    private String db_person_number;
    
    public void create_path(String name){
          String[] strDosyalar = this.klasor.list();
          int counter=1;          
          int flag = 0;
          int deneme = 0;
            for(String dosya : strDosyalar){
                if(dosya.contains(name)){
                    //System.out.println("aynı dosya");
                    String[] parts = dosya.split("-");
                    person_number=Integer.valueOf(parts[0]);                    
                    counter++;
                }
                else{
                    flag++; 
                    if(strDosyalar.length==flag){
                        person_number++;
                    }
                }
            }
            this.filename=main_path+"\\"+person_number+"-"+name+"_"+counter+".png";
    }
    public void find_name(int predicted_label){
        String[] strDosyalar = this.klasor.list();
        String temp = null;
            for(String dosya : strDosyalar){
                if(dosya.split("-")[0].equals(String.valueOf(predicted_label)))
              temp=dosya.split("-")[1];
            }
           this.name=temp.split("_")[0];
    }
    public void find_person_number(){
        String[] strDosyalar = this.klasor.list();
        int biggest=0;
            for(String dosya : strDosyalar){
                 biggest=Integer.valueOf(dosya.split("-")[0]);
                 //System.out.println(biggest);
            }
          this.db_person_number=String.valueOf(biggest);
    }
    public String get_filename(){
        return this.filename;
    }
    public String get_name(){
        return this.name;
    }
    public String get_db_person_number(){
        return this.db_person_number;
    }
    
}
