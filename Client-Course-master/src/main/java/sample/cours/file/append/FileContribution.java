package sample.cours.file.append;

import java.io.FileWriter;
import java.io.IOException;

public class FileContribution {
    public static FileContribution fileContribution ;
    private  FileWriter writer ;
    private FileContribution () {}
    public void addDataInFile(String text,int count) throws IOException {
            for(int i=0;i<count;i++){
                writer.write(text);
                writer.append('\n');
                writer.flush();

            }
    }
    public void createFile(){
        try {
            writer = new FileWriter("archive.txt", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
