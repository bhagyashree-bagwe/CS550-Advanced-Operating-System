package evaluation;

import java.util.Scanner;

/**
 * The PerformanceEvaluation class performs and evaluates register, search and download functionality
 * @author Bhagyashree
 * @since  11-05-2017
 */
public class PerformanceEvaluation {
	
	
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String choice;
		EvaluationUtility.startServers();
		while(true)
		{
			
			System.out.println("\nEnter The Option :\n1. Evaluate operations for 1 client \n \n2. Evaluate operations for 2 parallel clients \n \n3. Evaluate operations for 4 clients \n \n4. Evaluate operations for 8 parallel clients\n \n5. Exit\n");	
			choice = scan.nextLine();
			if (choice.equals("1")){
				Thread p1 = new Thread (new EvaluationPeerClient());                     
				p1.start();
			}		
			if (choice.equals("2")){
				Thread p1 = new Thread (new EvaluationPeerClient());                     
				p1.start();
				Thread p2 = new Thread (new EvaluationPeerClient());                     
				p2.start();
			}
			if (choice.equals("3")){
				Thread p1 = new Thread (new EvaluationPeerClient());                     
				p1.start();
				Thread p2 = new Thread (new EvaluationPeerClient());                     
				p2.start();
				Thread p3 = new Thread (new EvaluationPeerClient());                     
				p3.start();
				Thread p4 = new Thread (new EvaluationPeerClient());                     
				p4.start();
			}
			if (choice.equals("4")){
				Thread p1 = new Thread (new EvaluationPeerClient());                     
				p1.start();
				Thread p2 = new Thread (new EvaluationPeerClient());                     
				p2.start();
				Thread p3 = new Thread (new EvaluationPeerClient());                     
				p3.start();
				Thread p4 = new Thread (new EvaluationPeerClient());                     
				p4.start();
				Thread p5 = new Thread (new EvaluationPeerClient());                     
				p5.start();
				Thread p6 = new Thread (new EvaluationPeerClient());                     
				p6.start();
				Thread p7 = new Thread (new EvaluationPeerClient());                     
				p7.start();
				Thread p8 = new Thread (new EvaluationPeerClient());                     
				p8.start();
			}
			if (choice.equals("5")){
				System.out.println("Exiting.");
				System.exit(0); 
			}	
		}
	}
	
}
