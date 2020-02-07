package gov.usgs.wma.waterdata;

import java.io.IOException;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PreProcess implements Function<RequestObject, ResultObject> {

	private JsonDataDao jsonDataDao;

	@Autowired
	public PreProcess(JsonDataDao jsonDataDao) {
		this.jsonDataDao = jsonDataDao;
	}

	@Override
	public ResultObject apply(RequestObject requestObject) {
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
