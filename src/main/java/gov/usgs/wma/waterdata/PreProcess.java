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

		TimeSeries timeSeries = jsonDataDao.doHeaderInfo(request.getId());
		if (null != timeSeries) {
			result.setTimeSeries(Arrays.asList(timeSeries));
			jsonDataDao.doApprovals(request.getId());
			jsonDataDao.doGapTolerances(request.getId());
			jsonDataDao.doGrades(request.getId());
			jsonDataDao.doInterpolationTypes(request.getId());
			jsonDataDao.doMethods(request.getId());
			jsonDataDao.doPoints(request.getId());
		}

		return result;
	}

}
