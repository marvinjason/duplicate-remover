
import com.srch07.duplicate_image_finder.api.DuplicateImageFinder;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main
{
    private JFrame frame;
    private JPanel panel;
    private JButton btn1;
    private JButton btn2;
    private JButton btn3;
    private JFileChooser fileChooser;
    private String refDir;
    private String srcDir;
    private List<List<String>> duplicates;
    
    public Main()
    {
        try
        {
            fileChooser = new JFileChooser(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
        catch (Exception ex)
        {
            System.out.println("Exception Thrown in File Chooser: " + ex.getMessage());
        }
        
        btn1 = new JButton("Reference");
        btn1.setBounds(25, 20, 100, 25);
        btn1.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btn1.setFocusPainted(false);
        btn1.addActionListener((e)->{
            fileChooser.showDialog(panel, "Select");
            refDir = fileChooser.getSelectedFile().toString();
        });
        
        btn2 = new JButton("Source");
        btn2.setBounds(150, 20, 100, 25);
        btn2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btn2.setFocusPainted(false);
        btn2.addActionListener((e)->{
            fileChooser.showDialog(panel, "Select");
            srcDir = fileChooser.getSelectedFile().toString();
        });
        
        btn3 = new JButton("Process");
        btn3.setBounds(88, 75, 100, 25);
        btn3.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btn3.setFocusPainted(false);
        btn3.addActionListener((e)->{
            File folder = new File(refDir);
            File[] files = folder.listFiles();
            
            for (File file : files)
            {
                String tempName = srcDir + "\\" + "tmp_" + file.getName();
                File tempFile = new File(tempName);
                
                try
                {
                    Files.copy(file.toPath(), tempFile.toPath());
                }
                catch (Exception ex)
                {
                    System.out.println("Exception Thrown in File Copy: " + ex.getMessage());
                }
            }
            
            duplicates = DuplicateImageFinder.findDuplicatePairs(srcDir);
            
            for (List<String> list : duplicates)
            {
                for (String str : list)
                {
                    File tempFile = new File(str);
                    tempFile.delete();
                }
            }
            
            for (File file : files)
            {
                String tempName = srcDir + "\\" + "tmp_" + file.getName();
                File tempFile = new File(tempName);
                
                if (tempFile.exists())
                {
                    tempFile.delete();
                }
            }
            
            JOptionPane.showMessageDialog(panel, "Duplicates removed!");
        });
        
        panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(275, 130));
        panel.add(btn1);
        panel.add(btn2);
        panel.add(btn3);
        
        frame = new JFrame("Duplicate Remover");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocation(800, 400);
        frame.add(panel);
        frame.pack();
    }
    
    public static void main(String[] args)
    {
        Main main = new Main();
    }
}
