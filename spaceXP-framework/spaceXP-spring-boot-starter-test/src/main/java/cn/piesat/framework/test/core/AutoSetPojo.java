package cn.piesat.framework.test.core;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;

import cn.hutool.core.util.StrUtil;
import cn.piesat.framework.test.properties.TestProperties;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import static cn.piesat.framework.test.constants.Constant.*;
/**
 * <p/>
 * {@code @description}: 自动填充pojo类 参考芋道
 * <p/>
 * {@code @create}: 2024-08-13 9:41
 * {@code @author}: zhouxp
 */

public class AutoSetPojo {



    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    public AutoSetPojo(TestProperties  testProperties) {
        PODAM_FACTORY.getStrategy().addOrReplaceTypeManufacturer(String.class,
                (dataProviderStrategy, attributeMetadata, map) -> randomString());

        PODAM_FACTORY.getStrategy().addOrReplaceTypeManufacturer(Integer.class, (dataProviderStrategy, attributeMetadata, map) -> {
            // 如果是 status 的字段，返回 0 或 1
            if (StrUtil.endWithAnyIgnoreCase(attributeMetadata.getAttributeName(),
                    testProperties.getEnumFieldsNameSuffix().split(COMMA))) {
                return RandomUtil.randomEle(testProperties.getEnumFieldsValue());
            }
            // 如果是 type、status 结尾的字段，返回 tinyint 范围
            if (StrUtil.endWithAnyIgnoreCase(attributeMetadata.getAttributeName(),
                    testProperties.getTinyIntFieldsNameSuffix().split(COMMA))) {
                return RandomUtil.randomInt(0, TINYINT_MAX + 1);
            }
            return RandomUtil.randomInt();
        });

        PODAM_FACTORY.getStrategy().addOrReplaceTypeManufacturer(LocalDateTime.class,
                (dataProviderStrategy, attributeMetadata, map) -> randomLocalDateTime());
        // Boolean
        PODAM_FACTORY.getStrategy().addOrReplaceTypeManufacturer(Boolean.class, (dataProviderStrategy, attributeMetadata, map) -> {
            if (StrUtil.endWithAnyIgnoreCase(attributeMetadata.getAttributeName(),
                    testProperties.getFalseFieldsNameSuffix().split(COMMA))) {
                return false;
            }

            return RandomUtil.randomBoolean();
        });
    }

    public static String randomString() {
        return RandomUtil.randomString(RANDOM_STRING_LENGTH);
    }

    public static Long randomLongId() {
        return RandomUtil.randomLong(0, Long.MAX_VALUE);
    }

    public static Integer randomInteger() {
        return RandomUtil.randomInt(0, Integer.MAX_VALUE);
    }

    public static Date randomDate() {
        return RandomUtil.randomDay(0, RANDOM_DATE_MAX);
    }

    public static LocalDateTime randomLocalDateTime() {
        // 设置 Nano 为零的原因，避免 MySQL、H2 存储不到时间戳
        return LocalDateTimeUtil.of(randomDate()).withNano(0);
    }

    public static Short randomShort() {
        return (short) RandomUtil.randomInt(0, Short.MAX_VALUE);
    }


    @SafeVarargs
    public static <T> T randomPojo(Class<T> clazz, Consumer<T>... consumers) {
        return randomPojo(clazz, null, consumers);
    }

    @SafeVarargs
    public static <T> T randomPojo(Class<T> clazz, Type type, Consumer<T>... consumers) {
        // 确保clazz不为空
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }

        T pojo;
        if (type == null) {
            pojo = PODAM_FACTORY.manufacturePojo(clazz);
        } else {
            pojo = PODAM_FACTORY.manufacturePojo(clazz, type);
        }

        // 非空时，回调逻辑。通过它，可以实现 Pojo 的进一步处理
        if (ArrayUtil.isNotEmpty(consumers)) {
            // 明确表示finalPojo不可变
            final T finalPojo = pojo;
            Arrays.stream(consumers).forEach(consumer -> consumer.accept(finalPojo));
        }

        return pojo;
    }

    @SafeVarargs
    public static <T> List<T> randomPojoList(Class<T> clazz, Consumer<T>... consumers) {
        // 确保随机数生成器是线程安全的
        int size = RandomUtil.randomInt(1, RANDOM_COLLECTION_LENGTH);

        List<T> resultList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            T pojo = randomPojo(clazz, consumers);
            resultList.add(pojo);
        }

        return resultList;
    }

}
