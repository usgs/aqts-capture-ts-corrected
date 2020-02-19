package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment=WebEnvironment.NONE)
public class PreProcessTest {

	@MockBean
	private JsonDataDao jsonDataDao;
	private PreProcess preProcess;
	private RequestObject request;

	@BeforeEach
	public void beforeEach() {
		preProcess = new PreProcess(jsonDataDao);
		request = new RequestObject();
		request.setId(JsonDataDaoIT.JSON_DATA_ID);
	}

	@Test
	public void notFoundTest() {
		ResultObject result = preProcess.apply(request);
		assertNull(result.getUniqueId());
		assertEquals(JsonDataDaoIT.JSON_DATA_ID, result.getJsonDataId());
		verify(jsonDataDao).doApprovals(JsonDataDaoIT.JSON_DATA_ID);
		verify(jsonDataDao).doGapTolerances(JsonDataDaoIT.JSON_DATA_ID);
		verify(jsonDataDao).doGrades(JsonDataDaoIT.JSON_DATA_ID);
		verify(jsonDataDao).doHeaderInfo(JsonDataDaoIT.JSON_DATA_ID);
		verify(jsonDataDao).doInterpolationTypes(JsonDataDaoIT.JSON_DATA_ID);
		verify(jsonDataDao).doMethods(JsonDataDaoIT.JSON_DATA_ID);
		verify(jsonDataDao).doPoints(JsonDataDaoIT.JSON_DATA_ID);
	}

	@Test
	public void foundTest() {
		when(jsonDataDao.doHeaderInfo(anyLong())).thenReturn(JsonDataDaoIT.TIME_SERIES_UNIQUE_ID);
		ResultObject result = preProcess.processRequest(request);
		assertEquals(JsonDataDaoIT.TIME_SERIES_UNIQUE_ID, result.getUniqueId());
		assertEquals(JsonDataDaoIT.JSON_DATA_ID, result.getJsonDataId());
		verify(jsonDataDao).doApprovals(JsonDataDaoIT.JSON_DATA_ID);
		verify(jsonDataDao).doGapTolerances(JsonDataDaoIT.JSON_DATA_ID);
		verify(jsonDataDao).doGrades(JsonDataDaoIT.JSON_DATA_ID);
		verify(jsonDataDao).doHeaderInfo(JsonDataDaoIT.JSON_DATA_ID);
		verify(jsonDataDao).doInterpolationTypes(JsonDataDaoIT.JSON_DATA_ID);
		verify(jsonDataDao).doMethods(JsonDataDaoIT.JSON_DATA_ID);
		verify(jsonDataDao).doPoints(JsonDataDaoIT.JSON_DATA_ID);
	}
}
