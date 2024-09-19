package cn.piesat.tests.mybaits.plus.dao.mapper;



import cn.piesat.tests.mybaits.plus.model.entity.TestDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 
 * 
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2022-10-08 14:43:40
 */
@Mapper
public interface TestMapper extends BaseMapper<TestDO> {

    @Select("${nativeSql}")
    void insertBySql(@Param("nativeSql") String nativeSql);
}
