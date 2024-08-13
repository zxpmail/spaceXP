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

/**
 * <p/>
 * {@code @description}: 自动赋值POJO工具类
 * <p/>
 * {@code @create}: 2024-08-12 16:53
 * {@code @author}: zhouxp
 */
public class AutoSetPojo {
    private static final int RANDOM_STRING_LENGTH = 10;

    private static final int TINYINT_MAX = 127;

    private static final int RANDOM_DATE_MAX = 30;

    private static final int RANDOM_COLLECTION_LENGTH = 5;

    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    public AutoSetPojo(TestProperties testProperties) {
        PODAM_FACTORY.getStrategy().addOrReplaceTypeManufacturer(String.class,
                (dataProviderStrategy, attributeMetadata, map) -> randomString());
        PODAM_FACTORY.getStrategy().addOrReplaceTypeManufacturer(Integer.class, (dataProviderStrategy, attributeMetadata, map) -> {
            if (StrUtil.endWithAnyIgnoreCase(attributeMetadata.getAttributeName(),
                    testProperties.getEnumFieldsNameSuffix().split(","))) {
                return RandomUtil.randomEle(testProperties.getEnumFieldsValue());
            }
            if (StrUtil.endWithAnyIgnoreCase(attributeMetadata.getAttributeName(),
                    testProperties.getTinyIntFieldsNameSuffix().split(","))) {
                return RandomUtil.randomInt(0, TINYINT_MAX + 1);
            }
            return RandomUtil.randomInt();
        });
        PODAM_FACTORY.getStrategy().addOrReplaceTypeManufacturer(LocalDateTime.class,
                (dataProviderStrategy, attributeMetadata, map) -> randomLocalDateTime());

        PODAM_FACTORY.getStrategy().addOrReplaceTypeManufacturer(Boolean.class, (dataProviderStrategy, attributeMetadata, map) -> {

            if (StrUtil.endWithAnyIgnoreCase(attributeMetadata.getAttributeName(),
                    testProperties.getFalseFieldsNameSuffix().split(","))) {
                return false;
            }
            return RandomUtil.randomBoolean();
        });
    }

    public static String randomString() {
        return RandomUtil.randomString(RANDOM_STRING_LENGTH);
    }



    public static Date randomDate() {
        return RandomUtil.randomDay(0, RANDOM_DATE_MAX);
    }

    public static LocalDateTime randomLocalDateTime() {
        return LocalDateTimeUtil.of(randomDate()).withNano(0);
    }


    @SafeVarargs
    public static <T> T fillPojo(Class<T> clazz, Consumer<T>... consumers) {
       return fillPojo(clazz, null, consumers);
    }

    @SafeVarargs
    public static <T> T fillPojo(Class<T> clazz, Type type, Consumer<T>... consumers) {
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
    public static <T> List<T> fillPojoList(Class<T> clazz, Consumer<T>... consumers) {
        // 确保随机数生成器是线程安全的
        int size = RandomUtil.randomInt(1, RANDOM_COLLECTION_LENGTH);

        List<T> resultList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            T pojo = fillPojo(clazz, consumers);
            resultList.add(pojo);
        }

        return resultList;
    }
}
