package com.thinking.machines.network.client;
import com.thinking.machines.network.common.*;
import com.thinking.machines.network.common.exceptions.*;
import java.net.*;
import java.io.*;

public class NetworkClient {
  static Socket socket;
  static InputStream is;
  static OutputStream os;

  public static void sendInChunks(byte[] data) throws IOException{
   int bytesToSend= data.length;
   int chunkSize=1024;
   for(int i=0;i<bytesToSend;i+=chunkSize)
   {
    if((bytesToSend-i)<chunkSize)
    chunkSize=bytesToSend-i;
  
    os.write(data,i,chunkSize);
    os.flush();
   } 
  }

  public static byte[] recieveChunks(int bytesToRecieve) throws IOException{
   int bytesRead;
   byte[] recievedData=new byte[bytesToRecieve];
   byte[] tmp= new byte[1024];
   
   for(int bytesRecieved=0;bytesRecieved<bytesToRecieve;)
   {
    bytesRead=is.read(tmp);
    if(bytesRead==-1)
    continue;
    
    for(int i=0;i<bytesRead;i++)
    {
     recievedData[bytesRecieved]=tmp[i];
     bytesRecieved++;
    }
   }
   return recievedData;
  }

  public static void waitForAcknowledgemnet() throws IOException{
   int byteReadCount;
   byte ack[]=new byte[1];
   while(true)
   {
    byteReadCount=is.read(ack);
    if(byteReadCount==-1) continue;
   
    break;
   } 
  }

  public static void sendAcknowledgement() throws IOException{
   byte ack[]=new byte[1];
   os.write(ack,0,1);
   os.flush();
  }

  public static byte[] createHeader(int x){
   byte result[]= new byte[1024];
   int i=1023;
   while(x>0)
   {
    result[i]=(byte)(x%10);
    x/=10;
    i--;
   }
   return result;
  }    
   
  public static int parseHeader(byte[] header) {
   int no=0;
   int j=1;
   for(int i=1023;i>=0;i--)
   {
    no=no+(header[i]*j);
    j*=10;
   }
   return no;
  }

  public Response send(Request request) throws NetworkException{
   try
   {
    socket= new Socket("localhost",5500);
    is= socket.getInputStream();
    os= socket.getOutputStream();
  
    ByteArrayOutputStream baos= new ByteArrayOutputStream();
    ObjectOutputStream oos= new ObjectOutputStream(baos);
    oos.writeObject(request);
    byte serializedObject[]= baos.toByteArray();
  
    byte header[]=createHeader(serializedObject.length);
    sendInChunks(header);
    waitForAcknowledgemnet();
    sendInChunks(serializedObject);
      
    byte headerRecieved[]= recieveChunks(1024);
    int responseLength= parseHeader(headerRecieved);
    sendAcknowledgement();
    byte[] response= recieveChunks(responseLength);
   
    ByteArrayInputStream bais= new ByteArrayInputStream(response);
    ObjectInputStream ois= new ObjectInputStream(bais);
    Response responseObject=(Response)ois.readObject();
    socket.close();
    return responseObject;
   }
   catch(Exception e)
   {
    NetworkException ne=new NetworkException(e.getMessage());
    throw ne;
   }
  }
}