package gov.usgs.wma.waterdata;

import java.util.Arrays;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PreProcess implements Function<RequestObject, ResultObject> {
	private static final Logger LOG = LoggerFactory.getLogger(PreProcess.class);

	private JsonDataDao jsonDataDao;

	@Autowired
	public PreProcess(JsonDataDao jsonDataDao) {
		this.jsonDataDao = jsonDataDao;
	}

	@Override
	@Transactional
	public ResultObject apply(RequestObject request) {
		return processRequest(request);
	}

	protected ResultObject processRequest(RequestObject request) {
		LOG.debug("json_data_id: {}", request.getId());
		ResultObject result = new ResultObject();

		String timeSeriesUniqueId = jsonDataDao.doHeaderInfo(request.getId());
		if (null == timeSeriesUniqueId) {
			// We can do nothing further if the data doesn't have a time series unique id
			return result;
		}

		TimeSeries timeSeries = jsonDataDao.getRouting(timeSeriesUniqueId);
		// getRouting throws a runtime error if the time series description is not available. 
		//   That way the state machine will error and this data will get reprocessed after the 
		//   description is available. (Any data updates will also be rolled back.)
		// Otherwise check to see if the data is a type which we currently process.
		if (null != timeSeries.getDataType()) {
			//If it is, process the remaining data and pass on the pertinent information.
			result.setTimeSeriesList(Arrays.asList(timeSeries));
			jsonDataDao.doApprovals(request.getId());
			jsonDataDao.doGapTolerances(request.getId());
			jsonDataDao.doGrades(request.getId());
			jsonDataDao.doInterpolationTypes(request.getId());
			jsonDataDao.doMethods(request.getId());
			jsonDataDao.doPoints(request.getId());
			jsonDataDao.doQualifiers(request.getId());
		}

		return result;
	}
}
