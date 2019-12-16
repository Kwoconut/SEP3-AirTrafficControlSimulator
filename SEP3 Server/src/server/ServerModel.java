package server;

import java.util.ArrayList;

import model.PathfindingGraph;
import model.Edge;
import model.Node;
import model.NodeDTO;
import model.InAirState;
import model.Plane;
import model.PlaneDTO;
import model.StaticPosition;
import model.Timer;

public class ServerModel
{
   private ArrayList<Plane> groundPlanes;
   private ArrayList<Plane> airPlanes;
   private ArrayList<Plane> simulationGroundPlanes;
   private ArrayList<Plane> simulationAirPlanes;
   private ArrayList<Node> nodes;
   private ArrayList<Edge> edges;
   private PathfindingGraph airportGraph;
   private PathfindingGraph airspaceGraph;
   private Timer timer;
   private boolean wind;

   public ServerModel()
   {
      groundPlanes = new ArrayList<Plane>();
      airPlanes = new ArrayList<Plane>();
      simulationGroundPlanes = new ArrayList<Plane>();
      simulationAirPlanes = new ArrayList<Plane>();
      wind = false;
      timer = new Timer(0, 0, 8);
   }

   public void loadPlanesFromDatabase(ArrayList<Plane> planes)
   {
      for (int i = 0; i < planes.size(); i++)
      {
         if (planes.get(i).getFlightPlan().getEndLocation().equals("Aalborg"))
         {
            planes.get(i).setState(new InAirState());
            planes.get(i).getPosition()
                  .setPosition(getApproachNodesByDirection(
                        planes.get(i).getFlightPlan().getStartLocation())
                              .getPosition());

            airPlanes.add(planes.get(i));
            setInitialRoute(airPlanes.get(i).getRegistrationNo(),
                  getApproachNodesByDirection(
                        planes.get(i).getFlightPlan().getStartLocation())
                              .getNodeId(),
                  getAirNodes().stream().filter(node -> node.getNodeId() == 22)
                        .findFirst().get().getNodeId());
         }
         if (planes.get(i).getFlightPlan().getStartLocation().equals("Aalborg"))
         {
            planes.get(i).landPlane(getLandingNode(getWind()));
            groundPlanes.add(planes.get(i));
         }
      }
   }

   public void loadNodesFromDatabase(ArrayList<Node> nodes)
   {
      this.nodes = nodes;
      airportGraph = new PathfindingGraph(getGroundNodes());
      airspaceGraph = new PathfindingGraph(getAirNodes());

      for (Node node : nodes)
      {
         node.setShortestPath(new ArrayList<Node>());
         node.setJointEdges(new ArrayList<Edge>());
      }
   }

   public ArrayList<Node> getGroundNodes()
   {
      ArrayList<Node> groundNodes = new ArrayList<Node>();
      for (int i = 0; i < nodes.size(); i++)
      {
         if (nodes.get(i).IsGroundNode())
         {
            groundNodes.add(nodes.get(i));
         }
      }
      return groundNodes;
   }

   public ArrayList<Node> getAirNodes()
   {
      ArrayList<Node> airNodes = new ArrayList<Node>();
      for (int i = 0; i < nodes.size(); i++)
      {
         if (!nodes.get(i).IsGroundNode())
         {
            airNodes.add(nodes.get(i));
         }
      }
      return airNodes;
   }

   public void loadEdgesFromDatabase(ArrayList<Edge> edges)
   {
      this.edges = edges;
   }

   public ArrayList<Edge> getAirEdges()
   {
      ArrayList<Edge> airEdges = new ArrayList<Edge>();
      for (int i = 0; i < edges.size(); i++)
      {
         if (!edges.get(i).isGroundEdge())
         {
            airEdges.add(edges.get(i));
         }
      }
      return airEdges;
   }

   public ArrayList<Edge> getGroundEdges()
   {
      ArrayList<Edge> groundEdges = new ArrayList<Edge>();
      for (int i = 0; i < edges.size(); i++)
      {
         if (edges.get(i).isGroundEdge())
         {
            groundEdges.add(edges.get(i));
         }
      }
      return groundEdges;
   }

   public ArrayList<Plane> getGroundPlanes()
   {
      return groundPlanes;
   }

   public ArrayList<Plane> getAirPlanes()
   {
      return airPlanes;
   }

