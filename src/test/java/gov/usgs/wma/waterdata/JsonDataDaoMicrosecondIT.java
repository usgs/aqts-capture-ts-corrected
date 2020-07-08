package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
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
public class JsonDataDaoMicrosecondIT {

	@Autowired
	private JsonDataDao jsonDataDao;

	public static final Long JSON_DATA_ID_TENTHS = 2L;

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/processMicroseconds/timeSeriesApprovals/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
	)
	@Test
	public void doApprovalsTest() {
		assertDoesNotThrow(() -> {
			jsonDataDao.doApprovals(JSON_DATA_ID_TENTHS, JsonDataDaoIT.PARTITION_NUMBER);
		}, "should not have thrown an exception but did");
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/processMicroseconds/timeSeriesGapTolerances/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
	)
	@Test
	public void doGapTolerancesTest() {
		assertDoesNotThrow(() -> {
			jsonDataDao.doGapTolerances(JSON_DATA_ID_TENTHS, JsonDataDaoIT.PARTITION_NUMBER);
		}, "should not have thrown an exception but did");
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/processMicroseconds/timeSeriesGrades/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
	)
	@Test
	public void doGradesTest() {
		assertDoesNotThrow(() -> {
			jsonDataDao.doGrades(JSON_DATA_ID_TENTHS, JsonDataDaoIT.PARTITION_NUMBER);
		}, "should not have thrown an exception but did");
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/processMicroseconds/timeSeriesInterpolationTypes/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
	)
	@Test
	public void doInterpolationTypesTest() {
		assertDoesNotThrow(() -> {
			jsonDataDao.doInterpolationTypes(JSON_DATA_ID_TENTHS, JsonDataDaoIT.PARTITION_NUMBER);
		}, "should not have thrown an exception but did");
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/processMicroseconds/timeSeriesMethods/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
	)
	@Test
	public void doMethodsTest() {
		assertDoesNotThrow(() -> {
			jsonDataDao.doMethods(JSON_DATA_ID_TENTHS, JsonDataDaoIT.PARTITION_NUMBER);
		}, "should not have thrown an exception but did");
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/processMicroseconds/timeSeriesQualifiers/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
	)
	@Test
	public void doQualifiersTest() {
		assertDoesNotThrow(() -> {
			jsonDataDao.doQualifiers(JSON_DATA_ID_TENTHS, JsonDataDaoIT.PARTITION_NUMBER);
		}, "should not have thrown an exception but did");
	}
}
