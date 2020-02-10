package gov.usgs.wma.waterdata;

import java.io.IOException;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PreProcess implements Function<RequestObject, ResultObject> {
	private static final Logger LOG = LoggerFactory.getLogger(PreProcess.class);

	private JsonDataDao jsonDataDao;

	@Autowired
	public PreProcess(JsonDataDao jsonDataDao) {
		this.jsonDataDao = jsonDataDao;
	}

	@Override
	public ResultObject apply(RequestObject requestObject) {
		LOG.debug("json_data_id: " + requestObject.getId());
		ResultObject resultObject = new ResultObject();
		try {
			resultObject.setUniqueId(jsonDataDao.getUniqueId(requestObject.getId()));
			jsonDataDao.doApprovals(requestObject.getId());
			jsonDataDao.doGapTolerances(requestObject.getId());
			jsonDataDao.doGrades(requestObject.getId());
			jsonDataDao.doInterpolationTypes(requestObject.getId());
			jsonDataDao.doMethods(requestObject.getId());
			jsonDataDao.doPoints(requestObject.getId());
		} catch (IOException e) {
			
		}
		return resultObject;
	}

}
