import java.io.*;
import java.util.*;
import java.util.stream.Stream;
import java.io.LineNumberReader;

public class CirculationProblem
{
	public static Graph g;
	public static Queue<Vertex> myQueue=new LinkedList<Vertex>();
	public static int[] Visited;
	public static int numElements;
	public static int[] level;
	public static int[] parent;
	public static int l=0;
	public static Vertex child;
	public static  int sink;
	public static int source;
	public static int length;
	public static int width;
	public static Graph residualGraph;
	
	public static void main(String[] args)
   { 
		int result=0;
		int sum_of_demands=0;
		int max_flow=0;
		args[0]=args[0].toLowerCase();
	     switch(args[0])
	      {
		      case "-b":
					  String File= args[1];
		    	      source=Integer.parseInt(args[2]);
		    	      sink=Integer.parseInt(args[3]);
		    	      g=new Graph(source,sink,File);
		    	      Bfs(g);
		    	      System.out.println();
		              break;
		      case "-f":
		    	     String inputGraph=args[1];
					 try
						(
							FileReader       input = new FileReader(inputGraph);
							LineNumberReader count = new LineNumberReader(input);
						)
						{
							while (count.skip(Long.MAX_VALUE) > 0)
							{
									// Loop just in case the file is > Long.MAX_VALUE or skip() decides to not read the entire file
							}
						result = count.getLineNumber();
						}
					catch(Exception e)
					{
						System.out.println("This Ex2 is printed:"+e);
					}
					 //System.out.println(result);
		    	     g=new Graph(result,inputGraph);
		    	     try
		    	     {
		    	    	 System.out.println("The Max Flow in the Network is : "+ MaxFlowUsingFordFulkerson());
		    	     }
		    	     catch(Exception e)
		    	     {
		    	    	 System.out.println("This Ex3 is printed"+e.getMessage());
		    	     }		    	 
		    	      break;
			   case "-c":
					String IGraph=args[1];
					g=new Graph(IGraph);
					sum_of_demands=g.getSum_of_Demands();
					//System.out.println("Before BFS!")
					//Bfs(g);
					//System.out.println("Adjacency List"+g.getAdjacencyList());
					try
		    	     {
						 max_flow=MaxFlowUsingFordFulkerson();
						 //System.out.println("Max Flow ="+max_flow+" Sum of Demands ="+sum_of_demands);
						 if(max_flow==sum_of_demands)
							System.out.println("Network has Circulation! The Max Flow in the Network is : "+ max_flow);
						 else
							System.out.println("Network doesn't have Circulation since Sum of Demands & Supplies: "+sum_of_demands+
						"is not equal to Max Flow: "+max_flow);
		    	     }
		    	     catch(Exception e)
		    	     {
		    	    	 System.out.println("This  message is printed:"+e.getMessage());
		    	     }	
					break;
		      default:
		    	  
		    	   System.out.println("Please enter : -b inputGraph.txt source sink"
		    	   		+ " "+"-----"+"For finding shortest path using BFS"
		    	   		+"-f inputGraph.txt"+"------"+" For finding Max Flow through a Network using FordFulkerson Algorithm "
						+"-c inputGraph.txt"+"------"+"For solving Circulation problem");
	    } 
   }
	
