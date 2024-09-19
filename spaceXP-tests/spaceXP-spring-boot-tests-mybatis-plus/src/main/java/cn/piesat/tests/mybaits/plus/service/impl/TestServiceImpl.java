package cn.piesat.tests.mybaits.plus.service.impl;

import cn.piesat.tests.mybaits.plus.dao.mapper.TestMapper;
import cn.piesat.tests.mybaits.plus.dao.mapper.UserMapper;
import cn.piesat.tests.mybaits.plus.model.entity.TestDO;
import cn.piesat.tests.mybaits.plus.service.TestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service("testService")
public class TestServiceImpl extends ServiceImpl<TestMapper, TestDO> implements TestService {

    @Override
    public long insertUsersOneByOne() {
        this.baseMapper.insertBySql("TRUNCATE TABLE t_test");
        List<TestDO> testDOS = getTestDOS();
        long startTime = System.currentTimeMillis();
        for (TestDO testDO : testDOS) {
            save(testDO);
        }
        System.out.println(System.currentTimeMillis() - startTime);
        return 0;
    }

    private static List<TestDO> getTestDOS() {
        List<TestDO> testDOS = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) {
            TestDO testDO = new TestDO(i, "name: " + i);
            testDOS.add(testDO);

        }
        return testDOS;
    }
    @Override
    public long insertUsersBySql() {
        List<TestDO> testDOS = getTestDOS();
        this.baseMapper.insertBySql("TRUNCATE TABLE t_test");
        long startTime = System.currentTimeMillis();
        StringBuilder sql = new StringBuilder("INSERT INTO t_test (name,id) VALUES ");
        for (TestDO testDO : testDOS) {
            sql.append(String.format("('%s', %d),", testDO.getName(), testDO.getId()));
        }
        sql.deleteCharAt(sql.length() - 1);
        this.baseMapper.insertBySql(sql.toString());
        System.out.println(System.currentTimeMillis() - startTime);
        return 0;
    }

    @Override
    public long saveUsersBatch() {
        List<TestDO> testDOS = getTestDOS();
        this.baseMapper.insertBySql("TRUNCATE TABLE t_test");
        long startTime = System.currentTimeMillis();
        saveBatch(testDOS,1000); // MyBatis-Plus 提供的批量插入方法
        System.out.println(System.currentTimeMillis() - startTime);
        return 0;
    }

    @Override
    public long insertUsersWithBatchProcessing() {
        List<TestDO> testDOS = getTestDOS();
        this.baseMapper.insertBySql("TRUNCATE TABLE t_test");
        long startTime = System.currentTimeMillis();
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            for (TestDO user : testDOS) {
                session.insert("cn.piesat.tests.mybaits.plus.dao.mapper.TestMapper.insert", user);
            }
            session.commit();
        } finally {
            session.close();
        }
        System.out.println(System.currentTimeMillis() - startTime);
        return 0;
    }
    @Autowired
    private DataSource dataSource;
    @Override
    public long insertUsersWithJdbcBatch() {
        List<TestDO> testDOS = getTestDOS();
        this.baseMapper.insertBySql("TRUNCATE TABLE t_test");
        long startTime = System.currentTimeMillis();
        String sql = "INSERT INTO t_test (id,name) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (TestDO test : testDOS) {
                ps.setString(2, test.getName());
                ps.setInt(1, test.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - startTime);
        return 0;
    }

    @Override
    public long insertUsersWithCustomBatch() {
        List<TestDO> testDOS = getTestDOS();
        this.baseMapper.insertBySql("TRUNCATE TABLE t_test");
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        long startTime = System.currentTimeMillis();
        List<Object[]> batchArgs = new ArrayList<>();
        for (TestDO test : testDOS) {
            batchArgs.add(new Object[]{test.getId(),test.getName()});
        }
        jdbcTemplate.batchUpdate("INSERT INTO t_test (id,name) VALUES (?, ?)", batchArgs);
        System.out.println(System.currentTimeMillis() - startTime);
        return 0;
    }
}