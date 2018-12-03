import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Node {
    public Node(){}
    public Node(int position, char name, String mot, boolean state){
        this.position = position;
        this.name = name;
        this.mot = mot;
        this.isFiniteState = state;
    }
    private int numberOfTimeUsed = 0;
    private boolean mostRecently = false;
    private int position = 0;
    private boolean isFiniteState;
    private Node previous;
    private Character name = ' ';
    private String mot = "";

    public boolean getFiniteState(){ return isFiniteState;}
    public char getName() {return name;}

    public int getPosition() {return position;}
    public Node getPrevious() {return previous;}
    public Node setPrevious(Node previous) {return this.previous = previous;}
    public String getString(){return mot;}
    public String setString(String mot){return this.mot = mot;}
    public void addUsed(){numberOfTimeUsed++;};
    public int getUsed(){return numberOfTimeUsed;};

    /////////////////////////////////

    List<Node> nexts = new ArrayList<>();
    public void addNext(Node next) {nexts.add(next);}
    public List<Node> getNexts() {return nexts;}
    public boolean hasNext() {return nexts.size() >0;}
    public int hashCode(){
        return name.hashCode();
    }

    public void setMostRecently(){mostRecently = true;}
    public void resetMostRecently(){mostRecently = false;}
    public boolean isSetMostRecently(){return mostRecently;}
}
