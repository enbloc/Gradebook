import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class LoadingWindow  {
 
	private JFrame 		frame;
	private JLabel		loadingLabel;
	private Icon 		loadingIcon;
	private String 		loadingMessage;
	
	public LoadingWindow(){
		frame = new JFrame("Login");
		loadingIcon = new ImageIcon("res/ajax-loader.gif");
		loadingMessage = "Logging In...";
		loadingLabel = new JLabel(loadingMessage, loadingIcon, JLabel.CENTER);
		frame.add(loadingLabel);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(300, 200);
	    frame.setLocationRelativeTo(null);
	}
	
	public void displayWindow(){
		frame.setVisible(true);
	}
	
	public void setLoadingMessage(String loadingMessage){
		loadingLabel.setText(loadingMessage);
		frame.setVisible(true);
	}
	
	public void closeWindow(){
		frame.setVisible(false);
	}
}