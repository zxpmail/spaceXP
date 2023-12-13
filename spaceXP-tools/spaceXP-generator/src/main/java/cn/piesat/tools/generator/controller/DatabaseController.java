package cn.piesat.tools.generator.controller;

import cn.piesat.tools.generator.model.vo.DatabaseVO;
import cn.piesat.tools.generator.service.DatabaseService;
import lombok.AllArgsConstructor;

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
public class DatabaseController {
    private final DatabaseService databaseService;

    @GetMapping("list")
    public List<DatabaseVO> all(){
        return databaseService.all();
    }

    @PostMapping("add")
    public Boolean add(@RequestBody DatabaseVO databaseVO){
        return databaseService.add(databaseVO)
    }

    @PutMapping("update")
    public Boolean update(@RequestBody DatabaseVO databaseVO){
        return databaseService.update(databaseVO)
    }

    @PutMapping("delete/{id}")
    public Boolean delete(@PathVariable("id") Long id){
        return databaseService.delete(id)
    }

    @PutMapping("info/{id}")
    public Boolean info(@PathVariable("id") Long id){
        return databaseService.info(id)
    }
}
