package com.kytech.namjoshi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import com.kytech.namjoshi.bo.DailyCollection;
import com.kytech.namjoshi.bo.Patient;
import com.kytech.namjoshi.bo.Prescription;

public final class DBUtil {
	private static final String SELECT_MAX_PATIENT_ID = "select max(pid) as Pid from Patient_Master";
	private static final String DBCP2_POOLING_DRIVER_CLASS = "org.apache.commons.dbcp2.PoolingDriver";
	private static final String JDBC_APACHE_COMMONS_DBCP = "jdbc:apache:commons:dbcp:";
	public static final String OLTP_POOL_NAME = "oltp";
	public static final String ARCVIVE_POOL_NAME = "archive";
	public static final String LOAD_PATIENT = "select * from Patient_Master where Pid=?";
	public static final String LOAD_PRESCRIPTION = "select * from Prescription_Transaction where P_id=? order by Ex_date desc";
	private static final String SEARCH_PATIENT = "select * from Patient_Master where ";
	private static final String INSERT_PATIENT_MASTER = "insert into Patient_Master (Pid, PFName, PMName, PLName, Address, TPhone, MPhone, DrReference, birth_date) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_PATIENT = "update Patient_Master set PFName=?, PMName=?, PLName=?, Address=?, TPhone=?, MPhone=?, DrReference=?, birth_date=? where Pid=?";
	private static final String SELECT_MAX_PRESCRIPTION_ID = "select max(Pre_id) from Prescription_Transaction";
	private static final String INSERT_PRESCRIPTION = "insert into Prescription_Transaction(Pre_id, P_id, sym_nm, Med_nm, Adv_nm, Ex_date, Fee_Code) values (?, ?, ?, ?, ?, ?, ?)";
	private static final String SELECT_COLLECTION = "select m.PFName, m.PLName,t.Fee_Code,t.Pre_id, t.P_id, t.Ex_date, m.Pre_Bal, t.Net_Payable from Prescription_Transaction t inner join Patient_Master m on m.Pid=t.P_id where Ex_date>=? and Ex_date<? order by t.Ex_date";
	private static final String UPDATE_PATIENT_OUTSTANDING = "update Patient_Master set Pre_Bal=? where Pid=?";
	public static final int MORNING = 1;
	public static final int EVENING = 2;
	public static final int BOTH = 3;
	private static final String SELECT_PATIENT_OUTSTANDING = "select Pid, PFName, PLName, Pre_Bal from Patient_Master where Pre_Bal>0 order by Pid";
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

	public static long createPatient(String firstName, String middleName,
			String lastName, String address, String telephone, String mobile,
			String reference, Date dob) {

		long maxPatientId = -1;
		try (Connection con = getConnection(OLTP_POOL_NAME)) {
			maxPatientId = getMaxPatientId(con);
			
			try (PreparedStatement pstmt = con.prepareStatement(INSERT_PATIENT_MASTER)) {
				pstmt.setLong(1, ++maxPatientId);
				pstmt.setString(2, firstName);
				pstmt.setString(3, middleName);
				pstmt.setString(4, lastName);
				pstmt.setString(5, address);
				pstmt.setString(6, telephone);
				pstmt.setString(7, mobile);
				pstmt.setString(8, reference);
				pstmt.setDate(9, dob != null ? new java.sql.Date(dob.getTime()) : null);
				pstmt.execute();
			}
		} catch(SQLException e) {
			e.printStackTrace(System.err);
		}
		return maxPatientId;
	}

