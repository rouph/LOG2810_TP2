import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Node> nodes = new ArrayList<>();
    private List<Node> finiteNodes = new ArrayList<>();
    private Node start = new Node();

    public Graph() {
    }

    public boolean samePrevious(Node i, Node c) {

        return i.getString().equals(c.getString());
    }

    public void addChild(Node c) {

        for (Node node : nodes) {
            if (samePrevious(node, c))
                return;
        }
        nodes.add(c);
    }

    public void readFromFile(String filePath) {

        try {
            start.setPrevious(start);
            File fichier = new File(filePath);
            BufferedReader data = new BufferedReader(new FileReader(fichier));
            String lexique;
            while ((lexique = data.readLine()) != null) {
                List<Node> tmpNodes = new ArrayList<>();
                for (int i = 0; i < lexique.length(); i++) {
                    if (i == lexique.length() - 1) {
                        Node finiteNode = new Node(tmpNodes.get(i - 1).getString() + lexique.charAt(i), true);
                        tmpNodes.add(finiteNode);
                        if (i != 0) {
                            finiteNode.setPrevious(tmpNodes.get(i - 1));
                        } else
                            finiteNode.setPrevious(start);

                        addChild(finiteNode);
                        finiteNodes.add(finiteNode);
                    } else {
                        Node node = new Node();
                        if (i == 0) {
                            node = new Node(start.getString() + lexique.charAt(i), false);
                        } else {
                            node = new Node(tmpNodes.get(i - 1).getString() + lexique.charAt(i), false);
                        }
                        tmpNodes.add(node);
                        if (i != 0) {
                            node.setPrevious(tmpNodes.get(i - 1));
                        } else
                            node.setPrevious(start);

                        addChild(node);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayAutomate() {

        for (Node i : nodes)
            System.out.println(i.getString());
    }

    public void displayWords() {

        for (Node i : nodes) {
            if (i.getFiniteState())
                System.out.println(i.getString());
        }
    }

    public void displayPrevious(){

        for (Node i : nodes){
            System.out.println(i.getPrevious().getString());
        }
    }

    public List<String> displayFiniteState(String e) {

        Node nodeOfString = new Node();
        List<String> FiniteStateList = new ArrayList<>();
        for (Node i : nodes) {
            if (i.getString().equals(e)) {
                nodeOfString = i;
                break;
            }
        }
        if (!(nodeOfString.getString().equals(""))) {
            int compt = 0;
            for (Node j : finiteNodes) {
                for (int i = 0; i < nodeOfString.getString().length(); i++) {
                    if (nodeOfString.getString().length() <= j.getString().length()) {
                        if (nodeOfString.getString().charAt(i) == j.getString().charAt(i)) {
                            compt += 1;
                        }
                    }
                }
                if (compt == nodeOfString.getString().length()) {
                    FiniteStateList.add(j.getString());
                }
                compt = 0;
            }
        }
        return FiniteStateList;
    }

}