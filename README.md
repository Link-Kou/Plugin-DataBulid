# DataBulid

### Plugin-DataBulid 能做什么？

> 基于注解，进行代码构建
---
### 使用环境

    JAVA1.8以上

### 在线安装

 ```xml：

 <dependency>
   <groupId>com.github.link-kou</groupId>
   <artifactId>databulid-all</artifactId>
   <version>1.0.0</version>
 </dependency>

 ```

### 手动安装

   无
   
### 一、示列(已实现)

```java：
//多个实体转换为单一实体
@Mappers
public interface TestInterface {

    @MapperImpl(BulidAsingleField.class)
    TestUserDTO tos3(@Regexs TestUserDomain user);

    @MapperImpl(BulidAsingleField.class)
    TestUserDTO tos4(TestUserDomain user,TestUserDomain2 user2);

}
```




### 二、说明

> ##### 说明文档：
1.请勿在生产环境中使用


## 开源推荐
- `selma` bean转换工具：[http://www.selma-java.org](http://www.selma-java.org)
- `mapstruct` bean转换工具：[https://mapstruct.org/](https://mapstruct.org/)

