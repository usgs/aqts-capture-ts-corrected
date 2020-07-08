package gov.usgs.wma.waterdata;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

@Component
public class JsonDataDao {
	private static final Logger LOG = LoggerFactory.getLogger(JsonDataDao.class);

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	@Value("classpath:sql/approvals.sql")
	private Resource approvals;


	@Value("classpath:sql/gapTolerances.sql")
	private Resource gapTolerances;

	@Value("classpath:sql/grades.sql")
	private Resource grades;

	@Value("classpath:sql/headerInfo.sql")
	private Resource headerInfo;

	@Value("classpath:sql/interpolationTypes.sql")
	private Resource interpolationTypes;

	@Value("classpath:sql/methods.sql")
	private Resource methods;

	@Value("classpath:sql/points.sql")
	private Resource points;

	@Value("classpath:sql/qualifiers.sql")
	private Resource qualifiers;

	@Value("classpath:sql/routing.sql")
	private Resource routing;

	@Transactional
	public void doApprovals(Long jsonDataId, Integer partitionNumber) {
		doUpdate(jsonDataId, approvals, partitionNumber);
	}

	@Transactional
	public void doGapTolerances(Long jsonDataId, Integer partitionNumber) {
		doUpdate(jsonDataId, gapTolerances, partitionNumber);
	}

	@Transactional
	public void doGrades(Long jsonDataId, Integer partitionNumber) {
		doUpdate(jsonDataId, grades, partitionNumber);
	}

	@Transactional
	public void doHeaderInfo(Long jsonDataId, Integer partitionNumber) {
		doUpdate(jsonDataId, headerInfo, partitionNumber);
	}

	@Transactional
	public void doInterpolationTypes(Long jsonDataId, Integer partitionNumber) {
		doUpdate(jsonDataId, interpolationTypes, partitionNumber);
	}

	@Transactional
	public void doMethods(Long jsonDataId, Integer partitionNumber) {
		doUpdate(jsonDataId, methods, partitionNumber);
	}

	@Transactional
	public void doPoints(Long jsonDataId, Integer partitionNumber) {
		doUpdate(jsonDataId, points, partitionNumber);
	}

	@Transactional
	public void doQualifiers(Long jsonDataId, Integer partitionNumber) {
		doUpdate(jsonDataId, qualifiers, partitionNumber);
	}

	@Transactional
	public TimeSeries getRouting(Long jsonDataId, Integer partitionNumber) {
		return jdbcTemplate.queryForObject(
				getSql(routing),
				new TimeSeriesRowMapper(),
				jsonDataId,
				partitionNumber
			);
	}

	@Transactional
	protected void doUpdate(Long jsonDataId, Resource resource, Integer partitionNumber) {
		jdbcTemplate.update(
				getSql(resource),
				jsonDataId,
				partitionNumber
		);
	}

	protected String getSql(Resource resource) {
		String sql = null;
		try {
			sql = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
		} catch (IOException e) {
			LOG.error("Unable to get SQL statement", e);
			throw new RuntimeException(e);
		}
		return sql;
	}
}
