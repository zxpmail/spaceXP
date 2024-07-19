package cn.piesat.tools.generator.constants;

/**
 * <p/>
 * {@code @description}  :常量类
 * <p/>
 * <b>@create:</b> 2024/1/18 15:52.
 *
 * @author zhouxp
 */
public interface Constants {
    String EMPTY = "";
    char UNDERLINE_CHAR = '_';

    /**
     * 模版目录
     */
    String TEMPLATE_DIRECTORY = "/template/";

    /**
     * 文件生成时间
     */
    String CREATE_TIME ="openingTime";

    /**
     * 日期格式
     */
    String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 生成项目包
     */
    String PACKAGE = "package";

    /**
     * 项目包路径
     */
    String PACKAGE_PATH="packagePath";

    /**
     * 模版名称
     */
    String TEMPLATE_NAME = "templateName";

    /**
     * 版本号
     */
    String VERSION ="version";

    /**
     * 模块名称
     */
    String MODULE_NAME ="moduleName";

    /**
     * 数据库类型
     */

    String DB_TYPE ="dbType";

    /**
     * 作者
     */
    String AUTHOR ="author";

    /**
     * EMAIL
     */
    String  EMAIL="email";

    /**
     * 单模块
     */
    int SINGLE_MODULE = 1;

    /**
     * .
     */
    String PERIOD =".";

    /**
     * 业务路径
     */
    String BIZ_PATH ="bizPath";

    /**
     * 模块路径
     */
    String MODEL_PATH ="modelPath";

    /**
     * 表名称
     */
    String TABLE_NAME="tableName";

    /**
     * 表说明
     */
    String TABLE_COMMENT ="tableComment";

    /**
     * 类名
     */
    String CLASS_NAME ="className";

    /**
     * 函数名称
     */
    String FUNCTION_NAME="functionName";

    /**
     * 主键类型
     */
    String PK_TYPE ="pkType";

    /**
     * 主键
     */
    String PK ="pk";

    /**
     * 增加列表
     */
    int ONE = 1;
    int ZERO = 0;
    /**
     * 生成vo数据
     */
    String VO_LIST ="voList";
    /**
     * 用来生成dto数据
     */
    String DTO_LIST ="dtoList";
    /**
     * 用来生成Query条件数据
     */
    String QUERY_LIST ="queryList";
    /**
     * 用来生成重复判断数据
     */
    String REPEAT_LIST ="repeatList";
    /**
     * 用来生成order条件数据
     */
    String ORDER_LIST ="orderList";
    /**
     * 生成表单列表
     */
    String FORM_LIST ="formList";
    /**
     * 生成表格格列表
     */
    String GRID_LIST ="gridList";
    /**
     * 生成前端必填数据
     */
    String REQUIRED_LIST ="requiredList";
    /**
     * 用来生成select条件数据
     */
    String SELECT ="select";
    /**
     * 模版名称
     */
    String templateName ="templateName";
}
