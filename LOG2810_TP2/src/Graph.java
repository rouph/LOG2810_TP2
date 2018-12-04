import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Etat> Etats = new ArrayList<>();
    private List<Etat> finiteEtats = new ArrayList<>();
    private LinkedListQueue mostUsedEtats = new LinkedListQueue();
    private boolean hasPrevious = false;
    private Etat start = new Etat();
    private StringBuilder displayLabels = new StringBuilder();
    private ArrayList<String> words = new ArrayList<>();
    private JFrame labelframe = new JFrame();

    public Graph() {
    }

    public boolean addChild(Etat n, String s) {

        boolean finitstate = (s.length() == 1);
        boolean added = false;
        if (s.length() > 0) {

            for (Etat next : n.getNexts()) {
                if (next.getName() == s.charAt(0))
                    added = addChild(next, s.substring(1));
            }
            if (!added) {
                n.addNext(new Etat(s.charAt(0), finitstate));
                addChild(n.getNexts().get(n.getNexts().size() - 1), s.substring(1));
                return true;
            }
        }
        return added;
    }

    public void readFromFile(String filePath) {
        try {
            File fichier = new File(filePath);
            BufferedReader data = new BufferedReader(new FileReader(fichier));
            String lexique;
            while ((lexique = data.readLine()) != null) {
                addChild(start, lexique);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<String> displayFiniteState(String e, boolean withNbrUsed) {
        words.clear();
        if (!e.isEmpty()) {
            Etat starte = getStartingEtat(e);
            if (starte != null)
                for (Etat next : starte.getNexts()) {
                    displayFiniteState(e, next, withNbrUsed);
                }
        }
        return words;
    }

    public void displayFiniteState(String e, Etat Etat, boolean withNbrUsed) {
        if (Etat != null) {
            e += Etat.getName();
            if (Etat.getFiniteState()) {
                if (withNbrUsed)
                    displayLabels.append(e + ":  Fréquence: " + Etat.getUsed() + " | Récemment Utilisé: " + Etat.isSetMostRecently() +"\n");
                else
                    words.add(e);
            }
            for (Etat next : Etat.getNexts()) {
                displayFiniteState(e, next, withNbrUsed);
            }
        }
    }

    public Etat getStartingEtat(String s) {
        if (s.charAt(0) == start.getName() && s.length() == 1) {
            return start;
        }
        for (Etat next : start.getNexts()) {
            Etat temp = getStartingEtat(next, s);
            if (temp != null)
                return temp;
        }
        return null;
    }

    public Etat getStartingEtat(Etat n, String s) {
        if (n.getName() == s.charAt(0) && s.length() > 1) {
            Etat toReturn = null;
            for (Etat next : n.getNexts()) {
                toReturn = getStartingEtat(next, s.substring(1));
                if (toReturn != null) return toReturn;
            }
        } else if (n.getName() == s.charAt(0) && s.length() == 1) {
            return n;
        }
        return null;

    }

    public void addUsed(String e) {
        Etat last = getStartingEtat(e);
        if (last != null) {
            last.addUsed();
            addMostUsed(last);
        }
    }

    public void addMostUsed(Etat item) {
        mostUsedEtats.push(item);
    }
    public void showLabels() {
        displayFiniteState(" ", true);
        labelframe.dispose();
        labelframe = new JFrame();
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        labelframe.getContentPane().add(scrollPane);
        textArea.setText(displayLabels.toString());
        textArea.setCaretPosition(0);
        displayLabels.setLength(0);
        displayLabels = new StringBuilder();
        labelframe.pack();
        labelframe.setVisible(true);
        labelframe.setLocationRelativeTo(null);
    }
}