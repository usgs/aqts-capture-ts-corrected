package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileUrlResource;
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

	public static final Long JSON_DATA_ID_1 = 1l;
	public static final Long JSON_DATA_ID_2 = 2l;
	public static final Long JSON_DATA_ID_3 = 3l;
	public static final Long JSON_DATA_ID_4 = 4l;
	public static final String TIME_SERIES_UNIQUE_ID = "d9a9bcc1106a4819ad4e7a4f64894ceb";
	public static final String TIME_SERIES_UNIQUE_ID_TO_SKIP = "skipme";
	public static final String TIME_SERIES_UNIQUE_ID_NOT_FOUND = "notfound";
	public static final String PROCESS_DATA_TYPE = "tsDailyValueStatisticalTransform";

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesApprovals/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doApprovalsTest() {
		jsonDataDao.doApprovals(JSON_DATA_ID_1);
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesGapTolerances/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doGapTolerancesTest() {
		jsonDataDao.doGapTolerances(JSON_DATA_ID_1);
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesGrades/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doGradesTest() {
		jsonDataDao.doGrades(JSON_DATA_ID_1);
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesHeaderInfo/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doHeaderInfoTest() {
		String timeSeriesUniqueId = jsonDataDao.doHeaderInfo(JSON_DATA_ID_1);
		assertEquals(TIME_SERIES_UNIQUE_ID, timeSeriesUniqueId);
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@Test
	public void doHeaderInfoNoIdTest() {
		assertNull(jsonDataDao.doHeaderInfo(JSON_DATA_ID_4));
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesInterpolationTypes/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doInterpolationTypesTest() {
		jsonDataDao.doInterpolationTypes(JSON_DATA_ID_1);
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesMethods/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doMethodsTest() {
		jsonDataDao.doMethods(JSON_DATA_ID_1);
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesPoints/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doPointsTest() {
		jsonDataDao.doPoints(JSON_DATA_ID_1);
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/happyPath/timeSeriesQualifiers/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doQualifiersTest() {
		jsonDataDao.doQualifiers(JSON_DATA_ID_1);
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@Test
	public void getRoutingToProcessTest() {
		TimeSeries timeSeries = jsonDataDao.getRouting(TIME_SERIES_UNIQUE_ID);
		assertNotNull(timeSeries);
		assertEquals(TIME_SERIES_UNIQUE_ID, timeSeries.getUniqueId());
		assertEquals(PROCESS_DATA_TYPE, timeSeries.getDataType());
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@Test
	public void getRoutingToSkipTest() {
		TimeSeries timeSeries = jsonDataDao.getRouting(TIME_SERIES_UNIQUE_ID_TO_SKIP);
		assertNotNull(timeSeries);
		assertEquals(TIME_SERIES_UNIQUE_ID_TO_SKIP, timeSeries.getUniqueId());
		assertNull(timeSeries.getDataType());
	}

//	@DatabaseSetup("classpath:/testData/cleanseOutput/")
//	@Test
//	public void getRoutingNotFoundTest() {
//		assertThrows(RuntimeException.class, () -> {
//			jsonDataDao.getRouting(TIME_SERIES_UNIQUE_ID_NOT_FOUND);
//		}, "should have thrown an exception but did not");
//	}

	@Test
	public void badResource() {
		try {
			jsonDataDao.getSql(new FileUrlResource("classpath:sql/missing.sql"));
			fail("Should have gotten a RuntimeException");
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
			assertEquals("java.io.FileNotFoundException: classpath:sql/missing.sql (No such file or directory)",
					e.getMessage());
		}
	}

}
