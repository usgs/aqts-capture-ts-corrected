package gov.usgs.wma.waterdata;

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

	@Transactional
	protected ResultObject processRequest(RequestObject request) {
		Long jsonDataId = request.getId();
		Integer partitionNumber = request.getPartitionNumber();
		LOG.debug("json_data_id: {}, partition number: {}", jsonDataId, partitionNumber);
		ResultObject result = new ResultObject();
		jsonDataDao.doHeaderInfo(jsonDataId, partitionNumber);

		TimeSeries timeSeries = jsonDataDao.getRouting(jsonDataId, partitionNumber);
		// getRouting throws a runtime error if the time series description is not available. 
		//   That way the state machine will error and this data will get reprocessed after the 
		//   description is available. (Any data updates will also be rolled back.)
		// Otherwise check to see if the data is a type which we currently process.
		if (null != timeSeries.getDataType()) {
			//If it is, process the remaining data and pass on the pertinent information.
			result.setTimeSeries(timeSeries);
			jsonDataDao.doApprovals(jsonDataId, partitionNumber);
			jsonDataDao.doGapTolerances(jsonDataId, partitionNumber);
			jsonDataDao.doGrades(jsonDataId, partitionNumber);
			jsonDataDao.doInterpolationTypes(jsonDataId, partitionNumber);
			jsonDataDao.doMethods(jsonDataId, partitionNumber);
			jsonDataDao.doPoints(jsonDataId, partitionNumber);
			jsonDataDao.doQualifiers(jsonDataId, partitionNumber);
		}

		return result;
	}
}
