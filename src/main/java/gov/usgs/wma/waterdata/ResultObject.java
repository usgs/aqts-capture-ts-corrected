package gov.usgs.wma.waterdata;

public class ResultObject {
	private String uniqueId;
	private Long jsonDataId;
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public Long getJsonDataId() {
		return jsonDataId;
	}
	public void setJsonDataId(Long jsonDataId) {
		this.jsonDataId = jsonDataId;
	}
}
