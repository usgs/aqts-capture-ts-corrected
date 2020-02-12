package gov.usgs.wma.waterdata;

import java.io.IOException;
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
	public ResultObject apply(RequestObject requestObject) {
		LOG.debug("json_data_id: {}", requestObject.getId());
		ResultObject resultObject = new ResultObject();
		try {
			resultObject.setUniqueId(jsonDataDao.getUniqueId(requestObject.getId()));
			LOG.debug("guid: {}", resultObject.getUniqueId());
			LOG.debug("Before Approvals");
			jsonDataDao.doApprovals(requestObject.getId());
			LOG.debug("After Approvals");
			LOG.debug("Before GapTolerances");
			jsonDataDao.doGapTolerances(requestObject.getId());
			LOG.debug("After GapTolerances");
			LOG.debug("Before Grades");
			jsonDataDao.doGrades(requestObject.getId());
			LOG.debug("After Grades");
			LOG.debug("Before InterpolationTypes");
			jsonDataDao.doInterpolationTypes(requestObject.getId());
			LOG.debug("After InterpolationTypes");
			LOG.debug("Before Methods");
			jsonDataDao.doMethods(requestObject.getId());
			LOG.debug("After Methods");
			LOG.debug("Before Points");
			jsonDataDao.doPoints(requestObject.getId());
			LOG.debug("After Points");
		} catch (IOException e) {
			LOG.error(e.getLocalizedMessage());
		}
		return resultObject;
	}

}
