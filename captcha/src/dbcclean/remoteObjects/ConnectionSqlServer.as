package dbcclean.remoteObjects
{
	[RemoteClass(alias="dbclean.model.ConnectionSqlServer")]
	[Bindable]
	public class ConnectionSqlServer
	{
//		private Long id;
//		private String connectionName;
//		private String databaseUserName;
//		private String password;
//		private String host;
//		private String port;
//		private String login;
		
		public var id:Number;
		public var connectionName:String;
		public var databaseUserName:String;
		public var password:String;
		public var host:String;
		public var port:String;
		public var dataBaseName:String;
		public var login:String;
		
		public function ConnectionSqlServer()
		{
		}
	}
}