import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OperatorInformation implements AutoCloseable{
	private static final String DB_NAME = "sz0619_OperatorInformation";
    private static final String DB_USER = "token_6409";
    private static final String DB_PASSWORD = "IZZLA6UyHXHUBaJT";
    
    private static final String QUERY1="select Operator.RealName,Operator.Affliliation,Operator.CodeName,Operator.Birthdate,Operator.Birthplace,Operator.Biography,Operator.PhychologicalReport,PrimaryWeapen.WeaponName,SecondaryWeapon.WeaponName,Equipment.WeaponName,UniquneGadget.WeaponName from Operator\n"
    		                            +"inner join PrimaryWeapen on PrimaryWeapen.id=Operator.PrimaryWeaponId\n"
    		                            +"inner join SecondaryWeapon on SecondaryWeapon.id=Operator.SecondaryWeaponId\n"
    		                            +"inner join Equipment on Equipment.id=Operator.EquipmentId\n"
    		                            +"inner join UniquneGadget on UniquneGadget.id=Operator.UniquneGadgetId\n"
    		                            +"where Operator.CodeName like ?\n"
    		                            +"order by Operator.id";
    
    private static final String QUERY2="select * from PrimaryWeapen where PrimaryWeapen.WeaponName like ? order by PrimaryWeapen.id";
    
    private static final String QUERY3="select * from SecondaryWeapon where SecondaryWeapon.WeaponName like ? order by SecondaryWeapon.id";
    
    private static final String QUERY4="select * from Equipment where Equipment.WeaponName like ? order by Equipment.id";
    
    private static final String QUERY5="select * from UniquneGadget where UniquneGadget.WeaponName like ? order by UniquneGadget.id";
    
    private final String dbHost;
    private final int dbPort;
    private final String dbName;
    private final String dbUser, dbPassword;

    private Connection connection;
    private PreparedStatement query1;
    private PreparedStatement query2;
    private PreparedStatement query3;
    private PreparedStatement query4;
    private PreparedStatement query5;
    
    public OperatorInformation(String dbHost, int dbPort, String dbName,String dbUser, String dbPassword) throws SQLException {
        this.dbHost = dbHost;
        this.dbPort = dbPort;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;

        connect();
    }
    
    private void connect() throws SQLException {
        // URL for connecting to the database: includes host, port, database name,
        // user, password
        final String url = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s",
                dbHost, dbPort, dbName,
                dbUser, dbPassword
        );

        // Attempt to connect, returning a Connection object if successful
        this.connection = DriverManager.getConnection(url);

        // Prepare the statement (query) that we will execute
        this.query1 = this.connection.prepareStatement(QUERY1);
        this.query2 = this.connection.prepareStatement(QUERY2);
        this.query3 = this.connection.prepareStatement(QUERY3);
        this.query4 = this.connection.prepareStatement(QUERY4);
        this.query5 = this.connection.prepareStatement(QUERY5);
    }
    
    public void runApp() throws SQLException {
    	String asset;
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("Welcome, type 1 to see operator's information, type 2 to see all the primary weapon information, type 3 to see all secondary weapon information, type 4 to see all equipment information, type 5 to see all uniqune gadget information or hit Enter to quit: ");
            String line = in.nextLine();
            if (line.isBlank())
                break;
            
            if(line.equals("1")) {
            	System.out.println("Okay, type the keyword of operator's code name at here:");
            	asset =in.nextLine();
            	Operator1(asset);
            }else if(line.equals("2")) {
            	System.out.println("Okay, type the keyword of primary weapon at here:");
            	asset =in.nextLine();
            	Operator2(asset);
            }else if(line.equals("3")) {
            	System.out.println("Okay, type the keyword of secondary weapon at here:");
            	asset =in.nextLine();
            	Operator3(asset);
            }else if(line.equals("4")) {
            	System.out.println("Okay, type the keyword equipment at here:");
            	asset =in.nextLine();
            	Operator4(asset);
            }else if(line.equals("5")) {
            	System.out.println("Okay, type the keyword of uniqune gadget at here:");
            	asset =in.nextLine();
            	Operator5(asset);
            }else {
            	System.out.println("Oh I don't know what you are talking about...");
            }
        }
    }
    
    public void Operator1(String name) throws SQLException{
    	query1.setString(1, "%"+name+"%");
    	ResultSet results = query1.executeQuery();
    	
    	int resultCount = 0;
    	
    	while (results.next()) {
            ++resultCount;
            String RealName = results.getString("Operator.RealName");
            String Affliliation = results.getString("Operator.Affliliation");
            String CodeName = results.getString("Operator.CodeName");
            String Birthdate = results.getString("Operator.Birthdate");
            String Birthplace = results.getString("Operator.Birthplace");
            String Biography = results.getString("Operator.Biography");
            String PhychologicalReport = results.getString("Operator.PhychologicalReport");
            String PrimaryWeapon = results.getString("PrimaryWeapen.WeaponName");
            String SecondaryWeapon = results.getString("SecondaryWeapon.WeaponName");
            String Equipment = results.getString("Equipment.WeaponName");
            String UniquneGadget = results.getString("UniquneGadget.WeaponName");
            System.out.printf("Real Name:%s, Affiliation:%s, Code Name:%s, Birthdate:%s, Birthplace:%s, Biography:%s,Phychological Report:%s,Primary Weapon:%s, Secondary Weapon:%s, Equipment:%s, Uniqune Gadget:%s\n", RealName,Affliliation,CodeName,Birthdate,Birthplace,Biography,PhychologicalReport,PrimaryWeapon,SecondaryWeapon,Equipment,UniquneGadget);
        }
    	System.out.printf("\n%d people in total.\n\n", resultCount);
    }
    
    public void Operator2(String name) throws SQLException{
    	query2.setString(1, "%"+name+"%");
    	ResultSet results = query2.executeQuery();
    	
    	int resultCount = 0;
    	
    	while(results.next()) {
    		++resultCount;
    		String WeaponName = results.getString("PrimaryWeapen.WeaponName");
    		String WeaponDescription = results.getString("PrimaryWeapen.WeaponDescription");
    		String Damage = results.getString("PrimaryWeapen.Damage");
    		String RateOfFire = results.getString("PrimaryWeapen.RateOfFire");
    		String Mobility = results.getString("PrimaryWeapen.Mobility");
    		String MagazineSize = results.getString("PrimaryWeapen.MagazineSize");
    		String TotalAmmunition = results.getString("PrimaryWeapen.TotalAmmunition");
    		System.out.printf("Name:%s, Description:%s, Damage:%s, Rate of Fire:%s, Mobility:%s, Magazine Size:%s, Total Ammunitation:%s\n",WeaponName,WeaponDescription,Damage,RateOfFire,Mobility,MagazineSize,TotalAmmunition);
    	}
    	System.out.printf("\n%d item in total.\n\n", resultCount);
    }
    
    public void Operator3(String name) throws SQLException{
    	query3.setString(1, "%"+name+"%");
    	ResultSet results = query3.executeQuery();
    	
    	int resultCount = 0;
    	
    	while(results.next()) {
    		++resultCount;
    		String WeaponName = results.getString("SecondaryWeapon.WeaponName");
    		String WeaponDescription = results.getString("SecondaryWeapon.WeaponDescription");
    		String Damage = results.getString("SecondaryWeapon.Damage");
    		String RateOfFire = results.getString("SecondaryWeapon.RateOfFire");
    		String Mobility = results.getString("SecondaryWeapon.Mobility");
    		String MagazineSize = results.getString("SecondaryWeapon.MagazineSize");
    		String TotalAmmunition = results.getString("SecondaryWeapon.TotalAmmunition");
    		System.out.printf("Name:%s, Description:%s, Damage:%s, Rate of Fire:%s, Mobility:%s, Magazine Size:%s, Total Ammunitation:%s\n",WeaponName,WeaponDescription,Damage,RateOfFire,Mobility,MagazineSize,TotalAmmunition);
    	}
    	System.out.printf("\n%d item in total.\n\n", resultCount);
    }
    
    public void Operator4(String name) throws SQLException{
    	query4.setString(1, "%"+name+"%");
    	ResultSet results = query4.executeQuery();
    	
    	int resultCount = 0;
    	
    	while(results.next()) {
    		++resultCount;
    		String WeaponName = results.getString("Equipment.WeaponName");
    		String WeaponDescription = results.getString("Equipment.WeaponDescription");
    		System.out.printf("Name:%s, Description:%s",WeaponName,WeaponDescription);
    	}
    	System.out.printf("\n%d item in total.\n\n", resultCount);
    }
    
    public void Operator5(String name) throws SQLException{
    	query5.setString(1, "%"+name+"%");
    	ResultSet results = query5.executeQuery();
    	
    	int resultCount = 0;
    	
    	while(results.next()) {
    		++resultCount;
    		String WeaponName = results.getString("UniquneGadget.WeaponName");
    		String WeaponDescription = results.getString("UniquneGadget.WeaponDescription");
    		System.out.printf("Name:%s, Description:%s",WeaponName,WeaponDescription);
    	}
    	System.out.printf("\n%d item in total.\n\n", resultCount);
    }
	@Override
	public void close() throws SQLException {
		// TODO Auto-generated method stub
		connection.close();
	}
	
	public static void main(String... args) {
        // Default connection parameters (can be overridden on command line)
        Map<String, String> params = new HashMap<>(Map.of(
            "dbname", "" + DB_NAME,
            "user", DB_USER,
            "password", DB_PASSWORD
        ));

        boolean printHelp = false;

        // Parse command-line arguments, overriding values in params
        for (int i = 0; i < args.length && !printHelp; ++i) {
            String arg = args[i];
            boolean isLast = (i + 1 == args.length);

            switch (arg) {
            case "-h":
            case "-help":
                printHelp = true;
                break;

            case "-dbname":
            case "-user":
            case "-password":
                if (isLast)
                    printHelp = true;
                else
                    params.put(arg.substring(1), args[++i]);
                break;

            default:
                System.err.println("Unrecognized option: " + arg);
                printHelp = true;
            }
        }

        // If help was requested, print it and exit
        if (printHelp) {
            printHelp();
            return;
        }

        // Connect to the database. This use of "try" ensures that the database connection
        // is closed, even if an exception occurs while running the app.
        try (DatabaseTunnel tunnel = new DatabaseTunnel();
             OperatorInformation app = new OperatorInformation(
                "localhost", tunnel.getForwardedPort(), params.get("dbname"),
                params.get("user"), params.get("password")
            )) {
            // Run the interactive mode of the application.
            app.runApp();
        } catch (IOException ex) {
            System.err.println("Error setting up ssh tunnel.");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.err.println("Error communicating with the database (see full message below).");
            ex.printStackTrace();
            System.err.println("\nParameters used to connect to the database:");
            System.err.printf("\tSSH keyfile: %s\n\tDatabase name: %s\n\tUser: %s\n\tPassword: %s\n\n",
                    params.get("sshkeyfile"), params.get("dbname"),
                    params.get("user"), params.get("password")
            );
            System.err.println("(Is the MySQL connector .jar in the CLASSPATH?)");
            System.err.println("(Are the username and password correct?)");
        }
        
    }
    
    private static void printHelp() {
        System.out.println("Accepted command-line arguments:");
        System.out.println();
        System.out.println("\t-help, -h          display this help text");
        System.out.println("\t-dbname <text>     override name of database to connect to");
        System.out.printf( "\t                   (default: %s)\n", DB_NAME);
        System.out.println("\t-user <text>       override database user");
        System.out.printf( "\t                   (default: %s)\n", DB_USER);
        System.out.println("\t-password <text>   override database password");
        System.out.println();
    }

}
