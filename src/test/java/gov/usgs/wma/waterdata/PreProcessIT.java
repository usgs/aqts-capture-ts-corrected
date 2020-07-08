package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

@SpringBootTest(webEnvironment=WebEnvironment.NONE,
	classes={DBTestConfig.class, JsonDataDao.class, PreProcess.class})
@ActiveProfiles("it")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader=FileSensingDataSetLoader.class)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Transactional(propagation=Propagation.NOT_SUPPORTED)
@Import({DBTestConfig.class})
@DirtiesContext
public class PreProcessIT {

	@Autowired
	private PreProcess preProcess;

	@DatabaseSetup("classpath:/testData/staticData/")
	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesApprovals/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesGapTolerances/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesGrades/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesHeaderInfo/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesInterpolationTypes/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesMethods/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesPoints/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesQualifiers/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void fullTest() {
		RequestObject request = new RequestObject();
		request.setId(JsonDataDaoIT.JSON_DATA_ID_1);
		request.setPartitionNumber(JsonDataDaoIT.PARTITION_NUMBER);
		ResultObject result = preProcess.apply(request);
		assertNotNull(result);
		assertEquals(JsonDataDaoIT.TIME_SERIES_UNIQUE_ID, result.getTimeSeries().getUniqueId());

		assertThrows(DuplicateKeyException.class, () -> {
			preProcess.apply(request);
		}, "This function is not set up to process the same file more than once and should fail if it does.");
	}

	@DatabaseSetup("classpath:/testData/staticData/")
	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testData/cleanseOutput/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void rollbackTest() {
		RequestObject request = new RequestObject();
		request.setId(JsonDataDaoIT.JSON_DATA_ID_4);
		assertThrows(EmptyResultDataAccessException.class, () -> {
			preProcess.apply(request);
		}, "This time series has no description and should have failed and rolled back time_series_header data.");
	}
}
