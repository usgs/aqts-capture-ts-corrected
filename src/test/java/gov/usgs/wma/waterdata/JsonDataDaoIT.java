package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileUrlResource;
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
	classes={DBTestConfig.class, JsonDataDao.class})
@DatabaseSetup("classpath:/testData/staticData/")
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
public class JsonDataDaoIT {

	@Autowired
	private JsonDataDao jsonDataDao;

	public static final Long JSON_DATA_ID_1 = 1L;
	public static final Long JSON_DATA_ID_4 = 4L;
	public static final Long JSON_DATA_ID_5 = 5L;
	public static final String TIME_SERIES_UNIQUE_ID = "d9a9bcc1106a4819ad4e7a4f64894ceb";
	public static final String TIME_SERIES_UNIQUE_ID_TO_SKIP = "skipme";
	public static final String PROCESS_DATA_TYPE = "tsDailyValueStatisticalTransform";
	public static final Integer PARTITION_NUMBER = 7;

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesApprovals/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doApprovalsTest() {
		assertDoesNotThrow(() -> {
			jsonDataDao.doApprovals(JSON_DATA_ID_1, PARTITION_NUMBER);
		}, "should not have thrown an exception but did");
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesGapTolerances/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doGapTolerancesTest() {
		assertDoesNotThrow(() -> {
			jsonDataDao.doGapTolerances(JSON_DATA_ID_1, PARTITION_NUMBER);
		}, "should not have thrown an exception but did");
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesGrades/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doGradesTest() {
		assertDoesNotThrow(() -> {
			jsonDataDao.doGrades(JSON_DATA_ID_1, PARTITION_NUMBER);
		}, "should not have thrown an exception but did");
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesHeaderInfo/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doHeaderInfoTest() {
		assertDoesNotThrow(() -> {
			jsonDataDao.doHeaderInfo(JSON_DATA_ID_1, PARTITION_NUMBER);
		}, "should not have thrown an exception but did");
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testData/cleanseOutput/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doHeaderInfoNoIdTest() {
		assertDoesNotThrow(() -> {
			jsonDataDao.doHeaderInfo(JSON_DATA_ID_4, PARTITION_NUMBER);
		}, "should not have thrown an exception but did");
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/processMicroseconds/timeSeriesHeaderInfo/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doHeaderInfoDuplicateTest() {
		assertDoesNotThrow(() -> {
			jsonDataDao.doHeaderInfo(JsonDataDaoIT.JSON_DATA_ID_1, PARTITION_NUMBER);
		}, "should not have thrown an exception but did");

		assertThrows(DuplicateKeyException.class, () -> {
			jsonDataDao.doHeaderInfo(JsonDataDaoIT.JSON_DATA_ID_1, PARTITION_NUMBER);
		}, "should have thrown a duplicate key exception but did not");
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesInterpolationTypes/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doInterpolationTypesTest() {
		assertDoesNotThrow(() -> {
			jsonDataDao.doInterpolationTypes(JSON_DATA_ID_1, PARTITION_NUMBER);
		}, "should not have thrown an exception but did");
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesMethods/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doMethodsTest() {
		assertDoesNotThrow(() -> {
			jsonDataDao.doMethods(JSON_DATA_ID_1, PARTITION_NUMBER);
		}, "should not have thrown an exception but did");
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesPoints/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doPointsTest() {
		assertDoesNotThrow(() -> {
			jsonDataDao.doPoints(JSON_DATA_ID_1, PARTITION_NUMBER);
		}, "should not have thrown an exception but did");
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesQualifiers/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doQualifiersTest() {
		assertDoesNotThrow(() -> {
			jsonDataDao.doQualifiers(JSON_DATA_ID_1, PARTITION_NUMBER);
		}, "should not have thrown an exception but did");
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@DatabaseSetup("classpath:/testData/routing/")
	@Test
	public void getRoutingToProcessTest() {
		TimeSeries timeSeries = jsonDataDao.getRouting(JSON_DATA_ID_1, PARTITION_NUMBER);
		assertNotNull(timeSeries);
		assertEquals(TIME_SERIES_UNIQUE_ID, timeSeries.getUniqueId());
		assertEquals(PROCESS_DATA_TYPE, timeSeries.getDataType());
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@DatabaseSetup("classpath:/testData/routing/")
	@Test
	public void getRoutingToSkipTest() {
		TimeSeries timeSeries = jsonDataDao.getRouting(JSON_DATA_ID_5, PARTITION_NUMBER);
		assertNotNull(timeSeries);
		assertEquals(TIME_SERIES_UNIQUE_ID_TO_SKIP, timeSeries.getUniqueId());
		assertNull(timeSeries.getDataType());
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@Test
	public void getRoutingNotFoundTest() {
		assertThrows(EmptyResultDataAccessException.class, () -> {
			jsonDataDao.getRouting(JSON_DATA_ID_1, PARTITION_NUMBER);
		}, "should have thrown an exception but did not");
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@Test
	public void getRoutingNoGuidTest() {
		assertThrows(EmptyResultDataAccessException.class, () -> {
			jsonDataDao.getRouting(JSON_DATA_ID_4, PARTITION_NUMBER);
		}, "should have thrown an exception but did not");
	}

	@Test
	public void badResource() {
		Exception e = assertThrows(RuntimeException.class, () -> {
			jsonDataDao.getSql(new FileUrlResource("classpath:sql/missing.sql"));
		}, "should have thrown a RuntimeException but did not");
		assertEquals("java.io.FileNotFoundException: classpath:sql/missing.sql (No such file or directory)",
				e.getMessage());
	}

}
