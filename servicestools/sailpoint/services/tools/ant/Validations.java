package sailpoint.services.tools.ant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Rohit Pant
 * This class has the code to validate missing tokens from SSD and report it. If any token is missing this will fail the build.
 * 
 * @args  working directory path of SSD
 */

public class Validations {
	
	private int errorCount = 0;
	private String ssdDirectory = null, targetPropertiesName = null;
	private HashMap<String,String> variablesAndValuesMap = new HashMap<String,String>();
	private List<File> changesRequiredList = new ArrayList<File>();
	
	public static void main(String[] args) throws Exception {
		if(args.length < 2) {
			System.err.println("Please provide the working directory path and target properties filename in arguments...");
			throw new Exception("Wrong number of arguments!!");
		}
		Validations validate = new Validations();
		// setting the SSD directory path
		validate.setSSDDirectory(args[0]);
		// setting the Target Properties name
		validate.setTargetPropertiesName(args[1]);
		// retrieving all the XML file objects
		List<File> xmlFiles = validate.getAllXmlFiles(validate.getSSDDirectory() + "/build/extract/WEB-INF/config/custom");
		// validating the XML files for missing tokens
		boolean validation = validate.validateXmlFiles(xmlFiles);
		if(validation) {
			// updating the tokens in XML files to fix all missing tokens
			List<Boolean> updateStatus = validate.updateTokensInXmlFiles();
			// checking if update of any file failed or still there's some missing tokens present
			if(updateStatus.contains(false)) {
				System.err.println("Unable to resolve all error(s)...");
				// if error is found the create a lock file in base directory for ant script to detect the failure
				validate.createLockFile(validate.getSSDDirectory());
			} else
				System.out.println("XML validation successful, proceeding further: Resolved " + validate.errorCount + " error(s)");
		} else {
			System.err.println("There are invalid token(s) in some XML file(s)...");
			System.err.println("Please check error(s) above to find XML line numbers for invalid token(s)...");
			System.err.println("Invalid token(s) needs to be fixed manually in order to proceed...");
			// if error is found the create a lock file in base directory for ant script to detect the failure
			validate.createLockFile(validate.getSSDDirectory());
		}
	}
	
	/**
	Method to get all subdirectories within directory
	@param File object of the base directory
	@return List having File objects of all subdirectories
	*/
	private List<File> getSubdirs(File file) {
	    List<File> subdirs = Arrays.asList(file.listFiles(new FileFilter() {
	        public boolean accept(File f) {
	            return f.isDirectory();
	        }
	    }));
	    subdirs = new ArrayList<File>(subdirs);

		// searching for all the directories recursively
	    List<File> deepSubdirs = new ArrayList<File>();
	    for(File subdir : subdirs) {
	        deepSubdirs.addAll(getSubdirs(subdir)); 
	    }
	    subdirs.addAll(deepSubdirs);
	    return subdirs;
	}
	
	/**
	Method to get all XML file objects
	@param String base directory path containing the XML files
	@return List having File objects of all XML files
	*/
	private List<File> getAllXmlFiles(String xmlsDirPath) {
		File file = new File(xmlsDirPath);
		List<File> subDirs = getSubdirs(file);
		List<File> xmlFilesList = new ArrayList<File>();
		for(File sub : subDirs) {
			// adding custom file filter to retrieve XML file objects
			File[] xmlFiles = sub.listFiles(new FileFilter() {
				@Override
				public boolean accept(File arg0) {
					if(arg0.getName().toLowerCase().endsWith(".xml"))
						return true;
					return false;
				}
			});
			if(xmlFiles != null && xmlFiles.length > 0) {
				xmlFilesList.addAll(Arrays.asList(xmlFiles));
			}
		}
		return xmlFilesList;
	}
	
