# DataBulid

### Plugin-DataBulid 能做什么？

> 基于注解，进行代码构建
---
### 使用环境

    JAVA1.8以上

### 在线安装

 ```xml：

 暂时不支持

 ```

### 手动安装

   无
   
### 一、示列

```java：
@Mappers
public interface TestInterface {


    @MapperImpl(BulidAsingleField.class)
    TestUserDTO tostring3(@Regexs TestUserDomain num);

}
```

### 二、说明

> ##### 说明文档：
1.请勿在生产环境中使用

