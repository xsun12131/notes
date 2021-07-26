package com.fatpanda.notes.config;

import com.fatpanda.notes.common.utils.DateUtil;
import com.fatpanda.notes.common.utils.HttpUtil;
import com.fatpanda.notes.common.utils.IdUtil;
import com.fatpanda.notes.common.utils.LocalTimeUtil;
import com.fatpanda.notes.pojo.entity.Log;
import com.fatpanda.notes.pojo.entity.User;
import com.fatpanda.notes.service.LogService;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author xyy
 * @date 2021/7/22
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    private static final ThreadLocal<LocalDateTime> beginTimeThreadLocal =
            new NamedThreadLocal<LocalDateTime>("ThreadLocal beginTime");
    private static final ThreadLocal<Log> logThreadLocal =
            new NamedThreadLocal<Log>("ThreadLocal log");

    private static final ThreadLocal<User> currentUser = new NamedThreadLocal<>("ThreadLocal user");

    @Autowired(required = false)
    private HttpServletRequest request;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private LogService logService;

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     */
    public static String getControllerMethodDescription2(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ApiOperation controllerLog = method
                .getAnnotation(ApiOperation.class);
        if(null == controllerLog) {
            return "no description";
        }
        String description = controllerLog.value();
        return description;
    }

    /**
     * 方法规则拦截
     */
    @Pointcut("execution(* com.fatpanda.notes.controller.*Controller.*(..))")
    public void controllerPointerCut() {
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作的开始时间
     *
     * @param joinPoint 切点
     * @throws InterruptedException
     */
    @Before("controllerPointerCut()")
    public void doBefore(JoinPoint joinPoint) throws InterruptedException {
        LocalDateTime beginTime = LocalDateTime.now();
        beginTimeThreadLocal.set(beginTime);
        //debug模式下 显式打印开始时间用于调试
        if (logger.isDebugEnabled()) {
            logger.debug("开始计时: {}  URI: {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(beginTime), request.getRequestURI());
        }

        // //读取session中的用户
        // HttpSession session = request.getSession();
        // User user = (User) session.getAttribute("ims_user");
        // currentUser.set(user);

    }

    /**
     * 后置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @SuppressWarnings("unchecked")
    @After("controllerPointerCut()")
    public void doAfter(JoinPoint joinPoint) {
        // User user = currentUser.get();
        // //登入login操作 前置通知时用户未校验 所以session中不存在用户信息
        // if (user == null) {
        //     HttpSession session = request.getSession();
        //     user = (User) session.getAttribute("ims_user");
        //     if (user == null) {
        //         return;
        //     }
        // }

        String title = "";
        //日志类型(info:入库,error:错误)
        String type = "info";
        //请求的IP
        String remoteAddr = request.getRemoteAddr();
        //请求的Uri
        String requestUri = request.getRequestURI();
        //请求的方法类型(post/get)
        String method = request.getMethod();
        //请求提交的参数
        Map<String, String[]> params = request.getParameterMap();
        try {
            title = getControllerMethodDescription2(joinPoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // debu模式下打印JVM信息。
        //得到线程绑定的局部变量（开始时间）
        long beginTime = LocalTimeUtil.toLong(beginTimeThreadLocal.get());
        //2、结束时间
        long endTime = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug("计时结束：{}  URI: {}  耗时： {}   最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(endTime),
                    request.getRequestURI(),
                    DateUtil.formatDateTime(endTime - beginTime),
                    Runtime.getRuntime().maxMemory() / 1024 / 1024,
                    Runtime.getRuntime().totalMemory() / 1024 / 1024,
                    Runtime.getRuntime().freeMemory() / 1024 / 1024,
                    (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024);
        }

        Log log = Log.builder()
                .id(IdUtil.getShortId())
                .title(title)
                .type(type)
                .remoteAddr(HttpUtil.localhostAddr(remoteAddr))
                .requestUri(requestUri)
                .method(method)
                .operateDate(beginTimeThreadLocal.get())
                .consumeTime(1L)
                .build();
        log.setMapToParams(params);
        log.setConsumeTime(endTime - beginTime);

        //通过线程池来执行日志保存
        threadPoolTaskExecutor.execute(new SaveLogThread(log, logService));
        logThreadLocal.set(log);


    }

    /**
     * 异常通知
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "controllerPointerCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        Log log = logThreadLocal.get();
        if (log != null) {
            log.setType("error");
            log.setException(e.toString());
            threadPoolTaskExecutor.execute(new SaveLogThread(log, logService));
        }
    }

    /**
     * 保存日志线程
     *
     * @author lin.r.x
     */
    private static class SaveLogThread implements Runnable {
        private Log log;
        private LogService logService;

        public SaveLogThread(Log log, LogService logService) {
            this.log = log;
            this.logService = logService;
        }

        @Override
        public void run() {
            logService.saveLog(log);
        }
    }

}
