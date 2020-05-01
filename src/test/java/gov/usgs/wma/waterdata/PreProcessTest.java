package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
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
		request.setId(JsonDataDaoIT.JSON_DATA_ID_1);
	}

	@Test
	public void notFoundTest() {
		ResultObject result = preProcess.apply(request);
		assertNotNull(result);
		assertTrue(result.getTimeSeriesList().isEmpty());
		verify(jsonDataDao, never()).doApprovals(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao, never()).doGapTolerances(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao, never()).doGrades(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao).doHeaderInfo(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao, never()).doInterpolationTypes(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao, never()).doMethods(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao, never()).doPoints(JsonDataDaoIT.JSON_DATA_ID_1);
	}

	@Test
	public void foundTest() {
		TimeSeries timeSeries = new TimeSeries();
		timeSeries.setUniqueId(JsonDataDaoIT.TIME_SERIES_UNIQUE_ID);
		timeSeries.setDataType("");
		when(jsonDataDao.doHeaderInfo(anyLong())).thenReturn(JsonDataDaoIT.TIME_SERIES_UNIQUE_ID);
		when(jsonDataDao.getRouting(anyString())).thenReturn(timeSeries);
		ResultObject result = preProcess.processRequest(request);
		assertNotNull(result);
		assertEquals(1, result.getTimeSeriesList().size());
		assertEquals(JsonDataDaoIT.TIME_SERIES_UNIQUE_ID, result.getTimeSeriesList().get(0).getUniqueId());
		verify(jsonDataDao).doApprovals(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao).doGapTolerances(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao).doGrades(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao).doHeaderInfo(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao).doInterpolationTypes(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao).doMethods(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao).doPoints(JsonDataDaoIT.JSON_DATA_ID_1);
	}
}
