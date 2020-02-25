package gov.usgs.wma.waterdata;

import java.util.ArrayList;
import java.util.List;

public class ResultObject {
	private List<TimeSeries> timeSeries;
	public List<TimeSeries> getTimeSeries() {
		return null != timeSeries ? timeSeries : new ArrayList<TimeSeries>();
	}
	public void setTimeSeries(List<TimeSeries> timeSeries) {
		this.timeSeries = timeSeries;
	}
}
