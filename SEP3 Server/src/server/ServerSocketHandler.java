package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;

import model.Edge;
import model.Node;
import model.Plane;
import server.ServerModel;

public class ServerSocketHandler implements Runnable
{
   private ServerModel model;
   private OutputStream out;
   private InputStream in;
   private Gson gson;

   public ServerSocketHandler(ServerModel model, Socket socket)
   {
      this.model = model;
      this.gson = new Gson();
      try
      {
         in = socket.getInputStream();
         out = socket.getOutputStream();
      }
      catch (IOException e)
      {

         e.printStackTrace();
      }
   }

   private void sendRequest(String jsonRequest) throws IOException
   {
      byte[] toSendBytes = jsonRequest.getBytes();
      int toSendLen = toSendBytes.length;
      byte[] toSendLenBytes = new byte[4];
      toSendLenBytes[0] = (byte) (toSendLen & 0xff);
      toSendLenBytes[1] = (byte) ((toSendLen >> 8) & 0xff);
      toSendLenBytes[2] = (byte) ((toSendLen >> 16) & 0xff);
      toSendLenBytes[3] = (byte) ((toSendLen >> 24) & 0xff);
      out.write(toSendLenBytes);
      out.write(toSendBytes);
   }

   public void requestPlanes() throws IOException
   {
      Request request = new Request("REQUESTPLANES");
      String jsonRequest = gson.toJson(request);
      sendRequest(jsonRequest);
   }

   public void requestNodes() throws IOException
   {
      Request request = new Request("REQUESTNODES");
      String jsonRequest = gson.toJson(request);
      sendRequest(jsonRequest);
   }

   public void requestEdges() throws IOException
   {
      Request request = new Request("REQUESTEDGES");
      String jsonRequest = gson.toJson(request);
      sendRequest(jsonRequest);
   }

   @Override
   public void run()
   {
      try
      {
         requestNodes();
         requestEdges();
         requestPlanes();

      }
      catch (IOException e1)
      {
         // TODO Auto-generated catch block
         e1.printStackTrace();
      }
      while (true)
      {
         try
         {
            byte[] lenBytes = new byte[4];
            in.read(lenBytes, 0, 4);
            int len = (((lenBytes[3] & 0xff) << 24)
                  | ((lenBytes[2] & 0xff) << 16) | ((lenBytes[1] & 0xff) << 8)
                  | (lenBytes[0] & 0xff));
            byte[] receivedBytes = new byte[len];
            in.read(receivedBytes, 0, len);
            String received = new String(receivedBytes, 0, len);
            System.out.println(received);
            Request req = gson.fromJson(received, Request.class);

            if (req.Type.equals("RESPONSEPLANES"))
            {
               ArrayList<Plane> planes = new ArrayList<Plane>(
                     Arrays.asList(req.Planes));
               this.model.loadPlanesFromDatabase(planes);
            }
            else if (req.Type.equals("RESPONSENODES"))
            {
            	System.out.println("hello");
               ArrayList<Node> nodes = new ArrayList<Node>(
                     Arrays.asList(req.Nodes));
               this.model.loadNodesFromDatabase(nodes);
            }
            else if (req.Type.equals("RESPONSEEDGES"))
            {
               ArrayList<Edge> edges = new ArrayList<Edge>(
                     Arrays.asList(req.Edges));
               this.model.loadEdgesFromDatabase(edges);
            }
         }
         catch (IOException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }
}
