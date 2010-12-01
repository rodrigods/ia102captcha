package utils;

public class RestrictionUtils {
	public static String getLeftInstanceRestrictionTableName(Long id){
		return "restriction_l_" + id.toString();
	}
	
	public static String getRightInstanceRestrictionTableName(Long id){
		return "restriction_r_" + id.toString();
	}
	
	public static String getInstanceRestrictionTableName(Long id){
		return "restriction_" + id.toString();
	}
}
