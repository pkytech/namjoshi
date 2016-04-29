package com.kytech.namjoshi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import com.kytech.namjoshi.bo.Patient;
import com.kytech.namjoshi.bo.Prescription;

public final class DBUtil {
	private static final String DBCP2_POOLING_DRIVER_CLASS = "org.apache.commons.dbcp2.PoolingDriver";
	private static final String JDBC_APACHE_COMMONS_DBCP = "jdbc:apache:commons:dbcp:";
	public static final String OLTP_POOL_NAME = "oltp";
	public static final String ARCVIVE_POOL_NAME = "archive";
	public static final String LOAD_PATIENT = "select * from Patient_Master where Pid=?";
	public static final String LOAD_PRESCRIPTION = "select * from Prescription_Transaction where P_id=? order by Ex_date desc";
	private DBUtil(){
	}

	static {
		//Initialize pool
		initializePool();
	}
	
	/**
	 * @param poolName
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(String poolName) throws SQLException {
		return DriverManager.getConnection(JDBC_APACHE_COMMONS_DBCP + poolName);
	}

	private static final void initializePool() {
		NamjoshiConfigurator config = NamjoshiConfigurator.getInstance();
		String driverName = config.getKeyValue(NamjoshiConfigurator.JDBC_CLASSNAME);
		String jdbcUrl = config.getKeyValue(NamjoshiConfigurator.JDBC_DB_URL);
		String userName = config.getKeyValue(NamjoshiConfigurator.DB_USER_NAME);
		String password = config.getKeyValue(NamjoshiConfigurator.DB_PASSWORD);
		
		try {
			Class.forName(driverName);
			
			Properties prop = new Properties();
			prop.put("user", userName);
			prop.put("password", password);
			prop.put("initialSize", "4");
			
			ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(jdbcUrl, prop);
			PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
			
			// Now we'll need a ObjectPool that serves as the
			// actual pool of connections.
			ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory);

			// Set the factory's pool property to the owning pool
			poolableConnectionFactory.setPool(connectionPool);
			
			// Finally, we create the PoolingDriver itself...
			Class.forName(DBCP2_POOLING_DRIVER_CLASS);
			PoolingDriver driver = (PoolingDriver) DriverManager.getDriver(JDBC_APACHE_COMMONS_DBCP);

			// ...and register our pool with it.
			driver.registerPool(OLTP_POOL_NAME,connectionPool);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Patient loadPatient(ResultSet rs) throws SQLException {
		Patient patient = new Patient();
		patient.setPid(rs.getLong("Pid"));
		patient.setFirstName(rs.getString("PFName"));
		patient.setMiddleName(rs.getString("PMName"));
		patient.setLastName(rs.getString("PLName"));
		patient.setAddress(rs.getString("Address"));
		patient.setCity(rs.getString("City"));
		patient.setPin(rs.getString("Pin"));
		patient.setTelephoneNumber(rs.getString("TPhone"));
		patient.setMobileNumber(rs.getString("MPhone"));
		patient.setDrReference(rs.getString("DrReference"));
		patient.setEmail(rs.getString("email_id"));
		patient.setBirthday(rs.getString("BirthDate"));
		patient.setActive(rs.getString("active"));
		patient.setAge(rs.getInt("Age"));
		patient.setPreviousBalance(rs.getDouble("Pre_Bal"));
		try {
			patient.setBirthDate(rs.getDate("birth_date"));
		} catch(SQLException e) {
			//Do nothing
		}
		return patient;
	}
	
	public static void shutdownDriver() throws Exception {
		PoolingDriver driver = (PoolingDriver) DriverManager.getDriver(JDBC_APACHE_COMMONS_DBCP);
		driver.closePool(OLTP_POOL_NAME);
	}
	
	public static void printDriverStats() throws Exception {
		PoolingDriver driver = (PoolingDriver) DriverManager
				.getDriver(JDBC_APACHE_COMMONS_DBCP);
		ObjectPool<? extends Connection> connectionPool = driver
				.getConnectionPool(OLTP_POOL_NAME);

		System.out.println("NumActive: " + connectionPool.getNumActive());
		System.out.println("NumIdle: " + connectionPool.getNumIdle());
	}
	
	public static Patient loadPatient(long patientId) {
		Patient pat = null;
		try (Connection con = getConnection(OLTP_POOL_NAME)) {
			try (PreparedStatement pstmt = con.prepareStatement(LOAD_PATIENT)) {
				pstmt.setLong(1, patientId);
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						pat = loadPatient(rs);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pat;
	}

	public static void main(String args[]) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		try {
		    System.out.println("Creating connection.");
		    conn = DriverManager.getConnection(JDBC_APACHE_COMMONS_DBCP + OLTP_POOL_NAME);
		    System.out.println("Creating statement.");
		    stmt = conn.createStatement();
		    System.out.println("Executing statement.");
		    rset = stmt.executeQuery("select * from Patient_Master");
		    System.out.println("Results:");
		    int numcols = rset.getMetaData().getColumnCount();
		    while(rset.next()) {
		        for(int i=1;i<=numcols;i++) {
		            //System.out.print("\t" + rset.getString(i));
		        }
		        //System.out.println("");
		    }
		} catch(SQLException e) {
		    e.printStackTrace();
		} finally {
		    try { if (rset != null) rset.close(); } catch(Exception e) { }
		    try { if (stmt != null) stmt.close(); } catch(Exception e) { }
		    try { if (conn != null) conn.close(); } catch(Exception e) { }
		}
		
		try {
		    printDriverStats();
		} catch (Exception e) {
		    e.printStackTrace();
		}

		// closes the pool
		try {
		    shutdownDriver();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

	public static List<Prescription> loadPrescription(long patientNo)  {
		List<Prescription> prescriptions = new ArrayList<Prescription>();
		try (Connection con = getConnection(OLTP_POOL_NAME)) {
			try (PreparedStatement pstmt = con.prepareStatement(LOAD_PRESCRIPTION)) {
				pstmt.setLong(1, patientNo);
				try (ResultSet rs = pstmt.executeQuery()) {
					while (rs.next()) {
						prescriptions.add(loadPrescription(rs));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return prescriptions;
	}

	private static Prescription loadPrescription(ResultSet rs) throws SQLException {
		Prescription pre = new Prescription();
		
		pre.setPrescriptionId(rs.getLong("Pre_id"));
		pre.setPid(rs.getLong("P_id"));
		pre.setSymtoms(rs.getString("sym_nm"));
		pre.setPrescription(rs.getString("Med_nm"));
		pre.setAdvice(rs.getString("Adv_nm"));
		pre.setExaminationDate(rs.getDate("Ex_date"));
		pre.setFeeCode(rs.getString("Fee_Code"));
		pre.setPaidAmount(rs.getDouble("PaidAmt"));
		pre.setOutstandingAmount(rs.getDouble("OS_Bal"));
		pre.setNetPayableAmount(rs.getDouble("Net_Payable"));
		pre.setReceivedAmount(rs.getDouble("Recevied_Amt"));
		
		return pre;
	}
	private static final String SEARCH_PATIENT = "select * from Patient_Master where ";
	public static List<Patient> searchPatients(String firstName,
			String middleName, String lastName) {
		List<Patient> patients = new ArrayList<Patient>();
		String query = SEARCH_PATIENT;
		boolean whereClauseAdded = false;
		if (firstName != null && firstName.trim().length()>0) {
			query+= "PFName like '%"+firstName.trim().toUpperCase()+"%' ";
			whereClauseAdded = true;
		}
		if (middleName != null && middleName.trim().length() > 0) {
			if (whereClauseAdded) {
				query+= " and ";
			}
			query+= "PMName like '%"+middleName+"%'";
			whereClauseAdded = true;
		}
		if (lastName != null && lastName.trim().length() > 0) {
			if (whereClauseAdded) {
				query+= " and ";
			}
			query+= "PLName like '%"+lastName+"%'";
		}
		try (Connection con = getConnection(OLTP_POOL_NAME)) {
			try (Statement stmt = con.createStatement()) {
				try (ResultSet rs = stmt.executeQuery(query)) {
					while (rs.next()) {
						patients.add(loadPatient(rs));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return patients;
	}
}