	public static boolean Bfs(Graph g)
	{
		Vertex u;
		int index=0;
		int num=g.getNoOfVertices();
		boolean[] Visited=new boolean[num];
	    boolean[] alreadyAssignedParent=new boolean[num];
		
		parent=new int[num];
		
		for(int i=0;i<Visited.length;i++)
		{
			Visited[i]=false;
		}
		
		source=g.getSource();
		sink=g.getSink();
		//System.out.println("I'm printing Source bruh!!!"+source);
		//System.out.println("I'm printing Sink bruh!!!"+sink);
		myQueue.add(new Vertex(g.getSource(),0));
		
		List<Vertex> connectedVertices;
		Iterator<Vertex> it;
		Vertex vertex;
		parent[g.getSource()]=0;
		alreadyAssignedParent[0]=true;
		int flag=0;
		
		 long startTime = System.nanoTime();
		 long elapsedTime = 0L;
		 
		try{
		while(!myQueue.isEmpty())
		{
			
			 u=myQueue.poll();
			 Visited[u.node]=true;
			 if(Visited[sink]!=true)
			if(g.getAdjacencyList().containsKey(u.node))
			{
				 connectedVertices=g.getAdjacencyList().get(u.node);	
				 it=connectedVertices.iterator();
				 Visited[u.node]=true;
				while(it.hasNext())
		   	    {
				   vertex=it.next();
				   if(Visited[vertex.node]==false && vertex.capacity>0)
				   {
					   
					   Visited[u.node]=true;
					   myQueue.add(vertex);	   
					   if(alreadyAssignedParent[vertex.node]==false)
					   {
					     parent[vertex.node]=u.node;	
					     alreadyAssignedParent[vertex.node]=true;
					   }
					
				   }
				}
				
			}
		  }
		}
		catch(Exception e)
		{
			System.out.println("In this exception bruh !!!"+e.getMessage());
			e.printStackTrace();
		}
		 elapsedTime=System.nanoTime() - startTime;
	      System.out.println();
	      System.out.println("Time taken: "+elapsedTime);
		  
	      if(Visited[sink]==true)
	      {  
	      
		 System.out.println("The shortest path found using BFS is:");
		 System.out.print(sink+"<--");
		 for(int i=sink;i!=source;i=parent[i])
		 {
			 System.out.print(parent[i]+"<--");
		 }
	      }
	      else
	    	  System.out.println("No path exists from source to sink");
		 
	  	// elapsedTime=System.currentTimeMillis() - startTime;
	      System.out.println();
	     // System.out.println("Time taken: "+elapsedTime);
	      
		if(Visited[sink]==true)
			return true;
		else
			return false;
	}

	
	public static int MaxFlowUsingFordFulkerson() throws CloneNotSupportedException
	{
	   int u,v;
	   int index=0;
	   int maxFlow=0;	
	   int minFlowAlongPath=0;
	   residualGraph=(Graph)g.clone();
	   List<Vertex> mygraph;
	   Vertex ver=null;
	   Vertex myVer=null;
	   //int[] parent=new int[10];
	   long startTime = System.nanoTime();
		 long elapsedTime = 0L;
	   while(Bfs(residualGraph))
	   {
			int path_flow =Integer.MAX_VALUE;
			for (v=sink; v!=source; v=parent[v])
			{
				System.out.println(v);
				u = parent[v];
				List<Vertex> list=residualGraph.getAdjacencyList().get(u);
				for(int i=0;i<list.size();i++)
				{
					if(v==list.get(i).node)
					{
						index=i;
					    break;
					}
				}
				path_flow = Math.min(path_flow,list.get(index).capacity);
			}
			maxFlow+=path_flow;
		  
		  for( v=sink;v!=source;v=parent[v])
		  {
			  u=parent[v];
			  List<Vertex> list=residualGraph.getAdjacencyList().get(u);
			  
				for(int i=0;i<list.size();i++)
				{
					if(v==list.get(i).node)
					{
						ver=list.get(i);
						index=i;
					}
				}
				
			 residualGraph.getAdjacencyList().get(u).get(index).capacity-=path_flow;
			 
			 index=-1;
			 if(residualGraph.getAdjacencyList().containsKey(ver.node))
			 {
				    List<Vertex> mylist=residualGraph.getAdjacencyList().get(ver.node);
					for(int i=0;i<mylist.size();i++)
					{
						
						if(u==mylist.get(i).node)
							index=i;
					}	 
					if(index!=-1)
					{
						residualGraph.getAdjacencyList().get(ver.node).get(index).capacity+=path_flow;// index find of u
					}
					else
					{
					    Vertex ver1=new Vertex(u,path_flow);
					    residualGraph.getAdjacencyList().get(ver.node).add(ver1);
					    
					}
			 }
			 else
			 {
				  myVer=new Vertex(u,path_flow);
				 List<Vertex> vlist=new ArrayList<Vertex>();
				 vlist.add(myVer);
				 residualGraph.getAdjacencyList().put(ver.node,vlist);
			 }
		  }
	   }
		  elapsedTime=System.nanoTime()-startTime;
	      System.out.println();
	      System.out.println("Time taken for finding maxflow: "+elapsedTime);
	   
	   System.out.println();
	   System.out.println();
	 
	   return maxFlow;
	}	
}	
	
