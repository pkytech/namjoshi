/**
 * 
 */
package com.kytech.namjoshi.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;

import com.kytech.namjoshi.AttachmentLabel;
import com.kytech.namjoshi.manager.NamjoshiUIManager;

/**
 * @author tphadke
 *
 */
public final class Util {
	private static final String baseDir = "/Users/tphadke/work/workspaceHCL/NamjoshiClinic";
	private static final String imageDir = baseDir + "/images/";
	private static final SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy");
	private static final String PRINT_DEVIDER = "-----------------------------------";
	private static final Map<String, String> FEE_MAP = new HashMap<String, String>();
	static {
		initializeFeeCodeMaster();
	}
	private Util(){
	}
	
	private static void initializeFeeCodeMaster() {
		String value = NamjoshiConfigurator.getInstance().getKeyValue(NamjoshiConfigurator.FEE_CODE);
		value = value.trim().toUpperCase();
		String values[] = value.split(",");
		for (String val : values) {
			String feeParts[] = val.split("-");
			String feeCode = feeParts[0];
			String fee = feeParts[1];
			FEE_MAP.put(feeCode, fee);
		}
	}

	public static void loadIconImage(JLabel label, String imageName) {
		BufferedImage img = loadImage(imageDir + imageName);
		ImageIcon iconLogo = new ImageIcon(img);
		label.setIcon(iconLogo);
	}
	
	public static void loadIconImage(JLabel label, String imageFilename, int width, int height) {
		BufferedImage img = loadImage(imageFilename);
		img = resize(img, width, height);
		ImageIcon iconLogo = new ImageIcon(img);
		label.setIcon(iconLogo);
		label.setSize(width, height);
	}

	public static void loadIconImage(JLabel label, int width, int height) {
		loadIconImage(label, imageDir + "no-image-icon-md.png", width, height);
	}
	
	public static void loadIconImage(JLabel label) {
		loadIconImage(label, imageDir + "no-image-icon-md.png", 100, 100);
	}

