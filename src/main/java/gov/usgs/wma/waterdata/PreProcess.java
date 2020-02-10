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
		LOG.info("json_data_id: {}", requestObject.getId());
		ResultObject resultObject = new ResultObject();
		try {
			resultObject.setUniqueId(jsonDataDao.getUniqueId(requestObject.getId()));
			LOG.info("guid: {}", resultObject.getUniqueId());
			LOG.info("Before Approvals");
			jsonDataDao.doApprovals(requestObject.getId());
			LOG.info("After Approvals");
			LOG.info("Before GapTolerances");
			jsonDataDao.doGapTolerances(requestObject.getId());
			LOG.info("After GapTolerances");
			LOG.info("Before Grades");
			jsonDataDao.doGrades(requestObject.getId());
			LOG.info("After Grades");
			LOG.info("Before InterpolationTypes");
			jsonDataDao.doInterpolationTypes(requestObject.getId());
			LOG.info("After InterpolationTypes");
			LOG.info("Before Methods");
			jsonDataDao.doMethods(requestObject.getId());
			LOG.info("After Methods");
			LOG.info("Before Points");
			jsonDataDao.doPoints(requestObject.getId());
			LOG.info("After Points");
		} catch (IOException e) {
			LOG.error(e.getLocalizedMessage());
		}
		return resultObject;
	}

}
