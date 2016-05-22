
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;

import java.util.Scanner;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/* Anime.java requires no other files. */
public class Anime extends JPanel implements ListSelectionListener {
  private JList list;
  //private ArrayList<String> arrayList; //No Collections class support
  private DefaultListModel listModel;
  //JScrollPane listScrollPane = null;

  private static final String deleteString = "Delete";
  private static final String searchString = "Search";
  private JButton delButton, searchButton;
  private static JTextField searchField;

  private String fileString = "D:\\Java_Files\\Swing\\ListDemo\\class\\AnimeList.txt";


  public Anime() {
    super(new BorderLayout());

    //Here is where I will read the file in and populate the 
    //listModel
    //arrayList = new ArrayList<String>();
    listModel = new DefaultListModel();
    /*arrayList.addElement("Debbie Scott");
    arrayList.addElement("Scott Hommel");
    arrayList.addElement("Sharon Zakhour");*/

    //Open a file for reading and put in to arrayList
    try{
      fileToListModel();
    }
    catch(Exception ex){
      System.out.println("Error Openning File");
      System.exit(1);
    }

    // Create the list and put it in a scroll pane.
    list = new JList(listModel);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.setSelectedIndex(0);
    list.addListSelectionListener(this);
    list.setVisibleRowCount(5);
    JScrollPane listScrollPane = new JScrollPane(list);

    delButton = new JButton(deleteString);
    delButton.setActionCommand(deleteString);
    delButton.addActionListener(new DeleteListener());
    delButton.setMnemonic('D');

    searchButton = new JButton(searchString);
    SearchListener searchListener = new SearchListener(searchButton);
    searchButton.addActionListener(searchListener);
    searchButton.setEnabled(false);
    searchButton.setMnemonic('S');

    searchField = new JTextField(10);
    searchField.addActionListener(searchListener);
    searchField.getDocument().addDocumentListener(searchListener);

    // Create a panel that uses BoxLayout.
    JPanel buttonPane = new JPanel();
    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
    buttonPane.add(delButton);
    buttonPane.add(Box.createHorizontalStrut(5));
    buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
    buttonPane.add(Box.createHorizontalStrut(5));
    buttonPane.add(searchField);
    //buttonPane.add(hireButton);
    buttonPane.add(searchButton);
    buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    add(listScrollPane, BorderLayout.CENTER);
    add(buttonPane, BorderLayout.PAGE_END);

  }

