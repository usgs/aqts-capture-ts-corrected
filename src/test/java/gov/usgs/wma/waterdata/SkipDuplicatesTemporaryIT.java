package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
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
@DatabaseSetup("classpath:/testData/jsonData/")
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
public class SkipDuplicatesTemporaryIT {

	@Autowired
	private JsonDataDao jsonDataDao;

	public static final Long JSON_DATA_ID_TENTHS = 2l;
	public static final Long JSON_DATA_ID_2400 = 3l;
	public static final String TIME_SERIES_UNIQUE_ID_TENTHS = "d9a9bcc1106a4819ad4e7a4f64894cec";
	public static final String TIME_SERIES_UNIQUE_ID_2400 = "d9a9bcc1106a4819ad4e7a4f64894ced";

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/skipDuplicates/timeSeriesApprovals/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doApprovalsTest() {
		jsonDataDao.doApprovals(JSON_DATA_ID_TENTHS);
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/skipDuplicates/timeSeriesGapTolerances/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doGapTolerancesTest() {
		jsonDataDao.doGapTolerances(JSON_DATA_ID_TENTHS);
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/skipDuplicates/timeSeriesGrades/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doGradesTest() {
		jsonDataDao.doGrades(JSON_DATA_ID_TENTHS);
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/skipDuplicates/timeSeriesHeaderInfo/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doHeaderInfoTest() {
		TimeSeries timeSeries = jsonDataDao.doHeaderInfo(JsonDataDaoIT.JSON_DATA_ID);
		assertNotNull(timeSeries);
		assertEquals(JsonDataDaoIT.TIME_SERIES_UNIQUE_ID, timeSeries.getUniqueId());
		try {
			//This one should fail - we should only get a duplicate here if the file is loaded
			//multiple times...
			timeSeries = jsonDataDao.doHeaderInfo(JsonDataDaoIT.JSON_DATA_ID);
			fail("Should have gotten duplicate");
		} catch (DuplicateKeyException e) {
			//This is what we want!
		}
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/skipDuplicates/timeSeriesInterpolationTypes/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doInterpolationTypesTest() {
		jsonDataDao.doInterpolationTypes(JSON_DATA_ID_TENTHS);
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/skipDuplicates/timeSeriesMethods/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doMethodsTest() {
		jsonDataDao.doMethods(JSON_DATA_ID_TENTHS);
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/skipDuplicates/timeSeriesPoints/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doPointsTest() {
		jsonDataDao.doPoints(JSON_DATA_ID_2400);
	}
}
