# 从前端视角学 Java Web：Cheer 项目对照指南

欢迎来到你的第一个 **Spring Boot 3 + JDK 21 + MyBatis-Plus + Vue 3** 全栈实战项目！本指南直接用你熟悉的**前端 / TypeScript 概念**来映射后端的每一层代码，帮你快速建立直觉。

---

## 一、如何启动项目

### 方式 1：双击一键脚本（最省心）

1. 确保本机 `MySQL` 服务处于运行状态（首次已为你导入 `sql/init.sql`）。
2. 双击根目录 `start-backend.cmd` 启动 Java 后端（监听 `http://localhost:8080`）。
3. 双击根目录 `start-frontend.cmd` 启动 Vue 3 前端（访问 `http://localhost:5173`）。

### 方式 2：使用 IntelliJ IDEA 打开后端（推荐日常学习与断点调试）

1. 打开 **IntelliJ IDEA** → `File -> Open`，选择 `e:\Product\Java\YouAreTheBest-cheer\backend` 目录（作为 Maven 项目打开）。
2. 打开 `File -> Project Structure -> Project`，确认 **SDK** 已自动选中 `.jdks\jdk-21`（IDEA 会自动识别我们安装在 `C:\Users\MYPC\.jdks\jdk-21` 的 OpenJDK 21）。
3. 找到 `src/main/java/com/cheer/demo/CheerApplication.java`，点击类名旁边的绿色三角形运行即可（支持打断点 Debug 单步跟踪前端发来的请求！）。

---

## 二、前端概念 ↔ Java Web 核心概念对照表

| 你熟悉的前端 / Node 概念                | 本项目对应的 Java 概念与文件                                    | 核心作用说明                                                                                                             |
| :-------------------------------------- | :-------------------------------------------------------------- | :----------------------------------------------------------------------------------------------------------------------- |
| `package.json` + `npm`                  | `backend/pom.xml` + `Maven`                                     | 管理第三方依赖包（Starter）、JDK 编译版本与打包插件                                                                      |
| `.env` + `vite.config.ts`               | `src/main/resources/application.yml`                            | 配置端口 `8080`、MySQL 连接串、JSON 时区与 SQL 日志打印                                                                  |
| `src/main.ts` (`createApp`)             | `CheerApplication.java` (`@SpringBootApplication`)              | 后端进程启动入口，自动扫描并装配所有组件                                                                                 |
| TS `interface ApiResult<T>`             | `common/Result.java`                                            | 泛型类 `Result<T>`，保证所有接口统一返回 `{ code, message, data }`                                                       |
| Axios 响应错误拦截 / Express 错误中间件 | `common/GlobalExceptionHandler.java` (`@RestControllerAdvice`)  | 集中捕获参数校验失败与 `BusinessException`，避免到处写 `try-catch`                                                       |
| Express / Koa `router.get('/tasks')`    | `controller/CheerTaskController.java` (`@RestController`)       | **控制层**：只负责接收 HTTP 参数（`@RequestParam` / `@PathVariable` / `@RequestBody`）、触发 `@Valid` 校验并调用 Service |
| Composable / Store 业务逻辑             | `service/impl/CheerTaskServiceImpl.java` (`@Service`)           | **业务层**：处理核心业务规则（如状态变为“已打卡”时自动记录 `completedAt`、计算完成率）                                   |
| Prisma / TypeORM Repository             | `mapper/CheerTaskMapper.java` (`extends BaseMapper<CheerTask>`) | **持久层**：继承 `BaseMapper` 直接获得单表增删改查，也支持用 `@Select` 编写原生统计 SQL                                  |
| Zod / Yup 表单校验 Schema               | `dto/TaskSaveDTO.java` (`@NotBlank`, `@Min`, `@Max`)            | **数据传输对象（DTO）**：专门接收并校验前端提交的数据，避免前端恶意传入 `id` 或 `createdAt` 污染数据库实体               |
| 数据库表行类型定义                      | `entity/CheerTask.java` (`@TableName("cheer_task")`)            | **实体类（Entity）**：与 MySQL `cheer_task` 表字段一一映射（自动开启下划线转驼峰）                                       |

---

## 三、跟着一条请求走完后端全链路（推荐阅读顺序）

建议你在 IDEA 中按以下顺序点开文件阅读，并在前端点击一次**「新建目标」**观察后端控制台输出的 SQL：

1. **前端发起请求**：`frontend/src/api/task.ts` 调用 `POST /api/tasks` → Vite 代理转发至 `http://127.0.0.1:8080/api/tasks`。
2. **Controller 接客与校验**：进入 `CheerTaskController#create(@Valid @RequestBody TaskSaveDTO dto)`。
   - `@RequestBody` 自动把前端发来的 JSON 反序列化为 Java 的 `TaskSaveDTO` 对象。
   - `@Valid` 自动检查 `TaskSaveDTO` 上的 `@NotBlank` 等注解；一旦不合法，直接跳入 `GlobalExceptionHandler#handleValidation` 返回 `400` 错误信息，前端 Axios 拦截器自动弹出 `ElMessage.error`。
3. **Service 执行业务逻辑**：进入 `CheerTaskServiceImpl#createTask`，将 DTO 转换为 `CheerTask` 实体，判断如果是已打卡状态则补齐 `completedAt = LocalDateTime.now()`。
4. **Mapper 落库**：调用 `taskMapper.insert(entity)`，MyBatis-Plus 自动生成并执行 `INSERT INTO cheer_task ...` 语句，将自增主键回填到 `entity.id`。
5. **统一包装返回**：`Result.ok(...)` 被 Spring 内置的 Jackson 序列化为 JSON 返回给前端。

---

## 四、给你的 3 个循序渐进动手小练习

当你把项目跑起来体验一遍后，可以尝试亲手改几行 Java 代码，体会后端开发手感：

1. **练习 1（热身 - 体验参数校验）**：
   打开 `TaskSaveDTO.java`，给 `title` 增加 `@Size(min = 2, message = "标题至少输入 2 个字")`，重启后端并在前端故意输入 1 个字保存，观察后端如何拦截并提示前端。
2. **练习 2（进阶 - 新增查询排序或过滤条件）**：
   打开 `CheerTaskController.java` 和 `CheerTaskServiceImpl#pageTasks`，仿照 `category` 参数，增加一个 `priority`（按优先级筛选）参数，并在前端 `TaskFilterBar.vue` 加一个优先级下拉框完成前后端联调。
3. **练习 3（实战 - 新增一个独立接口）**：
   在 `CheerTaskController` 中新增一个 `POST /api/tasks/batch-delete` 接口（接收 `@RequestBody List<Long> ids`），调用 `taskMapper.deleteByIds(ids)` 实现表格多选批量删除！
