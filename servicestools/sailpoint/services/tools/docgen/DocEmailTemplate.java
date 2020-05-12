package sailpoint.services.tools.docgen;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static sailpoint.services.tools.docgen.DocumentizerUtils.*;

/**
 * Created by adam.creaney on 3/24/16.
 */
public class DocEmailTemplate extends DocBase{

    public String seperator;

    public static void doDocumentation(ArrayList<File> files) throws ParserConfigurationException, SAXException, IOException {
        if(files != null && !files.isEmpty()) {
        	if(!DocBase.objectList.isEmpty()) {
	    		DocBase.objectList.clear();
	    	}
        	for(File f : files){
	            docObject(f);
	        }
	        Documentizer.base.setObjectType(DocBase.DOCTYPE.EmailTemplate.name());
	        Documentizer.base.buildFrameList();
        }
    }

    public static void docObject(File file) throws IOException, SAXException, ParserConfigurationException{
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        //TODO: Why/how can we avoid the dtd processing besides this line?
        dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        InputStream is = new FileInputStream(file);
        Document doc = dBuilder.parse(is);

        doc.getDocumentElement().normalize();

        try{
            Element root = getRealRoot(doc.getDocumentElement(), "EmailTemplate");

            Documentizer.base.docWriter = new DocWriter(DocBase.destination, root, DocBase.DOCTYPE.EmailTemplate);

            Documentizer.base.name = root.getAttribute("name");
            Documentizer.base.docWriter.appendH1(Documentizer.base.name);

            List<List<String>> table = Documentizer.base.docWriter.getTableEmpty();
            List<String> row = new ArrayList<String>();

            row.add("Name");
            row.add("Value");

            table.add(row);

            row = new ArrayList<String>();
            row.add("Description");
            row.add(Documentizer.base.docWriter.getCodeBlock(getNodeTextForPath(root, "Description")));

            table.add(row);

            row = new ArrayList<String>();
            row.add("Subject");
            row.add(getNodeTextForPath(root, "Subject"));

            table.add(row);

            row = new ArrayList<String>();
            row.add("Body");
            row.add(getNodeTextForPath(root, "Body"));

            table.add(row);

            Documentizer.base.docWriter.appendTable(table, Documentizer.base.name);
            Documentizer.base.docWriter.documentSignature(root);

            DocBase.objectList.put(getFilenameForObject(Documentizer.base.name), Documentizer.base.name);
        }  catch(NullPointerException e ){        	  
      	   
        }
        catch (Exception e) {
        	e.printStackTrace();
     	}
        finally{
            if(Documentizer.base.docWriter != null){
            	Documentizer.base.docWriter.close();
            }
        }

    }
}
