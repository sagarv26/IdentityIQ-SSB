package sailpoint.services.standard.task;

import java.util.Date;

import sailpoint.api.SailPointContext;
import sailpoint.object.Attributes;
import sailpoint.object.TaskResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReportExtractorSettings {
	
	private static final Log logger = LogFactory.getLog(ReportExtractorSettings.class);
	
	
	public String getConfigBackupAndPruneDate(){
		return config_backup_and_prune_before_date;
	}
	
	public String getConfigPathToFolder(){
		return config_path_to_backup_folder;
	}
	
	public Boolean getConfigZipResult(){
		return config_zip_result;
	}
	
	public Boolean getConfigProcessDeletes(){
		return config_process_deletes;
	}
	
	private String config_backup_and_prune_before_date = null;
	private String config_path_to_backup_folder = null;
	private Boolean config_zip_result = false;
	private Boolean config_process_deletes = false;
	private TaskResult taskResult = null;
	private SailPointContext context = null;
	private Attributes<String, Object> attrs = null;
	
	public ReportExtractorSettings(SailPointContext context, Attributes<String, Object> attrs, TaskResult taskResult) throws Exception{
		this.context = context;
		this.taskResult = taskResult;
		this.attrs = attrs;
		initSettings(attrs);
		validateSettings();
		
	}

	//TODO do any of these current settings need validation?
	private void validateSettings() throws Exception{
		
	}
	
	private void initSettings(Attributes<String, Object> attributes) throws Exception{
		config_backup_and_prune_before_date = getConfigString(attributes, "prune_before_date");
		config_path_to_backup_folder = getConfigString(attributes, "path_to_backup");
		config_zip_result = getConfigBoolean(attributes, "zip_result");
		config_process_deletes = getConfigBoolean(attributes, "process_deletes");
		
	}
	
	private Boolean getConfigBoolean(Attributes<String, Object> attributes, String key){
		Boolean configValue = attributes.getBoolean(key);
		if(null == configValue){
			logger.warn("configuration setting for key: " + key + " returned a null value!");
		}
		return configValue;
	}
	
	private String getConfigString(Attributes<String, Object> attributes, String key){
		String configValue = attributes.getString(key);
		if(null == configValue){
			logger.warn("configuration setting for key: " + key + " returned a null value!");
		}
		return configValue;
	}
	private Integer getConfigInteger(Attributes<String, Object> attributes, String key){
		Integer configValue = attributes.getInt(key);
		if(null == configValue){
			logger.warn("configuration setting for key: " + key + " returned a null value!");
		}
		return configValue;
	}
	private Date getConfigDate(Attributes<String, Object> attributes, String key){
		Date configValue = attributes.getDate(key);
		if(null == configValue){
			logger.warn("configuration setting for key: " + key + " returned a null value!");
		}
		return configValue;
	}
}
