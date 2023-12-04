package cn.piesat.tools.generator.service.impl;

import cn.piesat.tools.generator.mapper.TableMapper;
import cn.piesat.tools.generator.model.entity.TableDO;
import cn.piesat.tools.generator.service.TableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p/>
 * {@code @description}  :表接口实现类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:31.
 *
 * @author zhouxp
 */
@Service
public class TableServiceImpl  extends ServiceImpl<TableMapper, TableDO> implements TableService {
}
