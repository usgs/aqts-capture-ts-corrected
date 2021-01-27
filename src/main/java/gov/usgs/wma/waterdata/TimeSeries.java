package gov.usgs.wma.waterdata;

public class TimeSeries {
	private String uniqueId;
	private String dataType;
	private String computationIdentifier;
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getComputationIdentifier() {
		return computationIdentifier;
	}
	public void setComputationIdentifier(String computationIdentifier) {
		this.computationIdentifier = computationIdentifier;
	}
}