	/**
	 * Returns maximum Pid from Patient_Master.
	 * 
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private static long getMaxPatientId(Connection con) throws SQLException {
		long patientId = -1;
		try (Statement stmt = con.createStatement()) {
			try (ResultSet rs = stmt.executeQuery(SELECT_MAX_PATIENT_ID)) {
				if (rs.next()) {
					patientId = rs.getLong(1);
				} else {
					patientId = 1;
				}
			}
		}
		return patientId;
	}

	public static void updatePatient(String patientCode, String firstName,
			String middleName, String lastName, String address,
			String telephone, String mobile, String reference, Date dob) {

		try (Connection con = getConnection(OLTP_POOL_NAME)) {
			try (PreparedStatement stmt = con.prepareStatement(UPDATE_PATIENT)) {
				stmt.setString(1, firstName);
				stmt.setString(2, middleName);
				stmt.setString(3, lastName);
				stmt.setString(4, address);
				stmt.setString(5, telephone);
				stmt.setString(6, mobile);
				stmt.setString(7, reference);
				stmt.setDate(8, dob != null ? new java.sql.Date(dob.getTime()) : null);
				stmt.setLong(9, Long.parseLong(patientCode));
				stmt.executeUpdate();
			}
		} catch(SQLException e) {
			e.printStackTrace(System.err);
		}
	}

	public static boolean insertPrescription(long patientId, String symtom, String prescription,
			String advice, String feeCode) {

		boolean recordInserted = false;
		try (Connection con = getConnection(OLTP_POOL_NAME)) {
			long prescriptionId = getMaxPrescriptionId(con);
			
			try (PreparedStatement stmt = con.prepareStatement(INSERT_PRESCRIPTION)) {
				stmt.setLong(1, ++prescriptionId);
				stmt.setLong(2, patientId);
				stmt.setString(3, symtom);
				stmt.setString(4, prescription);
				stmt.setString(5, advice);
				stmt.setTimestamp(6, new Timestamp(new Date().getTime()));
				stmt.setString(7, feeCode);
				stmt.execute();
				recordInserted = true;
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
		return recordInserted;
	}
	
	/**
	 * Returns maximum Pid from Patient_Master.
	 * 
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private static long getMaxPrescriptionId(Connection con) throws SQLException {
		long prescriptionId = -1;
		try (Statement stmt = con.createStatement()) {
			try (ResultSet rs = stmt.executeQuery(SELECT_MAX_PRESCRIPTION_ID)) {
				if (rs.next()) {
					prescriptionId = rs.getLong(1);
				} else {
					prescriptionId = 1;
				}
			}
		}
		return prescriptionId;
	}
	
	public static List<DailyCollection> selectCollection(String exDateStr, int type) {
		if (!(type == MORNING || type == EVENING || type == BOTH)) {
			return null;
		}
		List<DailyCollection> rows = new ArrayList<DailyCollection>();
		Date startDate = null;
		Date endDate = null;
		try {
			Date exDate = Util.parseDate(exDateStr);
			Calendar cal = Calendar.getInstance();
			switch (type) {
				case MORNING:
					startDate = exDate;
					cal.setTime(exDate);
					cal.set(Calendar.HOUR, 15);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					endDate = cal.getTime();
					break;
				case EVENING:
					cal.setTime(exDate);
					cal.set(Calendar.HOUR, 15);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					startDate = cal.getTime();
					cal.setTime(exDate);
					cal.add(Calendar.DATE, 1);
					endDate = cal.getTime();
					break;
				case BOTH:
					startDate = exDate;
					cal.setTime(startDate);
					cal.add(Calendar.DATE, 1);
					endDate = cal.getTime();
					break;
				default:
					startDate = exDate;
					endDate = exDate;
			}
			try (Connection con = getConnection(OLTP_POOL_NAME)) {
				try (PreparedStatement pstmt = con.prepareStatement(SELECT_COLLECTION)) {
					pstmt.setTimestamp(1, new java.sql.Timestamp(startDate.getTime()));
					pstmt.setTimestamp(2, new java.sql.Timestamp(endDate.getTime()));
					try (ResultSet rs = pstmt.executeQuery()) {
						DailyCollection coll = null;
						while (rs.next()) {
							coll = new DailyCollection();
							coll.setPrescriptionCode(rs.getLong("Pre_id"));
							coll.setPatientCode(rs.getLong("P_id"));
							coll.setFirstName(rs.getString("PFName"));
							coll.setLastName(rs.getString("PLName"));
							coll.setFeeCode(rs.getString("Fee_Code"));
							coll.setExaminationDate(rs.getDate("Ex_date"));
							coll.setOutstanding(rs.getDouble("Pre_Bal"));
							coll.setAmountPayable(Util.calculateAmountPayable(coll.getFeeCode(), coll.getOutstanding()));
							rows.add(coll);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rows;
	}

	public static void updatePatientOutstandingAmount(long patientCode,
			double data) {
		
		try (Connection con = getConnection(OLTP_POOL_NAME)) {
			try (PreparedStatement stmt = con.prepareStatement(UPDATE_PATIENT_OUTSTANDING)) {
				con.setAutoCommit(false);
				stmt.setDouble(1, data);
				stmt.setLong(2, patientCode);
				int updatedRows = stmt.executeUpdate();
				if (updatedRows > 1) {
					con.rollback();
				}
				con.commit();
				con.setAutoCommit(true);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
		
	}

	public static List<DailyCollection> selectPatientDues() {
		List<DailyCollection> dues = new ArrayList<DailyCollection>();
		try (Connection con = getConnection(OLTP_POOL_NAME)) {
			try (PreparedStatement stmt = con.prepareStatement(SELECT_PATIENT_OUTSTANDING)) {
				try (ResultSet rs = stmt.executeQuery()) {
					DailyCollection col = null;
					while (rs.next()) {
						col = new DailyCollection();
						col.setPatientCode(rs.getLong("Pid"));
						col.setFirstName(rs.getString("PFName"));
						col.setLastName(rs.getString("PLName"));
						col.setOutstanding(rs.getDouble("Pre_Bal"));
						dues.add(col);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
		return dues;
	}
}
