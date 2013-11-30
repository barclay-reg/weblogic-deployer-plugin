/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.deployer;

/**
 * @author rchaumie
 *
 */
public enum WebLogicCommand {

	DEPLOY("deploy"),
	UNDEPLOY("undeploy");
	
	private String value;
	
	private WebLogicCommand(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