	/**
	Method to load target properties file
	@return Properties object for the target file
	*/
	private Properties loadTargetFile() {
		Properties props = new Properties();
		String propsFilePath = getSSDDirectory() + "/" + getTargetPropertiesName();
		try {
			props.load(propsFilePath);
		} catch(IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		return props;
	}
	
	/**
	Method to update target properties file with missing token values
	@param Properties object for the target file
	*/
	private void updatePropertiesFile(Properties props) {
		File target = new File(getSSDDirectory() + "/" + getTargetPropertiesName());
		try {
			OutputStream out = new FileOutputStream(target);
			props.store(out, "target.properties file updated with values for missing tokens");
			System.out.println("Target properties updated successfully...");
		} catch(FileNotFoundException fne) {
			System.err.println("Target properties file not found...");
		} catch(IOException ioe) {
			System.err.println("Unable to update target properties file...");
		}
	}
	
	/**
	Method to validate XML files for missing tokens
	@param List of File objects containing all the XML files
	*/
	private boolean validateXmlFiles(List<File> xmlFiles) {
		BufferedReader br = null;
		String currentLine = null, tokenName = null;
		int lineNumber = 1;
		Properties props = loadTargetFile();
		BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
		boolean validation = true;
		for(File xml : xmlFiles) {
			try {
				lineNumber = 1;
				System.out.println("Validating file : " + xml.getAbsolutePath());
				br = new BufferedReader(new FileReader(xml.getAbsolutePath()));
				while((currentLine = br.readLine()) != null) {
					// searching for tokens in each line
					if(currentLine.contains("%%")) {
						// if token found then retrieving the token name
						tokenName = getTokenFromXmlLine(currentLine);
						if(tokenName != null) {
							for(String token : tokenName.split(",")) {  // added loop to fix multiple tokens in same line - [SSDBUGS-79]
								if(isValidToken(token)) {
									System.err.println("XML Line : " + lineNumber + ": Token " + token + " not present in target.properties file...");
									// add token to the map if it doesn't exist already
									if(!variablesAndValuesMap.containsKey(token)) {
										// user needs to enter the value for missing token here
										System.out.println("Enter value for " + token + ": ");
										variablesAndValuesMap.put(token, consoleInput.readLine());
										props.setProperty(token, variablesAndValuesMap.get(token));
										changesRequiredList.add(xml);
									}
								} else {
									System.err.println("XML Line : " + lineNumber + ": Invalid token " + token + " found in XML file...");
									validation = false;
								}
							}
						} else {
							System.err.println("XML Line : " + lineNumber + ": Invalid token found in XML file, token name couldn't be retrieved...");
							validation = false;
						}
						// adding count for the total number of errors found
						errorCount ++;
					}
					lineNumber ++;
				}
			} catch(FileNotFoundException fne) {
				System.err.println(fne.getMessage());
			} catch(IOException ioe) {
				System.err.println(ioe.getMessage());
			}
		}
		if(variablesAndValuesMap.size() > 0 && validation)
			updatePropertiesFile(props);
		return validation;
	}
	
	/**
	Method to update the token values in XML files
	@return List of Boolean objects containing all success status
	*/
	private List<Boolean> updateTokensInXmlFiles() {
		List<Boolean> success = new ArrayList<Boolean>();
		if(variablesAndValuesMap.size() > 0 && changesRequiredList.size() > 0) {
			List<String> newLines = new ArrayList<String>();
			List<String> oldLines = null;
			String tokenName;
			for(File file : changesRequiredList) {
				try {
					oldLines = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);
					for(String line : oldLines) {
						if(line.contains("%%")) {
							tokenName = getTokenFromXmlLine(line);
							if(tokenName != null) {
								for(String token : tokenName.split(",")) {   // added the loop to fix multiple tokens in same line - [SSDBUGS-79]
									if(variablesAndValuesMap.containsKey(token))
										line = line.replace(token, variablesAndValuesMap.get(token));
									else
										success.add(false);
								}
							} else 
								success.add(false);
						}
						newLines.add(line);
					}
					Files.write(Paths.get(file.getAbsolutePath()), newLines, StandardCharsets.UTF_8, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
					newLines.clear();
					success.add(true);
				} catch(IOException ioe) {
					System.err.println("Unable to update XML file: " + file.getAbsolutePath());
					success.add(false);
				}
			}
		}
		return success;
	}
	
	/**
	Method to retrieve token name from the XML file
	@param String content of XML file per line
	@return String tokenName
	*/
	protected String getTokenFromXmlLine(String xmlLine) {
		String tokens = null;
		if(xmlLine != null && !xmlLine.isEmpty()) {
			int startIndex = -1, endIndex = -1, begin = 0;
			do {
				startIndex = xmlLine.indexOf("%%", begin);
				endIndex = xmlLine.indexOf("%%", startIndex + 2);
				if(startIndex > -1 && endIndex > -1){
					String token = xmlLine.substring(startIndex, endIndex + 2);
					begin = endIndex + 2;
					if(token.length() > 4) {
						if(tokens == null)
							tokens = "";
						if(tokens.isEmpty())
							tokens = token;
						else
							tokens += "," + token;
					}
				}
			} while(startIndex > -1 && endIndex > -1);  // added loop for multiple tokens in single line - [fix for SSDBUGS-79]
		}
		return tokens;
	}
	
	/**
	Method to verify if token is valid - [fix for SSDBUGS-62]
	@param String content of XML file per line
	@return String tokenName
	*/
	private boolean isValidToken(String token) {
		if(token !=null && !token.isEmpty() && token.startsWith("%%") && token.endsWith("%%") && token.length() > 4) {
			String substring = token.substring(2, token.length() - 2);
			if(substring.contains("%"))
				return false;
			else
				return true;
		} else
			return false;
	}
	
	private void setSSDDirectory(String ssdDirectory) {
		this.ssdDirectory = ssdDirectory;
	}
	
	private String getSSDDirectory() {
		return this.ssdDirectory;
	}
	
	private void setTargetPropertiesName(String targetPropertiesName) {
		this.targetPropertiesName = targetPropertiesName;
	}
	
	private String getTargetPropertiesName() {
		return this.targetPropertiesName;
	}
	
	/**
	Method to create lock file for ant script to detect failure
	@param String SSD base directory path
	*/
	private void createLockFile(String baseDir) {
		File lockFile = new File(baseDir + "/.lock");
		try {
			lockFile.createNewFile();
		} catch(IOException ioe) {
			System.err.println(ioe.getMessage());
		}
	}
	
	
	/**
	 * @author Rohit.Pant
	 * The purpose of creating this custom Properties class is to retain the existing comments 
	 * as well while updating the target properties file programmatically*/
	static class Properties extends Hashtable<Object,Object> {
		
		private static final long serialVersionUID = 4112578634029874840L;
		
		protected Properties defaults;
		private Map<String, String> lineNumbers = new HashMap<String,String>();
		private static int lineNo = 1;
		
		private static final char[] hexDigit = {
				'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
		};
		
		public Properties() {
			this(null);
		}
		
		public Properties(Properties defaults) {
			this.defaults = defaults;
		}
		
		public synchronized Object setProperty(String key, String value) {
			if(get(key) == null) {
				lineNumbers.put(String.valueOf(lineNo), key);
				lineNo ++;
			}
			return put(key, value);
		}

		public synchronized void load(String filePath) throws IOException {
			load0(new BufferedReader(new FileReader(filePath)));
		}
		
		private void load0(BufferedReader br) throws IOException {
			String line = null, tempLine = null;
			int limit, keyLen, valueStart;
			char c;
			boolean hasSep, precedingBackslash, append = false;
			
			// code below had been modified to handle multi-line token values - [SSDBUGS-80]
			while((tempLine = br.readLine()) != null) {
				if(tempLine.startsWith("#")) {
					lineNumbers.put(String.valueOf(lineNo), tempLine);
					lineNo ++;
				} else {
					if(line == null || !append)
						line = "";
					if(tempLine.length() > 1 && tempLine.charAt(tempLine.length() - 1) == '\\') {
						line += tempLine;
						append = true;
						continue;
					} else if(append){
						line += tempLine;
						append = false;
					} else {
						line = tempLine;
						append = false;
					}
					char[] convBuf = new char[1024];
					c = 0;
					keyLen = 0;
					limit = line.length();
					valueStart = limit;
					hasSep = false;
					
					precedingBackslash = false;
					while(keyLen < limit) {
						c = line.charAt(keyLen);
						if((c == '=' || c == ':') && !precedingBackslash) {
							valueStart = keyLen + 1;
							break;
						} else if((c == ' ' || c == '\t' || c == '\f') && !precedingBackslash) {
							valueStart = keyLen +1;
							break;
						}
						if(c == '\\') {
							precedingBackslash = !precedingBackslash;
						} else {
							precedingBackslash = false;
						}
						keyLen ++;
					}
					while(valueStart < limit) {
						c = line.charAt(valueStart);
						if(c != ' ' && c != '\t' && c != '\f') {
							if(!hasSep && (c == '=' || c == ':')) {
								hasSep = true;
							} else {
								break;
							}
						}
						valueStart ++;
					}
					String key = loadConvert(line.toCharArray(), 0, keyLen, convBuf, true);
					String value = loadConvert(line.toCharArray(), valueStart, limit - valueStart, convBuf, false);
					put(key, value);
					lineNumbers.put(String.valueOf(lineNo), key);
					lineNo ++;
				}
			}
		}
		
		// loadConvert method had been modified to handle multi-line token values - [SSDBUGS-80]
		private String loadConvert(char[] in, int off, int len, char[] convBuf, boolean isKey) {
			if(convBuf.length < len) {
				int newLen = len * 2;
				if(newLen < 0) {
					newLen = Integer.MAX_VALUE;
				}
				convBuf = new char[newLen];
			}
			char aChar;
			char[] out = convBuf;
			int outLen = 0;
			int end = off + len;
			
			while(off < end) {
				aChar = in[off++];
				if(aChar == '\\') {
					if((off+1) < end) {
						aChar = in[off++];
						if(isKey) {
							if(aChar == 'u') {
								int value = 0;
								for(int i=0; i < 4; i ++) {
									aChar = in[off++];
									switch(aChar) {
									case '0' : case '1' : case '2' : case '3' : case '4' : case '5' : case '6' : case '7' : case '8' : case '9':
										value = (value << 4) + aChar - '0';
										break;
									case 'a' : case 'b' : case 'c' : case 'd' : case 'e' : case 'f' :
										value = (value << 4) + 10 + aChar - 'a';
										break;
									case 'A' : case 'B' : case 'C' : case 'D' : case 'E' : case 'F' :
										value = (value << 4) + 10 + aChar - 'A';
										break;
									default:
										throw new IllegalArgumentException("Malformed \\uxxxxx encodeing.");	
									}
								}
								out[outLen++] = (char) value;
							} else {
								if(aChar == 't')  aChar = '\t';
								if(aChar == 'r') aChar = '\r';
								if(aChar == 'n') aChar = '\n';
								if(aChar == 'f') aChar = '\f';
								out[outLen++] = aChar;
							}
						} else 
							out[outLen++] = aChar;
					}
				} else {
					out[outLen++] = aChar;
				}
			}
			return new String(out, 0, outLen);
		}
		
		public void store(OutputStream out, String comments) throws IOException {
			store0(new BufferedWriter(new OutputStreamWriter(out)), comments, true);
		}
		
		private void store0(BufferedWriter bw, String comments, boolean escUnicode) throws IOException {
			synchronized (this) {
				int size = lineNumbers.size();
				String line;
				boolean commentsAdded = false;
				for(int i=1; i<=size; i++) {
					line = lineNumbers.get(String.valueOf(i));
					if(i==1 && line.startsWith("#") && comments != null && comments.length() > 0 && line.contains(comments)) {
						bw.write("# " + comments);
						bw.newLine();
						bw.write("# " + new Date().toString());
						bw.newLine();
						i++;
						commentsAdded = true;
					} else if(comments != null && comments.length() > 0 && !line.contains(comments) && !commentsAdded) {
						bw.write("# " + comments);
						bw.newLine();
						bw.write("# " + new Date().toString());
						bw.newLine();
						commentsAdded = true;
						// fix for bug [SSDBUGS-86] - begin
						if(line.startsWith("#")) {
							bw.write(line);
							bw.newLine();
						} else {
							String key = line;
							String value = (String) get(key);
							key = saveConvert(key, true, escUnicode);
							value = saveConvert(value, false, escUnicode);
							if(key != null && key.length() > 0) {
								bw.write(key + "=" + value);
								bw.newLine();
							}
						}
						// fix for bug [SSDBUGS-86] - end
					} else if(line.startsWith("#")) {
						bw.write(line);
						bw.newLine();
					} else {
						String key = line;
						String value = (String) get(key);
						key = saveConvert(key, true, escUnicode);
						value = saveConvert(value, false, escUnicode);
						if(key != null && key.length() > 0) {
							bw.write(key + "=" + value);
							bw.newLine();
						}
					}
				}
			}
			bw.flush();
		}
		
		private String saveConvert(String theString, boolean escapeSpace, boolean escapeUnicode) {
			int len = theString.length();
			int bufLen = len * 2;
			if(bufLen < 0) {
				bufLen = Integer.MAX_VALUE;
			}
			StringBuffer outBuffer = new StringBuffer(bufLen);
			
			for(int x=0; x<len; x++) {
				char aChar = theString.charAt(x);
				if((aChar > 61) && (aChar < 127)) {
					if(aChar == '\\') {
						outBuffer.append('\\'); outBuffer.append('\\');
						continue;
					}
					outBuffer.append(aChar);
					continue;
				}
				switch(aChar) {
				case ' ':
					if(x==0 || escapeSpace)
						outBuffer.append('\\');
					outBuffer.append(' ');
					break;
				case '\t':
					outBuffer.append('\\'); outBuffer.append('t');
					break;
				case '\n':
					outBuffer.append('\\'); outBuffer.append('n');
					break;
				case '\r':
					outBuffer.append('\\'); outBuffer.append('r');
					break;
				case '\f':
					outBuffer.append('\\'); outBuffer.append('f');
					break;
				case '=':  // fall through
				case ':':  // fall through
				case '#':  // fall through
				case '!':
					outBuffer.append("\\");  outBuffer.append(aChar);
					break;
				default:
					if(((aChar < 0x0020) || (aChar > 0x007e)) && escapeUnicode) {
						outBuffer.append('\\');
						outBuffer.append('u');
						outBuffer.append(toHex((aChar >> 12) & 0xF));
						outBuffer.append(toHex((aChar >> 8)  & 0xF));
						outBuffer.append(toHex((aChar >> 4)  & 0xF));
						outBuffer.append(toHex( aChar        & 0xF));
					} else {
						outBuffer.append(aChar);
					}
				}
			}
			return outBuffer.toString();
		}
		
		private static char toHex(int nibble) {
			return hexDigit[(nibble & 0xF)];
		}
	}
}