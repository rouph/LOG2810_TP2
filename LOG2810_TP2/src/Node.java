import java.util.ArrayList;
import java.util.List;

public class Node {

    public Node(){}

    public Node(String mot, boolean state){
        this.mot = mot;
        this.isFiniteState = state;
    }

    private int numberOfTimeUsed = 0;
    private int mostRecently = 0;
    private boolean isFiniteState;
    private Node previous;
    private String mot = "";

    public boolean getFiniteState(){ return isFiniteState;}
    public Node getPrevious() {return previous;}
    public Node setPrevious(Node previous) {return this.previous = previous;}
    public String getString(){return mot;}

}
