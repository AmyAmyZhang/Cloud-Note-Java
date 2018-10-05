package cn.tedu.note.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 对软件的业务层进行性能测试
 * @author zhangyiyu
 *
 */
@Component
@Aspect
public class TimeAspect {
	
	@Around("bean(*Service)")
	public Object test(ProceedingJoinPoint jp) 
		throws Throwable {
		long t1 = System.currentTimeMillis();
		Object val = jp.proceed(); //目标业务方法
		long t2 = System.currentTimeMillis();
		long t = t2 - t1;
		
		//JoinPoint 对象可以获取目标业务的详细信息
		//详细信息: 方法签名，调用参数等
		Signature m = jp.getSignature();
		//Signature: 这里是方法签名(默认有方法名和参数列表)
		System.out.println(m + "用时:" + t);
		return val;
	}
	

}
