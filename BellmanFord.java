import java.util.*;
//no collaborators
public class BellmanFord{

    private int[] distances = null;
    private int[] predecessors = null;
    private int source;

    class BellmanFordException extends Exception{
        public BellmanFordException(String str){
            super(str);
        }
    }

    class NegativeWeightException extends BellmanFordException{
        public NegativeWeightException(String str){
            super(str);
        }
    }

    class PathDoesNotExistException extends BellmanFordException{
        public PathDoesNotExistException(String str){
            super(str);
        }
    }

    BellmanFord(WGraph g, int source) throws NegativeWeightException{
        /* Constructor, input a graph and a source
         * Computes the Bellman Ford algorithm to populate the
         * attributes 
         *  distances - at position "n" the distance of node "n" to the source is kept
         *  predecessors - at position "n" the predecessor of node "n" on the path
         *                 to the source is kept
         *  source - the source node
         *
         *  If the node is not reachable from the source, the
         *  distance value must be Integer.MAX_VALUE
         */
        this.source = source;
        List<Integer> range = range(g.getNbNodes());
        predecessors = range.stream().mapToInt(i -> -1).toArray();
        distances = range.stream().mapToInt(i -> i == this.source ? 0 : Integer.MAX_VALUE).toArray();

        ArrayList<Edge> edges = g.getEdges();

        range(g.getNbNodes()-1).forEach(i -> {
            for (Edge edge : edges) {
            if(distances[edge.nodes[0]] < Integer.MAX_VALUE
                    && distances[edge.nodes[0]] + edge.weight < distances[edge.nodes[1]]) {
                distances[edge.nodes[1]]=distances[edge.nodes[0]] + edge.weight;
                predecessors[edge.nodes[1]]=edge.nodes[0];
            }}});

        for (Edge edge : edges) {
            if (distances[edge.nodes[0]] < Integer.MAX_VALUE
                    && distances[edge.nodes[0]] + edge.weight < distances[edge.nodes[1]]) {
                throw new NegativeWeightException("A negative cycle has been found :(");
            }
        }
    }

    /**
     * Returns a list of integers from 0, 1, ..., length-1.
     * It is essentially equivalent to IntStream or Python's range(length), but in List<>.
     */
    private static List<Integer> range(Integer length) {
        ArrayList<Integer> range = new ArrayList<>();

        for (int i=0; i<length; i++) range.add(i);

        return range;
    }

    public int[] shortestPath(int destination) throws PathDoesNotExistException{
        /*Returns the list of nodes along the shortest path from 
         * the object source to the input destination
         * If not path exists an Error is thrown
         */
        List<Integer> pathFromDestination = obtainShortestPathDestination(predecessors, destination);
        Collections.reverse(pathFromDestination);

        int[] shortestPath = pathFromDestination.stream().mapToInt(x->x.intValue()).toArray();
        return shortestPath;
    }

    /**
     * A helper method that obtains the shortest path from destination to source
     */
    private List<Integer> obtainShortestPathDestination(int[] predecessors, int destination)
            throws PathDoesNotExistException{
        List<Integer> pathFromDestination = new ArrayList<>();
        int walk = destination;
        while(walk!=source) {
            if(predecessors[walk]==-1) {
                // the path does not exist. Graph is disconnected.
                throw new PathDoesNotExistException("We cannot find a path from node "
                        + source + " to node "+ destination);
            }
            pathFromDestination.add(walk);
            walk=predecessors[walk];
        }
        pathFromDestination.add(walk);
        return pathFromDestination;
    }

    public void printPath(int destination){
        /*Print the path in the format s->n1->n2->destination
         *if the path exists, else catch the Error and 
         *prints it
         */
        try {
            int[] path = this.shortestPath(destination);
            for (int i = 0; i < path.length; i++){
                int next = path[i];
                if (next == destination){
                    System.out.println(destination);
                }
                else {
                    System.out.print(next + "-->");
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){

        String file = args[0];
        WGraph g = new WGraph(file);
        try{
            BellmanFord bf = new BellmanFord(g, g.getSource());
            bf.printPath(g.getDestination());
        }
        catch (Exception e){
            System.out.println(e);
        }

   } 
}

