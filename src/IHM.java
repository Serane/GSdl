import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class IHM extends JFrame {

	private JPanel panel = new JPanel();
	private JButton bouton = new JButton("Mon bouton");

	public IHM() {
		initComponents();
	}

	private void initComponents() {
		this.setSize(400, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.add(bouton);
		this.setContentPane(panel);
		this.setVisible(true);
	}

}
