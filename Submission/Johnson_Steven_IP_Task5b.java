import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Johnson_Steven_IP_Task5b {
	// Database credentials
	final static String HOSTNAME = "john0387-sql-server.database.windows.net";
	final static String DBNAME = "cs-dsa-4513-sql-db";
	final static String USERNAME = "john0387";
	final static String PASSWORD = "hootnanny111.OU";
 
	// Database connection string
	final static String URL = 
			String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
			HOSTNAME, DBNAME, USERNAME, PASSWORD);
 
	// Query templates for each query 1-15
	// Every statement in each query got its own template
	// Despite how inefficient this method is, everything that I tried to implement works!
	// Note: some queries had to be split into multiple steps hence the notation: a, b, c / i, ii, iii
	final static String QUERY_TEMPLATE_1 = 
			"INSERT INTO Customer " + 
			"VALUES (?, ?, ?);";
	
	final static String QUERY_TEMPLATE_2 = 
			"INSERT INTO Department " +
			"VALUES (?, ?, NULL);";
	
	final static String QUERY_TEMPLATE_3 = 
			"INSERT INTO Process " +
			"VALUES (?, ?, NULL, ?, NULL);";
	
	final static String QUERY_TEMPLATE_3a = 
			"INSERT INTO Fit_Process " +
			"VALUES (?, ?);";
	
	final static String QUERY_TEMPLATE_3b = 
			"INSERT INTO Paint_Process " +
			"VALUES (?, ?, ?);";
	
	final static String QUERY_TEMPLATE_3c = 
			"INSERT INTO Cut_Process " +
			"VALUES (?, ?, ?);";
	
	final static String QUERY_TEMPLATE_4a = 
			"INSERT INTO Assembly_Obj " +
			"VALUES (?, ?, ?, ?, NULL);";
	
	final static String QUERY_TEMPLATE_4b = 
			"UPDATE Process " +
			"SET assembly_id = ? " +
			"WHERE process_id = ?;";
	
	final static String QUERY_TEMPLATE_5 = 
			"INSERT INTO Account " +
			"VALUES (?);";
	
	final static String QUERY_TEMPLATE_5ai = 
			"INSERT INTO Assembly_Acc " +
			"VALUES (?, ?);";
	
	final static String QUERY_TEMPLATE_5aii = 
			"UPDATE Assembly_Obj " +
			"SET account_no = ? " +
			"WHERE assembly_id = ?;";
	
	final static String QUERY_TEMPLATE_5bi = 
			"INSERT INTO Department_Acc " +
			"VALUES (?, ?);";
	
	final static String QUERY_TEMPLATE_5bii = 
			"UPDATE Department " +
			"SET account_no = ? " +
			"WHERE department_no = ?;";
	
	final static String QUERY_TEMPLATE_5ci = 
			"INSERT INTO Process_Acc " +
			"VALUES (?, ?);";
	
	final static String QUERY_TEMPLATE_5cii = 
			"UPDATE Process " +
			"SET account_no = ? " +
			"WHERE process_id = ?;";
	
	final static String QUERY_TEMPLATE_6a = 
			"INSERT INTO Job " +
			"VALUES (?, ?, NULL, ?);";
	
	final static String QUERY_TEMPLATE_6b = 
			"UPDATE Process " +
			"SET assembly_id = ? " +
			"WHERE process_id = ?;";
	
	final static String QUERY_TEMPLATE_7 = 
			"UPDATE Job " +
			"SET date_completed = ? " +
			"WHERE job_no = ?;";
	
	final static String QUERY_TEMPLATE_7a = 
			"INSERT INTO Fit_Job " +
			"VALUES (?, ?);";
	
	final static String QUERY_TEMPLATE_7b = 
			"INSERT INTO Paint_Job " +
			"VALUES (?, ?, ?, ?);";
	
	final static String QUERY_TEMPLATE_7c = 
			"INSERT INTO Cut_Job " +
			"VALUES (?, ?, ?, ?, ?);";
	
	//placeholder for query 8, DID NOT implement
	
	final static String QUERY_TEMPLATE_9 = 
			"SELECT SUM(ISNULL(S.sup_cost, 0)) " +
			"FROM (" +
				"SELECT sup_cost " +
				"FROM Transaction_Obj " +
				"WHERE " +
					"job_no IN(" +
						"SELECT job_no " +
						"FROM Job " +
						"WHERE process_id IN(" +
							"SELECT process_id " +
							"FROM Process " +
							"WHERE " +
								"assembly_id = ?)" +
					")" +
			") AS S;";
	
	final static String QUERY_TEMPLATE_10 = 
		"SELECT SUM(S.labor_time) " + 
		"FROM (" + 
			"SELECT job_no, labor_time FROM Fit_Job " +
			"UNION " +
			"SELECT job_no, labor_time FROM Paint_Job " + 
			"UNION " +
			"SELECT job_no, labor_time FROM Cut_Job" +
		") AS S " +
		"WHERE S.job_no IN(" +
			"SELECT job_no " +
			"FROM Job " +
			"WHERE " +
				"date_completed = ? AND process_id IN(" +
					"SELECT process_id " +
					"FROM Process " +
					"WHERE department_no = ?" +
				")" +
		");";
	
	final static String QUERY_TEMPLATE_11 = 
			"SELECT process_id, department_no " +
			"FROM Process " +
			"WHERE assembly_id = ?;";
	
	//placeholder for query 12, DID NOT implement!
	
	final static String QUERY_TEMPLATE_13 = 
			"SELECT * " +
			"FROM Customer " +
			"WHERE category BETWEEN ? AND ?;";
	
	final static String QUERY_TEMPLATE_14a = 
			"DROP TABLE IF EXISTS temp_cut_jobs;";
	
	final static String QUERY_TEMPLATE_14b = 
			"SELECT * " +
			"INTO temp_cut_jobs " +
			"FROM Cut_Job;";
	
	final static String QUERY_TEMPLATE_14c = 
			"DELETE FROM Cut_Job " +
			"WHERE job_no BETWEEN ? AND ?;";
	
	final static String QUERY_TEMPLATE_14d = 
			"DELETE " + 
			"FROM Transaction_Obj " +
			"WHERE job_no IN(" +
				"SELECT job_no " + 
				"FROM temp_cut_jobs" + 
				"WHERE job_no BETWEEN ? AND ?" + 
			");";
	
	final static String QUERY_TEMPLATE_14e = 
			"DELETE" + 
			"FROM Job " +
			"WHERE job_no IN(" +
				"SELECT job_no " + 
				"FROM temp_cut_jobs " + 
				"WHERE job_no BETWEEN ? AND ?" + 
			");";
	
	final static String QUERY_TEMPLATE_15 = "UPDATE Paint_Job " +
			"SET color = ? " +
			"WHERE job_no = ?;";
 
	
	
	// User input prompt//
	final static String PROMPT = 
			"(1) Enter a new customer \n" + 
			"(2) Enter a new department \n" + 
			"(3) Enter a new process-id and its department together with its type and information relevant to the type \n" + 
			"(4) Enter a new assembly with its customer-name, assembly-details, assembly-id, and date-ordered and associate it with one or more processes \n" + 
			"(5) Create a new account and associate it with the process, assembly, or department to which it is applicable \n" + 
			"(6) Enter a new job, given its job-no, assembly-id, process-id, and date the job commenced \n" + 
			"(7) At the completion of a job, enter the date it completed and the information relevant to the type of job \n" + 
			"(8) Enter a transaction-no and its sup-cost and update all the costs (details) of the affected accounts by adding sup-cost to their current values of details \n" + 
			"(9) Retrieve the total cost incurred on an assembly-id \n" + 
			"(10) Retrieve the total labor time within a department for jobs completed in the department during a given date \n" + 
			"(11) Retrieve the processes through which a given assembly-id has passed so far (in date-commenced order) and the department responsible for each process \n" + 
			"(12) Retrieve the jobs (together with their type information and assembly-id) completed during a given date in a given department \n" + 
			"(13) Retrieve the customers (in name order) whose category is in a given range \n" + 
			"(14) Delete all cut-jobs whose job-no is in a given range \n" + 
			"(15) Change the color of a given paint job \n" + 
			"(16) Import \n" + 
			"(17) Export \n" + 
			"(18) Quit";

	public static void main(String[] args) throws SQLException {
		System.out.println("Welcome to the Job-Shop Accounting Database System");
		final Scanner sc = new Scanner(System.in); // Scanner is used to collect the user input
		String option = ""; // Initialize user option selection as nothing
		
		while (!option.equals("18")) { // Ask user for options until options 16, 17, or 18 are selected
			System.out.println(PROMPT); // Print the available options
			option = sc.next(); // Read in the user option selection
 
			switch (option) { // Switch between different options
				case "1": // Insert a new Customer
					// Collect the new data from the user
					System.out.println("Please enter customer name:");
					sc.nextLine();
					final String name = sc.nextLine();
 
					System.out.println("Please enter customer address:");
					final String address = sc.nextLine();
 
					System.out.println("Please enter customer category:");
					final int category = sc.nextInt();
  
					System.out.println("Connecting to the database...");
					// Get a database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) {
						try (
							final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_1)) {
							// Populate the query template with the data collected from the user
							statement.setString(1, name);
							statement.setString(2, address);
							statement.setInt(3, category);
 
							System.out.println("Dispatching the query...");
							// Actually execute the populated query
							final int rows_inserted = statement.executeUpdate();
							System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
						}
					}
 
					break;
				case "2": // Insert a new Department
					// Collect the new data from the user
					System.out.println("Please enter department number:");
					final int department_no = sc.nextInt();
 
					System.out.println("Please enter department data(64 characters MAX):");
					sc.nextLine();
					final String department_data = sc.nextLine();
 
					
  
					System.out.println("Connecting to the database...");
					// Get a database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) {
						try (
							final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_2)) {
							// Populate the query template with the data collected from the user
							statement.setInt(1, department_no);
							statement.setString(2, department_data);
							
							System.out.println("Dispatching the query...");
							// Actually execute the populated query
							final int rows_inserted = statement.executeUpdate();
							System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
						}
					}
					
					break;
				case "3": // Insert a new Process given its department, type, and type information
					// Collect the new data from the user
					System.out.println("Please enter process ID:");
					final int process_id = sc.nextInt();
 
					System.out.println("Please enter process data(64 characters MAX):");
					sc.nextLine();
					final String process_data = sc.nextLine();
										
					System.out.println("Please enter department number:");
					final int proc_supervisor_dept_no = sc.nextInt();
										
					try (final Connection connection = DriverManager.getConnection(URL)) {
						try (
							final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_3)) {
							// Populate the query template with the data collected from the user
							statement.setInt(1, process_id);
							statement.setString(2, process_data);
							statement.setInt(3, proc_supervisor_dept_no);
							statement.executeUpdate();
						}
					}
					
					//Depending on the type of process, ask for different inputs and perform a different query
					System.out.println("Please enter process type(fit, paint, or cut):");
					sc.nextLine();
					final String process_type = sc.nextLine();
					
					if(process_type.equalsIgnoreCase("fit")) {
						System.out.println("Please enter fit-type(64 characters MAX):");
						final String fit_type = sc.nextLine();
						
						System.out.println("Connecting to the database...");
						// Get a database connection and prepare a query statement
						try (final Connection connection = DriverManager.getConnection(URL)) {
							try (
								final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_3a)) {
								// Populate the query template with the data collected from the user
								statement.setInt(1, process_id);
								statement.setString(2, fit_type);
								final int rows_inserted = statement.executeUpdate();
								System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
							}
						}
					}
					else if(process_type.equalsIgnoreCase("paint")) {
						System.out.println("Please enter paint-type(64 characters MAX):");
						final String paint_type = sc.nextLine();
						
						System.out.println("Please enter paint-method(64 characters MAX):");
						final String paint_method = sc.nextLine();
									  
						System.out.println("Connecting to the database...");
						// Get a database connection and prepare a query statement
						try (final Connection connection = DriverManager.getConnection(URL)) {
							try (
								final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_3b)) {
								// Populate the query template with the data collected from the user
								statement.setInt(1, process_id);
								statement.setString(2, paint_type);
								statement.setString(3, paint_method);
								final int rows_inserted = statement.executeUpdate();
								System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
							}
						}
					}
					else if(process_type.equalsIgnoreCase("cut")) {
						System.out.println("Please enter cutting-type(64 characters MAX):");
						final String cutting_type = sc.nextLine();
						
						System.out.println("Please enter machine-type(64 characters MAX):");
						final String machine_type = sc.nextLine();
						
						System.out.println("Connecting to the database...");
						// Get a database connection and prepare a query statement
						try (final Connection connection = DriverManager.getConnection(URL)) {
							try (
								final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_3c)) {
								// Populate the query template with the data collected from the user
								statement.setInt(1, process_id);
								statement.setString(2, cutting_type);
								statement.setString(3, machine_type);
								final int rows_inserted = statement.executeUpdate();
								System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
							}
						}
					}
					else {
						System.out.println("Error: Invalid type");
						break;
					}
 
					break;
				case "4": // Insert a new Assembly_Obj given various inputs
					// Collect the new data from the user
					System.out.println("Please enter assembly ID:");
					final int assembly_id = sc.nextInt();
 
					System.out.println("Please enter assembly details(64 characters MAX):");
					sc.nextLine();
					final String assembly_details = sc.nextLine();
					
					System.out.println("Please enter customer name:");
					final String customer_name_on_order = sc.nextLine();
					
					System.out.println("Please enter the date this assembly was ordered(MM/DD/YYYY):");
					final String date_ordered = sc.nextLine();
					
					System.out.println("Please enter the process associated with this assembly:");
					final int parent_process_id = sc.nextInt();
										
					System.out.println("Connecting to the database...");
					// Get the database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) {
						try (
							final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_4a)) {
							// Populate the query template with the data collected from the user
							statement.setInt(1, assembly_id);
							statement.setString(2, date_ordered);
							statement.setString(3, assembly_details);
							statement.setString(4, customer_name_on_order);
							
							System.out.println("Dispatching the first query...");
							final int rows_inserted = statement.executeUpdate();
							System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
						}
						try (
								final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_4b)) {
								// Populate the query template with the data collected from the user
								statement.setInt(1, assembly_id);
								statement.setInt(2, parent_process_id);
								
								System.out.println("Dispatching the second query...");
								final int rows_updated = statement.executeUpdate();
								System.out.println(String.format("Done. %d row(s) updated.\n", rows_updated));
						}
					}
				
					break;
				case "5": // Insert a new Account given various inputs
					// Collect the new data from the user
					System.out.println("Please enter the account number:");
					final int account_no = sc.nextInt();
					 														
					System.out.println("Connecting to the database...");
					// Get the database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) {
						try (
							final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_5)) {
							// Populate the query template with the data collected from the user
							statement.setInt(1, account_no);
														
							System.out.println("Dispatching the query...");
							final int rows_inserted = statement.executeUpdate();
							System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
						}					
					}	
					
					System.out.println("What type of account is this?");
					sc.nextLine();
					final String account_type = sc.nextLine();
					if(account_type.equalsIgnoreCase("assembly")) {
						System.out.println("Please enter the associated assembly ID:");
						final int assembly_id_for_account = sc.nextInt();
						
						System.out.println("Please enter the initial balance:");
						final int details_1 = sc.nextInt();
														  
						System.out.println("Connecting to the database...");
						// Get a database connection and prepare a query statement
						try (final Connection connection = DriverManager.getConnection(URL)) {
							try (
								final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_5ai)) {
								// Populate the query template with the data collected from the user
								statement.setInt(1, account_no);
								statement.setInt(2, details_1);
								final int rows_inserted = statement.executeUpdate();
								System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
							}
							
							try (
									final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_5aii)) {
									// Populate the query template with the data collected from the user
									statement.setInt(1, account_no);
									statement.setInt(2, assembly_id_for_account);
									final int rows_inserted = statement.executeUpdate();
									System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
								}
						}
					}
					else if(account_type.equalsIgnoreCase("department")){
						System.out.println("Please enter the associated department number:");
						final int department_no_for_account = sc.nextInt();
						
						System.out.println("Please enter the initial balance:");
						final int details_2 = sc.nextInt();
														  
						System.out.println("Connecting to the database...");
						// Get a database connection and prepare a query statement
						try (final Connection connection = DriverManager.getConnection(URL)) {
							try (
								final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_5bi)) {
								// Populate the query template with the data collected from the user
								statement.setInt(1, account_no);
								statement.setInt(2, details_2);
								final int rows_inserted = statement.executeUpdate();
								System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
							}
							
							try (
									final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_5bii)) {
									// Populate the query template with the data collected from the user
									statement.setInt(1, account_no);
									statement.setInt(2, department_no_for_account);
									final int rows_inserted = statement.executeUpdate();
									System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
								}
						}
					}
					else if(account_type.equalsIgnoreCase("process")) {
						System.out.println("Please enter the associated process ID:");
						final int process_id_for_account = sc.nextInt();
						
						System.out.println("Please enter the initial balance:");
						final int details_3 = sc.nextInt();
														  
						System.out.println("Connecting to the database...");
						// Get a database connection and prepare a query statement
						try (final Connection connection = DriverManager.getConnection(URL)) {
							try (
								final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_5ci)) {
								// Populate the query template with the data collected from the user
								statement.setInt(1, account_no);
								statement.setInt(2, details_3);
								final int rows_inserted = statement.executeUpdate();
								System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
							}
							
							try (
									final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_5cii)) {
									// Populate the query template with the data collected from the user
									statement.setInt(1, account_no);
									statement.setInt(2, process_id_for_account);
									final int rows_inserted = statement.executeUpdate();
									System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
							}
						}
					}
					else {
						System.out.println("Error: Invalid type");
						break;
					}
					
					
					
					break;
				case "6": // Insert a new Job given various inputs
					// Collect the new data from the user
					System.out.println("Please enter the job number:");
					final int job_no = sc.nextInt();
					
					System.out.println("Please enter the date commenced(MM/DD/YYYY):");
					sc.nextLine();
					final String date_commenced = sc.nextLine();
					
					System.out.println("Please enter the ID of the parent process:");
					final int proc_id_for_parent_of_job = sc.nextInt();
					
					System.out.println("Please enter the ID of the assembly that this job is working on:");
					final int assembly_id_for_job = sc.nextInt();
 														
					System.out.println("Connecting to the database...");
					// Get the database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) {
						try (
							final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_6a)) {
							// Populate the query template with the data collected from the user
							statement.setInt(1, job_no);
							statement.setString(2, date_commenced);
							statement.setInt(3, proc_id_for_parent_of_job);						
							
							System.out.println("Dispatching the query...");
							final int rows_inserted = statement.executeUpdate();
							System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
						}
						
						try (
							final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_6b)) {
							// Populate the query template with the data collected from the user
							statement.setInt(1, assembly_id_for_job);
							statement.setInt(2, proc_id_for_parent_of_job);						
								
							System.out.println("Dispatching the query...");
							final int rows_inserted = statement.executeUpdate();
							System.out.println(String.format("Done. %d row(s) updated.\n", rows_inserted));
						}		
					}	
					
					break;
				case "7":
					// Collect the new data from the user
					System.out.println("Which job was completed?:");
					final int completed_job_no = sc.nextInt();
					 											
					System.out.println("Please enter the date the job was completed(MM/DD/YYYY):");
					sc.nextLine();
					final String completion_date = sc.nextLine();
					
					System.out.println("Connecting to the database...");
					// Get the database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) {
						try (
							final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_7)) {
							// Populate the query template with the data collected from the user
							statement.setString(1, completion_date);
							statement.setInt(2, completed_job_no);

														
							System.out.println("Dispatching the query...");
							final int rows_inserted = statement.executeUpdate();
							System.out.println(String.format("Done. %d row(s) updated.\n", rows_inserted));
						}					
					}	
					
					System.out.println("What type of job wat it?");
					final String job_type = sc.nextLine();
					if(job_type.equalsIgnoreCase("fit")) {
						System.out.println("Please enter the labor time(in minutes):");
						final int labor_time = sc.nextInt();
																				  
						System.out.println("Connecting to the database...");
						// Get a database connection and prepare a query statement
						try (final Connection connection = DriverManager.getConnection(URL)) {
							try (
								final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_7a)) {
								// Populate the query template with the data collected from the user
								statement.setInt(1, completed_job_no);
								statement.setInt(2, labor_time);
								final int rows_inserted = statement.executeUpdate();
								System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
							}
						}
					}
					else if(job_type.equalsIgnoreCase("paint")){
						System.out.println("Please enter the color of paint used in the job:");
						final String color_used_on_completed = sc.nextLine();
						
						System.out.println("Please enter the volume of paint used(in gallons):");
						final int volume = sc.nextInt();
						
						System.out.println("Please enter the labor time(in minutes):");
						final int labor_time = sc.nextInt();
														  
						System.out.println("Connecting to the database...");
						// Get a database connection and prepare a query statement
						try (final Connection connection = DriverManager.getConnection(URL)) {
							try (
								final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_7b)) {
								// Populate the query template with the data collected from the user
								statement.setInt(1, completed_job_no);
								statement.setString(2, color_used_on_completed);
								statement.setInt(3, volume);
								statement.setInt(4, labor_time);
								final int rows_inserted = statement.executeUpdate();
								System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
							}
						}
					}
					else if(job_type.equalsIgnoreCase("cut")) {
						System.out.println("Please enter the machine type:");
						final String machine_type = sc.nextLine();
						
						System.out.println("Please enter the time machine was used(in minutes):");
						final int time_used = sc.nextInt();
						
						System.out.println("Please enter the cutting type:");
						final String cutting_type = sc.nextLine();
						
						System.out.println("Please enter the labor time(in minutes):");
						final int labor_time = sc.nextInt();
														  
						System.out.println("Connecting to the database...");
						// Get a database connection and prepare a query statement
						try (final Connection connection = DriverManager.getConnection(URL)) {
							try (
								final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_7c)) {
								// Populate the query template with the data collected from the user
								statement.setInt(1, completed_job_no);
								statement.setString(2, machine_type);
								statement.setInt(3, time_used);
								statement.setString(4, cutting_type);
								statement.setInt(5, labor_time);
								final int rows_inserted = statement.executeUpdate();
								System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
							}
						}
					}
					else {
						System.out.println("Error: Invalid type");
						break;
					}
					
					break;
				case "8":
					System.out.println("Did not implement query 8. Type 18 to exit. Thank you!");
					
					break;
				case "9":
					System.out.println("Please enter the assembly ID to find the cost of:");
					final int assembly_id_to_sum = sc.nextInt();
					
					System.out.println("Connecting to the database...");
					// Get the database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) {
						try (
							final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_9)) {
							// Populate the query template with the data collected from the user
							statement.setInt(1, assembly_id_to_sum);
														
							System.out.println("Dispatching the query...");
							final ResultSet resultSet = statement.executeQuery();
							System.out.println(String.format("Sum of cost on assembly ID: %d", assembly_id_to_sum)); 
							System.out.println(String.format("%d", resultSet.getInt(1)));
						}
					}
					
					break;
				case "10":
					System.out.println("Please enter the date of completion for the jobs:");
					sc.nextLine();
					final String date_to_check = sc.nextLine();
					
					System.out.println("Please enter the department to check:");
					final int dept_no_to_check = sc.nextInt();
					
					System.out.println("Connecting to the database...");
					// Get the database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) {
						try (
							final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_10)) {
							// Populate the query template with the data collected from the user
							statement.setString(1, date_to_check);
							statement.setInt(2, dept_no_to_check);
							
							System.out.println("Dispatching the query...");
							final ResultSet resultSet = statement.executeQuery();
							System.out.println(String.format("Total labor time for jobs within department %d that completed on %s", dept_no_to_check, date_to_check)); 
							
							System.out.println(String.format("%d", resultSet.getInt(1)));
						}
					}
					
					break;
				case "11":
					System.out.println("Please enter an assembly ID to find which processes and departments worked on it:");
					final int assembly_id_path = sc.nextInt();
					
					System.out.println("Connecting to the database...");
					// Get the database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) {
						try (
							final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_11)) {
							// Populate the query template with the data collected from the user
							statement.setInt(1, assembly_id_path);
												
							System.out.println("Dispatching the query...");
							final ResultSet resultSet = statement.executeQuery();
							System.out.println(String.format("process_id | corresponding department_no")); 
							
							while (resultSet.next()) {
								System.out.println(String.format("%d | %d",
										resultSet.getInt(1),
										resultSet.getInt(2)));
							}						
						}
					}
					
					
					break;
				case "12":
					System.out.println("Did not implement query 12. Type 18 to exit. Thank you!");
					
					break;
				case "13":
					System.out.println("Please enter the lower bound on category:");
					final int lower = sc.nextInt();
					
					System.out.println("Please enter the upper bound on category:");
					final int upper = sc.nextInt();
					
					System.out.println("Connecting to the database...");
					// Get the database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) {
						try (
							final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_13)) {
							// Populate the query template with the data collected from the user
							statement.setInt(1, lower);
							statement.setInt(2, upper);
							
							System.out.println("Dispatching the query...");
							final ResultSet resultSet = statement.executeQuery();
							System.out.println(String.format("Customers with category >= %d and <= %d", lower, upper)); 
							
							while (resultSet.next()) {
								System.out.println(String.format("%s | %s | %d",
										resultSet.getString(1),
										resultSet.getString(2),
										resultSet.getInt(3)));
							}
						}
					}
				
					break;
				case "14":
					System.out.println("Please enter the lower bound on the job number:");
					final int lower_job_no = sc.nextInt();
					
					System.out.println("Please enter the upper bound on the job number:");
					final int upper_job_no = sc.nextInt();
					
					System.out.println("Connecting to the database...");
					// Get the database connection, create statement and execute it right away, as no user input need be collected
					try (final Connection connection = DriverManager.getConnection(URL)) {
						try (final Statement statement = connection.createStatement()){
							statement.executeUpdate(QUERY_TEMPLATE_14a);
						}
						try (final Statement statement = connection.createStatement()){
							statement.executeUpdate(QUERY_TEMPLATE_14b);
						}
						try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_14c)) {
							// Populate the query template with the data collected from the user
							statement.setInt(1, lower_job_no);
							statement.setInt(2, upper_job_no);
							statement.executeUpdate();
						}
						try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_14d)) {
							// Populate the query template with the data collected from the user
							statement.setInt(1, lower_job_no);
							statement.setInt(2, upper_job_no);
							statement.executeUpdate();
						}
						try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_14e)) {
							// Populate the query template with the data collected from the user
							statement.setInt(1, lower_job_no);
							statement.setInt(2, upper_job_no);
							statement.executeUpdate();
						}
					}
					
					break;
				case "15":
					System.out.println("Please enter the job number of the paint job you wish to edit:");
					final int paint_job_no_to_edit = sc.nextInt();
					
					System.out.println("Please enter the color you wish to change it to:");
					sc.nextLine();
					final String new_color = sc.nextLine();
					
					System.out.println("Connecting to the database...");
					// Get the database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) {
						try (
							final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_15)) {
							// Populate the query template with the data collected from the user
							statement.setString(1, new_color);
							statement.setInt(2, paint_job_no_to_edit);
							
							System.out.println("Dispatching the query...");
							final int rows_inserted = statement.executeUpdate();
							System.out.println(String.format("Done. %d row(s) updated.\n", rows_inserted));
						}
					}
					
					break;
				case "16": 
					System.out.println("Did not implement the import function. Type 18 to exit. Thank you!");
					
					break;
				case "17": 
					System.out.println("Did not implement the export function. Type 18 to exit. Thank you!");
 
					break;
				case "18": // Do nothing, the while loop will terminate upon the next iteration
					System.out.println("Exiting! Good-bye!");
					
					break;
				default: // Unrecognized option, re-prompt the user for the correct one
					System.out.println(String.format(
							"Unrecognized option: %s\n" + 
							"Please try again!", 
							option));
					break;
			}
		}
 
		sc.close(); // Close the scanner before exiting the application
	}
} 