package POJO;

import java.io.Serializable;
import java.util.ArrayList;

/*
Author: Bhagyashree Bagwe
Date: 	09/25/2017
A POJO that stores information of registered peer
*/

public class PeerInformation implements Serializable{
String peerHostname;
String filePath;
ArrayList<String> fileNames; 
long peerID;

public String getPeerHostname() {
	return peerHostname;
}
public void setPeerHostname(String peerHostname) {
	this.peerHostname = peerHostname;
}
public String getFilePath() {
	return filePath;
}
public void setFilePath(String filePath) {
	this.filePath = filePath;
}
public ArrayList<String> getFileNames() {
	return fileNames;
}
public void setFileNames(ArrayList<String> fileNames) {
	this.fileNames = fileNames;
}
public long getPeerID() {
	return peerID;
}
public void setPeerID(long peerID) {
	this.peerID = peerID;
}


}
