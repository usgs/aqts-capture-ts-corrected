package gov.usgs.wma.waterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class SkipDuplicatesTemporaryIT {

	@Autowired
	private JsonDataDao jsonDataDao;

	public static final Long JSON_DATA_ID_2400 = 3l;

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/processMicroseconds/timeSeriesHeaderInfo/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doHeaderInfoTest() {
		String timeSeriesUniqueId = jsonDataDao.doHeaderInfo(JsonDataDaoIT.JSON_DATA_ID_1);
		assertEquals(JsonDataDaoIT.TIME_SERIES_UNIQUE_ID, timeSeriesUniqueId);
		try {
			//This one should fail - we should only get a duplicate here if the file is loaded
			//multiple times...
			timeSeriesUniqueId = jsonDataDao.doHeaderInfo(JsonDataDaoIT.JSON_DATA_ID_1);
			fail("Should have gotten duplicate");
		} catch (DuplicateKeyException e) {
			//This is what we want!
		}
	}

	@DatabaseSetup("classpath:/testData/cleanseOutput/")
	@ExpectedDatabase(
			value="classpath:/testResult/processMicroseconds/timeSeriesPoints/",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED
			)
	@Test
	public void doPointsTest() {
		jsonDataDao.doPoints(JSON_DATA_ID_2400);
	}
}
