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

    public Graph() {
    }

    public void samePrevious(Node i, Node c) {
        if (i.getPrevious().getName() == c.getPrevious().getName()) {
            hasPrevious = true;
        } else {
            hasPrevious = false;
            return;
        }
        if (i.getPrevious().getName() == i.getName()) {
            return;
        }
        samePrevious(i.getPrevious(), c.getPrevious());
    }

    public void addChild(Node c) {

        for (int i = 0; i < nodes.size(); i++) {
            samePrevious(nodes.get(i), c);
            if (nodes.get(i).getPosition() == c.getPosition()
                    && nodes.get(i).getName() == c.getName()
                    && hasPrevious) {
                return;
            }
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
                        Node finiteNode = new Node(i, lexique.charAt(i), tmpNodes.get(i - 1).getString() + lexique.charAt(i), true);
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
                            node = new Node(i, lexique.charAt(i), start.getString() + lexique.charAt(i), false);
                        } else {
                            node = new Node(i, lexique.charAt(i), tmpNodes.get(i - 1).getString() + lexique.charAt(i), false);
                        }
                        tmpNodes.add(node);
                        if (i != 0) {
                            node.setPrevious(tmpNodes.get(i - 1));
                        } else
                            node.setPrevious(start);

                        addChild(node);
                    }
                    //System.out.println(c);
                }

            }
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

    public List<String> displayFiniteState(String e) {

        Node nodeOfString = new Node();
        List<String> FiniteStateList = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getString().equals(e)) {
                nodeOfString = nodes.get(i);
                break;
            }
        }
        int compt = 0;
        for (int j = 0; j < finiteNodes.size(); j++) {
            for (int i = 0; i < nodeOfString.getString().length(); i++) {
                if (nodeOfString.getString().length() <= finiteNodes.get(j).getString().length()) {
                    if (nodeOfString.getString().charAt(i) == finiteNodes.get(j).getString().charAt(i)) {
                        compt += 1;
                    }
                }
            }
            if (compt == nodeOfString.getString().length()){
                FiniteStateList.add(finiteNodes.get(j).getString());
            }
            compt = 0;
        }
        return FiniteStateList;
    }

}