//This class represents a DataStructure to store the edges( nodeId and weight of the Edge)	
 class Vertex
{
	int node;
	int capacity;	
	public Vertex(int node,int capacity)
	{
		this.node=node;
		this.capacity=capacity;
	}

}
 


 //This class represents a DataStructure to store a Network.
 class Graph implements Cloneable
 {
	 CirculationProblem b = new CirculationProblem();
     private Map<Integer,List<Vertex>> adjList;
     private int source;
     private int sink;
     private int numOfVertices;
	 private int Sum_of_Demands=0;
	 private int Sum_of_Supplies=0;
   
     public int getSource()
     {
     	return this.source;
     }
     public void setSource(int source)
     {
    	 this.source=source;
     }
     public void setSink(int sink)
     {
    	 this.sink=sink;
     }
     public int getSink()
     {
     	return this.sink;
     }
     public int getNoOfVertices()
     {
     	return this.numOfVertices;
     }
     public void setNoOfVertices(int numOfVertices)
     {
     	if(numOfVertices>=0)
     	{
     		this.numOfVertices=numOfVertices;
     	}
     }
	 public int getSum_of_Demands()
     {
     	return this.Sum_of_Demands;
     }
     public void setSum_of_Demands(int Sum_of_Demands)
     {
    	 this.Sum_of_Demands=Sum_of_Demands;
     }
     public Map<Integer,List<Vertex>> getAdjacencyList()
     {
     	return this.adjList;
     }
     protected Object clone() throws CloneNotSupportedException
     {
         return super.clone();
     }
     
     public Graph(int noOfNodes)
     {
       source=0;
       sink=noOfNodes-1;
       numOfVertices=noOfNodes;
       adjList=new HashMap<Integer,List<Vertex>>();
     }

	  public Graph(int noOfNodes,String inputGraph)
     {
       source=0;
       sink=noOfNodes-1;
       numOfVertices=noOfNodes;
       adjList=new HashMap<Integer,List<Vertex>>();
       
       try
		{
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(new File(inputGraph)));
			String line=br.readLine();
			List<Vertex> ConnectedVertices;
			int vertex=0;
			int n=1;
			 while(line!=null)
			 {
				if(line.isEmpty()) 
					{  
					
					  ConnectedVertices=new ArrayList<Vertex>();
					  adjList.put(vertex,ConnectedVertices);
					  line=br.readLine();
					  vertex++;
					  continue;
					}
				ConnectedVertices=new ArrayList<Vertex>();
				String[] tokens=line.split(" ");
				int[] val=Stream.of(tokens).mapToInt(Integer::parseInt).toArray();
				int i=0;
				for(int j=0;j<val.length;j=j+2)
				{
				   ConnectedVertices.add(new Vertex(val[j],val[j+1]));	
				   n++;
				}
				
				adjList.put(vertex,ConnectedVertices);
				line=br.readLine(); 
				vertex++;
			 }
		//	 this.numOfVertices=n;
		}	
		catch(Exception e)
		{
		   System.out.println("This ex4 is printed:"+e.getMessage());
		   e.printStackTrace();
		}
       
      
       
     }
     
     public Graph(int source,int sink)
     {
     	this.source=source;
     	this.sink=sink;
     	this.adjList=new HashMap<Integer,List<Vertex>>();
     }
	 
	  public Graph(String inputGraph)
     {
		 int noOfNodes=0;
		 int Sum_of_Demands=0;
		 int Sum_of_Supplies=0;
		 int max_flow=0;
		try
		{
			FileReader       input = new FileReader(inputGraph);
			LineNumberReader count = new LineNumberReader(input);
			while (count.skip(Long.MAX_VALUE) > 0)
			{
					// Loop just in case the file is > Long.MAX_VALUE or skip() decides to not read the entire file
			}
		noOfNodes = count.getLineNumber();
		input.close();
		}
		catch(Exception e)
		{
			System.out.println("This Exception is printed:"+e);
			
		}
       source=0;
       sink=noOfNodes+2;
       numOfVertices=noOfNodes+3;
       adjList=new HashMap<Integer,List<Vertex>>();
       
       try
		{
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(new File(inputGraph)));
			String line=br.readLine();
			List<Vertex> ConnectedVertices;
			List<Vertex> DemandVertices;
			List<Vertex> SupplyVertices;
			int vertex=1;
			List<Integer> demand=new ArrayList<>();
			List<Integer> supply=new ArrayList<>();
			SupplyVertices=new ArrayList<Vertex>();
			 while(line!=null)
			 {
				if(line.isEmpty()) 
					{  
					
					  ConnectedVertices=new ArrayList<Vertex>();
					  adjList.put(vertex,ConnectedVertices);
					  line=br.readLine();
					  vertex++;
					  continue;
					}
				ConnectedVertices=new ArrayList<Vertex>();
				//DemandVertices=new ArrayList<Vertex>();
				String[] tokens=line.split(" ");
				int[] val=Stream.of(tokens).mapToInt(Integer::parseInt).toArray();
				if(val[0]>0)
				{
					demand.add(val[0]);
					ConnectedVertices.add(new Vertex(noOfNodes+2,val[0]));
					//System.out.println(vertex+","+(noOfNodes+2)+","+val[0]);
				}
				else if(val[0]<0)
				{
					supply.add(val[0]);
					SupplyVertices.add(new Vertex(vertex,Math.abs(val[0])));
					//System.out.println("0,"+vertex+","+val[0]);
				}
				for(int j=1;j<val.length;j=j+2)
				{
				   ConnectedVertices.add(new Vertex(val[j]+1,val[j+1]));	
				}
				
				adjList.put(vertex,ConnectedVertices);
				//adjList.put(vertex,DemandVertices);
				line=br.readLine(); 
				vertex++;
			 }
				Sum_of_Demands =demand.stream().mapToInt(Integer::intValue).sum();
				Sum_of_Supplies =supply.stream().mapToInt(Integer::intValue).sum();
				if(Sum_of_Demands!=Math.abs(Sum_of_Supplies))
				{
					System.out.println("Circulation doesn't exist as Sum of Demands:"+Sum_of_Demands+"doesn't match Sum of Supplies:"+Sum_of_Supplies);
					System.exit(0);
				}
			 adjList.put(0,SupplyVertices);
			 //System.out.println("Sum_of_Demands ="+demand.stream().mapToInt(Integer::intValue).sum());
			 //System.out.println("Sum_of_Supplies ="+supply.stream().mapToInt(Integer::intValue).sum());
			 adjList.put(vertex,new ArrayList<Vertex>());
			 setSum_of_Demands(Sum_of_Demands);
		}	
		catch(Exception e)
		{
		   e.printStackTrace();	
		}
     }
	 
 	public Graph(int source,int sink,String inputFile)
	 {
 		
 		this.source=source;
 		this.sink=sink;
 		
 		adjList=new HashMap<Integer,List<Vertex>>();

 		try
 		{
 			@SuppressWarnings("resource")
 			BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
 			String line=br.readLine();
 			List<Vertex> ConnectedVertices;
 			int vertex=0;
 			int n=1;
 			 while(line!=null)
 			 {

 				if(line.isEmpty()) 
 					{  
 					 ConnectedVertices=new ArrayList<Vertex>();
 					  adjList.put(vertex,ConnectedVertices);
 					  line=br.readLine();
 					  vertex++;
 					  continue;
 					}
 				
 				ConnectedVertices=new ArrayList<Vertex>();
 				String[] tokens=line.split(" ");
 				int[] val=Stream.of(tokens).mapToInt(Integer::parseInt).toArray();
 				int i=0;
 				for(int j=0;j<val.length;j=j+2)
 				{
 				   ConnectedVertices.add(new Vertex(val[j],val[j+1]));	
 				   n++;
 				}
 				
 				adjList.put(vertex,ConnectedVertices);
 				line=br.readLine(); 
 				vertex++;
 			 }
 			 this.numOfVertices=n;
 		}	
 		catch(Exception e)
 		{
 		   System.out.println("This Exception1 is printed:"+e.getMessage());	
 		}
 	}

 }
	




