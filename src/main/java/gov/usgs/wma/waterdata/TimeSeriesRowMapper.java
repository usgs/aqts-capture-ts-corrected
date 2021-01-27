package gov.usgs.wma.waterdata;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TimeSeriesRowMapper implements RowMapper<TimeSeries> {

	@Override
	public TimeSeries mapRow(ResultSet rs, int rowNum) throws SQLException {
		TimeSeries timeSeries = new TimeSeries();
		timeSeries.setUniqueId(rs.getString("time_series_unique_id"));
		timeSeries.setDataType(rs.getString("data_type"));
		timeSeries.setComputationIdentifier(rs.getString("computation_identifier"));
		return timeSeries;
	}

}
