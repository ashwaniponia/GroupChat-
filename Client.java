import java.io.*;
import java.net.*;
import java.util.*;

class Client implements Runnable{
   
    final DataInputStream din;
    final DataOutputStream dout;
    boolean flag=true;
    Socket skt;
    String ip;
    Thread send;
    Thread rec;

    Client(String address,int pno)throws Exception{
      ip=address;
      
      skt  = new Socket(address,pno);
      din  = new DataInputStream(skt.getInputStream());
      dout = new DataOutputStream(skt.getOutputStream());
     
      send = new Thread(this);
      rec  = new Thread(this);

      send.start();
      rec.start();
    
    }

  public void run(){
  	if(Thread.currentThread().equals(send)){
  		send();
  	}else{
  		rec();
  	}
  }

    void send(){
       while(flag){
    	try{
    	//System.out.println("Enter the message you want to send:");
        Scanner sc= new Scanner(System.in);
        String msg =sc.nextLine();
        msg+=" # "+ip;
        dout.writeUTF(msg);
        }catch(Exception ex){}
      }
    }

    void rec(){
      while(flag){
        try{
        	String received = din.readUTF();
        	System.out.println(received);
          }catch(Exception ex){}
      }

    }

	public static void main(String args[]){
          try{
             Client cn = new Client("127.0.0.1",5000);	
          }catch(Exception ex){}
           
	}
}