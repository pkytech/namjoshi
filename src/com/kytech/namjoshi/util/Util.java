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

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * @author tphadke
 *
 */
public final class Util {
	private static final String baseDir = "/Users/tphadke/work/workspaceHCL/NamjoshiClinic";
	private static final String imageDir = baseDir + "/images/";
	
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
	
	public static Font getSystemFont() {
		return new Font("Lucida Grande", Font.BOLD, 24);
	}
	
}
