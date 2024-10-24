package cn.piesat.framework.security.core;

import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

/**
 * <p/>
 * {@code @description}: LicenseManager的单例
 * <p/>
 * {@code @create}: 2024-10-24 16:01
 * {@code @author}: zhouxp
 */
public class LicenseManagerHolder {
    private static volatile LicenseManager LICENSE_MANAGER;

    public static LicenseManager getInstance(LicenseParam param) {
        if (LICENSE_MANAGER == null) {
            synchronized (LicenseManagerHolder.class) {
                if (LICENSE_MANAGER == null) {
                    LICENSE_MANAGER = new LicenseManager(param);
                }
            }
        }
        return LICENSE_MANAGER;
    }
}
