package model;

import java.util.ArrayList;

public class PathfindingGraph
{
   private ArrayList<Node> nodes;
   private ArrayList<Edge> edges;

   public PathfindingGraph(ArrayList<Node> nodes)
   {
      this.nodes = nodes;
   }

   public ArrayList<Node> getNodes()
   {
      return nodes;
   }

   public synchronized ArrayList<Node> calculateShortestDistance(
         Node startNode, Node endNode)
   {

      nodes.stream().filter(node -> node.equals(startNode)).findFirst().get()
            .setDistanceFromSource(0);

      int evaluatedNodeId = startNode.getNodeId();

      for (int i = 0; i < this.nodes.size(); i++)
      {
         ArrayList<Edge> currentNodeEdges = this.nodes.get(evaluatedNodeId)
               .getJointEdges();

         for (int joinedEdge = 0; joinedEdge < currentNodeEdges
               .size(); joinedEdge++)
         {
            int neighbourIndex = currentNodeEdges.get(joinedEdge)
                  .getNeighbourIndex(evaluatedNodeId);

            if (!this.nodes.get(neighbourIndex).isVisited())
            {
               int tentative = this.nodes.get(evaluatedNodeId)
                     .getDistanceFromSource()
                     + currentNodeEdges.get(joinedEdge).getLength();

               if (tentative < nodes.get(neighbourIndex)
                     .getDistanceFromSource())
               {
                  nodes.get(neighbourIndex).setDistanceFromSource(tentative);
                  ArrayList<Node> shortestPath = new ArrayList<Node>(
                        nodes.get(evaluatedNodeId).getShortestPath());
                  shortestPath.add(nodes.get(evaluatedNodeId));
                  nodes.get(neighbourIndex).setShortestPath(shortestPath);
               }
            }
         }
         nodes.get(evaluatedNodeId).setIsVisited(true);
         evaluatedNodeId = evaluateNextNode();

      }

      nodes.stream().filter(node -> node.equals(endNode)).findFirst().get()
            .addShortDistanceNode(endNode);
      return nodes.stream().filter(node -> node.equals(endNode)).findFirst()
            .get().getShortestPath();
   }

   private int evaluateNextNode()
   {
      {
         int storedNodeIndex = 0;
         int storedDist = Integer.MAX_VALUE;

         for (int i = 0; i < this.nodes.size(); i++)
         {
            int currentDist = this.nodes.get(i).getDistanceFromSource();

            if (!this.nodes.get(i).isVisited() && currentDist < storedDist)
            {
               storedDist = currentDist;
               storedNodeIndex = i;
            }
         }

         return storedNodeIndex;
      }
   }

   public void generateGraph(ArrayList<Node> nodes,
         ArrayList<Edge> edges)
   {

      Edge[] sampleEdges = new Edge[edges.size()];

      for (int i = 0; i < sampleEdges.length; i++)
      {
         sampleEdges[i] = new Edge(edges.get(i).getFromNodeIndex(),
               edges.get(i).getToNodeIndex(), edges.get(i).getLength());
      }

/*
 * GroundNode groundNode0 = new GroundNode("Gate A", 0, new StaticPosition(1095,
 * 770)); GroundNode groundNode1 = new GroundNode("Gate B", 1, new
 * StaticPosition(1100, 730)); GroundNode groundNode2 = new GroundNode("Gate C",
 * 2, new StaticPosition(1090, 690)); GroundNode groundNode3 = new
 * GroundNode("Gate D", 3, new StaticPosition(1070, 660)); GroundNode
 * groundNode4 = new GroundNode("Main Taxiway", 4, new StaticPosition(1020,
 * 700)); GroundNode groundNode5 = new GroundNode("Taxiway Chokepoint", 5, new
 * StaticPosition(845, 238)); GroundNode groundNode6 = new
 * GroundNode("Taxiway A2", 6, new StaticPosition(505, 238)); GroundNode
 * groundNode7 = new GroundNode("Taxiway A2", 7, new StaticPosition(335, 238));
 * GroundNode groundNode8 = new GroundNode("Auxiliary Taxiway C32", 8, new
 * StaticPosition(323, 185)); GroundNode groundNode9 = new
 * GroundNode("Runway 14", 9, new StaticPosition(330, 114)); GroundNode
 * groundNode10 = new GroundNode("Runway", 10, new StaticPosition(520, 114));
 * GroundNode groundNode11 = new GroundNode("Auxiliary Taxiway C33", 11, new
 * StaticPosition(500, 185)); GroundNode groundNode12 = new GroundNode("Runway",
 * 12, new StaticPosition(800, 114)); GroundNode groundNode13 = new
 * GroundNode("Auxiliary Taxiway C34", 13, new StaticPosition(828, 185));
 * GroundNode groundNode14 = new GroundNode("Runway", 14, new
 * StaticPosition(1160, 114)); GroundNode groundNode15 = new
 * GroundNode("Auxiliary Taxiway C35", 15, new StaticPosition(1175, 185));
 * GroundNode groundNode16 = new GroundNode("Runway 25", 16, new
 * StaticPosition(1300, 114)); GroundNode groundNode17 = new
 * GroundNode("Auxiliary Taxiway C36", 17, new StaticPosition(1327, 185));
 * GroundNode groundNode18 = new GroundNode("Taxiway A2", 18, new
 * StaticPosition(1315, 238)); GroundNode groundNode19 = new
 * GroundNode("Taxiway A2", 19, new StaticPosition(1170, 238));
 */

      this.nodes = new ArrayList<Node>();

      for (Node node : nodes)
      {
         this.nodes.add(new Node(node.getName(), node.getNodeId(),
               new StaticPosition(node.getPosition().getXCoordinate(),
                     node.getPosition().getYCoordinate())));
      }
      
      this.edges = new ArrayList<Edge>();
      for (int i = 0; i < sampleEdges.length; i++)
      {
         this.edges.add(sampleEdges[i]);
      }

      for (int i = 0; i < edges.size(); i++)
      {
         this.nodes.get(edges.get(i).getFromNodeIndex()).getJointEdges()
               .add(edges.get(i));
         this.nodes.get(edges.get(i).getToNodeIndex()).getJointEdges()
               .add(edges.get(i));
      }

   }
   


/*
 * public void refreshNodes(ArrayList<GroundNode> nodes, ArrayList<Edge> edges)
 * { this.nodes = nodes; this.edges = edges; for (int i = 0; i < edges.size();
 * i++) { this.nodes.get(edges.get(i).getFromNodeIndex()).getJointEdges()
 * .add(edges.get(i));
 * this.nodes.get(edges.get(i).getToNodeIndex()).getJointEdges()
 * .add(edges.get(i)); } }
 */

}
