package com.kytech.namjoshi.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public final class NamjoshiConfigurator {
	public static final String JDBC_DB_URL = "namjoshi.jdbc.url";
	public static final String JDBC_CLASSNAME = "namjoshi.jdbc.classname";
	public static final String DB_USER_NAME = "namjoshi.jdbc.userid";
	public static final String DB_PASSWORD = "namjoshi.jdbc.password";
	public static final String BIIL_DIR = "namjoshi.bills.dir";
	public static final String BIIL_PRINT_EXEC = "namjoshi.bills.print";
	public static final String PATIENT_PROFILE_PIC_DIR = "namjoshi.profilePic.dir";
	public static final String PATIENT_ATTACH_DIR = "namjoshi.attachment.dir";
	public static final String  FEE_CODE = "namjoshi.fee.code";
	private static NamjoshiConfigurator configurator = null;
	private Properties configValues = null;
	private NamjoshiConfigurator() {
		loadProperties();
	}
	
	private void loadProperties() {
		configValues = new Properties();
		try {
			try (FileInputStream fin = new FileInputStream(new File("/Users/tphadke/work/workspaceHCL/NamjoshiClinic/namjoshi.properties"))) {
				configValues.load(fin);
			}
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		//System.out.println("Configurator loaded: "+ configValues);
	}

	public static final NamjoshiConfigurator getInstance() {
		if (configurator == null) {
			configurator = new NamjoshiConfigurator();
		}
		return configurator;
	}
	
	public String getKeyValue(String key) {
		if (configValues.containsKey(key)) return configValues.getProperty(key);
		return null;
	}
}
