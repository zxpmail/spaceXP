package cn.piesat.framework.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * {@code @description}: 参数工具类
 * <p/>
 * {@code @create}: 2024-07-24 15:47
 * {@code @author}: zhouxp
 */
@Slf4j
public class ArgsUtils {
    /**
     * 前端参数数组转换成list ，file只记录filename
     *
     * @param args 前端参数数组
     * @return 转换成list
     */
    public static List<Object> processArgs(Object[] args) {
        List<Object> argsList = new ArrayList<>();
        if (args != null && args.length != 0) {
            for (Object arg : args) {
                if (!ObjectUtils.isEmpty(arg)) {
                    if (arg instanceof MultipartFile) {
                        addToArgsList(argsList, processSingleFile((MultipartFile) arg));
                    } else if (arg instanceof MultipartFile[]) {
                        MultipartFile[] files = (MultipartFile[]) arg;
                        if (files.length != 0) {
                            addToArgsList(argsList, processMultipleFiles(files));
                        }
                    } else {
                        argsList.add(arg);
                    }
                }
            }
        }
        return argsList;
    }

    private static void addToArgsList(List<Object> list, String value) {
        list.add(value);
    }

    private static String processSingleFile(MultipartFile file) {
        try {
            return file.getOriginalFilename();
        } catch (Exception e) {
            log.error("Error obtaining filename", e);
            return "Error obtaining filename";
        }
    }

    private static String processMultipleFiles(MultipartFile[] files) {
        StringBuilder sb = new StringBuilder(files.length * 16); // Estimate size
        for (MultipartFile file : files) {
            sb.append(processSingleFile(file));
            sb.append(";");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

}
