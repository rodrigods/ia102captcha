package dbcclean.remoteObjects
{
	[RemoteClass(alias="dbclean.model.Column")]
	[Bindable]
	public class Column
	{
//		private String columnName;
//		
//		private String dataType;
		public var columnName: String;
		public var dataType: String;
		
		public function Column()
		{
		}
	}
}