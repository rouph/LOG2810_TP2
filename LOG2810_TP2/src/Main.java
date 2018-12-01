public class Main {
    public static void main(String[] args) {
        Graph g = new Graph();
        g.readFromFile("./src/lexique5.txt");
        //g.displayAutomate();
        //g.displayWords();
       System.out.println(g.displayFiniteState("broui"));
    }
}
