package com.cooksys.ftd.assignments.socket;

import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.cooksys.ftd.assignments.socket.model.Config;
import com.cooksys.ftd.assignments.socket.model.Student;

public class Server extends Utils {

    /**
     * Reads a {@link Student} object from the given file path
     *
     * @param studentFilePath the file path from which to read the student config file
     * @param jaxb the JAXB context to use during unmarshalling
     * @return a {@link Student} object unmarshalled from the given file path
     * @throws JAXBException 
     */
    public static Student loadStudent(String studentFilePath, JAXBContext jaxb) throws JAXBException {
    	  Unmarshaller unmarshal = null;
          Student student = null;
          
          unmarshal = jaxb.createUnmarshaller();
          student = (Student) unmarshal.unmarshal(new File(studentFilePath));
      	return student;
      }

    /**
     * The server should load a {@link com.cooksys.ftd.assignments.socket.model.Config} object from the
     * <project-root>/config/config.xml path, using the "port" property of the embedded
     * {@link com.cooksys.ftd.assignments.socket.model.LocalConfig} object to create a server socket that
     * listens for connections on the configured port.
     *
     * Upon receiving a connection, the server should unmarshal a {@link Student} object from a file location
     * specified by the config's "studentFilePath" property. It should then re-marshal the object to xml over the
     * socket's output stream, sending the object to the client.
     *
     * Following this transaction, the server may shut down or listen for more connections.
     * @throws IOException 
     * @throws JAXBException 
     */
    public static void main(String[] args) throws IOException, JAXBException {
    	Socket socket = null;
    	ServerSocket serverSocket = null;
    	JAXBContext context = Utils.createJAXBContext();
    	Integer port = null;
    	OutputStream out = null;
    	Marshaller marshal = null;
    	XMLStreamWriter streamWriter = null;
    	XMLOutputFactory output = XMLOutputFactory.newInstance();
    	Student student = null;
    	Config config = Utils.loadConfig("config.xml", context);
    	
    	port = config.getLocal().getPort();
    	student = loadStudent(config.getStudentFilePath(), context);
    	
    	System.out.println("Waiting...");
    	
    	try {
			serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();
			out = new FilterOutputStream (socket.getOutputStream());
			streamWriter = output.createXMLStreamWriter(out);
			
		} catch (IOException e){
			e.printStackTrace();
		} catch (XMLStreamException e){
			e.printStackTrace();
		}
    	
    	System.out.println("Sending File...");
    	
    	try {
			marshal = context.createMarshaller();
			marshal.marshal(student, streamWriter);
		} catch (JAXBException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
			socket.close();
			serverSocket.close();
		}
    	System.out.println("Finished.");
    }
}