import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Etat {
    public Etat(){}
    public Etat(char name, boolean state){
        this.name = name;
        this.isFiniteState = state;
    }
    private int numberOfTimeUsed = 0;
    private boolean mostRecently = false;
    private boolean isFiniteState;
    private Character name = ' ';
    private List<Etat> nexts = new ArrayList<>();

    public boolean getFiniteState(){ return isFiniteState;}

    public char getName() {return name;}

    public void addUsed(){numberOfTimeUsed++;};

    public int getUsed(){return numberOfTimeUsed;};

    public void addNext(Etat next) {nexts.add(next);}

    public List<Etat> getNexts() {return nexts;}

    public boolean hasNext() {return nexts.size() > 0;}

    public int hashCode(){
        return name.hashCode();
    }

    public void setMostRecently(){mostRecently = true;}

    public void resetMostRecently(){mostRecently = false;}

    public boolean isSetMostRecently(){return mostRecently;}

}
