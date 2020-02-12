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

	@Value("classpath:sql/interpolationTypes.sql")
	private Resource interpolationTypes;

	@Value("classpath:sql/methods.sql")
	private Resource methods;

	@Value("classpath:sql/points.sql")
	private Resource points;

	@Value("classpath:sql/uniqueId.sql")
	private Resource uniqueId;

	@Transactional(readOnly=true)
	public String getUniqueId(Long jsonDataId) throws IOException {
		String sql = new String(FileCopyUtils.copyToByteArray(uniqueId.getInputStream()));
		return jdbcTemplate.queryForObject(sql,
				new Object[] {jsonDataId},
				String.class
			);
	}

	@Transactional
	public void doApprovals(Long jsonDataId) throws IOException {
		doUpdate(jsonDataId, approvals);
	}

	@Transactional
	public void doGapTolerances(Long jsonDataId) throws IOException {
		doUpdate(jsonDataId, gapTolerances);
	}

	@Transactional
	public void doGrades(Long jsonDataId) throws IOException {
		doUpdate(jsonDataId, grades);
	}

	@Transactional
	public void doInterpolationTypes(Long jsonDataId) throws IOException {
		doUpdate(jsonDataId, interpolationTypes);
	}

	@Transactional
	public void doMethods(Long jsonDataId) throws IOException {
		doUpdate(jsonDataId, methods);
	}

	@Transactional
	public void doPoints(Long jsonDataId) throws IOException {
		doUpdate(jsonDataId, points);
	}

	@Transactional
	protected void doUpdate(Long jsonDataId, Resource resource) throws IOException {
		String sql = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
		try {
			jdbcTemplate.update(sql, jsonDataId);
		} catch (Exception e) {
			LOG.error("in doUpdate", e);
			throw e;
		}
	}
}
