import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import static javax.swing.JOptionPane.showMessageDialog;

public class Main {

    private Graph g = new Graph();
    public Main() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 130));
        JTextField path = new JTextField(10);
        path.setMaximumSize(new Dimension(200, 25));
        JButton enter = new JButton("Entrer");
        JButton displayLabels = new JButton("Afficher Labels");
        displayLabels.setPreferredSize(displayLabels.getPreferredSize());
        displayLabels.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                g.showLabels();
            }
        });
        displayLabels.setVisible(false);
        JTextField f = new JTextField(10);
        f.setMaximumSize(new Dimension(200, 25));

        f.setVisible(false);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        JPanel p2 = new JPanel();
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);


        JLabel labelPath = new JLabel("Entrer le Path de votre lexique: ");
        p.add(labelPath);

        p.add(path);
        p.add(f);
        p2.add(enter);
        p2.add(displayLabels);
        sp.add(p);
        sp.add(p2);
        sp.setResizeWeight(0.7);
        sp.setEnabled(false);
        sp.setDividerSize(0);
        frame.add(sp);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File fichier = new File(path.getText());
                if (fichier.exists()) {
                    f.setVisible(true);
                    path.setVisible(false);
                    enter.setVisible(false);
                    displayLabels.setVisible(true);
                    labelPath.setText(" Enter text: ");
                    AutoSuggestor autoSuggestor = new AutoSuggestor(f, frame, Color.WHITE.brighter(), Color.BLUE, path.getText(), g) ;
                }
                else{
                    showMessageDialog(null, "Entrer un Path correct");
                }
            }
        });
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
          @Override
         public void run() {
            new Main();
         }
        });
    }
}

