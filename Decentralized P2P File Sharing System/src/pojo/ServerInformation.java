package pojo;

import java.io.Serializable;

/**
 * POJO for server
 * 
 * @author Bhagyashree
 * @since 11-05-2017
 */
public class ServerInformation implements Serializable{
String name;
String ipAddress;
String registerPort;
String searchPort;
public String getIpAddress() {
	return ipAddress;
}
public void setIpAddress(String ipAddress) {
	this.ipAddress = ipAddress;
}
public String getRegisterPort() {
	return registerPort;
}
public void setRegisterPort(String registerPort) {
	this.registerPort = registerPort;
}
public String getSearchPort() {
	return searchPort;
}
public void setSearchPort(String searchPort) {
	this.searchPort = searchPort;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}

}
