public class SocialNetworks {
    class Graph {
        int[][] adjacencyMatrix;
        int numVertices;
        int numEdges;

        public Graph(int numVertices) {
            this.numVertices = numVertices;
            this.adjacencyMatrix = new int[numVertices][numVertices];
        }
    }

}
