package cn.piesat.tools.generator.controller;

import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import cn.piesat.tools.generator.model.vo.DatabaseVO;
import cn.piesat.tools.generator.service.DatabaseService;
import lombok.AllArgsConstructor;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * <p/>
 * {@code @description}  :数据库控制类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:51.
 *
 * @author zhouxp
 */
@RestController
@RequestMapping("database")
@AllArgsConstructor
@CrossOrigin
public class DatabaseController {
    private final DatabaseService databaseService;

    @PostMapping("list")
    public List<DatabaseVO> all(){
        return databaseService.all();
    }

    @PostMapping("add")
    public Boolean add(@Validated(AddGroup.class)  @RequestBody DatabaseVO databaseVO){
        return databaseService.add(databaseVO);
    }

    @PutMapping("update")
    public Boolean update(@Validated(UpdateGroup.class)  @RequestBody DatabaseVO databaseVO){
        return databaseService.update(databaseVO);
    }

    @DeleteMapping("delete/{id}")
    public Boolean delete(@PathVariable("id") Long id){
        return databaseService.delete(id);
    }

    @GetMapping("info/{id}")
    public DatabaseVO info(@PathVariable("id") Long id){
        return databaseService.info(id);
    }
}
