package view.GUIView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class Video extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final JFXPanel jfxPanel = new JFXPanel();
	private JPanel jPanel;

	private String path;

	public Video(String path, String title) {
		this.path = path;
		initComponents();
		createScene();
		setTitle(title);

		setResizable(false);
		setLocationRelativeTo(null);

		jPanel.setLayout(new BorderLayout());
		jPanel.add(jfxPanel, BorderLayout.CENTER);
		this.setLocationRelativeTo(null);

		this.pack();
		this.setSize(new Dimension(1920, 1080)); //Para que no se salga la lista de puntuaciones si los nombres son demasiado largos
		this.setMinimumSize(this.getSize());
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private void initComponents() {
		jPanel = new JPanel();
		this.setContentPane(jPanel);
	}

	private void createScene() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				File file = new File(path);
				MediaPlayer oracleVid = null;	//Para que eclipse no se queje
				try {
					oracleVid = new MediaPlayer (new Media(file.toURI().toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				jfxPanel.setScene(new Scene(new Group(new MediaView(oracleVid))));
				oracleVid.setVolume(0.7); //Volumen
				oracleVid.setCycleCount(MediaPlayer.INDEFINITE);
				oracleVid.play();
			}

		});
	}



}