	public static void loadIconImage(String patientCode, JLabel label, int width, int height) {
		String uploadDirStr = NamjoshiConfigurator.getInstance().getKeyValue(
				NamjoshiConfigurator.PATIENT_PROFILE_PIC_DIR);
		File profilePic = new File(uploadDirStr, patientCode+".jpg");
		if (profilePic.exists()) {
			BufferedImage img = loadImage(uploadDirStr + File.separator + patientCode+".jpg");
			img = resize(img, width, height);
			ImageIcon iconLogo = new ImageIcon(img);
			label.setIcon(iconLogo);
			label.setSize(width, height);
			label.repaint();
		}
	}
	public static void loadIconImage(String patientCode, JLabel label) {
		loadIconImage(patientCode, label, 100, 100);
	}
	public static final BufferedImage loadImage(String imageFileName) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(imageFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

	public static void loadImage(JLabel label) {
		ImageIcon iconLogo = new ImageIcon(loadImage(imageDir + "/no-image-icon-md.png"));
		label.setIcon(iconLogo);
	}

	public static void loadImage(JLabel label, String imageFileName) {
		ImageIcon iconLogo = new ImageIcon(loadImage(imageFileName));
		label.setIcon(iconLogo);
	}
	
	public static JPanel getBasePanel() {
		JPanel blankPanel = new JPanel();
		blankPanel.setBackground(Color.BLUE);
		blankPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblImage = new JLabel("");
		loadIconImage(lblImage, "ganpati-small.jpg");
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		blankPanel.add(lblImage, BorderLayout.CENTER);
		return blankPanel;
	}
	
	public static BufferedImage resize(BufferedImage image, int width, int height) {
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(image, 0, 0, width, height, null);
	    g2d.dispose();
	    return bi;
	}
	
	public static void loadEmptyAttachmentIcon(JPanel iconPanel) {
		AttachmentLabel label = new AttachmentLabel();
		String fullFileName = imageDir + "noimageavailable.png"; 
		loadIconImage(label, fullFileName, 100, 100);
		label.setFullFileName(fullFileName);
		
		iconPanel.add(label);
	}
	
	
	public static void loadAttachmentIcons(JPanel attachmentIconPanel, JPanel attachmentImagePanel, String patientCode) {
		String uploadDirStr = NamjoshiConfigurator.getInstance().getKeyValue(NamjoshiConfigurator.PATIENT_ATTACH_DIR);
		String subFolder = determineSubfolder(Long.parseLong(patientCode));
		File rootFolder = new File(uploadDirStr, subFolder);
		File attachDir = new File(rootFolder, patientCode);
		if (attachDir.exists()) {
			String files[] = attachDir.list();
			if (files == null || files.length <= -1) {
				loadEmptyAttachmentIcon(attachmentIconPanel);
				return;
			}
			String sortedFiles[] = sortFilesAsPerName(files);
			if (sortedFiles.length > 0) {
				NamjoshiUIManager.getUIManager().attachmentImgesFound();
			}
			for (String file : sortedFiles) {
				AttachmentLabel label = new AttachmentLabel();
				addMouseListener(label);
				String fullFileName = attachDir.getAbsolutePath() + File.separator + file; 
				loadIconImage(label, fullFileName, 100, 100);
				label.setFullFileName(fullFileName);
				attachmentIconPanel.add(label);
			}
		} else {
			loadEmptyAttachmentIcon(attachmentIconPanel);
		}
	}

	private static String[] sortFilesAsPerName(String[] files) {
		List<String> fileNames = new ArrayList<String>();
		for (String file : files) {
			if (!file.startsWith(".")) {
				fileNames.add(file);
			}
		}
		Collections.sort(fileNames, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				Long time1 = Long.parseLong(o1.substring(0, o1.indexOf('.')));
				Long time2 = Long.parseLong(o2.substring(0, o2.indexOf('.')));
				return time1.compareTo(time2);
			}
		});
		return fileNames.toArray(new String[fileNames.size()]);
	}

	public static void addNewAttachmentIcon(JPanel iconPanel, JPanel attachmentImagePanel, File newFile) {
		AttachmentLabel label = new AttachmentLabel();
		addMouseListener(label);
		loadIconImage(label, newFile.getAbsolutePath(), 100, 100);
		if (!NamjoshiUIManager.getUIManager().isAttachmentImagesFound()) {
			iconPanel.removeAll();
		}
		label.setFullFileName(newFile.getAbsolutePath());
		iconPanel.add(label);
		NamjoshiUIManager.getUIManager().attachmentImgesFound();
	}

	private static void addMouseListener(AttachmentLabel label) {
		label.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Object o = e.getSource();
				AttachmentLabel sourceLabel = null;
				String fullFileName = imageDir + "noimageavailable.png";
				if (o instanceof AttachmentLabel) {
					sourceLabel = (AttachmentLabel)o;
					fullFileName = sourceLabel.getFullFileName();
				}
				
				System.out.println("Image clicked");
				
				
				
				JLabel image = NamjoshiUIManager.getUIManager().getAttachmentImageLabel();
				image.setBorder(BorderFactory.createLineBorder(Color.red));
				loadImage(image, fullFileName);
				
				NamjoshiUIManager.getUIManager().repaintAttachmentImagePanel();
				System.out.println("Image Added");
			}
		});
	}
	
	public static String formatDate(Date exDate) {
		return exDate != null ? sfd.format(exDate) : "";
	}
	
	public static Date parseDate(String exDate) throws ParseException {
		return exDate != null ? sfd.parse(exDate) : null;
	}
	
	public static Font getSystemFont() {
		return new Font("Lucida Grande", Font.BOLD, 24);
	}
	
	public static int findAgeOfPatient(Date patientDob) {
		Date now = new Date();
		long timeBetween = now.getTime() - patientDob.getTime();
		double yearsBetween = timeBetween / 3.156e+10;
		int age = (int) Math.floor(yearsBetween);
		return age;
	}

	public static void addUpperCaseDocumentFilter(JTextField textField) {
		DocumentFilter filter = new UppercaseDocumentFilter();
		((AbstractDocument) textField.getDocument()).setDocumentFilter(filter);
	}

	public static void printPrescription(String patientCode, String name,
			String prescription, String feeCode) {
		StringBuffer sbOut = new StringBuffer();
		sbOut.append(patientCode).append(" - ").append(name).append("\n").append(PRINT_DEVIDER).append("\n");
		sbOut.append(prescription).append("\t\t").append(feeCode).append("\n");
		sbOut.append(PRINT_DEVIDER).append("\n");
		
		String pathToBills = NamjoshiConfigurator.getInstance().getKeyValue(NamjoshiConfigurator.BIIL_DIR);
		String billsPrint = NamjoshiConfigurator.getInstance().getKeyValue(NamjoshiConfigurator.BIIL_PRINT_EXEC);
		if (pathToBills == null || pathToBills.trim().length() == 0) {
			NamjoshiUIManager.getUIManager().showErrorMessage("No directory configured for biils");
			return;
		}
		File fileDir = new File(pathToBills);
		if (fileDir.exists()) {
			fileDir.mkdirs();
		}
		File billFile = new File(fileDir, "bills.txt");
		if (billFile.exists()) {
			billFile.delete();
		}
		Process process;
		try {
			Files.write(Paths.get(billFile.toURI()), sbOut.toString().getBytes("utf-8"), StandardOpenOption.CREATE_NEW);
			process = Runtime.getRuntime().exec(pathToBills+File.separator+billsPrint, null, new File(pathToBills));
			int result = process.waitFor();
			System.out.println("Result : " + result);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void uploadProfilePicture(String patientCode,
			File selectedFile) {

		String uploadDirStr = NamjoshiConfigurator.getInstance().getKeyValue(
				NamjoshiConfigurator.PATIENT_PROFILE_PIC_DIR);
		File uploadDir = new File(uploadDirStr);
		if (!uploadDir.exists()) uploadDir.mkdirs(); 
		File destinationFile = new File(uploadDir, patientCode+".jpg");
		boolean success = copyFile(selectedFile, destinationFile);
		if (!success) {
			NamjoshiUIManager.getUIManager().showErrorMessage("Failed to upload file. Please check logs.");
		}
	}

	public static boolean copyFile(File inputFile, File destinationFile) {
		boolean result = false;
		byte data[] = new byte[512*5];
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(inputFile);
			out = new FileOutputStream(destinationFile);
			int len = -1;
			while ((len = in.read(data)) != -1) {
				out.write(data, 0, len);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return result;
	}

	public static void uploadAttachment(JPanel iconPanel, JPanel attachmentImagePanel, String patientCode, File selectedFile) {
		String uploadDirStr = NamjoshiConfigurator.getInstance().getKeyValue(NamjoshiConfigurator.PATIENT_ATTACH_DIR);
		String subFolder = determineSubfolder(Long.parseLong(patientCode));
		File rootFolder = new File(uploadDirStr, subFolder);
		if (!rootFolder.exists()) {
			rootFolder.mkdirs();
		}
		File attachDir = new File(rootFolder, patientCode);
		if (!attachDir.exists()) {
			attachDir.mkdir();
		}
		String fileName = selectedFile.getName();
		String ext = fileName.substring(fileName.indexOf('.'), fileName.length());
		String destFileName = (new Date().getTime()) + ext;
		File destFile = new File(attachDir, destFileName);
		boolean success = copyFile(selectedFile, destFile);
		if (!success) {
			NamjoshiUIManager.getUIManager().showErrorMessage("Failed to upload file. Please check logs.");
			return;
		}
		addNewAttachmentIcon(iconPanel, attachmentImagePanel, destFile);
		iconPanel.repaint();
	}

	public static String determineSubfolder(long patientCode) {
		return String.valueOf(((patientCode/1000)+1)*1000);
	}

	public static double findFileSizeInMb(File selectedFile) {
		return (selectedFile.length()/1024)/1024;
	}

	public static void emptyAttachmentImage(JLabel attachmentImageLabel) {
		attachmentImageLabel.setIcon(null);
	}

	public static double calculateAmountPayable(String feeCode,
			double outstanding) {
		
		String fee = feeCode.trim().toUpperCase();
		char parts[] = fee.toCharArray();
		StringBuffer sbOut = new StringBuffer();
		for (char code : parts) {
			sbOut.append(FEE_MAP.get(String.valueOf(code)));
		}
		return Double.valueOf(sbOut.toString());
	}
}
