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

	@Transactional
	public void doApprovals(Long jsonDataId) {
		doUpdate(jsonDataId, approvals);
	}

	@Transactional
	public void doGapTolerances(Long jsonDataId) {
		doUpdate(jsonDataId, gapTolerances);
	}

	@Transactional
	public void doGrades(Long jsonDataId) {
		doUpdate(jsonDataId, grades);
	}

	@Transactional
	public TimeSeries doHeaderInfo(Long jsonDataId) {
		return jdbcTemplate.queryForObject(
				getSql(headerInfo),
				new Object[] {jsonDataId},
				new TimeSeriesRowMapper()
			);
	}

	@Transactional
	public void doInterpolationTypes(Long jsonDataId) {
		doUpdate(jsonDataId, interpolationTypes);
	}

	@Transactional
	public void doMethods(Long jsonDataId) {
		doUpdate(jsonDataId, methods);
	}

	@Transactional
	public void doPoints(Long jsonDataId) {
		doUpdate(jsonDataId, points);
	}

	@Transactional
	protected void doUpdate(Long jsonDataId, Resource resource) {
		jdbcTemplate.update(getSql(resource), jsonDataId);
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
