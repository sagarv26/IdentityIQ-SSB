package sailpoint.services.standard.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import sailpoint.api.SailPointContext;
import sailpoint.object.*;
import sailpoint.persistence.PersistedFileInputStream;
import sailpoint.rest.ReportResource.StreamingFileResult;
import sailpoint.task.AbstractTaskExecutor;
import sailpoint.task.Monitor;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Message;
import sailpoint.api.Terminator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReportExtractor extends AbstractTaskExecutor{
	
	private ReportExtractorSettings settings = null;
	
	private TaskResult taskResult = null;
	private TaskSchedule taskSchedule = null;
	private SailPointContext context = null;
	private long totalLiveReports = 0;
	private long startTime = 0;
	private Monitor _monitor = null;
	private int prunedCount = 0;
	private int backupCount = 0;
	boolean shouldTerminate = false;
	
	private static final Log logger = LogFactory.getLog(ReportExtractorSettings.class);
	
	@Override
	public void execute(SailPointContext context, TaskSchedule taskSchedule, TaskResult taskResult, Attributes<String, Object> attrs) throws Exception{
		logger.debug("Entering run...");
		
		this.settings = new ReportExtractorSettings(context, attrs, taskResult);
		this.context = context;
		this.taskResult = taskResult;
		this.taskSchedule = taskSchedule;
		this.startTime = new Date().getTime();
		
		Boolean isDelete = this.settings.getConfigProcessDeletes();
		String path = this.settings.getConfigPathToFolder();
		Boolean isZip = this.settings.getConfigZipResult();
		String dateToDelete = this.settings.getConfigBackupAndPruneDate();
		logger.debug("The path input was   : " + path);
		logger.debug("The zip input was    : " + isZip);
		logger.debug("The date input was   : " + dateToDelete);
		logger.debug("The delete input was : " + isDelete);
		//create folder
		folderSetup(path);
		
		QueryOptions qo = new QueryOptions();
		
		try {
			Iterator it = context.search(TaskResult.class, qo);
			int cached = 0;
			while(it.hasNext()){
				TaskResult tr = (TaskResult) it.next();
				boolean isDeleteOkay = false;
				if(null != tr){
					cached++;
					if(tr.getType().toString().equalsIgnoreCase("liveReport")){
						Date created = tr.getCreated();
						Integer dateToInt = Integer.parseInt(dateToDelete);
						Date pruneDate = getPruneDate(dateToInt);
						if(created.before(pruneDate)){
							logger.debug("Have a TaskResult for a Live Report!");
							totalLiveReports++;
							isDeleteOkay = true;
							String dateStamp = getDate();
							
							//create the folder
							StringBuilder sbPre = new StringBuilder();
							sbPre.append(path).append(tr.getName()).append(dateStamp).append("/");
							
							folderSetup(sbPre.toString());
												
							//now create the TR xml file
							String finalPath = sbPre.toString();						
							StringBuilder sb = new StringBuilder();	
							sb.append(finalPath).append(tr.getName()).append(dateStamp).append(".xml");		
							
							copyTRXml(sb.toString(), tr);
							
							JasperResult jr = tr.getReport();
							List l = new ArrayList();
							if(null != jr){
								l = jr.getFiles();
								for(Object pf : l){
									String id = ((PersistedFile) pf).getId();
									String name = ((PersistedFile) pf).getName();
									String type = ((PersistedFile) pf).getContentType();
									
									StreamingFileResult sfr = new StreamingFileResult(context, (PersistedFile) pf);
									File temp = new File(sbPre.toString() + name);
									FileOutputStream output = new FileOutputStream(temp);

									if(writeFile(sfr, output, (PersistedFile) pf)){							
										if(!temp.exists() || temp.length() == 0 ){
											isDelete = false;
										}
									}
									else{
										logger.error("Could not write file for TaskResult: " + tr.getName());		
									}
								}
								if(isZip){
									//le barf...gross
									zipIt(sbPre.toString().substring(0, sbPre.toString().length()-1) + ".zip",sbPre.toString().substring(0, sbPre.toString().length()-1));
									deleteDirectory(sbPre.toString());
								}
							}
							backupCount++;
						}						
					}
					if(isDeleteOkay && isDelete){
						logger.debug("Pruning TaskResult for report: " + tr.getName());
						deleteTaskResult(tr, context);
					}
				}
				if(cached % 100 == 0){
					context.decache();
				}
			}
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			this.taskResult.addMessage(Message.error("Error exporting reports from table persisted_file",  e));
			e.printStackTrace();
		}
		
		taskResult.setAttribute("reports_backed_up", backupCount);
		taskResult.setAttribute("reports_pruned", prunedCount);
	}
	
	@Override
	public boolean terminate(){
		shouldTerminate = true;
		this.taskResult.addMessage(Message.error("Task Terminated by User"));
		return shouldTerminate;
	}	
	
	/**
	 * Helper method that returns a date in yyyyMMddhhmm format
	 * 
	 * @return String
	 */
	public String getDate(){
		logger.debug("Entering getDate");
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
		Date date = new Date();
		logger.debug("Leaving getDate");
		return dateFormat.format(date);
	}
	
	/**
	 * Returns a date object that is todays date - input days
	 * @param i the Integer representing prune-after period
	 * @return a date object with today - Integer number of days
	 */
	public Date getPruneDate(Integer i){
		logger.debug("Entering getPruneDate...");
		Calendar c = new GregorianCalendar();
		c.add(Calendar.DATE, (-1)*i);
		Date d = c.getTime();
		logger.debug("Leaving getPruneDate...");
		return d;	
	}
	
	/**
	 * writes PersistedFile object to output stream
	 * 
	 * @param sfr the StreamFileResult object - unused
	 * @param fos the FileOutputStream object to write buffer to
	 * @param file the PersistedFile object from the spt_persisted_file table
	 * @return boolean
	 * @throws Exception
	 */
	public boolean writeFile(StreamingFileResult sfr, FileOutputStream fos, PersistedFile file){
		logger.debug("Entering writeFile...");
		try{
			PersistedFileInputStream reader = new PersistedFileInputStream(context, file);
            byte[] buffer = new byte[4096];
            int len;
            while ((len = reader.read(buffer)) > 0) {
              fos.write(buffer, 0, len);
            }
            fos.flush();
            fos.close();
            logger.debug("Successfully leaving writeFile...");
            return true;
			
		} catch (Exception e){
			e.printStackTrace();
			logger.error("Leaving writeFile with exception/error...");
			return false;
		}
	}
	
	/**
	 * Sets up folder at given path for copying, creates if doesn't exist already
	 * 
	 * @param s the path to the folder to create
	 * @return void
	 */
	public void folderSetup(String s){
		logger.debug("Entering folderSetup(String)...");
		boolean exists = false;
		File f = new File(s);
		if(!f.exists()){
			logger.debug("Folder did not exist creating...");
			exists = f.mkdir();
		}
		else{
			logger.debug("Folder already existed, skipping...");
		}
		logger.debug("Leaving folderSetup(String), folder was setup? " + exists);
	}
	
	/**
	 * Copies the xml from a TaskResult object to a file
	 * 
	 * @param path the path to the destination file
	 * @param tr TaskResult object to be copied
	 * @return void
	 * @throws Exception
	 */
	public void copyTRXml(String path, TaskResult tr) throws Exception{
		logger.debug("Entering copyTRXMl with path: " + path + " and TR: " + tr.getName());
		File f = new File(path);
		FileWriter fw = new FileWriter(f);
		fw.write(tr.toXml());
		fw.close();
		logger.debug("Closing FileWriter after creating TaskResult.xml...");
	}
	
	/**
	 * Zips report folder - from JavaCodeGeeks
	 * 
	 * @param zipName the name of the zip file created
	 * @param sourceFolder the name of the folder to zip
	 * @return void
	 * @throws IOException
	 */
	public void zipIt(String zipName, String sourceFolder){
		logger.debug("Entering zipIt...");
		try {			
			// create byte buffer
			byte[] buffer = new byte[1024];
			FileOutputStream fos = new FileOutputStream(zipName);
			ZipOutputStream zos = new ZipOutputStream(fos);
			File dir = new File(sourceFolder);
			File[] files = dir.listFiles();

			for (int i = 0; i < files.length; i++) {
				
				logger.debug("Adding file: " + files[i].getName());
				FileInputStream fis = new FileInputStream(files[i]);
				// begin writing a new ZIP entry, positions the stream to the start of the entry data
				zos.putNextEntry(new ZipEntry(files[i].getName()));
				int length;
				while ((length = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, length);
				}
				zos.closeEntry();
				// close the InputStream
				fis.close();
			}
			// close the ZipOutputStream
			zos.close();	
			logger.debug("Leaving zipIt...");
		}
		catch (IOException e) {
			logger.error("Error creating zip file" + e);
			e.printStackTrace();
		}	
	}
	
	/**
	 * Initiates delete of folder
	 * 
	 * @param s the folder to delete
	 * @return void
	 * @throws IOException
	 */
	public void deleteDirectory(String s) throws IOException{
		logger.debug("Entering deleteFolder...");
		File directory = new File(s);
		if(!directory.exists()){
			logger.warn("Directory does not exist to delete!");
		}
		else{
			try{
				recursiveDelete(directory);
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}
		logger.debug("Leaving deleteFolder...");
	}
	
	/**
	 * Recursively deletes folder structure
	 * 
	 * @param f the file to delete
	 * @return void
	 * @throws IOException
	 */
	public void recursiveDelete(File f) throws IOException{
		logger.debug("Entering recursive delete...");
		if(f.isDirectory()){
			if(f.list().length == 0){
				f.delete();
			}
			else{
				String[] fs = f.list();
				for(String s : fs){
					File toDelete = new File(f, s);
					recursiveDelete(toDelete);
				}
				if(f.list().length == 0){
					f.delete();
				}
			}
		}
		else{
			f.delete();
		}
		logger.debug("Exiting recursive delete...");
	}
	
	/**
	 * Removes a TaskResult object from the spt database after archiving
	 * 
	 * @param tr TaskResult object to be removed
	 * @param context the current SailPointContext
	 * @return void
	 * @throws GeneralException
	 */
	public void deleteTaskResult(TaskResult tr, SailPointContext context) throws GeneralException{
		logger.debug("Entering deleteTaskResult...");
		Terminator t1000 = new Terminator(context);
		try{
			t1000.deleteObject(tr);
			prunedCount++;
		}catch(Exception e){
			logger.error("Error occured in deleleteTaskResult!");
			e.printStackTrace();
		}
		logger.debug("TaskResult: " + tr.getName() + " terminated.");
	}

}