  class DeleteListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      // This method can be called only if
      // there's a valid selection
      // so go ahead and remove whatever's selected.
      Toolkit.getDefaultToolkit().beep();
      //System.out.println("Name NOT found");
      //Ask to insert new value
      int result = JOptionPane.showOptionDialog(new JFrame(), "Delete value " + (String)list.getSelectedValue()+"?", "Confirm Deletion",
           JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

      if (result == JOptionPane.YES_OPTION) {
            
        int index = list.getSelectedIndex();
        //System.out.println("Deleting: " + arrayList.get(index));
        listModel.remove(index);
        

        int size = listModel.size();

        if (size == 0) { // Nobody's left, disable firing.
          delButton.setEnabled(false);

        } else { // Select an index.
          /**************************************************************
          Collections.sort(arrayList);
          //list = null;
          list = new JList(arrayList.toArray());

          list.setSelectedIndex(0);
          list.ensureIndexIsVisible(0);

          listScrollPane = new JScrollPane(list);
          **************************************************************/

          //FROM LISTDEMO
          if (index == listModel.getSize()) {
            // removed item in last position
            index--;
          }

          list.setSelectedIndex(index);
          list.ensureIndexIsVisible(index);

          try{
              listModelToFile();
            }
            catch(Exception ex){
              ex.printStackTrace();
            }

          searchField.requestFocusInWindow();
          searchField.setText("");
        }
      }
    }
  }

  /****************************************************************
  //Searches through the ArrayList to find element.
  //If element not found asks user if they would like to save it
  *****************************************************************/
  class SearchListener implements ActionListener, DocumentListener{
    private boolean alreadyEnabled = false;
    private JButton button;

    public SearchListener(JButton button) {
      this.button = button;
    }

    public void actionPerformed(ActionEvent e){
      //Get the text from the searchField
      String temp = searchField.getText().toLowerCase();
      //System.out.println(temp);
      if(temp.equals("")){
        Toolkit.getDefaultToolkit().beep();
        searchField.setText("Enter query here");
        searchField.requestFocusInWindow();
        searchField.selectAll();
        return; //Why?
      }
      else{
        //See if the arrayList contains the value
        if(listModel.contains(temp)){
          int indexOfSearch = listModel.indexOf(temp);
          //System.out.println("Name found");
          //System.out.println((String)arrayList.getElementAt(indexOfSearch));
          list.setSelectedIndex(indexOfSearch);
          list.ensureIndexIsVisible(indexOfSearch);
        }
        else{
          Toolkit.getDefaultToolkit().beep();
          //System.out.println("Name NOT found");
          //Ask to insert new value

          int result = JOptionPane.showOptionDialog(new JFrame(), temp + " NOT found. Insert value?", "Confirm Addition",
              JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

          if (result == JOptionPane.YES_OPTION) {
            
            /*int index = list.getSelectedIndex(); // get selected index
            if (index == -1) { // no selection, so insert at beginning
              index = 0;
            } else { // add after the selected item
              index++;
            }*/

            /*arrayList.add(temp);
            Collections.sort(arrayList);
            list = new JList(arrayList.toArray());
            int index = arrayList.indexOf(temp);*/
            listModel.insertElementAt(temp, 0); //Set at beginning

            searchField.requestFocusInWindow();
            searchField.setText("");

            
            list.setSelectedIndex(0);
            list.ensureIndexIsVisible(0);
            //listScrollPane = new JScrollPane(list);

            

            //Save arrayList elements to a file
            try{
              listModelToFile();
            }
            catch(Exception ex){
              ex.printStackTrace();
            }

          } 
        }//End if/else block
      }//End else
    }//End ActionPerformed

    // Required by DocumentListener.
    public void insertUpdate(DocumentEvent e) {
      enableButton();
    }

    // Required by DocumentListener.
    public void removeUpdate(DocumentEvent e) {
      handleEmptyTextField(e);
    }

    // Required by DocumentListener.
    public void changedUpdate(DocumentEvent e) {
      if (!handleEmptyTextField(e)) {
        enableButton();
      }
    }

    private void enableButton() {
      if (!alreadyEnabled) {
        button.setEnabled(true);
      }
    }

    private boolean handleEmptyTextField(DocumentEvent e) {
      if (e.getDocument().getLength() <= 0) {
        button.setEnabled(false);
        alreadyEnabled = false;
        return true;
      }
      return false;
    }

  }//End SearchListener

  // This method is required by ListSelectionListener.
  public void valueChanged(ListSelectionEvent e) {
    if (e.getValueIsAdjusting() == false) {

      if (list.getSelectedIndex() == -1) {
        // No selection, disable fire button.
        delButton.setEnabled(false);

      } else {
        // Selection, enable the fire button.
        delButton.setEnabled(true);
      }
    }
  }

  /****************************************************************
  //Reads data from file to an ArrayList and sorts it. 
  //Could be optimized
  *****************************************************************/
  public void fileToListModel() throws Exception{
    java.io.File file = new java.io.File(fileString);

    Scanner input = new Scanner(file);

    while(input.hasNext()){
      listModel.addElement(input.nextLine().toLowerCase());
    }

    input.close();

  }

  /****************************************************************
  //Saves data in ArrayList to a file. I could optimize with 
  //a thread
  *****************************************************************/
  public void listModelToFile() throws Exception{
    //Iterator<String> e = arrayList.iterator();
    ArrayList<String> temp = new ArrayList<>();
    Enumeration<String> e = listModel.elements();
    while(e.hasMoreElements())
      temp.add(e.nextElement());
    Collections.sort(temp);
    Iterator<String> iter = temp.iterator();
    java.io.File file = new java.io.File(fileString);
    PrintWriter output = new PrintWriter(file);
    while(iter.hasNext())
      output.println(iter.next().toLowerCase());

    output.close();
  }

  /**
   * Create the GUI and show it. For thread safety, this method should be
   * invoked from the event-dispatching thread.
   */
  private static void createAndShowGUI() {
    // Create and set up the window.
    JFrame frame = new JFrame("AnimeList");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Create and set up the content pane.
    JComponent newContentPane = new Anime();
    newContentPane.setOpaque(true); // content panes must be opaque
    frame.setContentPane(newContentPane);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    //Get the height and width of the screen and set window in center
    int width = (((screenSize.width - frame.getWidth()) /2 ) /2);
    int height = (((screenSize.width - frame.getWidth()) /2 ) /2);
    frame.setLocation(width, height);
    frame.setSize(800, 400);
    frame.validate();
    frame.setVisible(true);

    searchField.requestFocusInWindow();  //Used because platform independent
  }

  public static void main(String[] args) {
    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }
}