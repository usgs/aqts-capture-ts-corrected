package gov.usgs.wma.waterdata;

public class TimeSeriesData {
	private String uniqueId;
	private Long contentSize;
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public Long getContentSize() {
		return contentSize;
	}
	public void setContentSize(Long contentSize) {
		this.contentSize = contentSize;
	}
}
