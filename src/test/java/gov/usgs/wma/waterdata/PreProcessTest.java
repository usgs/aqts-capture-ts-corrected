package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;


@SpringBootTest(webEnvironment=WebEnvironment.NONE)
public class PreProcessTest {

	@MockBean
	private JsonDataDao jsonDataDao;
	private PreProcess preProcess;
	private RequestObject request;
	private TimeSeries timeSeries;

	@BeforeEach
	public void beforeEach() {
		preProcess = new PreProcess(jsonDataDao);
		request = new RequestObject();
		request.setId(JsonDataDaoIT.JSON_DATA_ID_1);
		timeSeries = new TimeSeries();
		when(jsonDataDao.getRouting(anyLong())).thenReturn(timeSeries);
	}

	@Test
	public void notFoundTimeSeriesTest() {
		when(jsonDataDao.getRouting(anyLong())).thenThrow(new EmptyResultDataAccessException(1));
		assertThrows(EmptyResultDataAccessException.class, () -> {
			preProcess.apply(request);
		}, "should have thrown an exception but did not");
		verify(jsonDataDao, never()).doApprovals(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao, never()).doGapTolerances(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao, never()).doGrades(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao).doHeaderInfo(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao, never()).doInterpolationTypes(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao, never()).doMethods(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao, never()).doPoints(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao).getRouting(JsonDataDaoIT.JSON_DATA_ID_1);
	}

	@Test
	public void notFoundRoutingTest() {
		timeSeries.setUniqueId(JsonDataDaoIT.TIME_SERIES_UNIQUE_ID);
		timeSeries.setDataType(null);
		ResultObject result = preProcess.apply(request);
		assertNotNull(result);
		assertNotNull(result.getTimeSeries());
		verify(jsonDataDao, never()).doApprovals(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao, never()).doGapTolerances(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao, never()).doGrades(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao).doHeaderInfo(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao, never()).doInterpolationTypes(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao, never()).doMethods(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao, never()).doPoints(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao).getRouting(JsonDataDaoIT.JSON_DATA_ID_1);
	}

	@Test
	public void foundTest() {
		timeSeries.setUniqueId(JsonDataDaoIT.TIME_SERIES_UNIQUE_ID);
		timeSeries.setDataType(JsonDataDaoIT.PROCESS_DATA_TYPE);
		ResultObject result = preProcess.processRequest(request);
		assertNotNull(result);
		assertEquals(JsonDataDaoIT.TIME_SERIES_UNIQUE_ID, result.getTimeSeries().getUniqueId());
		assertEquals(JsonDataDaoIT.PROCESS_DATA_TYPE, result.getTimeSeries().getDataType());
		verify(jsonDataDao).doApprovals(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao).doGapTolerances(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao).doGrades(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao).doHeaderInfo(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao).doInterpolationTypes(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao).doMethods(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao).doPoints(JsonDataDaoIT.JSON_DATA_ID_1);
		verify(jsonDataDao).getRouting(JsonDataDaoIT.JSON_DATA_ID_1);
	}
}