   public ArrayList<Plane> getSimulationGroundPlanes()
   {
      return simulationGroundPlanes;
   }

   public ArrayList<Plane> getSimulationAirPlanes()
   {
      return simulationAirPlanes;
   }

   public ArrayList<PlaneDTO> getSimulationGroundPlanesDTO()
   {

      ArrayList<PlaneDTO> planesToSend = new ArrayList<PlaneDTO>();
      for (int i = 0; i < simulationGroundPlanes.size(); i++)
      {
         planesToSend.add(simulationGroundPlanes.get(i).convertToDTO());
      }
      return planesToSend;
   }

   public ArrayList<PlaneDTO> getSimulationAirPlanesDTO()
   {
      ArrayList<PlaneDTO> planesToSend = new ArrayList<PlaneDTO>();
      for (int i = 0; i < simulationAirPlanes.size(); i++)
      {
         planesToSend.add(simulationAirPlanes.get(i).convertToDTO());
      }
      return planesToSend;
   }

   public ArrayList<NodeDTO> getGroundNodesDTO()
   {
      ArrayList<NodeDTO> nodes = new ArrayList<NodeDTO>();
      for (int i = 0; i < this.nodes.size(); i++)
      {
         if (this.nodes.get(i).IsGroundNode())
         {
            nodes.add(this.nodes.get(i).convertToDTO());
         }
      }
      return nodes;
   }

   public ArrayList<NodeDTO> getAirNodesDTO()
   {
      ArrayList<NodeDTO> nodes = new ArrayList<NodeDTO>();
      for (int i = 19; i < this.nodes.size(); i++)
      {
         if (!this.nodes.get(i).IsGroundNode())
         {
            nodes.add(this.nodes.get(i).convertToDTO());
         }
      }
      return nodes;
   }

   public PathfindingGraph getGraph()
   {
      return airportGraph;
   }

   public boolean getWind()
   {
      return wind;
   }

   public void changeWind()
   {
      wind = !wind;
   }

   public void changeGroundPlaneRoute(String registrationNo, int startNodeId,
         int endNodeId)
   {
      simulationGroundPlanes.stream()
            .filter(plane -> plane.getRegistrationNo().equals(registrationNo))
            .findFirst().get().stopPlane();

      airportGraph.generateGraph(nodes, edges);

      ArrayList<Node> shortestDistance = airportGraph.calculateShortestDistance(
            airportGraph.getNodes().get(startNodeId),
            airportGraph.getNodes().get(endNodeId));

      simulationGroundPlanes.stream()
            .filter(plane -> plane.getRegistrationNo().equals(registrationNo))
            .findFirst().get().setRoute(shortestDistance);

   }

   public void setInitialRoute(String registrationNo, int startNodeId,
         int endNodeId)
   {
      airPlanes.stream()
            .filter(plane -> plane.getRegistrationNo().equals(registrationNo))
            .findFirst().get().stopPlane();

      if (!wind)
      {
         edges.get(23).setLength(200);
         edges.get(24).setLength(1);
      }
      else
      {
         edges.get(23).setLength(200);
         edges.get(24).setLength(1);
      }

      airspaceGraph.generateGraph(nodes, edges);

      ArrayList<Node> shortestDistance = airspaceGraph
            .calculateShortestDistance(
                  airspaceGraph.getNodes().get(startNodeId),
                  airspaceGraph.getNodes().get(endNodeId));

      airPlanes.stream()
            .filter(plane -> plane.getRegistrationNo().equals(registrationNo))
            .findFirst().get().setRoute(shortestDistance);
   }

   public void setTakeoffRoute(String registrationNo, int startNodeId,
         int endNodeId, boolean direction)
   {
      simulationAirPlanes.stream()
            .filter(plane -> plane.getRegistrationNo().equals(registrationNo))
            .findFirst().get().stopPlane();

      if (!direction)
      {
         edges.get(23).setLength(200);
         edges.get(24).setLength(1);
      }
      else
      {
         edges.get(23).setLength(1);
         edges.get(24).setLength(200);
      }

      airspaceGraph.generateGraph(nodes, edges);

      ArrayList<Node> shortestDistance = airspaceGraph
            .calculateShortestDistance(
                  airspaceGraph.getNodes().get(startNodeId),
                  airspaceGraph.getNodes().get(endNodeId));

      simulationAirPlanes.stream()
            .filter(plane -> plane.getRegistrationNo().equals(registrationNo))
            .findFirst().get().setRoute(shortestDistance);

   }

