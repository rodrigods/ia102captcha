package dbclean.model;

import java.io.Serializable;

/**
 * Encapsulates some information that is required to make a connection to a
 * database
 */
public class Connection implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String connectionName;
	private String databaseUserName;
	private String password;
	private String host;
	private String port;
	private String login;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the connectionName
	 */
	public String getConnectionName() {
		return connectionName;
	}
	/**
	 * @param connectionName the connectionName to set
	 */
	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}
	/**
	 * @return the databaseUserName
	 */
	public String getDatabaseUserName() {
		return databaseUserName;
	}
	/**
	 * @param databaseUserName the databaseUserName to set
	 */
	public void setDatabaseUserName(String databaseUserName) {
		this.databaseUserName = databaseUserName;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

}
