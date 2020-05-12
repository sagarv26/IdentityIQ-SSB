package sailpoint.services.tools.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.w3c.dom.Element;
import sailpoint.services.tools.docgen.DocumentizerUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by surya.nellepalli on 25/06/2017.
 * This class returns the list of IIQ Objects found in a XML file
 */
public class getIIQObjectName extends Task{

    private File _file;

    private String _ignoreFolderList;
    private String _property;
    private boolean _logErrors;



    // The setter for the "configLocation" attribute
    public void setIIQFile(File configLocation) {
        this._file = configLocation;
    }


    // The setter for the "IgnoreFolderList" attribute
    public void setIgnoreFolderList(String ignoreList) {
        this._ignoreFolderList = ignoreList;
    }

    // The setter for the "property" attribute
    public void setProperty(String prop) { this._property = prop; }

    // The setter for the "logErrors" attribute
    public void setLogErrors(boolean shouldLogErrors) { this._logErrors = shouldLogErrors; }

    // The method executing the task
    public void execute() throws BuildException {

        if(_file==null) {
            logErrors("Cannot find file to check");
            return;
        }

        if(_file.isDirectory()) {
            logErrors("Found folder, please specify file "+_file.getAbsolutePath());
            return;
        }

        if(_ignoreFolderList!=null && _ignoreFolderList.indexOf(_file.getParentFile().getName())>=0)
        {
            logErrors(_file.getAbsolutePath()+" is in ignore list");

            return;
        }

        Element currentFileXmlRoot  = DocumentizerUtils.getRoot(this._file);
        if(currentFileXmlRoot==null)
        {
            logErrors("File is not XML file "+_file.getAbsolutePath());
            return;
        }
        ArrayList<String> realRoot=DocumentizerUtils.getRealRootList(currentFileXmlRoot);

        getProject().setProperty(_property,listToCSV(realRoot));



    }

    protected void logErrors(String msg){
        if(_logErrors){
            log(msg);
        }
    }

    public static String listToCSV(List<String> lst){
        StringBuffer csvSB=new StringBuffer();
        if(lst!=null) {
            for (String s : lst) {
                csvSB.append(s);
                csvSB.append(",");
            }
        }
        String csv=csvSB.toString();
        if(csv.endsWith(",")){
            csv=csv.substring(0,csv.length()-1);
        }
        return csv;
    }


}
