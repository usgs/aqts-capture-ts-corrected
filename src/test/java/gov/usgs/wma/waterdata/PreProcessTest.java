package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
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
		request.setPartitionNumber(JsonDataDaoIT.PARTITION_NUMBER);
		timeSeries = new TimeSeries();
		when(jsonDataDao.getRouting(any())).thenReturn(timeSeries);
	}

	@Test
	public void notFoundTimeSeriesTest() {
		when(jsonDataDao.getRouting(any())).thenThrow(new EmptyResultDataAccessException(1));
		assertThrows(EmptyResultDataAccessException.class, () -> {
			preProcess.apply(request);
		}, "should have thrown an exception but did not");
		verify(jsonDataDao, never()).doApprovals(request);
		verify(jsonDataDao, never()).doGapTolerances(request);
		verify(jsonDataDao, never()).doGrades(request);
		verify(jsonDataDao).doHeaderInfo(request);
		verify(jsonDataDao, never()).doInterpolationTypes(request);
		verify(jsonDataDao, never()).doMethods(request);
		verify(jsonDataDao, never()).doPoints(request);
		verify(jsonDataDao).getRouting(request);
	}

	@Test
	public void notFoundRoutingTest() {
		timeSeries.setUniqueId(JsonDataDaoIT.TIME_SERIES_UNIQUE_ID);
		ResultObject result = preProcess.apply(request);
		assertNotNull(result);
		assertNotNull(result.getTimeSeries());
		assertEquals("other", result.getTimeSeries().getDataType());
		verify(jsonDataDao, never()).doApprovals(request);
		verify(jsonDataDao, never()).doGapTolerances(request);
		verify(jsonDataDao, never()).doGrades(request);
		verify(jsonDataDao).doHeaderInfo(request);
		verify(jsonDataDao, never()).doInterpolationTypes(request);
		verify(jsonDataDao, never()).doMethods(request);
		verify(jsonDataDao, never()).doPoints(request);
		verify(jsonDataDao).getRouting(request);
	}

	@Test
	public void foundTest() {
		timeSeries.setUniqueId(JsonDataDaoIT.TIME_SERIES_UNIQUE_ID);
		timeSeries.setDataType(JsonDataDaoIT.PROCESS_DATA_TYPE);
		ResultObject result = preProcess.processRequest(request);
		assertNotNull(result);
		assertEquals(JsonDataDaoIT.TIME_SERIES_UNIQUE_ID, result.getTimeSeries().getUniqueId());
		assertEquals(JsonDataDaoIT.PROCESS_DATA_TYPE, result.getTimeSeries().getDataType());
		verify(jsonDataDao).doApprovals(request);
		verify(jsonDataDao).doGapTolerances(request);
		verify(jsonDataDao).doGrades(request);
		verify(jsonDataDao).doHeaderInfo(request);
		verify(jsonDataDao).doInterpolationTypes(request);
		verify(jsonDataDao).doMethods(request);
		verify(jsonDataDao).doPoints(request);
		verify(jsonDataDao).getRouting(request);
	}
}
