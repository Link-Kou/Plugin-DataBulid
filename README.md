# DataBulid

### Plugin-DataBulid 能做什么？

> 基于注解，进行Bean to Bean 的转换
> 基于注解，对所有转换进行归类

---

### 使用环境

    JAVA1.8以上

### 在线安装

 ```xml

 <dependency>
   <groupId>com.github.link-kou</groupId>
   <artifactId>databulid-all</artifactId>
   <version>1.0.2</version>
 </dependency>

 ```

###BUG

1.接口 default 支持 void类型
2.添加方法排除注解

### 手动安装

   无
   
### 为什么重新造轮子

 1、使用场景中大多情况下是一对一的转换。
 2、少数情况下是不同字段转换为不同字段（字段名称、字段类型）
 3、因为历史习惯的遗留数据库字段有前缀
 4、因为DTO、VO需要排除某一些特点字段
 5、基于方法进行匹配
 6、扩展性

### 一、案例说明示列
```java

/**
 * 菜单管理(OrgMenus)实体类
 *
 * @author lk
 * @since 2020-05-28 18:22:11
 */
@Data
@Accessors(chain = true)
public class MenusVO {

    private String id;
    /**
     * 菜单名称
     */
    private String title;
    /**
     * key
     */
    private String keyId;
    /**
     * 父id
     */
    private String parentId;
    /**
     * 上一个节点id
     */
    private String preId;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 参数
     */
    private String param;

    private BoolTypeEnum deleted;
    /**
     * 创建时间
     */
    private Date createtime;
    private Date updatedtime;
}

```

```java

/**
 * 菜单管理(OrgMenus)实体类
 *
 * @author lk
 * @since 2020-05-28 18:22:11
 */
@Data
@Accessors(chain = true)
public class OrgMenusDomain {

    private Integer frows;
    private String fId;
    /**
     * 菜单名称
     */
    private String fTitle;
    /**
     * key
     */
    private String fKey;
    /**
     * 父id
     */
    private String fParent;
    /**
     * 上一个节点id
     */
    private String fPreid;
    /**
     * 参数
     */
    private String fParam;
    /**
     * 类型
     */
    private Integer fType;

    /**
     * 1 表示删除，0 表示未删除。
     */
    private BoolTypeEnum isDeleted;
    /**
     * 创建时间
     */
    private Date createtime;
    private Date updatedtime;
}

```

```java

    /**
     * @author lk
     * @version 1.0
     * @date 2020/5/11 19:15
     */
    @Mappers
    public interface ToFromMenuMapping {
    
        @MappersField
        MenusVO fromToMenusVO(
                @Regexs(replaceFirstMap = {
                        @Regexs.Regex(methodsName = "getFParent", regex = {""}, rename = "ParentId"),
                        @Regexs.Regex(methodsName = "getFKey", regex = {""}, rename = "KeyId"),
                        @Regexs.Regex(methodsName = "getFPreid", regex = {""}, rename = "PreId"),
                        @Regexs.Regex(methodsName = "getIsDeleted", regex = {""}, rename = "Deleted")
                }) OrgMenusDomain orgMenusDomain);
        
        @MappersField
        MenusVO fromToMenusVO(MenusDTO menusdto);
    
    
    }

```

```java

@Component
public class ToFromMenuMappingImpl implements ToFromMenuMapping {
    public MenusVO fromToMenusVO(OrgMenusDomain orgMenusDomain) {
        MenusVO returnlocalvar = new MenusVO();
        returnlocalvar.setId(orgMenusDomain.getFId());
        returnlocalvar.setTitle(orgMenusDomain.getFTitle());
        returnlocalvar.setKeyId(orgMenusDomain.getFKey());
        returnlocalvar.setParentId(orgMenusDomain.getFParent());
        returnlocalvar.setPreId(orgMenusDomain.getFPreid());
        returnlocalvar.setType(orgMenusDomain.getFType());
        returnlocalvar.setParam(orgMenusDomain.getFParam());
        returnlocalvar.setDeleted(orgMenusDomain.getIsDeleted());
        returnlocalvar.setCreatetime(orgMenusDomain.getCreatetime());
        returnlocalvar.setUpdatedtime(orgMenusDomain.getUpdatedtime());
        return returnlocalvar;
    }
}

```

### 二、说明

> ##### 说明文档：

    待补充

## 开源推荐

- `selma` bean转换工具：[http://www.selma-java.org](http://www.selma-java.org)
- `mapstruct` bean转换工具：[https://mapstruct.org/](https://mapstruct.org/)

