import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;
import java.io.*;
import java.util.ArrayList;

public final class ClipboardEntityConverter implements ClipboardOwner {

  public static void main (String[] args){
    ClipboardEntityConverter clipboard = new ClipboardEntityConverter();
    Object[] entities = clipboard.loadEntities("entities.txt");
    String content = null;
    String newContent;

    while (true) {
    	while (true) {
    		newContent = clipboard.getClipboardContents();

    		if (newContent.equals(content)) {
    			try {
    				Thread.sleep(5000);
    			} catch (Exception e) {
    			}
    		} else {
    			System.out.print ("New content detected ... ");
    			content = newContent;
    			break;
    		}
    	}

	    for (int i = 0; i < entities.length; i++) {
	    	String thisCharacter = ((Entity)entities[i]).character;
	    	String thisEntity = ((Entity)entities[i]).entity;
	    	content = content.replace (thisCharacter,thisEntity);
	    }

		System.out.println ("converted");
	    clipboard.setClipboardContents(content);
    }
  }

  private Object[] loadEntities(String filename) {
  	ArrayList entities = new ArrayList();

  	try {
  		BufferedReader in = new BufferedReader (new FileReader (filename));
		String line;
		int index = 0;

		System.out.print ("Loading Entities: ");

		while ((line = in.readLine()) != null) {
  			String[] lineSegments = line.split (",");
  			System.out.print (lineSegments[0] + ">" + lineSegments[1] + " ");
			entities.add (index++, new Entity (lineSegments[0], lineSegments[1]));
		}
		System.out.println ();
		System.out.println ("Entities Loaded");
  	} catch (Exception e) {
  	}

  	return entities.toArray();
  }

   /**
   * Empty implementation of the ClipboardOwner interface.
   */
   public void lostOwnership( Clipboard aClipboard, Transferable aContents) {
     //do nothing
   }

  /**
  * Place a String on the clipboard, and make this class the
  * owner of the Clipboard's contents.
  */
  public void setClipboardContents( String aString ){
    StringSelection stringSelection = new StringSelection( aString );
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents( stringSelection, this );
  }

  /**
  * Get the String residing on the clipboard.
  *
  * @return any text found on the Clipboard; if none found, return an
  * empty String.
  */
  public String getClipboardContents() {
    String result = "";
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    //odd: the Object param of getContents is not currently used
    Transferable contents = clipboard.getContents(null);
    boolean hasTransferableText =
      (contents != null) &&
      contents.isDataFlavorSupported(DataFlavor.stringFlavor)
    ;
    if ( hasTransferableText ) {
      try {
        result = (String)contents.getTransferData(DataFlavor.stringFlavor);
      }
      catch (UnsupportedFlavorException ex){
        //highly unlikely since we are using a standard DataFlavor
        System.out.println(ex);
        ex.printStackTrace();
      }
      catch (IOException ex) {
        System.out.println(ex);
        ex.printStackTrace();
      }
    }
    return result;
  }
}