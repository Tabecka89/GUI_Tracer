// Gal Tabecka 201668001

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HW3_GalTabecka extends JApplet {
	private DrawCanvas canvas = new DrawCanvas();
	private ArrayList<Point> pointArr = new ArrayList<Point>();
	private int pointArrayIndex = 0;
	private Timer timer = new Timer(FAST_DELAY, new DrawListener());
	private JFrame controlPanelFrame = new JFrame();
	private ControlPanel ctrlPanel = new ControlPanel();
	private boolean isRecording = true;
	private boolean isNewRecording = false;
	private static final String RECORDING_STR = "RECORDING...";
	private static final String RESTORING_STR = "RESTORING...";
	private Font font = new Font("Arial", Font.BOLD + Font.ITALIC, 20);

	private static final int THIN_LINE = 3;
	private static final int MEDIUM_LINE = 5;
	private static final int THICK_LINE = 9;

	private static final int SLOW_DELAY = 10;
	private static final int MEDIUM_DELAY = 5;
	private static final int FAST_DELAY = 1;

	public static void main(String[] args) {
		JFrame frame = new JFrame("HW3");
		JApplet applet = new HW3_GalTabecka();
		frame.add(applet);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 700);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		frame.setResizable(false);
	}

	public HW3_GalTabecka() {
		add(canvas);
		canvas.setBorder(new LineBorder(Color.LIGHT_GRAY, 5));
		controlPanelFrame.add(ctrlPanel);
		controlPanelFrame.pack();
		setFocus();

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_SPACE) {
					ctrlPanel.resetPanel();
					timer.stop();
					controlPanelFrame.setAlwaysOnTop(true);
					controlPanelFrame.setVisible(true);
					controlPanelFrame.setResizable(false);
					controlPanelFrame.setSize(300, 350);
					controlPanelFrame.setTitle("ControlPanel");
					controlPanelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					controlPanelFrame.setLocationRelativeTo(canvas);
					isRecording = false;
					controlPanelFrame.requestFocus();
				}
			}
		});
		
		controlPanelFrame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					paintRecording();
				}
			}
		});
		
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				canvas.drawOnMouse(e.getPoint());
			}
		});
		canvas.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				canvas.drawOnMouse(e.getPoint());
			}
		});
	}


	public void setFocus() {
		setFocusable(true);
		requestFocusInWindow();
	}
	

	public void setFocusControl() {
		controlPanelFrame.setFocusable(true);
		controlPanelFrame.requestFocusInWindow();
	}

	public class ControlPanel extends JPanel {
		private JRadioButton jrbBlack, jrbRed, jrbBlue, jrbGreen;
		private String[] thickness = { "Default Thickness", "Slim", "Thick" };
		private JSlider jSlider = new JSlider(SwingConstants.HORIZONTAL, 1, 3, 3);
		private JButton jbtOk = new JButton("OK");
		private JComboBox<String> jcombo = new JComboBox<String>(thickness);
		private JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 30));

		public ControlPanel() {
			setLayout(new BorderLayout());
			JPanel jpRadioButtons = new JPanel();
			setBorder(new EmptyBorder(0, 15, 20, 15));
			jpRadioButtons.add(jrbBlack = new JRadioButton("Black"));
			jpRadioButtons.add(jrbRed = new JRadioButton("Red"));
			jpRadioButtons.add(jrbBlue = new JRadioButton("Blue"));
			jpRadioButtons.add(jrbGreen = new JRadioButton("Green"));

			northPanel.add(jpRadioButtons);

			
			ButtonGroup group = new ButtonGroup();
			jrbBlack.setMnemonic('B');
			jrbRed.setMnemonic('R');
			jrbBlue.setMnemonic('L');
			jrbGreen.setMnemonic('G');
			group.add(jrbBlack);
			group.add(jrbRed);
			group.add(jrbBlue);
			group.add(jrbGreen);
			jrbBlack.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					canvas.setColor(Color.BLACK);
					setFocusControl(); 
				}
			});
			jrbRed.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					canvas.setColor(Color.RED);
					setFocusControl();
				}
			});
			jrbBlue.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					canvas.setColor(Color.BLUE);
					setFocusControl();
				}
			});
			jrbGreen.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					canvas.setColor(Color.GREEN);
					setFocusControl();
				}
			});

			TitledBorder title = new TitledBorder("Colors");
			jpRadioButtons.setBorder(title);

			
			jcombo.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					switch (jcombo.getSelectedIndex()) {
					case 0:
						canvas.setLineThickness(MEDIUM_LINE);
						break;
					case 1:
						canvas.setLineThickness(THIN_LINE);
						break;
					case 2:
						canvas.setLineThickness(THICK_LINE);
						break;

					default:
						break;
					}
					setFocusControl();
				}
			});
			
			jcombo.setToolTipText("Choose the thickness of the line");
			northPanel.add(jcombo);

			JPanel jSliderPanel = new JPanel();

			jSlider.setPaintTicks(true);
			jSlider.setPaintLabels(true);
			jSlider.setMajorTickSpacing(1);
			jSliderPanel.setBorder(new TitledBorder("Speed"));
			jSliderPanel.add(jSlider);
			northPanel.add(jSliderPanel);
			
			
			jSlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					switch (jSlider.getValue()) {
					case 1:
						timer.setDelay(SLOW_DELAY);
						break;
					case 2:
						timer.setDelay(MEDIUM_DELAY);
						break;
					case 3:
						timer.setDelay(FAST_DELAY);
						break;

					default:
						break;
					}
					setFocusControl();
				}
			});
			add(northPanel);
			JPanel jButtonPanel = new JPanel();
			jButtonPanel.add(jbtOk);
			jButtonPanel.setSize(getWidth(), 50);
			add(jButtonPanel, BorderLayout.SOUTH);

			
			jbtOk.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					paintRecording();
				}
			});
		}

		public void resetPanel() {
			jcombo.setSelectedIndex(0);
			jrbBlack.setSelected(true);
			jSlider.setValue(3);
		}
	}

	
	public void paintRecording() {
		pointArrayIndex = 0;
		controlPanelFrame.dispose();
		isRecording = false;
		canvas.repaint();
		timer.start();
	}

	
	public class DrawListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (pointArrayIndex < pointArr.size()) {
				pointArrayIndex++;
			} else {
				timer.stop();
				isRecording = true;
				isNewRecording = true;
			}
			repaint();
		}
	}

	public class DrawCanvas extends JPanel {
		private int lineThickness = 5;
		private Color color = Color.BLACK;
		private static final int STR_X = 15; 
		private static final int STR_Y = 30; 

	
		public void drawOnMouse(Point point) {
			if (isNewRecording) {
				pointArr.clear();
				setLineThickness(MEDIUM_LINE);
				setColor(Color.BLACK);
				isNewRecording = false;
				repaint();
			}
			if (isRecording) {
				pointArr.add(point);
				repaint();
			}
		}
		
		public void setColor(Color color) {
			this.color = color;
		}

		public void setLineThickness(int lineThickness) {
			this.lineThickness = lineThickness;
		}
		
		public void setNewPointArr(Point point) {
			pointArr.add(point);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(color);
			
			if (isRecording) {
				for (int i = 0; i < pointArr.size(); i++) {
					g.fillOval(pointArr.get(i).x, pointArr.get(i).y, lineThickness, lineThickness);
				}
			} else {
				for (int i = 0; i < pointArrayIndex; i++) {
					g.fillOval(pointArr.get(i).x, pointArr.get(i).y, lineThickness, lineThickness);
				}
			}
			
			g.setFont(font);
			if (isRecording) {
				g.setColor(Color.RED);
				g.drawString(RECORDING_STR, STR_X, STR_Y);
			} else {
				g.setColor(Color.BLACK);
				g.drawString(RESTORING_STR, STR_X, STR_Y);
			}
		}
	}
}
