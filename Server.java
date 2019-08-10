import java.io.*;
import java.net.*;
import java.util.*;

class Server implements Runnable{
 ServerSocket port;
 Thread t;
 static int i=0;

 boolean flag;
 static LinkedList<ClientProcessor>obj= new LinkedList<ClientProcessor>();

  Server(int pn)throws Exception{
   port = new ServerSocket(pn);
   flag=true;
   t= new Thread(this);
   t.start();
  }

  public void run(){
  
     	acceptConnections();
   
  }

  void acceptConnections(){

      Socket clnt;
      flag=true;

      while(flag){
        i++;
        try{
           System.out.println("Waiting for the clients to join");
           clnt = port.accept();
           System.out.println("Connection Accepted");     
           
           DataInputStream din = new DataInputStream(clnt.getInputStream());
           DataOutputStream dout = new DataOutputStream(clnt.getOutputStream());

           
           ClientProcessor c = new ClientProcessor(clnt,"Client "+i,din,dout);
           Thread t = new Thread(c);

           obj.add(c);
           t.start();
          

         }catch(Exception ex)
         {}
      }//while

  }

  public static void main(String args[]){
      try{
      Server svr =  new Server(5000);
      }catch(Exception ex){}

  }

}

class ClientProcessor implements Runnable{
   
  private String name;
  Socket skt;
  final DataInputStream din;
  final DataOutputStream dout;
  boolean loggedin;

   ClientProcessor(Socket s,String name,DataInputStream din,DataOutputStream dout){
      skt=s;
      this.din = din;
      this.dout = dout;
      this.name=name;
      loggedin =true;
   }
 
  public void run(){
    String received;
    while(true){
        try{

        	received = din.readUTF();

        	System.out.println(received);

        	if(received.equals("logout")){
        		this.loggedin = false;
        		this.skt.close();
        		break;
        	}

        	StringTokenizer st = new StringTokenizer(received,"#");         
            String MsgToSend = st.nextToken();
            String recipient = st.nextToken();

            for (ClientProcessor mc : Server.obj){
                 if(mc.loggedin == true){
                 	mc.dout.writeUTF(this.name+" : "+MsgToSend);
                 }
            }


        }catch(Exception ex){}

    }

    try{
    	this.din.close();
    	this.dout.close();
    }catch(Exception ex){
    	ex.printStackTrace();
    }
  }

}