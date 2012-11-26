import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;

public class ChangePropertiesActionListener implements ActionListener {
	private Component component;

	public ChangePropertiesActionListener(Component component) {
		this.component = component;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final JPanel panel = new JPanel();
		JButton updateButton = new JButton("Update");
		panel.add(updateButton,BorderLayout.SOUTH);
		
		
		
		PopupFactory factory = PopupFactory.getSharedInstance();
		final Popup popup = factory.getPopup(component, panel, 200, 200);
		popup.show();

		ActionListener validAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				popup.hide();
			}
		};
		updateButton.addActionListener(validAction);

	}

}
