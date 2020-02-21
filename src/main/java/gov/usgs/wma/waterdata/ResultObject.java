package gov.usgs.wma.waterdata;

import java.util.ArrayList;
import java.util.List;

public class ResultObject {
	private List<TimeSeries> timesSeriesList;
	public List<TimeSeries> getTimesSeriesList() {
		return null != timesSeriesList ? timesSeriesList : new ArrayList<TimeSeries>();
	}
	public void setTimesSeriesList(List<TimeSeries> timesSeriesList) {
		this.timesSeriesList = timesSeriesList;
	}
}
