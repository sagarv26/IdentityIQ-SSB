package sailpoint.services.tools.ant;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Component;
import java.io.Console;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.input.InputRequest;

public class ReadFromConsole implements InputHandler { 
	
	private Component parent = null;
	
	public ReadFromConsole() {
		
	}
	
	public ReadFromConsole(Component parent) {
		this.parent = parent;
	}
	
	public static void main(String[] args) {
		try {

			
			String password = (new ReadFromConsole()).askUserUI("Passwd?:","spadmin");
			if (password != null)
			    System.out.println("your password: " + new String(password));
		 
		} catch (Exception  e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleInput(InputRequest request) throws BuildException {
		String prompt = request.getPrompt();
		String defaultUser = request.getDefaultValue();
		
		
		
		
		do {
			String input = askUserConsole(prompt,defaultUser);
			if(input==null)
				input = askUserUI(prompt,defaultUser);
			request.setInput(input);
		} while (!(request.isInputValid()));
		
		
	}
	
	private String askUserConsole(String question,final String defaultUserName)
	{
		Console console = System.console();
		String username=defaultUserName;
		if(console==null)
			return null;
		
		if(defaultUserName==null || defaultUserName.isEmpty())
		{
			console.printf("Please enter username: ");
	        username = console.readLine();
	        
		}
        console.printf("Please enter password: ");
        char[] passwordChars = console.readPassword();
        String passwordString = new String(passwordChars);

         
        return username  +"\n" + passwordString;
	}
	

	
	private  String askUserUI(String question,final String defaultUserName)
	{
		 
		
		final JTextField userName = new JTextField();
		
		userName.setText(defaultUserName);
	 
		final JPasswordField password = new JPasswordField();
		final JComponent[] inputs = new JComponent[] {
		        new JLabel("User Name"),
		        userName,
		        new JLabel("Password"),
		        password 
		};
		
		JOptionPane jop = new JOptionPane(inputs, JOptionPane.QUESTION_MESSAGE,
		        JOptionPane.OK_CANCEL_OPTION);
		JDialog dialog = jop.createDialog(question);
		
		 
		dialog.addComponentListener(new ComponentAdapter() {
		    @Override
		    public void componentShown(ComponentEvent e) {
		        SwingUtilities.invokeLater(new Runnable() {
		            @Override
		            public void run() {
		            	 
		            	password.requestFocusInWindow();
		            	if(defaultUserName==null||defaultUserName.isEmpty())
		            		userName.requestFocusInWindow();
 
		            }
		        });
		    }
		});
		
		dialog.setVisible(true);
		if(jop!=null && jop.getValue()!=null)
		{
			int result = (Integer) jop.getValue();
			dialog.dispose();
			char[] passwordVal = null;
			if (result == JOptionPane.OK_OPTION) {
				passwordVal = password.getPassword();
			    return userName.getText()  +"\n" + new String(passwordVal);
			}
		}
		dialog.dispose();
		return null;
	}
}
