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
	public void doApprovals(RequestObject request) {
		doUpdate(request, approvals);
	}

	@Transactional
	public void doGapTolerances(RequestObject request) {
		doUpdate(request, gapTolerances);
	}

	@Transactional
	public void doGrades(RequestObject request) {
		doUpdate(request, grades);
	}

	@Transactional
	public void doHeaderInfo(RequestObject request) {
		doUpdate(request, headerInfo);
	}

	@Transactional
	public void doInterpolationTypes(RequestObject request) {
		doUpdate(request, interpolationTypes);
	}

	@Transactional
	public void doMethods(RequestObject request) {
		doUpdate(request, methods);
	}

	@Transactional
	public void doPoints(RequestObject request) {
		doUpdate(request, points);
	}

	@Transactional
	public void doQualifiers(RequestObject request) {
		doUpdate(request, qualifiers);
	}

	@Transactional
	public TimeSeries getRouting(RequestObject request) {
		return jdbcTemplate.queryForObject(
				getSql(routing),
				new TimeSeriesRowMapper(),
				request.getId(),
				request.getPartitionNumber()
			);
	}

	@Transactional
	protected void doUpdate(RequestObject request, Resource resource) {
		jdbcTemplate.update(
				getSql(resource),
				request.getId(),
				request.getPartitionNumber()
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
