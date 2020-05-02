import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class PrereqBuilder {
    
    //this class will be the backbone for the traversal later on in order to topological sort
    //this will be subject to your input!
    
    String course;
    LinkedList<String> coursesVisited;
    HashMap<String, LinkedList<String>> adjacencyList;
    LinkedList<String> topologicalOrder;
    Map<String, Course> courseMap;

    
    public PrereqBuilder(String course, Map<String, Course> courseMap) {
        this.course = course;
        this.courseMap = courseMap;
        this.adjacencyList = new HashMap<String, LinkedList<String>>();
        this.coursesVisited = new LinkedList<String>();
        this.topologicalOrder = new LinkedList<String>();
        
        traversal();
        topological();
    }
    
   //this method traverse through the preqs in a BFS manner adding them to courses visited...
   // Also creates an adjacencyList while doing so. EDGES in the adjacency list go from: preq --> course
    // eg: CIS 160 --> CIS 121. MATH 104 --> MATH 114
    
    private void traversal() {
        Queue <String> current = new LinkedList<String>();
        current.add(course);
        coursesVisited.add(course);
        adjacencyList.put(course, new LinkedList<String>());
        while (!current.isEmpty()) {
            String s = current.remove();
            List<String> preqs = courseMap.get(s).getPrereqs();
            for (String preq : preqs) {
                if (adjacencyList.containsKey(preq)) {
                    LinkedList<String> toInsert = adjacencyList.get(preq);
                    toInsert.add(s);
                    adjacencyList.put(preq, toInsert);
                } else {
                    LinkedList<String> toInsert = new LinkedList<String>();
                    toInsert.add(s);
                    adjacencyList.put(preq, toInsert);                    
                }
                if(!coursesVisited.contains(preq)) {
                    coursesVisited.add(preq);
                    current.add(preq);
                }
            }           
        }
    }
    
    //does the topological ordering... NOTE THAT IT WILL ADD IT IN REVERSE ORDER
    
    private void topological() {
        Queue <String> current = new LinkedList<String>();
        HashMap<String, Integer> indeg = new HashMap<String, Integer>();
        for (String s : adjacencyList.keySet()) {
            if (!indeg.containsKey(s)) {
                indeg.put(s, 0);
            }
            for (String neighbours : adjacencyList.get(s)) {
                if (!indeg.containsKey(neighbours)){
                    indeg.put(neighbours, 1);
                } else {
                    int toPut = indeg.get(neighbours) + 1;
                    indeg.put(neighbours, toPut);
                }
            }
        }
        
        for (String s : adjacencyList.keySet()) {
            if (indeg.get(s).equals(0)) {
                current.add(s);
            }
        }
        
        while (!current.isEmpty()) {
            String toR = current.remove();
            topologicalOrder.add(toR);
            for (String s : adjacencyList.get(toR)) {
                int toD = indeg.get(s) - 1;
                indeg.put(s, toD);
                if (toD == 0) {
                    current.add(s);
                }
            }
            
        }
    }
    
    public List<String> getTopologicalSorting() {
        return topologicalOrder;
    }
}
