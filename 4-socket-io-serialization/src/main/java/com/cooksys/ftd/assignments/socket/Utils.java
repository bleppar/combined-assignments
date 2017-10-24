package com.cooksys.ftd.assignments.socket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.cooksys.ftd.assignments.socket.model.Config;
import com.cooksys.ftd.assignments.socket.model.LocalConfig;
import com.cooksys.ftd.assignments.socket.model.RemoteConfig;
import com.cooksys.ftd.assignments.socket.model.Student;

/**
 * Shared static methods to be used by both the {@link Client} and {@link Server} classes.
 */
public class Utils {
    /**
     * @return a {@link JAXBContext} initialized with the classes in the
     * com.cooksys.socket.assignment.model package
     * @throws JAXBException 
     */
    public static JAXBContext createJAXBContext() throws JAXBException {
    	JAXBContext context = JAXBContext.newInstance(Student.class, Config.class);
    	return context;
    }

    /**
     * Reads a {@link Config} object from the given file path.
     *
     * @param configFilePath the file path to the config.xml file
     * @param jaxb the JAXBContext to use
     * @return a {@link Config} object that was read from the config.xml file
     * @throws JAXBException 
     */
    public static Config loadConfig(String configFilePath, JAXBContext jaxb) throws JAXBException {
        Unmarshaller unmarshal = null;
        Config config = null;
        
        unmarshal = jaxb.createUnmarshaller();
        config = (Config) unmarshal.unmarshal(new File(configFilePath));
    	return config;
    }
    public static void main(String[] args) throws JAXBException, IOException {
		Config config = new Config();
		LocalConfig localConfig = new LocalConfig();
		RemoteConfig remoteConfig = new RemoteConfig();
		Student student = new Student();
		JAXBContext context = JAXBContext.newInstance(Config.class, Student.class);
		Marshaller marshal = context.createMarshaller();
		
		config.setLocal(localConfig);
		config.setRemote(remoteConfig);
		config.setStudentFilePath("student.xml");
		
		localConfig.setPort(8000);
		
		remoteConfig.setHost("127.0.0.1");
		remoteConfig.setPort(8000);
		
		student.setFavoriteIDE("Eclipse");
		student.setFavoriteLanguage("JavaScript");
		student.setFavoriteParadigm("Paradigm");
		student.setFirstName("Ben");
		student.setLastName("Leppard");
		
		FileOutputStream configOut = new FileOutputStream("config/config.xml");
		marshal.marshal(config, configOut);			
		configOut.close();
		
		FileOutputStream studentOut = new FileOutputStream("config/student.xml");
		marshal.marshal(student, studentOut);
		studentOut.close();
	
	}
}