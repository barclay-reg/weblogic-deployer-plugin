/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.jenkinsci.plugins.deploy.weblogic.data.TransfertConfiguration;
import org.jenkinsci.plugins.deploy.weblogic.exception.TransfertFileException;


/**
 * @author rchaumie
 *
 */
public class FTPUtils {
	
	
	public static final void transfertFile(TransfertConfiguration transfertConfiguration, PrintStream log) throws TransfertFileException {
		
		FTPClient ftpClient = new FTPClient();
    	FileInputStream localFileToTransfert = null;
    	
		try {
        	int reply;
        	ftpClient.setConnectTimeout(20000); // set timeout to 20s
        	ftpClient.connect(transfertConfiguration.getHost());
        	
        	reply = ftpClient.getReplyCode();
        	
        	// After connection attempt, you should check the reply code to verify success.
            if(!FTPReply.isPositiveCompletion(reply)) {
            	throw new TransfertFileException("FTP SERVER REFUSED CONNECTION. ERROR CODE = " + reply);
            }
            
            
            // attempt login
            if (!ftpClient.login(transfertConfiguration.getUser(), transfertConfiguration.getPassword())) {
                throw new TransfertFileException("Failed to login to FTP");
            }
            
            if(!ftpClient.setFileType(FTP.BINARY_FILE_TYPE)) {
        		throw new TransfertFileException("Unable to set the file type to BINARY");
        	}
            
            // create local result file object
            localFileToTransfert = new FileInputStream(transfertConfiguration.getLocalFilePath());

            // store local file to remote server
            if(! ftpClient.storeFile(transfertConfiguration.getRemoteFilePath(), localFileToTransfert)) {
            	throw new TransfertFileException("Unable to transfert file " +transfertConfiguration.getLocalFilePath()+ " on " + transfertConfiguration.getHost());
            }
        	ftpClient.disconnect();
    	} catch (Exception e) {
    		throw new TransfertFileException("Failed to download file completely", e);
    	} finally {
    		IOUtils.closeQuietly(localFileToTransfert);
    		if(ftpClient.isConnected()) {
    	        try {
    	        	ftpClient.disconnect();
    	        } catch(IOException ioe) {
    	          // do nothing
    	        }
    	      }
    	}
	}

}
