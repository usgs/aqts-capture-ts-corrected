package gov.usgs.wma.waterdata;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultObject {
	@JsonProperty("timeSeries")
	private List<TimeSeries> timeSeriesList;
	public List<TimeSeries> getTimeSeriesList() {
		return null != timeSeriesList ? timeSeriesList : new ArrayList<TimeSeries>();
	}
	public void setTimeSeriesList(List<TimeSeries> timeSeriesList) {
		this.timeSeriesList = timeSeriesList;
	}
}
