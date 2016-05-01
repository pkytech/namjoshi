/**
 * 
 */
package com.kytech.namjoshi.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;

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
	private Util(){
	}
	
	public static void loadIconImage(JLabel label, String imageName) {
		BufferedImage img = loadImage(imageDir + imageName);
		ImageIcon iconLogo = new ImageIcon(img);
		label.setIcon(iconLogo);
	}
	
	public static void loadIconImage(JLabel label) {
		BufferedImage img = loadImage(imageDir + "no-image-icon-md.png");
		img = resize(img, 100, 100);
		ImageIcon iconLogo = new ImageIcon(img);
		label.setIcon(iconLogo);
		label.setSize(100, 100);
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
	
	public static void loadAttachmentIcons(JPanel panel, final JPanel imagePanel) {
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
		
		for (int i = 0; i < 20; i++) {
			JLabel label = new JLabel();
			
			label.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println("Image clicked");
					imagePanel.removeAll();
					imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
					
					JLabel image = new JLabel();
					image.setBorder(BorderFactory.createLineBorder(Color.red));
					loadImage(image);
					imagePanel.add(image);
					imagePanel.repaint();
					imagePanel.setVisible(true);
					System.out.println("Image Added");
				}
			});
			loadIconImage(label);
			panel.add(label);
			
		}
	}
	
	public static String formatDate(Date exDate) {
		return exDate != null ? sfd.format(exDate) : "";
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
}
