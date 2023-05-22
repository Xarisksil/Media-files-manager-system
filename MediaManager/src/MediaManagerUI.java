import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


import java.sql.ResultSet;
import org.eclipse.swt.widgets.MessageBox;

public class MediaManagerUI {

	protected Shell shell;
	private Text fileText;
	private List list;
	private Text searchText;
	private Button deleteButton;
	private Display display;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MediaManagerUI window = new MediaManagerUI();
			Database.connect();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void updateList(ResultSet data) {
		list.removeAll();
		try {
			while (data.next()) {
			
			    
			    String item =  data.getString("id") + " " + data.getString("name") + " " + data.getString("type");
			    list.add(item);
			    
			}
		} catch (Exception ex) {
			System.err.println("Error:" + ex.getMessage());
		}
		
	}
	
	public void initUI() {
		ResultSet rowsData = Database.getAllRecords();
		updateList(rowsData);
	}
	

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		initUI();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	
	public boolean message(String message, String title) {
		
	    MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION| SWT.OK);
	    messageBox.setText(title);
	    messageBox.setMessage(message);
        int response = messageBox.open();
        if (response == SWT.OK) {
        	return true;
        }
        	    
        return false;
	}
	
	public boolean confirmMessage(String message, String title) {
		
		
		    MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION| SWT.YES | SWT.NO);
		    messageBox.setText(title);
		  	messageBox.setMessage(message);
	        int response = messageBox.open();
	        if (response == SWT.YES) {
	        	return true;
	        }
	        if (response == SWT.NO) {
	        	return false;
	        }
		    
	        return false;
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(706, 495);
		shell.setText("SWT Application");
		
		Button addFileButton = new Button(shell, SWT.NONE);
		
		addFileButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				String fileName = fileText.getText();
				
				if (!fileName.isEmpty()) {
					String type = Utilities.getFileType(fileName);
					System.out.println("filename text,type: " + fileName + " : " + type);
					Database.insertRecord(fileName, type);

					ResultSet rowsData = Database.getAllRecords();
					updateList(rowsData);
					
				} else {
					System.out.println("Empty file!");
				}
				
			}
		});
		
		addFileButton.setBounds(582, 407, 75, 25);
		addFileButton.setText("Add file");
		
		fileText = new Text(shell, SWT.BORDER);
		fileText.setBounds(387, 409, 189, 21);
		
		list = new List(shell, SWT.BORDER);
		list.setBounds(10, 67, 341, 365);
		
		Button searchButton = new Button(shell, SWT.NONE);
		searchButton.addMouseListener(new MouseAdapter() {
			@Override
			
			public void mouseUp(MouseEvent e) {
				String searchString = searchText.getText();
				
				ResultSet rowsData = Database.getRecordsBySearch(searchString);
				updateList(rowsData);
				
				int count = 0;
				try {
					
					rowsData.last();
					count  = rowsData.getRow();
				} catch (Exception ex) {
					message("ERROR:" + ex.getMessage(), "Error");
				}
				
				if (count==0) {
					message("No results found!", "Empty results");
				}
				
				
					
			}
		});
		
		
		searchButton.setBounds(276, 30, 75, 25);
		searchButton.setText("Search");
		
		searchText = new Text(shell, SWT.BORDER);
		searchText.setBounds(10, 30, 242, 25);
		
		deleteButton = new Button(shell, SWT.NONE);
		deleteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				int index = list.getSelectionIndex();
				
				
				// .message
				if (index<0) {
					message("Please selecte a file", "Message");
					System.err.println("No selected item!");
					return;
				}
				
				
				String text = list.getItem(index);
				String[] texts = text.split(" ");
				
				if (texts.length<2) {
					System.err.println("No id found!");
					return;
				}
				
				String idStr  = texts[0];
				int id = Integer.parseInt(idStr);
				
				boolean confirmed = confirmMessage("Are you sure to delete " + texts[1] + " ?", "Confirm delete");
				
				if (confirmed) {
					boolean deleted = Database.deleteRecord(id);
					if (deleted) {
						ResultSet rowsData = Database.getAllRecords();
						updateList(rowsData);
					}
				}

				
			}
		});
		
		
		deleteButton.setBounds(416, 176, 88, 32);
		deleteButton.setText("Delete");

	}
}
