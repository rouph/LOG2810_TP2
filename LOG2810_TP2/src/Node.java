import java.util.ArrayList;
import java.util.List;

public class Node {
    public Node(){}
    public Node(int position, char name, String mot, boolean state){
        this.position = position;
        this.name = name;
        this.mot = mot;
        this.isFiniteState = state;
    }
    private int numberOfTimeUsed = 0;
    private int mostRecently = 0;
    private int position = 0;
    private boolean isFiniteState;
    private Node previous;
    private char name = ' ';
    private String mot = "";

    public boolean getFiniteState(){ return isFiniteState;}
    public char getName() {return name;}
    public int getPosition() {return position;}
    public Node getPrevious() {return previous;}
    public Node setPrevious(Node previous) {return this.previous = previous;}
    public String getString(){return mot;}
    public String setString(String mot){return this.mot = mot;}

}
