import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Node> nodes = new ArrayList<>();
    private List<Node> finiteNodes = new ArrayList<>();

    private boolean hasPrevious = false;
    private Node start = new Node();

    private ArrayList<String> words = new ArrayList<>();
    public Graph() {
    }

    public boolean addChild(Node n, String s) {

        boolean finitstate = (s.length() == 2);

        if(n.getName() == s.charAt(0) ){
            if(!n.hasNext() ) {
                if( s.length() > 1){
                    n.addNext(new Node(0,s.charAt(1),null,finitstate));
                    addChild(n.getNexts().get(0), s.substring(1));
                } return true;
            }else{
                boolean added = false;
                for(Node next : n.getNexts()){
                    if(!added)
                        added = addChild(next, s.substring(1));
                }
                if(!added){
                    n.addNext(new Node(0,s.charAt(1),null,finitstate));
                    addChild(n.getNexts().get(n.getNexts().size()-1), s.substring(1));
                }
                return true;
            }
        }
       return false;
    }

    public void readFromFile(String filePath) {
        try {
            start.setPrevious(start);
            File fichier = new File(filePath);
            BufferedReader data = new BufferedReader(new FileReader(fichier));
            String lexique;
            // fiye a33mela optimisation hn maa hashtable ta nle2e lstarting point
            while ((lexique = data.readLine()) != null) {
                if(!start.hasNext()) {
                    Node next = new Node(0, lexique.charAt(0), null, false);
                    start.addNext(next);
                }
                boolean added = false;
                for(Node next : start.getNexts()) {
                    added = addChild(next, lexique);
                    if (added) break;
                }
                if(!added){
                    Node next = new Node(0, lexique.charAt(0), null, false);
                    start.addNext(next);
                    addChild(next,lexique);
                }
            }
            int debug=0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayAutomate() {

        for (int i = 0; i < nodes.size(); i++) {
            //System.out.println(nodes.get(i).getName());
            if (nodes.get(i).getFiniteState()) {
                System.out.println("Finite State");
                System.out.println(nodes.get(i).getName());
            }
        }

    }

    public void displayWords() {

        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getFiniteState()) {
                System.out.println(nodes.get(i).getString());
            }
        }
    }

  public ArrayList<String> displayFiniteState(String e, boolean withNbrUsed) {
      words.clear();
      if(!e.isEmpty()){
          Node starte = getStartingNode(e);
          if(starte != null)
          for (Node next : starte.getNexts()) {
              displayFiniteState(e, next, withNbrUsed);
          }
      }
      return words;
  }
    public void displayFiniteState(String e, Node node, boolean withNbrUsed) {
      if(node != null) {
          e += node.getName();

          if (node.getFiniteState()) {
              String temp = e;
              if(withNbrUsed)
                  e += " is used " + node.getUsed()+" time";

              words.add(e);
              e = temp;
          }
          for (Node next : node.getNexts()) {
              displayFiniteState(e, next, withNbrUsed);
          }
      }
    }

    public Node getStartingNode(String s){
        if(s.charAt(0) == start.getName()) {
            return start;
        }
        for(Node next : start.getNexts())
        {
            Node temp = getStartingNode(next,s);
            if(temp != null)
                return temp;
        }
        return null;
    }
    public Node getStartingNode(Node n, String s){
        if(n.getName() == s.charAt(0) && s.length() > 1) {
            Node toReturn = null;
            for (Node next : n.getNexts()) {
                toReturn = getStartingNode(next, s.substring(1));
                if(toReturn!= null) return toReturn;
            }
        }
        else if(n.getName() == s.charAt(0) && s.length() == 1){
            return  n;
        }
        return null;

    }
    public void addUsed(String e){
        Node last = getStartingNode(e);
        if(last!= null){
            last.addUsed();
        }
        displayFiniteState(" ", true);
        int debug = 0;
    }
}