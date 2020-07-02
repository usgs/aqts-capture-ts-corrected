package gov.usgs.wma.waterdata;


public class ResultObject {
	private TimeSeries timeSeries;
	public TimeSeries getTimeSeries() {
		return null != timeSeries ? timeSeries : new TimeSeries();
	}
	public void setTimeSeries(TimeSeries timeSeries) {
		this.timeSeries = timeSeries;
	}
}
