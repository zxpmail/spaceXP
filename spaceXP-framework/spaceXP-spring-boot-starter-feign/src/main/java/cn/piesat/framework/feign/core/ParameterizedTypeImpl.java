package cn.piesat.framework.feign.core;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.StringJoiner;

/**
 * <p/>
 *
 * @author zhouxp
 * @description :参数化类型实现类
 * <p/>
 * <b>@create:</b> 2022/10/10 13:57.
 */
@RequiredArgsConstructor
public class ParameterizedTypeImpl implements ParameterizedType, Serializable {
    private final Type rawType;

    private final Type[] typeArguments;

    @Override
    public String getTypeName() {
        String typeName = this.rawType.getTypeName();
        if (this.typeArguments.length > 0) {
            StringJoiner stringJoiner = new StringJoiner(", ", "<", ">");
            for (Type argument : this.typeArguments) {
                stringJoiner.add(argument.getTypeName());
            }
            return typeName + stringJoiner;
        }
        return typeName;
    }
    @Override
    public Type getRawType() {
        return this.rawType;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
    @Override
    public Type[] getActualTypeArguments() {
        return this.typeArguments;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ParameterizedType)) {
            return false;
        }
        ParameterizedType otherType = (ParameterizedType) other;
        return (otherType.getOwnerType() == null && this.rawType.equals(otherType.getRawType()) &&
                Arrays.equals(this.typeArguments, otherType.getActualTypeArguments()));
    }

    @Override
    public int hashCode() {
        return (this.rawType.hashCode() * 31 + Arrays.hashCode(this.typeArguments));
    }

    @Override
    public String toString() {
        return getTypeName();
    }
}
