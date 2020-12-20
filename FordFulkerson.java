import java.io.File;
import java.util.*;
//no collaborators
public class FordFulkerson {

	public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph){
		ArrayList<Integer> path = new ArrayList<Integer>();
		/* YOUR CODE GOES HERE*/
		ArrayList<Integer> potentialPath = new ArrayList<>();
		//This will be our stack that searches through our graph
		Stack<Integer> scout = new Stack<>();
		potentialPath.add(source);
		scout.add(source);

		while (!scout.empty()) {
			Integer nodeToCheck = scout.pop();
			if (destination.equals(nodeToCheck)) break;
			includeNeighboursFromTheNode(nodeToCheck, graph.listOfEdgesSorted().iterator(), scout, potentialPath);
			}
		path = potentialPath.contains(destination) ? potentialPath : path;
		return path;
	}

	/**
	 * A helper method that searches and filters in the neighbours of a given node.
	 */
	private static void includeNeighboursFromTheNode(Integer node, Iterator<Edge> edges,
													Stack<Integer> stack, ArrayList<Integer> list) {
		while (edges.hasNext()) {
			Edge edge = edges.next();
			if (node.intValue() == edge.nodes[0]) {
				Integer neighbour = edge.nodes[1];
				if (!list.contains(neighbour) && edge.weight.intValue() != 0) {
					stack.push(neighbour);
					list.add(neighbour);
					break;
				}
			}
		}
	}

	public static String fordfulkerson( WGraph graph){
		String answer="";
		int maxFlow = 0;
		
		/* YOUR CODE GOES HERE		*/
		WGraph residualGraph = new WGraph(graph);

		graph.getEdges().forEach(edge -> graph.setEdge(edge.nodes[0], edge.nodes[1], 0));
		ArrayList<Integer> paths = pathDFS(graph.getSource(), graph.getDestination(), residualGraph);

		while (!paths.isEmpty()) {
			if (paths.contains(residualGraph.getDestination())) {
				ArrayList<Integer> finalPaths = paths;

				List<Integer> range = range(paths.size()-1);

				int beta = range.stream()
						.map(i -> residualGraph.getEdge(finalPaths.get(i), finalPaths.get(i+1)).weight)
						.mapToInt(Integer::intValue).min().getAsInt();

				maxFlow += beta;

				range.forEach(i -> {
					Integer leftNode = finalPaths.get(i);
					Integer rightNode = finalPaths.get(i+1);
					Integer flow = graph.getEdge(leftNode, rightNode).weight;
					Integer capacityLeft = residualGraph.getEdge(leftNode, rightNode).weight;
					graph.setEdge(leftNode, rightNode, flow + beta);
					residualGraph.setEdge(leftNode, rightNode, capacityLeft - beta);
				});
			}
			paths = pathDFS(residualGraph.getSource(), residualGraph.getDestination(), residualGraph);
		}
		answer += maxFlow + "\n" + graph.toString();	
		return answer;
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

	 public static void main(String[] args){
		 String file = args[0];
		 File f = new File(file);
		 WGraph g = new WGraph(file);
	         System.out.println(fordfulkerson(g));
	 }
}

