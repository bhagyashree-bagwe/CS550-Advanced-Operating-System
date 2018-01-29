package evaluation;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import server.CentralIndexingServerImpl;
/*
Author: Bhagyashree Bagwe
Date: 	09/24/2017
*/
public class PerformanceEvaluation {

	static int DOWNLOAD_TEST_COUNT = 1000;
	static int SEARCH_TEST_COUNT = 1000;
	static String CIS_HOSTNAME;
	static String searchFileName;
	
	public static void main(String[] args) {
		
		BufferedReader input = null;
        
		try 
		{
			//Testing Search functionality
			input = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter CIS hostname:");
			CIS_HOSTNAME = input.readLine();
			System.out.println("Enter the file name to search:");
			searchFileName=input.readLine();
			System.out.println("Enter the Number Of Request per Peer:");
			SEARCH_TEST_COUNT = Integer.parseInt(input.readLine());
			
			(new PerformaceEvaluationImpl(CIS_HOSTNAME, searchFileName, 2001, SEARCH_TEST_COUNT)).start();
			(new PerformaceEvaluationImpl(CIS_HOSTNAME, searchFileName, 2001, SEARCH_TEST_COUNT)).start();
			(new PerformaceEvaluationImpl(CIS_HOSTNAME, searchFileName, 2001, SEARCH_TEST_COUNT)).start();
			
			//Testing Download functionality
			System.out.println("Enter Peer hostname:");
			CIS_HOSTNAME = input.readLine();
			System.out.println("Enter the file name to Download:");
			searchFileName=input.readLine();
			System.out.println("Enter the Number Of Request per Peer:");
			SEARCH_TEST_COUNT = Integer.parseInt(input.readLine());
			
			(new PerformaceEvaluationImpl(CIS_HOSTNAME, searchFileName, 2000, SEARCH_TEST_COUNT)).start();
			(new PerformaceEvaluationImpl(CIS_HOSTNAME, searchFileName, 2000, SEARCH_TEST_COUNT)).start();
			(new PerformaceEvaluationImpl(CIS_HOSTNAME, searchFileName, 2000, SEARCH_TEST_COUNT)).start();
				
		} 
		catch(NumberFormatException e) 
		{
			System.out.println("Wrong input. Please Enter Number only. Try again!!!");
			System.exit(0);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	
	
}
