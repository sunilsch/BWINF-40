import javax.swing.JFileChooser;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String currentFolder = System.getProperty("user.dir").replace("executables","beispieldaten");
        JFileChooser chooser = new JFileChooser(currentFolder);
        int response = chooser.showOpenDialog(null);
        if(response == 0){
            String absolutPath = chooser.getSelectedFile().getAbsolutePath();
            j2.calculateChanges(absolutPath);
        }
    }
}