   public void setPlaneOnCourse(String registrationNo, int startNodeId)
   {
      int endNodeId = 0;

      simulationAirPlanes.stream()
            .filter(plane -> plane.getRegistrationNo().equals(registrationNo))
            .findFirst().get().stopPlane();

      if (simulationAirPlanes.stream()
            .filter(plane -> plane.getRegistrationNo().equals(registrationNo))
            .findFirst().get().isReadyForTakeOff())
      {
         if (wind)
         {
            edges.get(23).setLength(200);
            edges.get(24).setLength(1);
         }
         else
         {
            edges.get(23).setLength(1);
            edges.get(24).setLength(200);
         }

         endNodeId = getApproachNodesByDirection(simulationAirPlanes.stream()
               .filter(
                     plane -> plane.getRegistrationNo().equals(registrationNo))
               .findFirst().get().getFlightPlan().getStartLocation())
                     .getNodeId();
      }
      else
      {
         if (!wind)
         {
            edges.get(23).setLength(200);
            edges.get(24).setLength(1);
         }
         else
         {
            edges.get(23).setLength(1);
            edges.get(24).setLength(200);
         }

         endNodeId = getApproachNodesByDirection(simulationAirPlanes.stream()
               .filter(
                     plane -> plane.getRegistrationNo().equals(registrationNo))
               .findFirst().get().getFlightPlan().getEndLocation()).getNodeId();
      }

      airspaceGraph.generateGraph(nodes, edges);

      ArrayList<Node> shortestDistance = airspaceGraph
            .calculateShortestDistance(
                  airspaceGraph.getNodes().get(startNodeId),
                  airspaceGraph.getNodes().get(endNodeId));

      simulationAirPlanes.stream()
            .filter(plane -> plane.getRegistrationNo().equals(registrationNo))
            .findFirst().get().setRoute(shortestDistance);
   }

   public void addGroundPlane(Plane plane)
   {
      groundPlanes.add(plane);
   }

   public void addAirPlane(Plane plane)
   {
      airPlanes.add(plane);
   }

   public void incrementTimer()
   {
      timer.increment();
   }

   public Timer getTimer()
   {
      return timer;
   }

   public ArrayList<Node> getGateNodes()
   {
      ArrayList<Node> gateNodes = new ArrayList<Node>();

      for (Node nodes : this.nodes)
      {
         if (nodes.getName().contains("Gate"))
         {
            gateNodes.add(nodes);
         }
      }
      return gateNodes;
   }

   public Node getLandingNode(boolean wind)
   {
      if (wind)
      {
         return nodes.get(16);
      }
      else
      {
         return nodes.get(9);
      }
   }

   public void reRoutePlane(String callSign, StaticPosition position)
   {
      ArrayList<Node> route = new ArrayList<Node>();
      StaticPosition planePosition = airPlanes.stream()
            .filter(plane -> plane.getRegistrationNo().equals(callSign))
            .findFirst().get().getPosition();
      double xCoordinateLine = planePosition.getXCoordinate()
            - position.getXCoordinate();
      double yCoordinateLine = planePosition.getYCoordinate()
            - position.getYCoordinate();
      route.add(new Node("Aalborg Airspace", 50, position));
      for (int i = 0; i < 15; i++)
      {
         route.add(new Node("Aalborg Airpsace", 50, new StaticPosition(
               route.get(i).getPosition().getXCoordinate() - xCoordinateLine,
               route.get(i).getPosition().getYCoordinate() - yCoordinateLine)));
      }
      airPlanes.stream()
            .filter(plane -> plane.getRegistrationNo().equals(callSign))
            .findFirst().get().setRoute(route);
   }

   public Node getApproachNodesByDirection(String cityName)
   {
      return this.nodes.stream().filter(
            node -> !node.IsGroundNode() && node.getName().contains(cityName))
            .findFirst().get();
   }

   public void stopPlane(String registrationNo)
   {
      this.simulationGroundPlanes.stream()
            .filter(plane -> plane.getRegistrationNo().equals(registrationNo))
            .findFirst().get().stopPlane();
   }
}
