# DataBulid

### Plugin-DataBulid 能做什么？

> 基于注解，进行Bean to Bean 的转换
>

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
   
### 为什么重新造轮子

 1、使用场景中大多情况下是一对一的转换。
 2、少数情况下是不同字段转换为不同字段（字段名称、字段类型）
 3、因为历史习惯的遗留数据库字段有前缀
 4、因为DTO、VO需要排除某一些特点字段
 5、基于方法进行匹配
 6、扩展性

### 一、示列

```java：

    @MappersField
    ProductVO fromToProductVO(ProductDomain product);

```

### 二、说明

> ##### 说明文档：

    待补充

## 开源推荐

- `selma` bean转换工具：[http://www.selma-java.org](http://www.selma-java.org)
- `mapstruct` bean转换工具：[https://mapstruct.org/](https://mapstruct.org/)

